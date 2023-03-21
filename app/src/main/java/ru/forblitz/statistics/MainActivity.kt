package ru.forblitz.statistics

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.INVISIBLE
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import ru.forblitz.statistics.R.*
import ru.forblitz.statistics.R.string
import ru.forblitz.statistics.adapters.SessionAdapter
import ru.forblitz.statistics.adapters.VehicleAdapter
import ru.forblitz.statistics.adapters.ViewPagerAdapter
import ru.forblitz.statistics.api.*
import ru.forblitz.statistics.data.*
import ru.forblitz.statistics.utils.AdUtils
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.SessionUtils
import ru.forblitz.statistics.utils.Utils
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files.*
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList
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
        setContentView(layout.activity_main)

        // Configures ViewPager

        val viewPager = findViewById<ViewPager>(id.view_pager)
        val tabLayout = findViewById<TabLayout>(id.tabs)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, this@MainActivity, tabLayout.tabCount)
        tabLayout.setupWithViewPager(viewPager)
        viewPager.offscreenPageLimit = 3

        // Hides statistics elements and shows nickname input elements

        findViewById<ViewFlipper>(id.main_layouts_flipper).displayedChild = 0
        findViewById<LinearLayout>(id.view_pager_layout).visibility = INVISIBLE

        // Sets the action when the search button is pressed from the keyboard

        val searchField = findViewById<EditText>(id.search_field)
        searchField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    onClickSearchButton(findViewById(id.search_button))
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
                    onClickSearchButton(findViewById(id.search_button))
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

        findViewById<View>(id.select_region_ru).setOnClickListener { preferences.edit().putString("region", "ru").apply(); setRegion() }
        findViewById<View>(id.select_region_eu).setOnClickListener { preferences.edit().putString("region", "eu").apply(); setRegion() }
        findViewById<View>(id.select_region_na).setOnClickListener { preferences.edit().putString("region", "na").apply(); setRegion() }
        findViewById<View>(id.select_region_asia).setOnClickListener { preferences.edit().putString("region", "asia").apply(); setRegion() }

        // set onBackPressed

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val mainLayoutsFlipper = findViewById<ViewFlipper>(id.main_layouts_flipper)
                val randomLayoutsFlipper = findViewById<ViewFlipper>(id.random_layouts_flipper)
                val ratingLayoutsFlipper = findViewById<ViewFlipper>(id.rating_layouts_flipper)
                val clanLayoutsFlipper = findViewById<ViewFlipper>(id.clan_layouts_flipper)
                val tanksLayoutsFlipper = findViewById<ViewFlipper>(id.tanks_layouts_flipper)

                if (mainLayoutsFlipper.displayedChild == 2) {
                    onClickSettingsButton(findViewById(id.settings_button))
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

        val searchField = findViewById<EditText>(id.search_field)
        val imm: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val viewPager = findViewById<ViewPager>(id.view_pager)
        val randomLayoutsFlipper = findViewById<ViewFlipper>(id.random_layouts_flipper)
        val ratingLayoutsFlipper = findViewById<ViewFlipper>(id.rating_layouts_flipper)
        val clanLayoutsFlipper = findViewById<ViewFlipper>(id.clan_layouts_flipper)
        val tanksLayoutsFlipper = findViewById<ViewFlipper>(id.tanks_layouts_flipper)

        val randomDetailsButtonView = findViewById<View>(id.random_details_button)
        val randomDetailedStatisticsBackView = findViewById<View>(id.random_detailed_statistics_back)
        val randomSessionsButtonView = findViewById<View>(id.random_sessions_button)
        val randomSessionStatButton = findViewById<View>(id.random_session_stat_button)
        val ratingDetailsButtonView = findViewById<View>(id.rating_details_button)
        val ratingDetailedStatisticsBackView = findViewById<View>(id.rating_detailed_statistics_back)
        val clanMembersButton = findViewById<View>(id.clan_members_button)
        val clanMembersListBackView = findViewById<View>(id.clan_members_back)
        val tanksDetailedStatisticsBackView = findViewById<View>(id.tanks_detailed_statistics_back)
        val tanksList = findViewById<ListView>(id.tanks_list)
        val tanksFilters = findViewById<View>(id.tanks_filters)

        val viewPagerLayout = findViewById<View>(id.view_pager_layout)
        val mainFlipper = findViewById<ViewFlipper>(id.main_layouts_flipper)
        val searchButton = findViewById<View>(id.search_button)
        val lastSearched = findViewById<View>(id.last_searched_flipper)

        //

        viewPagerLayout.visibility = INVISIBLE
        mainFlipper.displayedChild = 3
        searchButton.isClickable = false
        lastSearched.isClickable = false

        // Plays animation

        Utils.playCycledAnimation(view, false)

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
        randomDetailedStatisticsBackView.setOnClickListener {
            randomLayoutsFlipper.displayedChild = 0
        }
        randomSessionsButtonView.setOnClickListener {
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
        ratingDetailedStatisticsBackView.setOnClickListener {
            ratingLayoutsFlipper.displayedChild = 0
        }
        clanMembersButton.setOnClickListener {
            clanLayoutsFlipper.displayedChild = 1
        }
        clanMembersListBackView.setOnClickListener {
            clanLayoutsFlipper.displayedChild = 0
        }
        tanksDetailedStatisticsBackView.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 0
        }
        tanksFilters.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 3
        }

        // Clears all data

        vehicles.clear()
        tanksList.emptyView = findViewById(id.item_nothing_found)

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
                        viewPagerLayout.visibility = VISIBLE
                        mainFlipper.displayedChild = 1
                        searchButton.isClickable = true
                        lastSearched.isClickable = true

                        // create base list of tanks

                        onClickApplyFilters(findViewById(id.tanks_apply_filters))
                    }
                } else {
                    runOnUiThread {
                        viewPagerLayout.visibility = INVISIBLE
                        mainFlipper.displayedChild = 0
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

        Utils.playCycledAnimation(view, true)

        // Shows/hides the settings layout

        val mainLayoutsFlipper = findViewById<ViewFlipper>(id.main_layouts_flipper)
        val viewPagerLayout = findViewById<LinearLayout>(id.view_pager_layout)

        if (!view.isActivated && userID == "") {

            mainLayoutsFlipper.displayedChild = 2

        } else if (!view.isActivated && userID != "") {

            mainLayoutsFlipper.displayedChild = 2
            viewPagerLayout.visibility = INVISIBLE

        } else if (view.isActivated) {

            mainLayoutsFlipper.displayedChild = 0
            viewPagerLayout.visibility = INVISIBLE

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

        val tanksLayoutsFlipper = findViewById<ViewFlipper>(id.tanks_layouts_flipper)
        val tanksList = findViewById<ListView>(id.tanks_list)

        val lt = findViewById<View>(id.tanks_type_lt)
        val mt = findViewById<View>(id.tanks_type_mt)
        val ht = findViewById<View>(id.tanks_type_ht)
        val at = findViewById<View>(id.tanks_type_at)

        val cn = findViewById<View>(id.cn)
        val eu = findViewById<View>(id.eu)
        val fr = findViewById<View>(id.fr)
        val gb = findViewById<View>(id.gb)
        val de = findViewById<View>(id.de)
        val jp = findViewById<View>(id.jp)
        val other = findViewById<View>(id.other)
        val us = findViewById<View>(id.us)
        val su = findViewById<View>(id.su)

        val i = findViewById<View>(id.tanks_tier_i)
        val ii = findViewById<View>(id.tanks_tier_ii)
        val iii = findViewById<View>(id.tanks_tier_iii)
        val iv = findViewById<View>(id.tanks_tier_iv)
        val v = findViewById<View>(id.tanks_tier_v)
        val vi = findViewById<View>(id.tanks_tier_vi)
        val vii = findViewById<View>(id.tanks_tier_vii)
        val viii = findViewById<View>(id.tanks_tier_viii)
        val ix = findViewById<View>(id.tanks_tier_ix)
        val x = findViewById<View>(id.tanks_tier_x)

        val tanksFilters = findViewById<FloatingActionButton>(id.tanks_filters)

        Utils.playCycledAnimation(view, true)
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

        val tanksSort = findViewById<RadioGroup>(id.tanks_sort)
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

        val adView = findViewById<BannerAdView>(id.tanks_list_banner)
        adUtils.setBanner(this@MainActivity, tanksList.width, 0, adView)
        adView.updateLayoutParams<ConstraintLayout.LayoutParams> { bottomToBottom = id.tanks_list_layout }

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

                            MaterialAlertDialogBuilder(this@MainActivity)
                                .setTitle(this@MainActivity.getString(string.update_available))
                                .setMessage(this@MainActivity.getString(string.update_available_desc))
                                .setCancelable(false)
                                .setNegativeButton(this@MainActivity.getString(android.R.string.cancel)) { _: DialogInterface?, _: Int -> }
                                .setPositiveButton(this@MainActivity.getString(string.update)) { _: DialogInterface?, _: Int -> Runnable {

                                    try {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                                    } catch (e: ActivityNotFoundException) {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                                    }

                                }.run()
                                }.show()

                            this@launch.cancel()

                        } else if (BuildConfig.VERSION_CODE < minimalVersionCodeServer) {

                            MaterialAlertDialogBuilder(this@MainActivity)
                                .setTitle(this@MainActivity.getString(string.update_available))
                                .setMessage(this@MainActivity.getString(string.update_available_desc))
                                .setCancelable(false)
                                .setPositiveButton(
                                    this@MainActivity.getString(string.update)
                                ) { _: DialogInterface?, _: Int -> Runnable {

                                    try {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                                    } catch (e: ActivityNotFoundException) {
                                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                                    }
                                    finish()

                                }.run()
                                }.show()

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
        val searchField = findViewById<EditText>(id.search_field)

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
                        Utils.createErrorAlertDialog(this@MainActivity, "Error ${error.code}", error.message)
                        searchField.text.clear()
                    } else if (userIDList.contains("\"count\": 0")) {
                        userID = "error"
                        Utils.createErrorAlertDialog(this@MainActivity, getString(string.error), getString(string.nickname_not_found))
                    } else {
                        userID = userIDList.substringAfter("\"account_id\": ").substringBefore("\n")
                    }

                }

            } catch (e: IOException) {
                @Suppress("ControlFlowWithEmptyBody") val mainCor = CoroutineScope(Dispatchers.IO).launch {
                    Utils.createNetworkAlertDialog(this@MainActivity) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val cor = getID()
                            cor.join()
                            cor.cancel()
                        }
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

                            findViewById<ViewFlipper>(id.main_layouts_flipper).displayedChild = 0
                            findViewById<View>(id.settings_button).isActivated = false
                            findViewById<LinearLayout>(id.view_pager_layout).visibility = INVISIBLE

                        } else {

                            baseStatisticsData = ParseUtils.parseStatisticsData(prettyJson1, "all", userID)
                            ratingStatisticsData = ParseUtils.parseStatisticsData(prettyJson1, "rating", userID)

                            StatisticsSet.setBaseStatistics(this@MainActivity, baseStatisticsData)
                            StatisticsSet.setRatingStatistics(this@MainActivity, ratingStatisticsData)

                            setSessionStatistics(prettyJson1, 0)

                            //

                            val searchField = findViewById<EditText>(id.search_field)
                            val searchButton = findViewById<ImageButton>(id.search_button)

                            searchField.setText(baseStatisticsData.nickname, TextView.BufferType.EDITABLE)
                            searchField.doOnTextChanged { _, _, _, _ -> searchButton.setImageResource(drawable.ic_outline_person_search_36) }
                            searchButton.setImageResource(drawable.ic_outline_change_circle_36)

                            findViewById<TextView>(id.text_nick).text = baseStatisticsData.nickname
                            findViewById<TextView>(id.rating_text_nick).text = baseStatisticsData.nickname

                            findViewById<ViewFlipper>(id.main_layouts_flipper).displayedChild = 1
                            findViewById<View>(id.settings_button).isActivated = false
                            findViewById<LinearLayout>(id.view_pager_layout).visibility = VISIBLE

                        }

                    }
                }

            } catch (e: IOException) {
                Utils.createNetworkAlertDialog(this@MainActivity) {
                    setPlayerStatistics()
                    this.cancel()
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
                                Utils.createNetworkAlertDialog(this@MainActivity) {
                                    setClanStat()
                                    this.cancel()
                                }
                            }
                        } else {
                            clanData.hide(this@MainActivity)
                        }

                    }
                }
            } catch (e: IOException) {
                Utils.createNetworkAlertDialog(this@MainActivity) {
                    setClanStat()
                    this.cancel()
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
                Utils.createNetworkAlertDialog(this@MainActivity) {
                    fillVehiclesSpecifications()
                    this.cancel()
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
                        Utils.createNetworkAlertDialog(this@MainActivity) {
                            getVehiclesStatistics(idLists)
                            this.cancel()
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

        val randomSessionsList = findViewById<ListView>(id.random_sessions_list)

        val files: ArrayList<String> = ArrayList(0)

        val dir = File(applicationContext.filesDir, "sessions")
        val resourcesPath = Paths.get(dir.toString())
        walk(resourcesPath)
            .filter { item -> isRegularFile(item) }
            .forEach { if (
                "$it".substringAfterLast("/").substringBefore("-") == userID
                &&
                "$it".substringAfterLast(".") == preferences.getString("region", "notSpecified")
            ) { files.add("$it") } }

        files.sort()
        files.reverse()

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
                setSessionStatistics(prettyJson1, i); Utils.randomToMain(this@MainActivity)
            }
            session.delete = Runnable {
                if (i == number) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(string.delete_select),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val alertDialog = MaterialAlertDialogBuilder(this@MainActivity)
                    alertDialog.setTitle(getString(string.delete))
                    alertDialog.setMessage(getString(string.delete_alert))
                    alertDialog.setPositiveButton(
                        getString(string.delete)
                    ) { _: DialogInterface?, _: Int ->
                        if (File(files[i]).delete()) {
                            Toast.makeText(
                                this@MainActivity,
                                this@MainActivity.getString(string.delete_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            setSessionStatistics(prettyJson1, i)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                this@MainActivity.getString(string.delete_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    alertDialog.setNegativeButton(getString(android.R.string.cancel)) { _: DialogInterface?, _: Int -> }
                    alertDialog.show()
                }
            }
            session.isSelected = i == number
            sessions.add(session)
        }

        when(todo) {
            0 -> {
                createSessionFile(prettyJson1)
                SessionUtils.hide(this@MainActivity)
            }
            1 -> {
                SessionUtils.hide(this@MainActivity)
            }
            2 -> {
                createSessionFile(prettyJson1)
                SessionUtils.show(this@MainActivity)
                SessionUtils.hideSelect(this@MainActivity)
                val session = File(files[0]).readText()
                sessionBaseStatisticsData = ParseUtils.parseStatisticsData(session, "all", userID)
                sessionRatingStatisticsData = ParseUtils.parseStatisticsData(session, "rating", userID)
                sessionBaseDifferencesStatisticsData = SessionUtils.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = SessionUtils.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                SessionUtils.set(this@MainActivity, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
            }
            else -> {
                createSessionFile(prettyJson1)
                SessionUtils.show(this@MainActivity)
                val sessionJson = File(files[number]).readText()
                sessionBaseStatisticsData = ParseUtils.parseStatisticsData(sessionJson, "all", userID)
                sessionRatingStatisticsData = ParseUtils.parseStatisticsData(sessionJson, "rating", userID)
                sessionBaseDifferencesStatisticsData = SessionUtils.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = SessionUtils.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                SessionUtils.set(this@MainActivity, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
                randomSessionsList.adapter = SessionAdapter(this@MainActivity, sessions)
            }
        }

    }

    /**
     * Sets the background for the region selection buttons, change region in [service]
     */
    private fun setRegion() {
        findViewById<EditText>(id.search_field).setText("", TextView.BufferType.EDITABLE)
        when (preferences.getString("region", "notSpecified")) {
            "notSpecified" -> {

                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(this@MainActivity.getString(string.terms_of_service))
                    .setMessage(this@MainActivity.getString(string.terms_of_service_desc))
                    .setCancelable(false)
                    .setNegativeButton(this@MainActivity.getString(string.exit)) { _: DialogInterface?, _: Int -> Runnable {
                        finish()
                    }.run()
                    }
                    .setPositiveButton(this@MainActivity.getString(string.accept)) { _: DialogInterface?, _: Int -> Runnable {
                        preferences.edit().putString("region", "ru").apply()
                    }.run()
                    }
                    .show()

                Utils.setSelectedRegion(this@MainActivity, 0)

                service.setRegion("ru")

            }
            "ru" -> {
                Utils.setSelectedRegion(this@MainActivity, 0)

                service.setRegion("ru")
            }
            "eu" -> {
                Utils.setSelectedRegion(this@MainActivity, 1)

                service.setRegion("eu")
            }
            "na" -> {
                Utils.setSelectedRegion(this@MainActivity, 2)

                service.setRegion("na")
            }
            "asia" -> {
                Utils.setSelectedRegion(this@MainActivity, 3)

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

        val lastSearchedFlipper = findViewById<ViewFlipper>(id.last_searched_flipper)
        val enterNicknameText = findViewById<TextView>(id.enter_nickname_text)

        if (lastFile != File("")) {
            lastSearchedFlipper.displayedChild = 0

            val lastSearchedName = findViewById<TextView>(id.last_searched_name)
            val lastSearchedInfo = findViewById<TextView>(id.last_searched_info)

            lastSearchedName.text = lastFile.readText().substringAfter("\"nickname\": \"").substringBefore("\"")
            val lastSearchedInfoText = getString(string.last_search) + Utils.parseTime((lastFile.lastModified() / 1000).toString())
            lastSearchedInfo.text = lastSearchedInfoText
            enterNicknameText.setText(string.enter_nickname_or_select)

            lastSearchedFlipper.setOnClickListener {
                findViewById<EditText>(id.search_field).setText(lastFile.readText().substringAfter("\"nickname\": \"").substringBefore("\""), TextView.BufferType.EDITABLE)
                onClickSearchButton(findViewById(id.search_button))
            }

        } else {

            lastSearchedFlipper.displayedChild = 1
            enterNicknameText.setText(string.enter_nickname)

        }

    }

}
