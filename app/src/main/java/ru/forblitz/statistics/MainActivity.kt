package ru.forblitz.statistics

import android.annotation.SuppressLint
import android.content.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.forblitz.statistics.adapters.SessionAdapter
import ru.forblitz.statistics.adapters.VehicleAdapter
import ru.forblitz.statistics.adapters.ViewPagerAdapter
import ru.forblitz.statistics.api.*
import ru.forblitz.statistics.data.*
import ru.forblitz.statistics.utils.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files.*
import java.nio.file.Paths
import java.util.*
import kotlin.math.ceil


class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    private var service: ApiService = ApiService(this@MainActivity)

    private var baseStatisticsData: StatisticsData = StatisticsData()
    private var ratingStatisticsData: StatisticsData = StatisticsData()
    private var sessionBaseStatisticsData: StatisticsData = StatisticsData()
    private var sessionRatingStatisticsData: StatisticsData = StatisticsData()
    private var sessionBaseDifferencesStatisticsData: StatisticsData = StatisticsData()
    private var sessionRatingDifferencesStatisticsData: StatisticsData = StatisticsData()
    private var vehicles: Vehicles = Vehicles()
    private var clanData: Clan = Clan()

    private var userID = ""
    private var fillVehiclesInfoJob: Job? = null
    private var getVehiclesStatisticsJob: ArrayList<Job?> = ArrayList(0)
    
    private val adUtils = AdUtils(this@MainActivity)

    //
    //
    //

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configures ViewPager

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val mainLayoutsFlipper = findViewById<ViewFlipper>(R.id.main_layouts_flipper)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, tabLayout.tabCount)
        tabLayout.setupWithViewPager(viewPager)

        val tabLayoutHeight = (InterfaceUtils.getY(
            this@MainActivity
        ) * 0.905 * 0.1).toInt()
        for (i in 0 until tabLayout.tabCount) {
            var icon: Drawable
            var contentDescription: String
            when (i) {
                0 -> {
                    icon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_outline_bar_chart_36)!!
                    contentDescription = getString(R.string.random)
                }
                1 -> {
                    icon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_outline_auto_graph_36)!!
                    contentDescription = getString(R.string.rating)
                }
                2 -> {
                    icon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_outline_group_36)!!
                    contentDescription = getString(R.string.clan)
                }
                else -> {
                    icon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_tanks)!!
                    contentDescription = getString(R.string.tanks)
                }
            }
            icon =
                InterfaceUtils.createScaledSquareDrawable(
                    this@MainActivity,
                    icon,
                    tabLayoutHeight - resources.getDimensionPixelSize(R.dimen.padding_giant) * 2,
                    tabLayoutHeight - resources.getDimensionPixelSize(R.dimen.padding_giant) * 2
                )
            val view = View(this@MainActivity)
            view.layoutParams = ViewGroup.LayoutParams(
                tabLayoutHeight - resources.getDimensionPixelSize(R.dimen.padding_giant) * 2,
                tabLayoutHeight - resources.getDimensionPixelSize(R.dimen.padding_giant) * 2
            )
            view.background = icon
            view.setOnClickListener { viewPager.setCurrentItem(i, true) }
            view.setOnLongClickListener {
                Toast.makeText(applicationContext, contentDescription, Toast.LENGTH_SHORT).show()
                true
            }
            tabLayout.getTabAt(i)?.customView = view
        }

        viewPager.offscreenPageLimit = 3

        // Hides statistics elements and shows nickname input elements

        mainLayoutsFlipper.displayedChild = Constants.MainViewFlipperItems.ENTER_NICKNAME

        // Sets the action when the search button is pressed from the keyboard

        val searchField = findViewById<EditText>(R.id.search_field)
        searchField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    onClickSearchButton(findViewById(R.id.search_button))
                    true
                }
                else -> false
            }
        }
        searchField.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            searchField.isCursorVisible = hasFocus
        }

        searchField.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    onClickSearchButton(findViewById(R.id.search_button))
                    return true
                }
                return false
            }
        })

        // Creates a session directory

        val sessionsDir = File(applicationContext.filesDir, "sessions")
        createDirectories(Paths.get(sessionsDir.toString()))

        // Get preferences

        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setRegion()

        // Sets preferences listeners

        findViewById<View>(R.id.select_region_ru).setOnClickListener { preferences.edit().putString("region", "ru").apply(); setRegion() }
        findViewById<View>(R.id.select_region_eu).setOnClickListener { preferences.edit().putString("region", "eu").apply(); setRegion() }
        findViewById<View>(R.id.select_region_na).setOnClickListener { preferences.edit().putString("region", "na").apply(); setRegion() }
        findViewById<View>(R.id.select_region_asia).setOnClickListener { preferences.edit().putString("region", "asia").apply(); setRegion() }

        // set onBackPressed

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val randomLayoutsFlipper = findViewById<ViewFlipper>(R.id.random_layouts_flipper)
                val ratingLayoutsFlipper = findViewById<ViewFlipper>(R.id.rating_layouts_flipper)
                val clanLayoutsFlipper = findViewById<ViewFlipper>(R.id.clan_layouts_flipper)
                val tanksLayoutsFlipper = findViewById<ViewFlipper>(R.id.tanks_layouts_flipper)

                if (mainLayoutsFlipper.displayedChild == Constants.MainViewFlipperItems.SETTINGS) {
                    onClickSettingsButton(findViewById(R.id.settings_button))
                } else {
                    when (viewPager.currentItem) {
                        0 -> {
                            if (randomLayoutsFlipper.displayedChild != 0) {
                                randomLayoutsFlipper.displayedChild = 0
                            }
                        }
                        1 -> {
                            if (ratingLayoutsFlipper.displayedChild != 0) {
                                ratingLayoutsFlipper.displayedChild = 0
                            }
                        }
                        2 -> {
                            if (clanLayoutsFlipper.displayedChild != 0) {
                                clanLayoutsFlipper.displayedChild = 0
                            }
                        }
                        3 -> {
                            if (tanksLayoutsFlipper.displayedChild == 1) {
                                tanksLayoutsFlipper.displayedChild = 0
                            }
                            if (tanksLayoutsFlipper.displayedChild == 3) {
                                tanksLayoutsFlipper.displayedChild = 0
                            }
                        }
                    }
                }

            }
        })

        // Check version and fill vehicles specifications

        CoroutineScope(Dispatchers.IO).launch {
            versionCheck().join()
            fillVehiclesSpecifications()
        }

        // Initialization of ads

        MobileAds.initialize(this) { Log.i("YandexMobileAds", "SDK initialized") }

    }

    /**
     * Called when the [search button]
     * [ru.forblitz.statistics.R.id.search_button] or the search button
     * on the keyboard is pressed. Performs necessary all actions to view
     * statistics.
     */
    fun onClickSearchButton(view: View) {

        val searchField = findViewById<EditText>(R.id.search_field)
        val imm: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val randomLayoutsFlipper = findViewById<ViewFlipper>(R.id.random_layouts_flipper)
        val ratingLayoutsFlipper = findViewById<ViewFlipper>(R.id.rating_layouts_flipper)
        val clanLayoutsFlipper = findViewById<ViewFlipper>(R.id.clan_layouts_flipper)
        val tanksLayoutsFlipper = findViewById<ViewFlipper>(R.id.tanks_layouts_flipper)

        val randomDetailsButtonView = findViewById<View>(R.id.random_details_button)
        val randomDetailsBack = findViewById<View>(R.id.random_details_back)
        val randomSessionListButton = findViewById<View>(R.id.random_sessions_list_button)
        val randomSessionStatButton = findViewById<View>(R.id.random_session_stat_button)
        val ratingDetailsButtonView = findViewById<View>(R.id.rating_details_button)
        val ratingDetailsBack = findViewById<View>(R.id.rating_details_back)
        val clanMembersButton = findViewById<View>(R.id.clan_members_button)
        val clanMembersListBackView = findViewById<View>(R.id.clan_members_back)
        val tanksDetailsBack = findViewById<View>(R.id.tanks_details_back)
        val tanksList = findViewById<ListView>(R.id.tanks_list)
        val tanksFilters = findViewById<View>(R.id.tanks_filters)

        val mainFlipper = findViewById<ViewFlipper>(R.id.main_layouts_flipper)
        val searchButton = findViewById<View>(R.id.search_button)
        val lastSearched = findViewById<View>(R.id.last_searched_flipper)

        //

        mainFlipper.displayedChild = Constants.MainViewFlipperItems.LOADING
        searchButton.isClickable = false
        lastSearched.isClickable = false

        // Plays animation

        InterfaceUtils.playCycledAnimation(
            view,
            false
        )

        // Hides keyboard

        imm.hideSoftInputFromWindow(searchField.windowToken, 0)
        searchField.clearFocus()

        // Scrolls to main statistics item for each ViewFlipper/ViewPager

        viewPager.setCurrentItem(0, true)
        randomLayoutsFlipper.displayedChild = 0
        ratingLayoutsFlipper.displayedChild = 0
        clanLayoutsFlipper.displayedChild = 0

        // Sets listeners for buttons

        randomDetailsButtonView.setOnClickListener {
            randomLayoutsFlipper.displayedChild = 1
        }
        randomDetailsBack.setOnClickListener {
            randomLayoutsFlipper.displayedChild = 0
        }
        randomSessionListButton.setOnClickListener {
            randomLayoutsFlipper.displayedChild = 2
        }
        randomSessionStatButton.setOnClickListener {
            if (!randomSessionStatButton.isActivated) {
                adUtils.showInterstitial(
                    Runnable { SessionUtils.to(this, baseStatisticsData, sessionBaseStatisticsData, ratingStatisticsData, sessionRatingStatisticsData) }
                )
            } else {
                SessionUtils.from(this, baseStatisticsData, ratingStatisticsData)
            }
        }
        ratingDetailsButtonView.setOnClickListener {
            ratingLayoutsFlipper.displayedChild = 1
        }
        ratingDetailsBack.setOnClickListener {
            ratingLayoutsFlipper.displayedChild = 0
        }
        clanMembersButton.setOnClickListener {
            clanLayoutsFlipper.displayedChild = 1
        }
        clanMembersListBackView.setOnClickListener {
            clanLayoutsFlipper.displayedChild = 0
        }
        tanksDetailsBack.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 0
        }
        tanksFilters.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 3
        }

        // Clears all data

        vehicles.clear()

        // set params of tanksList

        tanksList.emptyView = findViewById(R.id.item_nothing_found)

        val footer = View(this@MainActivity)
        val width = InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_very_big) * 2
        footer.layoutParams = AbsListView.LayoutParams(width, (width * 0.15).toInt())
        tanksList.addFooterView(footer)

        // Gets the data of the player you are looking for

        adUtils.showInterstitial {
            CoroutineScope(Dispatchers.IO).launch {

                val getIDCoroutine = getID()
                getIDCoroutine.join()
                getIDCoroutine.cancel()
                if (userID != "error") {
                    setPlayerStatistics()
                    setClanStat()

                    // Displays the loading screen on tanks layout and waits for data loading to finish

                    runOnUiThread { tanksLayoutsFlipper.displayedChild = 2 }
                    if (fillVehiclesInfoJob?.isCompleted == false) {
                        fillVehiclesInfoJob!!.join()
                    }
                    fillVehiclesStatistics().join()
                    getVehiclesStatisticsJob.forEach { it?.join() }

                    // Hides the loading screen on tanks layout

                    runOnUiThread { tanksLayoutsFlipper.displayedChild = 0 }

                    // all processes are finished, can unlock the button

                    runOnUiThread {
                        mainFlipper.displayedChild = Constants.MainViewFlipperItems.STATISTICS
                        searchButton.isClickable = true
                        lastSearched.isClickable = true

                        // create base list of tanks

                        onClickApplyFilters(findViewById(R.id.tanks_apply_filters))
                    }
                } else {
                    runOnUiThread {
                        mainFlipper.displayedChild = Constants.MainViewFlipperItems.ENTER_NICKNAME
                        searchButton.isClickable = true
                        lastSearched.isClickable = true
                    }
                }

            }
        }

    }

    /**
     * Called when the [settings button]
     * [ru.forblitz.statistics.R.id.settings_button] is pressed. Shows
     * the settings layout.
     */
    fun onClickSettingsButton(view: View) {

        // Plays animation

        InterfaceUtils.playCycledAnimation(
            view,
            true
        )

        // Shows/hides the settings layout

        val mainLayoutsFlipper = findViewById<ViewFlipper>(R.id.main_layouts_flipper)

        if (!view.isActivated) {

            mainLayoutsFlipper.displayedChild = Constants.MainViewFlipperItems.SETTINGS

        } else if (view.isActivated) {

            mainLayoutsFlipper.displayedChild = Constants.MainViewFlipperItems.ENTER_NICKNAME

        }

        // Inverses isActivated

        view.isActivated = !view.isActivated

    }

    /**
     * Called when the [apply filters button]
     * [ru.forblitz.statistics.R.id.tanks_apply_filters] or the search
     * button on the keyboard is pressed. Creates a display of suitable vehicle
     * statistics.
     */
    fun onClickApplyFilters(view: View) {

        if (!view.isClickable) { return }

        val tanksLayoutsFlipper = findViewById<ViewFlipper>(R.id.tanks_layouts_flipper)
        val tanksList = findViewById<ListView>(R.id.tanks_list)

        val lt = findViewById<View>(R.id.tanks_type_lt)
        val mt = findViewById<View>(R.id.tanks_type_mt)
        val ht = findViewById<View>(R.id.tanks_type_ht)
        val at = findViewById<View>(R.id.tanks_type_at)

        val cn = findViewById<View>(R.id.cn)
        val eu = findViewById<View>(R.id.eu)
        val fr = findViewById<View>(R.id.fr)
        val gb = findViewById<View>(R.id.gb)
        val de = findViewById<View>(R.id.de)
        val jp = findViewById<View>(R.id.jp)
        val other = findViewById<View>(R.id.other)
        val us = findViewById<View>(R.id.us)
        val su = findViewById<View>(R.id.su)

        val i = findViewById<View>(R.id.tanks_tier_i)
        val ii = findViewById<View>(R.id.tanks_tier_ii)
        val iii = findViewById<View>(R.id.tanks_tier_iii)
        val iv = findViewById<View>(R.id.tanks_tier_iv)
        val v = findViewById<View>(R.id.tanks_tier_v)
        val vi = findViewById<View>(R.id.tanks_tier_vi)
        val vii = findViewById<View>(R.id.tanks_tier_vii)
        val viii = findViewById<View>(R.id.tanks_tier_viii)
        val ix = findViewById<View>(R.id.tanks_tier_ix)
        val x = findViewById<View>(R.id.tanks_tier_x)

        val tanksFilters = findViewById<FloatingActionButton>(R.id.tanks_filters)

        InterfaceUtils.playCycledAnimation(
            view,
            true
        )
        tanksLayoutsFlipper.displayedChild = 0

        //
        ////
        //////
        ////
        //

        val sortedVehicles: ArrayList<Vehicle> = ArrayList(vehicles.list.values)
        val vehiclesToCreate: ArrayList<Vehicle> = ArrayList(0)
        
        val comparatorByBattles: Comparator<Vehicle> = Comparator<Vehicle> {
                v1, v2 -> v1.battles.toInt().compareTo(v2.battles.toInt())
        }
        val comparatorByAverageDamage: Comparator<Vehicle> = Comparator<Vehicle> {
                v1, v2 -> v1.averageDamage.toDouble().compareTo(v2.averageDamage.toDouble())
        }
        val comparatorByWinRate: Comparator<Vehicle> = Comparator<Vehicle> {
                v1, v2 -> v1.winRate.toDouble().compareTo(v2.winRate.toDouble())
        }
        val comparatorByAverageXp: Comparator<Vehicle> = Comparator<Vehicle> {
                v1, v2 -> v1.averageXp.toDouble().compareTo(v2.averageXp.toDouble())
        }
        val comparatorByEfficiency: Comparator<Vehicle> = Comparator<Vehicle> {
                v1, v2 -> v1.efficiency.toDouble().compareTo(v2.efficiency.toDouble())
        }

        val tanksSort = findViewById<RadioGroup>(R.id.tanks_sort)
        when (tanksSort.indexOfChild(findViewById(tanksSort.checkedRadioButtonId))) {
            0 -> {
                Collections.sort(sortedVehicles, comparatorByBattles)
            }
            1 -> {
                Collections.sort(sortedVehicles, comparatorByAverageDamage)
            }
            2 -> {
                Collections.sort(sortedVehicles, comparatorByEfficiency)
            }
            3 -> {
                Collections.sort(sortedVehicles, comparatorByAverageXp)
            }
            4 -> {
                Collections.sort(sortedVehicles, comparatorByWinRate)
            }
        }

        sortedVehicles.reverse()

        sortedVehicles
            .filter { it.battles.toInt() > 0 }
            .filter {
                if (!lt.isActivated and
                    !mt.isActivated and
                    !ht.isActivated and
                    !at.isActivated
                ) {
                    true
                } else if (lt.isActivated and (it.type == "lightTank") ) {
                    true
                } else if (mt.isActivated and (it.type == "mediumTank") ) {
                    true
                } else if (ht.isActivated and (it.type == "heavyTank") ) {
                    true
                } else {
                    at.isActivated and (it.type == "AT-SPG")
                }
            }
            .filter {
                if (
                    !cn.isActivated and
                    !eu.isActivated and
                    !fr.isActivated and
                    !gb.isActivated and
                    !de.isActivated and
                    !jp.isActivated and
                    !other.isActivated and
                    !us.isActivated and
                    !su.isActivated
                ) {
                    true
                } else if (cn.isActivated and (it.nation == "china") ) {
                    true
                } else if (eu.isActivated and (it.nation == "european") ) {
                    true
                } else if (fr.isActivated and (it.nation == "france") ) {
                    true
                } else if (gb.isActivated and (it.nation == "uk") ) {
                    true
                } else if (de.isActivated and (it.nation == "germany") ) {
                    true
                } else if (jp.isActivated and (it.nation == "japan") ) {
                    true
                } else if (other.isActivated and (it.nation == "other") ) {
                    true
                } else if (us.isActivated and (it.nation == "usa") ) {
                    true
                } else {
                    su.isActivated and (it.nation == "ussr")
                }
            }
            .filter {

                if (
                    !i.isActivated and
                    !ii.isActivated and
                    !iii.isActivated and
                    !iv.isActivated and
                    !v.isActivated and
                    !vi.isActivated and
                    !vii.isActivated and
                    !viii.isActivated and
                    !ix.isActivated and
                    !x.isActivated
                ) {
                    true
                } else {
                    if (i.isActivated && it.tier == 1) { true }
                    else if (ii.isActivated && it.tier == 2) { true }
                    else if (iii.isActivated && it.tier == 3) { true }
                    else if (iv.isActivated && it.tier == 4) { true }
                    else if (v.isActivated && it.tier == 5) { true }
                    else if (vi.isActivated && it.tier == 6) { true }
                    else if (vii.isActivated && it.tier == 7) { true }
                    else if (viii.isActivated && it.tier == 8) { true }
                    else if (ix.isActivated && it.tier == 9) { true }
                    else { (x.isActivated && it.tier == 10) }
                }
            }
            .forEach {
                vehiclesToCreate.add(it)
            }

        if (vehiclesToCreate.size == 1) {
            tanksFilters.hide()
        } else {
            tanksFilters.show()
        }
        tanksList.adapter = VehicleAdapter(this, vehiclesToCreate)

        val adView = findViewById<BannerAdView>(R.id.tanks_list_banner)
        adUtils.setBanner(InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_big), adView)
        adView.updateLayoutParams<ConstraintLayout.LayoutParams> { bottomToBottom = R.id.tanks_list_layout }

    }

    /**
     * Gets app version from the server and compares it with the [BuildConfig.VERSION_CODE]
     */
    private suspend fun versionCheck(): Job {

        return CoroutineScope(Dispatchers.IO).launch {
            try {

                val serverVersion = Retrofit.Builder()
                    .baseUrl("https://forblitz.ru/")
                    .client(OkHttpClient.Builder().addInterceptor(
                        NetworkConnectionInterceptor(
                            this@MainActivity
                        )
                    ).build())
                    .build()
                    .create(ApiInterfaceVersion::class.java)
                    .getCurrent()

                if (serverVersion.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val serverVersionJson = GsonBuilder().setPrettyPrinting().create().toJson(
                            JsonParser.parseString(
                                serverVersion.body()
                                    ?.string()
                            )
                        )

                        val versionCodeServer = serverVersionJson
                            .substringAfter("\"statisticAppVersion\": {")
                            .substringAfter("\"currentAppVersion\": \"")
                            .substringBefore("\",")
                            .toInt()

                        val minimalVersionCodeServer = serverVersionJson
                            .substringAfter("\"statisticAppVersion\": {")
                            .substringAfter("\"minimalAppVersion\": \"")
                            .substringBefore("\",")
                            .toInt()

                        if (BuildConfig.VERSION_CODE in minimalVersionCodeServer until versionCodeServer) {

                            InterfaceUtils.createAlertDialog(
                                this@MainActivity,
                                this@MainActivity.getString(R.string.update_available),
                                this@MainActivity.getString(R.string.update_available_desc),
                                this@MainActivity.getString(R.string.update),
                                Runnable {
                                    try {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                                    } catch (e: ActivityNotFoundException) {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                                    }
                                },
                                this@MainActivity.getString(android.R.string.cancel),
                                Runnable {  }
                            ).show()

                            this@launch.cancel()

                        } else if (BuildConfig.VERSION_CODE < minimalVersionCodeServer) {

                            InterfaceUtils.createAlertDialog(
                                this@MainActivity,
                                this@MainActivity.getString(R.string.update_available),
                                this@MainActivity.getString(R.string.update_available_desc),
                                this@MainActivity.getString(R.string.update),
                                Runnable {
                                    try {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                                    } catch (e: ActivityNotFoundException) {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                                    }
                                    finish()
                                }
                            ).show()

                        }

                        this@launch.cancel()

                    }
                }

            } catch (e: IOException) {
                this.cancel()
            }
        }

    }

    /**
     * Gets ID for nickname in [search field][ru.forblitz.statistics.R.id.search_field]
     */
    private suspend fun getID(): Job  {
        userID = ""
        val searchField = findViewById<EditText>(R.id.search_field)

        return CoroutineScope(Dispatchers.IO).launch {

            try {

                val userIdJson = service.getAccountId(searchField.text.toString())

                withContext(Dispatchers.Main) {

                    val userIDList = GsonBuilder().setPrettyPrinting().create().toJson(
                        JsonParser.parseString(
                            userIdJson.body()
                                ?.string()
                        )
                    )
                    if (userIDList.contains("error")) {
                        userID = "error"
                        val jsonObject = JsonParser.parseString(userIDList).asJsonObject
                        val data = jsonObject.getAsJsonObject("error")
                        val error = Gson().fromJson(
                            data,
                            ru.forblitz.statistics.jsonobjects.Error::class.java
                        )
                        /*InterfaceUtils.createErrorAlertDialog(
                            this@MainActivity,
                            "Error ${error.code}",
                            error.message
                        )*/
                        InterfaceUtils.createAlertDialog(
                            this@MainActivity,
                            "Error ${error.code}",
                            error.message
                        ).show()
                        searchField.text.clear()
                    } else if (userIDList.contains("\"count\": 0")) {
                        userID = "error"
                        /*InterfaceUtils.createErrorAlertDialog(
                            this@MainActivity,
                            getString(R.string.error),
                            getString(R.string.nickname_not_found)
                        )*/
                        InterfaceUtils.createAlertDialog(
                            this@MainActivity,
                            getString(R.string.error),
                            getString(R.string.nickname_not_found)
                        ).show()
                    } else {
                        userID = userIDList.substringAfter("\"account_id\": ").substringBefore("\n")
                    }

                }

            } catch (e: IOException) {
                @Suppress("ControlFlowWithEmptyBody") val mainCor = CoroutineScope(Dispatchers.IO).launch {

                    runOnUiThread {
                        InterfaceUtils.createAlertDialog(
                            this@MainActivity,
                            this@MainActivity.getString(R.string.network_error),
                            this@MainActivity.getString(R.string.network_error_desc),
                            this@MainActivity.getString(R.string.network_error_try_again)
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val cor = getID()
                                cor.join()
                                cor.cancel()
                            }
                        }.show()
                    }

                    while (userID == "") {  }
                }
                mainCor.join()
                mainCor.cancel()
            }

            this.cancel()
        }

    }

    /**
     * Gets player statistics and shows statistics interface elements
     */
    private fun setPlayerStatistics() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = service.getUsers(userID)

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        val prettyJson1 = GsonBuilder().setPrettyPrinting().create().toJson(
                            JsonParser.parseString(
                                response.body()
                                    ?.string()

                            )
                        )

                        if (prettyJson1.contains("error")) {

                            val message = prettyJson1.substringAfter("\"message\": \"").substringBefore("\"")
                            val code = prettyJson1.substringAfter("\"code\": ").substringBefore(",")
                            Toast.makeText(applicationContext, "Error $code: $message", Toast.LENGTH_SHORT).show()

                            findViewById<ViewFlipper>(R.id.main_layouts_flipper).displayedChild = Constants.MainViewFlipperItems.ENTER_NICKNAME
                            findViewById<View>(R.id.settings_button).isActivated = false

                        } else {

                            baseStatisticsData = ParseUtils.parseStatisticsData(prettyJson1, "all", userID)
                            ratingStatisticsData = ParseUtils.parseStatisticsData(prettyJson1, "rating", userID)

                            StatisticsSet.setBaseStatistics(this@MainActivity, baseStatisticsData)
                            StatisticsSet.setRatingStatistics(this@MainActivity, ratingStatisticsData)

                            findViewById<ViewFlipper>(R.id.fragment_random).displayedChild =
                                if (baseStatisticsData.battles == "0") { 1 } else { 0 }
                            findViewById<ViewFlipper>(R.id.fragment_rating).displayedChild =
                                if (ratingStatisticsData.battles == "0") { 1 } else { 0 }

                            setSessionStatistics(prettyJson1, 0)

                            //

                            val searchField = findViewById<EditText>(R.id.search_field)
                            val searchButton = findViewById<ImageButton>(R.id.search_button)

                            searchField.setText(baseStatisticsData.nickname, TextView.BufferType.EDITABLE)
                            searchField.doOnTextChanged { _, _, _, _ -> searchButton.setImageResource(R.drawable.ic_outline_person_search_36) }
                            searchButton.setImageResource(R.drawable.ic_outline_change_circle_36)

                            findViewById<TextView>(R.id.text_nick).text = baseStatisticsData.nickname
                            findViewById<TextView>(R.id.rating_text_nick).text = baseStatisticsData.nickname

                            findViewById<ViewFlipper>(R.id.main_layouts_flipper).displayedChild = Constants.MainViewFlipperItems.STATISTICS
                            findViewById<View>(R.id.settings_button).isActivated = false

                        }

                    }
                }

            } catch (e: IOException) {
                runOnUiThread {
                    InterfaceUtils.createAlertDialog(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.network_error),
                        this@MainActivity.getString(R.string.network_error_desc),
                        this@MainActivity.getString(R.string.network_error_try_again)
                    ) {
                        setPlayerStatistics()
                        this.cancel()
                    }.show()
                }
            }

        }

    }

    /**
     * Gets clan statistics and shows statistics interface elements
     */
    private fun setClanStat() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val clanInfoJson = service.getClanInfo(userID)

                if (clanInfoJson.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        val smallJson = GsonBuilder().setPrettyPrinting().create().toJson(
                            JsonParser.parseString(
                                clanInfoJson.body()
                                    ?.string()
                            )
                        )

                        val smallJsonObject = JsonParser
                            .parseString(smallJson)
                            .asJsonObject
                            .getAsJsonObject("data")
                            .getAsJsonObject(userID)

                        if (smallJsonObject != null) {
                            clanData.small = Gson().fromJson(
                                smallJsonObject,
                                SmallClanData::class.java
                            )

                            if (clanData.small.clanId != null) {
                                try {

                                    val fullClanInfoJson = service.getFullClanInfo(clanData.small.clanId)

                                    withContext(Dispatchers.Main) {

                                        val bigJson = GsonBuilder().setPrettyPrinting().create().toJson(
                                            JsonParser.parseString(
                                                fullClanInfoJson.body()
                                                    ?.string()
                                            )
                                        )

                                        val bigJsonObject = JsonParser
                                            .parseString(bigJson)
                                            .asJsonObject
                                            .getAsJsonObject("data")
                                            .getAsJsonObject(clanData.small.clanId)

                                        val bigClanData: BigClanData = Gson().fromJson(
                                            bigJsonObject,
                                            BigClanData::class.java
                                        )

                                        clanData.show(this@MainActivity)
                                        clanData.setBigClanData(bigClanData)
                                        clanData.set(this@MainActivity)

                                    }

                                } catch (e: IOException) {
                                    runOnUiThread {
                                        InterfaceUtils.createAlertDialog(
                                            this@MainActivity,
                                            this@MainActivity.getString(R.string.network_error),
                                            this@MainActivity.getString(R.string.network_error_desc),
                                            this@MainActivity.getString(R.string.network_error_try_again)
                                        ) {
                                            setClanStat()
                                            this.cancel()
                                        }.show()
                                    }
                                }
                            } else {
                                clanData.hide(this@MainActivity)
                            }
                        } else {
                            clanData.hide(this@MainActivity)
                        }

                    }
                }
            } catch (e: IOException) {
                runOnUiThread {
                    InterfaceUtils.createAlertDialog(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.network_error),
                        this@MainActivity.getString(R.string.network_error_desc),
                        this@MainActivity.getString(R.string.network_error_try_again)
                    ) {
                        setClanStat()
                        this.cancel()
                    }.show()
                }
            }
        }
    }

    /**
     * Gets a list of vehicles and their specifications. These are NOT
     * statistics, but just the characteristics of vehicles. Called from [onCreate]
     */
    private fun fillVehiclesSpecifications() {

        fillVehiclesInfoJob = CoroutineScope(Dispatchers.IO).launch {

            try {

                val vehiclesInfoListJson = service.getAllInformationAboutVehicles()

                withContext(Dispatchers.Main) {
                    val vehiclesInfoList = GsonBuilder().setPrettyPrinting().create().toJson(
                        JsonParser.parseString(
                            vehiclesInfoListJson.body()
                                ?.string()
                        )
                    )
                    
                    vehicles.fillVehiclesSpecifications(vehiclesInfoList)

                }

            } catch (e: IOException) {
                runOnUiThread {
                    InterfaceUtils.createAlertDialog(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.network_error),
                        this@MainActivity.getString(R.string.network_error_desc),
                        this@MainActivity.getString(R.string.network_error_try_again)
                    ) {
                        fillVehiclesSpecifications()
                        this.cancel()
                    }.show()
                }
            }

        }

    }

    /**
     * Gets statistics for each id list and fills in [vehicles] with it
     * @param idLists contains id lists having no more than 100 items each (the
     * API does not allow you to request the characteristics of more than 100
     * tanks per request)
     */
    private fun getVehiclesStatistics(idLists: Array<Array<String>>): Job {

        return CoroutineScope(Dispatchers.IO).launch {
            for (i in idLists.indices) {

                getVehiclesStatisticsJob.add(CoroutineScope(Dispatchers.IO).launch {

                    var ids = ""
                    for (j in idLists[i].indices) {
                        ids += idLists[i][j] + ","
                    }
                    ids = ids.substringBeforeLast(",")

                    try {

                        val vehiclesStatisticsJson = service.getTankStatistics(userID, ids)

                        withContext(Dispatchers.Main) {
                            val vehicleStatistics = GsonBuilder().setPrettyPrinting().create().toJson(
                                JsonParser.parseString(
                                    vehiclesStatisticsJson.body()
                                        ?.string()
                                )
                            )

                            vehicles.fillVehiclesStatistics(vehicleStatistics)

                        }

                    } catch (e: IOException) {
                        runOnUiThread {
                            InterfaceUtils.createAlertDialog(
                                this@MainActivity,
                                this@MainActivity.getString(R.string.network_error),
                                this@MainActivity.getString(R.string.network_error_desc),
                                this@MainActivity.getString(R.string.network_error_try_again)
                            ) {
                                getVehiclesStatistics(idLists)
                                this.cancel()
                            }.show()
                        }
                    }

                })

            }
        }

    }

    /**
     * Creates an array consisting of arrays of vehicle IDs. The API does not
     * allow us to request characteristics of more than 100 tanks per request,
     * so we are forced to distribute all identifiers into arrays containing
     * 100 or less elements
     */
    private fun fillVehiclesStatistics(): Job {
        return CoroutineScope(Dispatchers.IO).launch {

            val idLists: Array<Array<String>> = Array(
                ceil((vehicles.count - 1).toDouble() / 100).toInt()
            ) { Array(0) { "" } }

            for(i in idLists.indices) {

                idLists[i] = Array(if ((i + 1) * 100 <= vehicles.count) { 100 } else { vehicles.count % 100 }) { "" }

                for (j in idLists[i].indices) {
                    idLists[i][j] = vehicles.list.values.toList()[i * 100 + j].tankId
                }

            }
            getVehiclesStatistics(idLists).join()
        }
    }

    /**
     * Creates session file with name format "accountID-lastBattleTime"
     */
    private fun createSessionFile(text: String) {
        val filename =
            text
                .substringAfter("\"account_id\": ")
                .substringBefore(",") + "-" +
                    text
                        .substringAfter("\"last_battle_time\": ")
                        .substringBefore(",") +
                    "." + preferences.getString("region", "notSpecified")


        val dir = File(applicationContext.filesDir, "sessions")
        val file = File(dir, filename)

        if (!file.exists()) {
            createFile(Paths.get("$file"))
            FileWriter("$file").use { it.write(text) }
        }
    }

    /**
     * Sets all session statistics
     */
    private fun setSessionStatistics(prettyJson1: String, number: Int) {

        val randomSessionsList = findViewById<ListView>(R.id.random_sessions_list)

        val files: ArrayList<String> = SessionUtils.getSessions(applicationContext, userID, preferences.getString("region", "notSpecified"))

        val todo = when (files.size) {
            0 -> {
                0
            }
            1 -> {
                val currentDate = prettyJson1.substringAfter("\"last_battle_time\": ").substringBefore(",")
                val sessionDate = files[0].substringAfter("-").substringBeforeLast(".")
                if (currentDate == sessionDate) { 1 } else { 2 }
            }
            else -> {
                val currentDate = prettyJson1.substringAfter("\"last_battle_time\": ").substringBefore(",")
                val sessionDate = files[0].substringAfter("-").substringBeforeLast(".")
                if (currentDate == sessionDate) { 3 } else { 4 }
            }
        }

        if (todo == 3) { files.removeAt(0) }

        val sessions = ArrayList<Session>(0)
        for (i in 0 until files.size) {
            val session = Session()
            session.path = files[i]
            session.set = Runnable {
                setSessionStatistics(prettyJson1, i); InterfaceUtils.randomToMain(this@MainActivity)
            }
            session.delete = Runnable {
                if (i == number) {
                    Toast.makeText(this@MainActivity, getString(R.string.delete_select), Toast.LENGTH_SHORT).show()
                } else {

                    InterfaceUtils.createAlertDialog(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.delete),
                        this@MainActivity.getString(R.string.delete_alert),
                        this@MainActivity.getString(R.string.delete),
                        Runnable {
                            if (File(files[i]).delete()) {
                                Toast.makeText(this@MainActivity, this@MainActivity.getString(R.string.delete_successfully), Toast.LENGTH_SHORT).show()
                                setSessionStatistics(prettyJson1, i)
                            } else {
                                Toast.makeText(this@MainActivity, this@MainActivity.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                        this@MainActivity.getString(android.R.string.cancel),
                        Runnable {  }
                    ).show()

                }
            }
            session.isSelected = i == number
            sessions.add(session)
        }

        when(todo) {
            0 -> {
                createSessionFile(prettyJson1)
                SessionUtils.set(this@MainActivity, baseStatisticsData, ratingStatisticsData, StatisticsData(), StatisticsData())
            }
            1 -> {
                SessionUtils.set(this@MainActivity, baseStatisticsData, ratingStatisticsData, StatisticsData(), StatisticsData())
            }
            2 -> {
                createSessionFile(prettyJson1)
                val session = File(files[0]).readText()
                sessionBaseStatisticsData = ParseUtils.parseStatisticsData(session, "all", userID)
                sessionRatingStatisticsData = ParseUtils.parseStatisticsData(session, "rating", userID)
                sessionBaseDifferencesStatisticsData = SessionUtils.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = SessionUtils.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                SessionUtils.set(this@MainActivity, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
            }
            else -> {
                createSessionFile(prettyJson1)
                val sessionJson = File(files[number]).readText()
                sessionBaseStatisticsData = ParseUtils.parseStatisticsData(sessionJson, "all", userID)
                sessionRatingStatisticsData = ParseUtils.parseStatisticsData(sessionJson, "rating", userID)
                sessionBaseDifferencesStatisticsData = SessionUtils.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = SessionUtils.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                SessionUtils.set(this@MainActivity, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
                randomSessionsList.adapter = SessionAdapter(this@MainActivity, sessions)
            }
        }

        when(todo) {
            0 -> {
                SessionUtils.setButtonsVisibility(this@MainActivity, SessionUtils.ButtonsVisibility.NOTHING)
            }
            1 -> {
                SessionUtils.setButtonsVisibility(this@MainActivity, SessionUtils.ButtonsVisibility.NOTHING)
            }
            2 -> {
                SessionUtils.setButtonsVisibility(this@MainActivity, SessionUtils.ButtonsVisibility.ONLY_FLIP)
            }
            3 -> {
                if (files.size == 1) {
                    SessionUtils.setButtonsVisibility(this@MainActivity, SessionUtils.ButtonsVisibility.ONLY_FLIP)
                } else {
                    SessionUtils.setButtonsVisibility(this@MainActivity, SessionUtils.ButtonsVisibility.ALL)
                }
            }
            else -> {
                SessionUtils.setButtonsVisibility(this@MainActivity, SessionUtils.ButtonsVisibility.ALL)
            }
        }

    }

    /**
     * Sets the background for the region selection buttons, change region in [service]
     */
    private fun setRegion() {
        findViewById<EditText>(R.id.search_field).setText("", TextView.BufferType.EDITABLE)
        when (preferences.getString("region", "notSpecified")) {
            "notSpecified" -> {

                InterfaceUtils.createAlertDialog(
                    this@MainActivity,
                    this@MainActivity.getString(R.string.terms_of_service),
                    this@MainActivity.getString(R.string.terms_of_service_desc),
                    this@MainActivity.getString(R.string.accept),
                    Runnable { preferences.edit().putString("region", "ru").apply() },
                    this@MainActivity.getString(R.string.exit),
                    Runnable { finish() }
                ).show()

                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    0
                )

                service.setRegion("ru")

            }
            "ru" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    0
                )

                service.setRegion("ru")
            }
            "eu" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    1
                )

                service.setRegion("eu")
            }
            "na" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    2
                )

                service.setRegion("na")
            }
            "asia" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    3
                )

                service.setRegion("asia")
            }
        }
        updateLastSearch()
    }

    /**
     * Searches for saved sessions to suggest them instead of entering a nickname
     */
    private fun updateLastSearch() {

        val sessionsDir = File(applicationContext.filesDir, "sessions")
        var lastFile = File("")
        val resourcesPath = Paths.get(sessionsDir.toString())

        walk(resourcesPath)
            .filter { item -> isRegularFile(item) }
            .forEach { if (
                it.toFile().lastModified() > lastFile.lastModified()
                &&
                it.toFile().toString().substringAfterLast(".") == preferences.getString("region", "notSpecified")
            ) { lastFile = it.toFile() } }

        val lastSearchedFlipper = findViewById<ViewFlipper>(R.id.last_searched_flipper)
        val enterNicknameText = findViewById<TextView>(R.id.enter_nickname_text)

        if (lastFile != File("")) {
            lastSearchedFlipper.displayedChild = 0

            val lastSearchedName = findViewById<TextView>(R.id.last_searched_name)
            val lastSearchedInfo = findViewById<TextView>(R.id.last_searched_info)

            lastSearchedName.text = lastFile.readText().substringAfter("\"nickname\": \"").substringBefore("\"")
            val lastSearchedInfoText = getString(R.string.last_search) + ParseUtils.parseTime(
                (lastFile.lastModified() / 1000).toString()
            )
            lastSearchedInfo.text = lastSearchedInfoText
            enterNicknameText.setText(R.string.enter_nickname_or_select)

            lastSearchedFlipper.setOnClickListener {
                findViewById<EditText>(R.id.search_field).setText(lastFile.readText().substringAfter("\"nickname\": \"").substringBefore("\""), TextView.BufferType.EDITABLE)
                onClickSearchButton(findViewById(R.id.search_button))
            }

        } else {

            lastSearchedFlipper.displayedChild = 1
            enterNicknameText.setText(R.string.enter_nickname)

        }

    }

}
