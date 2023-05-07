package ru.forblitz.statistics

import android.annotation.SuppressLint
import android.content.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.viewpager.widget.ViewPager
import com.google.android.datatransport.BuildConfig
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.*
import ru.forblitz.statistics.adapters.SessionAdapter
import ru.forblitz.statistics.adapters.VehicleAdapter
import ru.forblitz.statistics.adapters.ViewPagerAdapter
import ru.forblitz.statistics.api.*
import ru.forblitz.statistics.data.*
import ru.forblitz.statistics.data.Constants.*
import ru.forblitz.statistics.dto.Session
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.dto.VehicleStat
import ru.forblitz.statistics.exception.ObjectException
import ru.forblitz.statistics.service.*
import ru.forblitz.statistics.utils.*
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper
import ru.forblitz.statistics.widget.data.ClanScreen
import ru.forblitz.statistics.widget.data.ClanSmall
import ru.forblitz.statistics.widget.data.PlayerFastStat
import ru.forblitz.statistics.widget.data.SessionButtonsLayout
import ru.forblitz.statistics.widget.data.SessionButtonsLayout.ButtonsVisibility
import java.io.File
import java.nio.file.Files.*
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    // TODO: все еще рассмотрите создание класса Application и хранить сервисы там
    private lateinit var preferences: SharedPreferences

    private lateinit var apiService: ApiService
    private lateinit var userIDService: UserIDService
    private lateinit var randomService: RandomService
    private lateinit var ratingService: RatingService
    private lateinit var userClanService: UserClanService
    private lateinit var clanService: ClanService
    private lateinit var sessionService: SessionService
    private lateinit var versionService: VersionService
    private lateinit var vehicleSpecsService: VehicleSpecsService
    private lateinit var vehicleStatService: VehicleStatService
    private lateinit var adService: AdService

    //
    //
    //

    private lateinit var app : MyApplication

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app = (application as MyApplication)

        // TODO: change services to app.service

        // Configures ViewPager

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val mainLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)

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

        mainLayoutsFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME

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

        // Initialization of services

        apiService = ApiService(this@MainActivity)
        userIDService = UserIDService(context = this@MainActivity, apiService = apiService)
        randomService = RandomService(apiService = apiService)
        ratingService = RatingService(apiService = apiService)
        userClanService = UserClanService(apiService = apiService)
        clanService = ClanService(apiService = apiService)
        sessionService = SessionService(context = applicationContext)
        versionService = VersionService(activity = this@MainActivity)
        vehicleSpecsService = VehicleSpecsService(apiService = apiService)
        vehicleStatService = VehicleStatService(apiService = apiService)
        adService = AdService(this@MainActivity)

        // Creates a session directory

        sessionService.createSessionDir()

        // Get preferences

        preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Sets region

        setRegion()

        // Sets preferences listeners

        findViewById<View>(R.id.select_region_ru).setOnClickListener { preferences.edit().putString("region", "ru").apply(); setRegion() }
        findViewById<View>(R.id.select_region_eu).setOnClickListener { preferences.edit().putString("region", "eu").apply(); setRegion() }
        findViewById<View>(R.id.select_region_na).setOnClickListener { preferences.edit().putString("region", "na").apply(); setRegion() }
        findViewById<View>(R.id.select_region_asia).setOnClickListener { preferences.edit().putString("region", "asia").apply(); setRegion() }

        // set onBackPressed

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val randomLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.random_layouts_flipper)
                val ratingLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.rating_layouts_flipper)
                val clanLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.clan_layouts_flipper)
                val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)

                if (mainLayoutsFlipper.displayedChild == MainViewFlipperItems.SETTINGS) {
                    onClickSettingsButton(findViewById(R.id.settings_button))
                } else {
                    when (viewPager.currentItem) {
                        0 -> {
                            if (randomLayoutsFlipper.displayedChild != 0) {
                                randomLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.STATISTICS
                            }
                        }
                        1 -> {
                            if (ratingLayoutsFlipper.displayedChild != 0) {
                                ratingLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.STATISTICS
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
            versionCheck()
            // get vehicle specifications
            vehicleSpecsService.get()

            if (mainLayoutsFlipper.displayedChild == MainViewFlipperItems.STATISTICS) {
                setVehiclesStat()
            }

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
        val randomLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.random_layouts_flipper)
        val ratingLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.rating_layouts_flipper)
        val clanLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.clan_layouts_flipper)
        val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)

        val randomDetailsButtonView = findViewById<View>(R.id.random_details_button)
        val randomDetailsBack = findViewById<View>(R.id.random_details_back)
        val randomSessionListButton = findViewById<View>(R.id.random_sessions_list_button)
        val ratingDetailsButtonView = findViewById<View>(R.id.rating_details_button)
        val ratingDetailsBack = findViewById<View>(R.id.rating_details_back)
        val clanMembersButton = findViewById<View>(R.id.clan_members_button)
        val clanMembersListBackView = findViewById<View>(R.id.clan_members_back)
        val tanksDetailsBack = findViewById<View>(R.id.tanks_details_back)
        val tanksList = findViewById<ListView>(R.id.tanks_list)
        val tanksFilters = findViewById<View>(R.id.tanks_filters)

        val mainFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)
        val searchButton = findViewById<View>(R.id.search_button)
        val lastSearched = findViewById<View>(R.id.last_searched_flipper)

        //

        mainFlipper.displayedChild = MainViewFlipperItems.LOADING
        searchButton.isClickable = false
        lastSearched.isClickable = false
        findViewById<View>(R.id.settings_button).isActivated = false
        findViewById<SessionButtonsLayout>(R.id.random_session_buttons).setButtonsVisibility(
            ButtonsVisibility.NOTHING)

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
        randomLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.STATISTICS
        ratingLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.STATISTICS
        clanLayoutsFlipper.displayedChild = 0

        // Sets listeners for buttons

        randomDetailsButtonView.setOnClickListener {
            randomLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.FALSE
        }
        randomDetailsBack.setOnClickListener {
            randomLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.STATISTICS
        }
        randomSessionListButton.setOnClickListener {
            randomLayoutsFlipper.displayedChild = 2
        }
        ratingDetailsButtonView.setOnClickListener {
            ratingLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.FALSE
        }
        ratingDetailsBack.setOnClickListener {
            ratingLayoutsFlipper.displayedChild = StatisticsViewFlipperItems.STATISTICS
        }
        clanMembersButton.setOnClickListener {
            clanLayoutsFlipper.displayedChild = ClanViewFlipperItems.NOT_IS_A_MEMBER
        }
        clanMembersListBackView.setOnClickListener {
            clanLayoutsFlipper.displayedChild = ClanViewFlipperItems.IS_A_MEMBER
        }
        tanksDetailsBack.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 0
        }
        tanksFilters.setOnClickListener {
            tanksLayoutsFlipper.displayedChild = 3
        }

        // set params of tanksList

        tanksList.emptyView = findViewById(R.id.item_nothing_found)

        if (tanksList.footerViewsCount == 0) {
            val footer = View(this@MainActivity)
            val width = InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_very_big) * 2
            footer.layoutParams = AbsListView.LayoutParams(width, (width * 0.15).toInt())
            tanksList.addFooterView(footer)
        }

        // Gets the data of the player you are looking for

        adService.showInterstitial {
            CoroutineScope(Dispatchers.IO).launch {

                try {

                    userIDService.clear()
                    userIDService.get(searchField.text.toString())

                    // set player statistics

                    sessionService.clear()
                    vehicleStatService.clear()

                    setRandomStat()
                    setRatingStat()
                    setClanStat()
                    setVehiclesStat()

                } catch (e: ObjectException) {
                    runOnUiThread {
                        mainFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME
                        searchButton.isClickable = true
                        lastSearched.isClickable = true
                        searchField.setText("", TextView.BufferType.EDITABLE)

                        InterfaceUtils.createAlertDialog(
                            this@MainActivity,
                            getString(R.string.error) + " " + e.error.code,
                            e.message.toString()
                        ).show()
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

        val mainLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)

        if (!view.isActivated) {

            mainLayoutsFlipper.displayedChild = MainViewFlipperItems.SETTINGS

        } else if (view.isActivated) {

            mainLayoutsFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME

        }

        // Inverses isActivated

        view.isActivated = !view.isActivated

    }

    /**
     * Gets app version from the server and compares it with the [BuildConfig.VERSION_CODE]
     */
    private suspend fun versionCheck() {

        if (BuildConfig.VERSION_CODE in versionService.getMinimalAppVersion() until versionService.getCurrentAppVersion()) {

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

        } else if (BuildConfig.VERSION_CODE < versionService.getMinimalAppVersion()) {

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

    }

    private fun setRandomStat() {
        CoroutineScope(Dispatchers.IO).launch {

            randomService.clear()
            try {
                InterfaceUtils.setBaseStatistics(
                    this@MainActivity,
                    randomService.get(userIDService.get()),
                    true)
                setSessionStat(0)
            } catch (e: ObjectException) {
                InterfaceUtils.createAlertDialog(
                    this@MainActivity,
                    e.error.code,
                    e.message
                )
                findViewById<ViewFlipper>(R.id.main_layouts_flipper).displayedChild = MainViewFlipperItems.ENTER_NICKNAME
                findViewById<View>(R.id.settings_button).isActivated = false
            }

        }
    }

    private fun setRatingStat() {
        CoroutineScope(Dispatchers.IO).launch {

            ratingService.clear()
            InterfaceUtils.setRatingStatistics(this@MainActivity,
                ratingService.get(userIDService.get()),
                true)
            setSessionStat(0)

        }
    }

    private fun setSessionStat(index: Int) {
        if (randomService.get() == null || ratingService.get() == null || sessionService.alreadySet) {
            return
        }
        sessionService.alreadySet = true

        sessionService.createSessionFile(
            randomService.getJson(),
            userIDService.get(),
            ParseUtils.timestamp(randomService.getJson(), false),
            preferences.getString("region", "notSpecified")!!
        )
        sessionService.getList(userIDService.get(), preferences.getString("region", "notSpecified")!!)

        if (
            ParseUtils.timestamp(randomService.getJson(), false)
            ==
            ParseUtils.timestamp(sessionService.getList()[0], true)
        ) {
            sessionService.getList().removeAt(0)
        }

        val sessions = ArrayList<Session>(0)
        for (i in 0 until sessionService.getList().size) {
            val session = Session()
            session.path = sessionService.getList()[i]
            session.set = Runnable {
                sessionService.clear()
                setSessionStat(i)
                findViewById<DifferenceViewFlipper>(R.id.random_layouts_flipper).displayedChild = 0
            }
            session.delete = Runnable {
                if (i == index) {
                    Toast.makeText(this@MainActivity, getString(R.string.delete_select), Toast.LENGTH_SHORT).show()
                } else {
                    InterfaceUtils.createAlertDialog(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.delete),
                        this@MainActivity.getString(R.string.delete_alert),
                        this@MainActivity.getString(R.string.delete),
                        Runnable {
                            if (File(sessionService.getList()[i]).delete()) {
                                Toast.makeText(this@MainActivity, this@MainActivity.getString(R.string.delete_successfully), Toast.LENGTH_SHORT).show()
                                if (index != sessions.size - 1) {
                                    sessionService.clear()
                                    setSessionStat(index)
                                } else {
                                    sessionService.clear()
                                    setSessionStat(index - 1)
                                }
                                findViewById<DifferenceViewFlipper>(R.id.random_layouts_flipper).displayedChild = 0
                            } else {
                                Toast.makeText(this@MainActivity, this@MainActivity.getString(R.string.delete_failed), Toast.LENGTH_SHORT).show()
                            }
                        },
                        this@MainActivity.getString(android.R.string.cancel),
                        Runnable {  }
                    ).show()
                }
            }
            session.isSelected = i == index
            sessions.add(session)
        }

        val randomSessionButtons = findViewById<SessionButtonsLayout>(R.id.random_session_buttons)
        val randomFastStat = findViewById<PlayerFastStat>(R.id.random_fast_stat)
        val ratingFastStat = findViewById<PlayerFastStat>(R.id.rating_fast_stat)
        val randomSessionsList = findViewById<ListView>(R.id.random_sessions_list)
        val randomSessionStatButton = findViewById<TextView>(R.id.random_session_stat_button)
        val fragmentRandom = findViewById<ViewFlipper>(R.id.fragment_random)
        val fragmentRating = findViewById<ViewFlipper>(R.id.fragment_rating)

        runOnUiThread {

            randomSessionButtons.setButtonsVisibility(
                when(sessionService.getList().size) {
                    0 -> {
                        ButtonsVisibility.NOTHING
                    }
                    1 -> {
                        ButtonsVisibility.ONLY_FLIP
                    }
                    else -> {
                        ButtonsVisibility.ALL
                    }
                }
            )

            randomFastStat.setSessionData(
                when(sessionService.getList().size) {
                    0 -> {
                        StatisticsData()
                    }
                    else -> {
                        SessionUtils.calculateDifferences(
                            randomService.get(),
                            ParseUtils.statisticsData(
                                File(sessionService.getList()[index]).readText(),
                                "all"
                            )
                        )
                    }
                }
            )

            ratingFastStat.setSessionData(
                when(sessionService.getList().size) {
                    0 -> {
                        StatisticsData()
                    }
                    else -> {
                        SessionUtils.calculateDifferences(
                            ratingService.get(),
                            ParseUtils.statisticsData(
                                File(sessionService.getList()[index]).readText(),
                                "rating"
                            )
                        )
                    }
                }
            )

            randomSessionsList.adapter = SessionAdapter(
                this@MainActivity, sessions
            )

            if (randomSessionStatButton.isActivated) {
                randomSessionStatButton.setText(R.string.to_session_stat)
                randomFastStat.setData(randomService.get())
                ratingFastStat.setData(ratingService.get())
                randomSessionStatButton.isActivated = false
            }
            randomSessionStatButton.setOnClickListener {
                fragmentRandom.startAnimation(fragmentRandom.outAnimation)
                fragmentRating.startAnimation(fragmentRating.outAnimation)

                if (!randomSessionStatButton.isActivated) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        randomSessionStatButton.text = getString(R.string.from_session_stat)

                        randomFastStat.setData(SessionUtils.calculate(
                            randomService.get(),
                            ParseUtils.statisticsData(
                                File(sessionService.getList()[index]).readText(),
                                "all"
                            )
                        ))
                        ratingFastStat.setData(SessionUtils.calculate(
                            ratingService.get(),
                            ParseUtils.statisticsData(
                                File(sessionService.getList()[index]).readText(),
                                "rating"
                            )
                        ))

                        fragmentRandom.startAnimation(fragmentRandom.inAnimation)
                        fragmentRating.startAnimation(fragmentRating.inAnimation)
                        randomSessionStatButton.isActivated = true
                    }, 125)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        randomSessionStatButton.text = getString(R.string.to_session_stat)

                        randomFastStat.setData(randomService.get())
                        ratingFastStat.setData(ratingService.get())

                        fragmentRandom.startAnimation(fragmentRandom.inAnimation)
                        fragmentRating.startAnimation(fragmentRating.inAnimation)
                        randomSessionStatButton.isActivated = false
                    }, 125)
                }
            }

            // Displays the loading screen on tanks layout and waits for data loading to finish

            findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper).displayedChild = MainViewFlipperItems.STATISTICS
            findViewById<View>(R.id.search_button).isClickable = true
            findViewById<View>(R.id.last_searched_flipper).isClickable = true
        }
    }

    /**
     * Called when the [apply filters button]
     * [ru.forblitz.statistics.R.id.tanks_apply_filters] or the search
     * button on the keyboard is pressed. Creates a display of suitable vehicle
     * statistics.
     */
    private fun setVehiclesStat() {
        if (vehicleSpecsService.getListSize() != 0) {

            val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)
            val tanksApplyFilters = findViewById<View>(R.id.tanks_apply_filters)

            runOnUiThread {
                tanksLayoutsFlipper.displayedChild = 2
            }
            tanksApplyFilters.setOnClickListener {
                setVehiclesStat()
                InterfaceUtils.playCycledAnimation(
                    tanksApplyFilters,
                    true
                )
            }

            CoroutineScope(Dispatchers.IO).launch {

                val pairs = HashMap<String, Pair<VehicleSpecs, VehicleStat>>()

                vehicleStatService.get(userIDService.get(), vehicleSpecsService.get().keys.toTypedArray()).forEach {
                    pairs[it.key] = Pair(vehicleSpecsService.get()[it.key]!!, it.value)
                }

                //

                val tanksList = findViewById<ListView>(R.id.tanks_list)
                
                val type = HashMap<String, View>()
                type["lightTank"] = findViewById(R.id.tanks_type_lt)
                type["mediumTank"] = findViewById(R.id.tanks_type_mt)
                type["heavyTank"] = findViewById(R.id.tanks_type_ht)
                type["AT-SPG"] = findViewById(R.id.tanks_type_at)
                
                val nation = HashMap<String, View>()
                nation["china"] = findViewById(R.id.cn)
                nation["european"] = findViewById(R.id.eu)
                nation["france"] = findViewById(R.id.fr)
                nation["uk"] = findViewById(R.id.gb)
                nation["germany"] = findViewById(R.id.de)
                nation["japan"] = findViewById(R.id.jp)
                nation["other"] = findViewById(R.id.other)
                nation["usa"] = findViewById(R.id.us)
                nation["ussr"] = findViewById(R.id.su)

                val tier = HashMap<Int, View>()
                tier[1] = findViewById(R.id.tanks_tier_i)
                tier[2] = findViewById(R.id.tanks_tier_ii)
                tier[3] = findViewById(R.id.tanks_tier_iii)
                tier[4] = findViewById(R.id.tanks_tier_iv)
                tier[5] = findViewById(R.id.tanks_tier_v)
                tier[6] = findViewById(R.id.tanks_tier_vi)
                tier[7] = findViewById(R.id.tanks_tier_vii)
                tier[8] = findViewById(R.id.tanks_tier_viii)
                tier[9] = findViewById(R.id.tanks_tier_ix)
                tier[10] = findViewById(R.id.tanks_tier_x)

                val tanksFilters = findViewById<FloatingActionButton>(R.id.tanks_filters)

                //
                ////
                //////
                ////
                //

                val sortedVehicles: ArrayList<Pair<VehicleSpecs, VehicleStat>> = ArrayList(pairs.values)
                val vehiclesToCreate: ArrayList<Pair<VehicleSpecs, VehicleStat>> = ArrayList(0)

                val comparatorByBattles: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator<Pair<VehicleSpecs, VehicleStat>> {
                        v1, v2 -> v1.second.all.battles.toInt().compareTo(v2.second.all.battles.toInt())
                }
                val comparatorByAverageDamage: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator<Pair<VehicleSpecs, VehicleStat>> {
                        v1, v2 -> v1.second.all.averageDamage.toDouble().compareTo(v2.second.all.averageDamage.toDouble())
                }
                val comparatorByWinRate: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator<Pair<VehicleSpecs, VehicleStat>> {
                        v1, v2 -> v1.second.all.winRate.toDouble().compareTo(v2.second.all.winRate.toDouble())
                }
                val comparatorByAverageXp: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator<Pair<VehicleSpecs, VehicleStat>> {
                        v1, v2 -> v1.second.all.averageXp.toDouble().compareTo(v2.second.all.averageXp.toDouble())
                }
                val comparatorByEfficiency: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator<Pair<VehicleSpecs, VehicleStat>> {
                        v1, v2 -> v1.second.all.efficiency.toDouble().compareTo(v2.second.all.efficiency.toDouble())
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
                if (sortedVehicles.filter { it.second.all.battles.toInt() > 1 }.size < 2) {
                    runOnUiThread { tanksFilters.hide() }
                } else {
                    runOnUiThread { tanksFilters.show() }
                }

                sortedVehicles
                    .filter { it.second.all.battles.toInt() > 0 }
                    .filter {
                        var filterIsActivated = false
                        type.forEach { typeView -> if (typeView.value.isActivated) { filterIsActivated = true } }
                        if (filterIsActivated) {
                            type[it.first.type]!!.isActivated
                        } else {
                            true
                        }
                    }
                    .filter {
                        var filterIsActivated = false
                        nation.forEach { nationView -> if (nationView.value.isActivated) { filterIsActivated = true } }

                        if (filterIsActivated) {
                            nation[it.first.nation]!!.isActivated
                        } else {
                            true
                        }
                    }
                    .filter {
                        var filterIsActivated = false
                        tier.forEach { tierView -> if (tierView.value.isActivated) { filterIsActivated = true } }

                        if (filterIsActivated) {
                            tier[it.first.tier]!!.isActivated
                        } else {
                            true
                        }
                    }
                    .forEach {
                        vehiclesToCreate.add(it)
                    }

                    runOnUiThread {
                        tanksList.adapter = VehicleAdapter(this@MainActivity, vehiclesToCreate)

                        val adView = findViewById<BannerAdView>(R.id.tanks_list_banner)
                        adService.setBanner(
                            InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_big),
                            adView
                        )
                        adView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            bottomToBottom = R.id.tanks_list_layout
                        }

                        tanksLayoutsFlipper.displayedChild = 0
                    }
            }
        }
    }

    /**
     * Gets clan statistics and shows statistics interface elements
     */
    private fun setClanStat() {
        CoroutineScope(Dispatchers.IO).launch {

            userClanService.clear()
            clanService.clear()

            if (userClanService.get(userIDService.get()) != null) {
                runOnUiThread {
                    findViewById<ClanSmall>(R.id.random_clan).setData(userClanService.get())
                    findViewById<ClanSmall>(R.id.rating_clan).setData(userClanService.get())
                }
                clanService.get(userClanService.get())
                runOnUiThread {
                    findViewById<ClanScreen>(R.id.fragment_clan).setData(userClanService.get(), clanService.get())
                }
            } else {
                findViewById<ClanScreen>(R.id.fragment_clan).setData(null, clanService.get())
            }

        }
    }

    /**
     * Sets the background for the region selection buttons, change region in [apiService]
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

                apiService.setRegion("ru")

            }
            "ru" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    0
                )

                apiService.setRegion("ru")
            }
            "eu" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    1
                )

                apiService.setRegion("eu")
            }
            "na" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    2
                )

                apiService.setRegion("na")
            }
            "asia" -> {
                InterfaceUtils.setSelectedRegion(
                    this@MainActivity,
                    3
                )

                apiService.setRegion("asia")
            }
        }
        updateLastSearch()
    }

    /**
     * Searches for saved sessions to suggest them instead of entering a nickname
     */
    private fun updateLastSearch() {

        val lastSearchedFlipper = findViewById<DifferenceViewFlipper>(R.id.last_searched_flipper)
        val enterNicknameText = findViewById<TextView>(R.id.enter_nickname_text)

        val lastFile = sessionService.getLastFile(preferences.getString("region", "notSpecified")!!)

        if (lastFile != null) {
            val lastSearchedName = findViewById<TextView>(R.id.last_searched_name)
            val lastSearchedInfo = findViewById<TextView>(R.id.last_searched_info)

            lastSearchedName.text = ParseUtils.statisticsData(lastFile.readText(), "all").nickname
            val lastSearchedInfoText = getString(R.string.last_search) + ParseUtils.time((lastFile.lastModified() / 1000).toString())
            lastSearchedInfo.text = lastSearchedInfoText
            enterNicknameText.setText(R.string.enter_nickname_or_select)

            lastSearchedFlipper.setOnClickListener {
                findViewById<EditText>(R.id.search_field).setText(lastSearchedName.text, TextView.BufferType.EDITABLE)
                onClickSearchButton(findViewById(R.id.search_button))
            }

            lastSearchedFlipper.displayedChild = 0

        } else {
            enterNicknameText.setText(R.string.enter_nickname)

            lastSearchedFlipper.displayedChild = 1
        }

    }

}
