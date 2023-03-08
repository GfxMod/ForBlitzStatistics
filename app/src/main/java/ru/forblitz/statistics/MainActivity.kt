package ru.forblitz.statistics

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout.INVISIBLE
import androidx.core.widget.doOnTextChanged
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.forblitz.statistics.R.*
import ru.forblitz.statistics.adapters.NothingFoundAdapter
import ru.forblitz.statistics.adapters.VehicleAdapter
import ru.forblitz.statistics.adapters.ViewPagerAdapter
import ru.forblitz.statistics.api.*
import ru.forblitz.statistics.data.*
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

        MobileAds.initialize(this) { Log.d("YandexMobileAds", "SDK initialized") }

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
        val clanMembersListBackView = findViewById<View>(id.clan_members_list_back)
        val tanksDetailedStatisticsBackView = findViewById<View>(id.tanks_detailed_statistics_back)
        val tanksList = findViewById<ListView>(id.tanks_list)
        val tanksTierSelect = findViewById<VerticalSeekBar>(id.tanks_tier_select)
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
                    Runnable { Session.to(this, baseStatisticsData, sessionBaseStatisticsData, ratingStatisticsData, sessionRatingStatisticsData) }
                )
            } else {
                Session.from(this, baseStatisticsData, ratingStatisticsData)
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
        tanksTierSelect.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(tanksTierSelect: SeekBar?, progress: Int, fromUser: Boolean) {

                val tanksTierSelectIndicator = findViewById<AppCompatTextView>(id.tanks_tier_select_indicator)

                if (progress == 0) { tanksTierSelectIndicator.text = getString(string.I) }
                if (progress == 1) { tanksTierSelectIndicator.text = getString(string.II) }
                if (progress == 2) { tanksTierSelectIndicator.text = getString(string.III) }
                if (progress == 3) { tanksTierSelectIndicator.text = getString(string.IV) }
                if (progress == 4) { tanksTierSelectIndicator.text = getString(string.V) }
                if (progress == 5) { tanksTierSelectIndicator.text = getString(string.VI) }
                if (progress == 6) { tanksTierSelectIndicator.text = getString(string.VII) }
                if (progress == 7) { tanksTierSelectIndicator.text = getString(string.VIII) }
                if (progress == 8) { tanksTierSelectIndicator.text = getString(string.IX) }
                if (progress == 9) { tanksTierSelectIndicator.text = getString(string.X) }

            }
            override fun onStartTrackingTouch(tanksTierSelect: SeekBar?) {}
            override fun onStopTrackingTouch(tanksTierSelect: SeekBar?) {}
        })
        tanksTierSelect.setOnTouchListener { _, event ->
            tanksTierSelect.performClick()
            tanksTierSelect.mOnTouchEvent(event)
            false
        }
        tanksFilters.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 3
        }

        // Clears all data

        baseStatisticsData.clear()
        ratingStatisticsData.clear()
        vehicles.clear()
        tanksList.adapter = null

        // Sets state list for sort radio buttons

        Utils.setStateListDrawable(this@MainActivity, drawable.ic_battles_on, drawable.ic_battles_off, id.radio_battles)
        Utils.setStateListDrawable(this@MainActivity, drawable.ic_damage_on, drawable.ic_damage_off, id.radio_damage)
        Utils.setStateListDrawable(this@MainActivity, drawable.ic_efficiency_on, drawable.ic_efficiency_off, id.radio_efficiency)
        Utils.setStateListDrawable(this@MainActivity, drawable.ic_xp_on, drawable.ic_xp_off, id.radio_xp)
        Utils.setStateListDrawable(this@MainActivity, drawable.ic_win_rate_on, drawable.ic_win_rate_off, id.radio_win_rate)

        // Sets click listener for filter buttons by type

        findViewById<AnimatedImageButton>(id.tanks_type_lt).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.tanks_type_mt).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.tanks_type_ht).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.tanks_type_at).setOnClickListener(this@MainActivity)

        // Sets click listener for filter buttons by nation

        findViewById<AnimatedImageButton>(id.cn).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.eu).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.fr).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.gb).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.de).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.jp).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.other).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.us).setOnClickListener(this@MainActivity)
        findViewById<AnimatedImageButton>(id.su).setOnClickListener(this@MainActivity)

        // Gets the data of the player you are looking for

        adUtils.showInterstitial {
            CoroutineScope(Dispatchers.IO).launch {

                val getIDCoroutine = getID()
                getIDCoroutine.join()
                getIDCoroutine.cancel()
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
        val tanksTierSelect = findViewById<SeekBar>(id.tanks_tier_select)
        val tanksTierSelectIndicator = findViewById<AppCompatTextView>(id.tanks_tier_select_indicator)

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

        val tanksFilters = findViewById<FloatingActionButton>(id.tanks_filters)

        Utils.playCycledAnimation(view, true)
        tanksLayoutsFlipper.displayedChild = 0
        tanksList.adapter = null

        //
        ////
        //////
        ////
        //

        val sortedVehicles: ArrayList<Vehicle> = ArrayList(vehicles.list)
        val vehiclesToCreate: ArrayList<Vehicle> = ArrayList(0)

        //sortedVehicles.forEach { Log.d("it", it.toString()) }

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
                if (tanksTierSelectIndicator.text != getString(string.empty)) {
                    tanksTierSelect.progress + 1 == it.tier
                } else {
                    true
                }
            }
            .forEach {
                vehiclesToCreate.add(it)
            }

        when (vehiclesToCreate.size) {
            0 -> {
                val list = ArrayList<Any>(0)
                list.add(Any())
                tanksList.adapter = NothingFoundAdapter(this, list)
            }
            1 -> {
                tanksList.adapter = VehicleAdapter(this, vehiclesToCreate)
                tanksFilters.hide()
            }
            else -> {
                tanksList.adapter = VehicleAdapter(this, vehiclesToCreate)
                tanksFilters.show()
            }
        }

    }

    /**
     * Clears the progress of the [tier filter SeekBar]
     * [ru.forblitz.statistics.R.id.tanks_tier_select] and sets an empty
     * value for the [tier filter indicator]
     * [ru.forblitz.statistics.R.id.tanks_tier_select_indicator]
     */
    fun onClickClearTierFilter(view: View) {

        Utils.playCycledAnimation(view, true)
        val tanksTierSelect = findViewById<SeekBar>(id.tanks_tier_select)
        val tanksTierSelectIndicator = findViewById<TextView>(id.tanks_tier_select_indicator)

        tanksTierSelect.progress = 7
        tanksTierSelectIndicator.text = getString(string.empty)

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
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val serverVersionJson = gson.toJson(
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

        return CoroutineScope(Dispatchers.IO).launch {

            try {

                val userIdJson = service.getAccountId(findViewById<EditText>(id.search_field).text.toString())

                withContext(Dispatchers.Main) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val userIDList = gson.toJson(
                        JsonParser.parseString(
                            userIdJson.body()
                                ?.string()
                        )
                    )

                    userID = userIDList.substringAfter("\"account_id\": ").substringBefore("\n")

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

                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val prettyJson1 = gson.toJson(
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

                            baseStatisticsData.json = prettyJson1.substringAfter("all")
                            ratingStatisticsData.json = prettyJson1.substringAfter("rating")

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

        clanData.clear(this@MainActivity)

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val clanInfoJson = service.getClanInfo(userID)

                if (clanInfoJson.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        val gson = GsonBuilder().setPrettyPrinting().create()
                        val clanInfo = gson.toJson(
                            JsonParser.parseString(
                                clanInfoJson.body()
                                    ?.string()
                            )
                        )

                        if (clanData.setSmallJson(clanInfo)) {

                            try {

                                val fullClanInfoJson = service.getFullClanInfo(clanData.clanId)

                                withContext(Dispatchers.Main) {

                                    val gsonFull = GsonBuilder().setPrettyPrinting().create()
                                    val fullClanInfo = gsonFull.toJson(
                                        JsonParser.parseString(
                                            fullClanInfoJson.body()
                                                ?.string()
                                        )
                                    )

                                    clanData.show(this@MainActivity)
                                    clanData.setBigJson(fullClanInfo)
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
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val vehiclesInfoList = gson.toJson(
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
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val vehicleStatistics = gson.toJson(
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
                    idLists[i][j] = vehicles.list[i * 100 + j].tankId
                }

            }
            Log.d("idLists.size", idLists.size.toString())
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

        when(todo) {
            0 -> {
                createSessionFile(prettyJson1)
                Session.hide(this@MainActivity)
            }
            1 -> {
                Session.hide(this@MainActivity)
            }
            2 -> {
                createSessionFile(prettyJson1)
                Session.show(this@MainActivity)
                Session.hideSelect(this@MainActivity)
                val session = File(files[0]).readText()
                sessionBaseStatisticsData.json = session.substringAfter("all")
                sessionRatingStatisticsData.json = session.substringAfter("rating")
                sessionBaseDifferencesStatisticsData = Session.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = Session.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                Session.set(this, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
            }
            3 -> {
                Session.show(this@MainActivity)
                val session = File(files[number + 1]).readText()
                sessionBaseStatisticsData.json = session.substringAfter("all")
                sessionRatingStatisticsData.json = session.substringAfter("rating")
                sessionBaseDifferencesStatisticsData = Session.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = Session.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                Session.set(this, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
                val dateViews = Session.createSelectList(this@MainActivity, files.drop(1), number)
                dateViews.forEach { it ->
                    it.setOnClickListener {
                        setSessionStatistics(prettyJson1, dateViews.indexOf(it)); Utils.randomToMain(this@MainActivity)
                    }
                }
            }
            4 -> {
                createSessionFile(prettyJson1)
                Session.show(this@MainActivity)
                val session = File(files[number]).readText()
                sessionBaseStatisticsData.json = session.substringAfter("all")
                sessionRatingStatisticsData.json = session.substringAfter("rating")
                sessionBaseDifferencesStatisticsData = Session.calculateDifferences(baseStatisticsData, sessionBaseStatisticsData)
                sessionRatingDifferencesStatisticsData = Session.calculateDifferences(ratingStatisticsData, sessionRatingStatisticsData)
                Session.set(this, baseStatisticsData, ratingStatisticsData, sessionBaseDifferencesStatisticsData, sessionRatingDifferencesStatisticsData)
                val dateViews = Session.createSelectList(this@MainActivity, files, number)
                dateViews.forEach { it ->
                    it.setOnClickListener {
                        setSessionStatistics(prettyJson1, dateViews.indexOf(it)); Utils.randomToMain(this@MainActivity)
                    }
                }
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

                service.region = "ru"

            }
            "ru" -> {
                Utils.setSelectedRegion(this@MainActivity, 0)

                service.region = "ru"
            }
            "eu" -> {
                Utils.setSelectedRegion(this@MainActivity, 1)

                service.region = "eu"
            }
            "na" -> {
                Utils.setSelectedRegion(this@MainActivity, 2)

                service.region = "na"
            }
            "asia" -> {
                Utils.setSelectedRegion(this@MainActivity, 3)

                service.region = "asia"
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
