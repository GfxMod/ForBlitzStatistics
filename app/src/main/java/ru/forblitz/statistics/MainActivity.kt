package ru.forblitz.statistics

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.LocaleList
import android.os.Looper
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.TextView.BufferType
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.LocaleListCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import androidx.room.Room.databaseBuilder
import androidx.viewpager.widget.ViewPager
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.tabs.TabLayout
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import ru.forblitz.statistics.adapters.AchievementsAdapter
import ru.forblitz.statistics.adapters.LastSearchedAdapter
import ru.forblitz.statistics.adapters.RequestLogAdapter
import ru.forblitz.statistics.adapters.SessionAdapter
import ru.forblitz.statistics.adapters.VehicleAdapter
import ru.forblitz.statistics.adapters.ViewPagerAdapter
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.api.ApiServiceForBlitz
import ru.forblitz.statistics.apiloadservice.APILoadService
import ru.forblitz.statistics.apiloadservice.AchievementsInformationService
import ru.forblitz.statistics.apiloadservice.ClanInformationService
import ru.forblitz.statistics.apiloadservice.MetadataService
import ru.forblitz.statistics.apiloadservice.UserAchievementsService
import ru.forblitz.statistics.apiloadservice.UserClanService
import ru.forblitz.statistics.apiloadservice.UserService
import ru.forblitz.statistics.apiloadservice.UserStatisticsService
import ru.forblitz.statistics.apiloadservice.VehicleSpecsService
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.data.Constants.AchievementsViewFlipperItems
import ru.forblitz.statistics.data.Constants.ClanBriefFlipperItems
import ru.forblitz.statistics.data.Constants.ClanViewFlipperItems
import ru.forblitz.statistics.data.Constants.MainViewFlipperItems
import ru.forblitz.statistics.data.Constants.PlayerStatisticsTypes
import ru.forblitz.statistics.data.Constants.StatisticsViewFlipperItems
import ru.forblitz.statistics.data.Constants.TABS_COUNT
import ru.forblitz.statistics.data.Constants.ViewPagerItems
import ru.forblitz.statistics.data.Constants.achievementsInRow
import ru.forblitz.statistics.data.Constants.animationDurationMillis
import ru.forblitz.statistics.data.RecordDatabase
import ru.forblitz.statistics.dto.AchievementInfo
import ru.forblitz.statistics.dto.Record
import ru.forblitz.statistics.dto.Session
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.dto.VehiclesStatisticsResponse
import ru.forblitz.statistics.exception.ObjectException
import ru.forblitz.statistics.helpers.ActivityResultActionManager
import ru.forblitz.statistics.service.AdService
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.PreferencesService
import ru.forblitz.statistics.service.RequestsService
import ru.forblitz.statistics.service.SessionService
import ru.forblitz.statistics.service.UserVehiclesStatisticsService
import ru.forblitz.statistics.utils.HapticUtils
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.utils.StatisticsDataUtils
import ru.forblitz.statistics.utils.Utils
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper
import ru.forblitz.statistics.widget.common.ExtendedImageButton
import ru.forblitz.statistics.widget.common.ExtendedRadioGroup
import ru.forblitz.statistics.widget.data.AchievementsScreen
import ru.forblitz.statistics.widget.data.ClanBrief
import ru.forblitz.statistics.widget.data.ClanScreen
import ru.forblitz.statistics.widget.data.PlayerFastStat
import ru.forblitz.statistics.widget.data.SessionButtonsLayout
import ru.forblitz.statistics.widget.data.SessionButtonsLayout.ButtonsVisibility
import ru.forblitz.statistics.widget.data.SettingsSwitchesList
import java.util.Collections
import java.util.Locale


class MainActivity : AppCompatActivity() {

    /**
    * Variable for accessing application services
     */
    private lateinit var app: ForBlitzStatisticsApplication

    /**
    * Is the keyboard currently showing
     */
    private var isKeyboardShowing: Boolean = false

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            activityResultActionManager.invoke(result.data)
        }
    }

    private val activityResultActionManager = ActivityResultActionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialization of app variable

        app = application as ForBlitzStatisticsApplication

        // Configures ViewPager and TabLayout

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val mainLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)

        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, TABS_COUNT)
        tabLayout.setupWithViewPager(viewPager)

        val tabLayoutHeight = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()
        for (i in 0 until TABS_COUNT) {
            var icon: Drawable
            var contentDescription: String
            when (i) {
                0 -> {
                    icon = AppCompatResources.getDrawable(applicationContext, R.drawable.ic_outline_bar_chart_36)!!
                    contentDescription = getString(R.string.random)
                }
                1 -> {
                    icon = AppCompatResources.getDrawable(applicationContext, R.drawable.outline_military_tech_24)!!
                    contentDescription = getString(R.string.achievements)
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
            if (tabLayout.getTabAt(i) == null) {
                tabLayout.addTab(tabLayout.newTab().setCustomView(view))
            } else {
                tabLayout.getTabAt(i)?.customView = view
            }
        }

        viewPager.offscreenPageLimit = TABS_COUNT - 1

        // Sets animations and dimensions for 'enter nickname' TextSwitcher

        val enterNicknameText = findViewById<TextSwitcher>(R.id.enter_nickname_text)

        enterNicknameText.inAnimation = AnimationUtils.loadAnimation(
            this@MainActivity,
            R.anim.appearance
        ).apply { duration =
            resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }
        enterNicknameText.outAnimation = AnimationUtils.loadAnimation(
            this@MainActivity,
            R.anim.disappearance
        ).apply { duration =
            resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        }

        //

        findViewById<View>(R.id.enter_nickname_text)
            .updateLayoutParams<ConstraintLayout.LayoutParams> {
                width = ConstraintLayout.LayoutParams.MATCH_PARENT
                height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()
                startToStart = R.id.enter_nickname_layout
                endToEnd = R.id.enter_nickname_layout
                bottomToBottom = R.id.enter_nickname_layout
            }

        // Shows 'enter nickname' screen

        mainLayoutsFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME

        // Sets the action when the search button is pressed from the keyboard

        val searchField = findViewById<EditText>(R.id.search_field)
        searchField.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    findViewById<View>(R.id.search_button).performClick()
                    true
                }
                else -> false
            }
        }

        searchField.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    findViewById<View>(R.id.search_button).performClick()
                    return true
                }
                return false
            }
        })

        // Initialization of services

        app.connectivityService = ConnectivityService()
        app.requestsService = RequestsService(this@MainActivity)
        app.metadataService = MetadataService(
            ApiServiceForBlitz(
                app.connectivityService,
                app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
                app.requestsService
            )
        )
        app.apiService = ApiService(
            app.connectivityService,
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
            app.requestsService,
            app.metadataService.tokens
        )
        app.userService = UserService(app.apiService)
        app.userStatisticsService = UserStatisticsService(app.apiService)
        app.userClanService = UserClanService(app.apiService)
        app.clanInformationService = ClanInformationService(app.apiService)
        app.sessionService = SessionService(applicationContext)
        app.vehicleSpecsService = VehicleSpecsService(app.apiService)
        app.userVehiclesStatisticsService = UserVehiclesStatisticsService(app.apiService)
        app.userAchievementsService = UserAchievementsService(app.apiService)
        app.achievementsInformationService = AchievementsInformationService(app.apiService)
        app.adService = AdService(
            this@MainActivity,
            app.metadataService.tokens
        )
        app.recordDatabase = databaseBuilder(
            applicationContext,
            RecordDatabase::class.java, "history-database"
        ).build()

        // Initialization of app.preferences

        app.preferencesService = PreferencesService(getSharedPreferences("settings", MODE_PRIVATE))

        // Getting the set settings

        Constants.preferencesTags.forEach {
            app.setSettings[it] = app.preferencesService.get(it)
        }

        // Sets app.preferences listeners

        val searchRegionLayout = findViewById<ExtendedRadioGroup>(R.id.search_region_layout)
        val settingsRegionLayout = findViewById<ExtendedRadioGroup>(R.id.settings_region_layout)
        val settingsLocaleLayout = findViewById<ExtendedRadioGroup>(R.id.settings_locale_layout)
        val settingsSessionsImport = findViewById<View>(R.id.settings_sessions_import)
        val settingsSessionsExport = findViewById<View>(R.id.settings_sessions_export)

        for (i in 0 until searchRegionLayout.childCount) {
            val view = searchRegionLayout.getChildAt(i)
            view.setOnClickListener {
                if (Constants.baseUrl.containsKey((view.tag.toString()))) {
                    app.preferencesService.region = view.tag.toString()
                    setRegion()
                    updateLastSearch(false)
                }
            }
        }
        for (i in 0 until settingsRegionLayout.childCount) {
            val view = settingsRegionLayout.getChildAt(i)
            view.setOnClickListener {
                if (Constants.baseUrl.containsKey((view.tag.toString()))) {
                    app.preferencesService.region = view.tag.toString()
                    setRegion()
                }
            }
        }
        for (i in 0 until Constants.localeCodes.size) {
            val locale = Constants.localeCodes.keys.toTypedArray()[i]

            settingsLocaleLayout.addView(
                InterfaceUtils.createLocaleItem(
                    this@MainActivity,
                    locale
                ) {
                    changeLocale(locale)
                }
            )
        }
        settingsSessionsImport.setOnClickListener {
            app.sessionService.importSessionsWithPicker(
                activityResultActionManager,
                activityResultLauncher
            ) {
                with(intent) {
                    finish()
                    startActivity(this)
                }
            }
        }
        settingsSessionsExport.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                app.sessionService.exportFBSS().also { file ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.sessions_exported_to, file.name),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Configured settings dimensions

        settingsRegionLayout.childHeight = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()
        settingsLocaleLayout.childHeight = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()

        findViewById<View>(R.id.settings_region).updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.05).toInt()
        }

        findViewById<View>(R.id.settings_locale).updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.05).toInt()
        }

        findViewById<View>(R.id.settings_sessions_io).updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.05).toInt()
        }

        settingsSessionsImport.updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.075).toInt()
        }

        settingsSessionsExport.updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.075).toInt()
        }

        findViewById<View>(R.id.settings_switches_name).updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.05).toInt()
        }

        // Sets the size of the text for the settings

        val settingsSwitchesList = findViewById<SettingsSwitchesList>(R.id.settings_switches_list)
        settingsSwitchesList.textSize = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.01).toInt()

        // Creates settings elements

        app.setSettings.toList().forEach {
            settingsSwitchesList.addItem(
                it.first,
                Utils.getStringResourceByName(this@MainActivity, it.first),
                Utils.getStringResourceByName(this@MainActivity, "${it.first}${Constants.stringResourcesDescriptionPostfix}"),
                it.second
            )
                .findViewWithTag<MaterialSwitch>("switch")
                .setOnCheckedChangeListener { _, isChecked ->
                    app.preferencesService.set(it.first, isChecked)
                    app.setSettings.replace(it.first, isChecked)
                }
        }

        // Sets onBackPressed

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val statisticsScreen = findViewById<DifferenceViewFlipper>(R.id.fragment_statistics)
                val clanScreen = findViewById<DifferenceViewFlipper>(R.id.fragment_clan)
                val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)

                if (mainLayoutsFlipper.displayedChild == MainViewFlipperItems.SETTINGS) {
                    findViewById<View>(R.id.settings_button).performClick()
                } else {
                    when (viewPager.currentItem) {
                        ViewPagerItems.FAST_STAT -> {
                            if (statisticsScreen.displayedChild != StatisticsViewFlipperItems.FALSE) {
                                statisticsScreen.displayedChild = StatisticsViewFlipperItems.STATISTICS
                            }
                        }
                        ViewPagerItems.CLAN -> {
                            if (clanScreen.displayedChild == ClanViewFlipperItems.MEMBERS_LIST) {
                                clanScreen.displayedChild = ClanViewFlipperItems.CLAN_INFORMATION
                            }
                        }
                        ViewPagerItems.VEHICLES -> {
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

        // Initialization of ads

        MobileAds.initialize(this) { Log.i("YandexMobileAds", "SDK initialized") }

    }

    override fun onStart() {
        super.onStart()
        app.connectivityService.subscribe(this)

        // Set keyboard visibility listener

        val contentView = findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)!!
        findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)!!
            .viewTreeObserver.addOnGlobalLayoutListener {
                val displayFrameRect = Rect()
                contentView.getWindowVisibleDisplayFrame(displayFrameRect)
                val screenHeight = contentView.rootView.height

                val keypadHeight = screenHeight - displayFrameRect.bottom
                if (keypadHeight > screenHeight * 0.15) {
                    if (!isKeyboardShowing) {
                        isKeyboardShowing = true
                        onKeyboardVisibilityChanged()
                    }
                } else {
                    if (isKeyboardShowing) {
                        isKeyboardShowing = false
                        onKeyboardVisibilityChanged()
                    }
                }
            }

        // check is privacy policy accepted

        checkIsPrivacyPolicyAccepted()

        // Set region

        setRegion()
        updateLastSearch(true)
        setLoadingIndicator()

        // Displays the selected locale in the settings

        val prefLocale = app.preferencesService.locale
        if (prefLocale != "notSpecified" && prefLocale != null) {
            findViewById<ExtendedRadioGroup>(R.id.settings_locale_layout).setCheckedItem(prefLocale)
        }

        // Check version and get tankopedia

        CoroutineScope(Dispatchers.IO).launch {
            // Loads tokens
            app.metadataService.get(null)
            app.metadataService.addTaskOnEndOfLoad {
                // Checks if the app version is up-to-date
                versionCheck()
                CoroutineScope(Dispatchers.IO).launch {
                    // Get tankopedia
                    app.vehicleSpecsService.get(null)
                    app.achievementsInformationService.get(null)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<DifferenceViewFlipper>(R.id.fragment_statistics)?.also { statisticsLayoutFlipper ->
            if (statisticsLayoutFlipper.displayedChild == StatisticsViewFlipperItems.STATISTICS) {
                app.userService.nickname?.also { nickname ->
                    findViewById<EditText>(R.id.search_field)?.apply {
                        setText(nickname, BufferType.EDITABLE)
                    }
                }
            }
        }
    }

    /**
     * Restarts the activity with the locale set
     * @param locale Locale code to be applied
     */
    private fun changeLocale(locale: String) {
        app.preferencesService.locale = locale

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(locale)
            AppCompatDelegate.setApplicationLocales(appLocale)
        } else {
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        applyOverrideConfiguration(Configuration())
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        val config = Configuration(overrideConfiguration)
        val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val prefLocale = preferences.getString("locale", "notSpecified")

        if (prefLocale != "notSpecified" && prefLocale != null) {
            config.setLocale(Locale(prefLocale))
        } else {
            config.setLocale(Locale("en"))
            preferences.edit().putString("locale", "en").apply()

            val systemLocales = LocaleList.getDefault()
            for (i in 0 until systemLocales.size()) {
                if (Constants.localeCodes.contains(systemLocales[i].language)) {
                    config.setLocale(Locale(systemLocales[i].language))
                    preferences.edit().putString("locale", systemLocales[i].language).apply()
                    break
                }
            }
        }

        super.applyOverrideConfiguration(config)
    }

    /**
     * Called when the keyboard visibility has changed
     */
    private fun onKeyboardVisibilityChanged() {
        findViewById<EditText>(R.id.search_field).isCursorVisible = isKeyboardShowing

        CoroutineScope(Dispatchers.Main).launch {
            InterfaceUtils.setRegionAlertVisibility(
                this@MainActivity,
                findViewById(R.id.enter_nickname_layout),
                findViewById(R.id.enter_nickname_text),
                findViewById(R.id.search_region_layout),
                isKeyboardShowing
            )

            if (isKeyboardShowing) {
                findViewById<TextSwitcher>(R.id.enter_nickname_text).setText(getString(
                    R.string.region_select
                ))
            } else {
                updateLastSearch(true)
            }
        }
    }

    /**
     * It is called when you press the [search button]
     * [ru.forblitz.statistics.R.id.search_button], the search button on the
     * keyboard, or the Enter button while entering a nickname. Performs
     * necessary all actions to view statistics. It can also be called forcibly
     * to perform a search.
     */
    fun onClickSearchButton(searchButton: View) {
        runOnUiThread {
            if (!app.searchProcessingController.searchProcessing) {
                app.searchProcessingController.startSearchProcessing()

                val searchField = findViewById<EditText>(R.id.search_field)
                val imm: InputMethodManager = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

                val clanScreen = findViewById<DifferenceViewFlipper>(R.id.fragment_clan)
                val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)

                val statisticsDetailsButton = findViewById<View>(R.id.statistics_details_button)
                val statisticsDetailsBack = findViewById<View>(R.id.statistics_details_back)
                val statisticsSessionButtons = findViewById<SessionButtonsLayout>(R.id.statistics_session_buttons)
                val clanMembersButton = findViewById<View>(R.id.clan_members_button)
                val clanMembersListBackView = findViewById<View>(R.id.clan_members_back)
                val tanksDetailsBack = findViewById<View>(R.id.tanks_details_back)
                val tanksList = findViewById<ListView>(R.id.tanks_list)
                val tanksFilters = findViewById<View>(R.id.tanks_filters)
                val sessionsReloadButton = findViewById<ExtendedImageButton>(R.id.statistics_session_reload)

                val mainFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)
                val statisticsScreen = findViewById<DifferenceViewFlipper>(R.id.fragment_statistics)
                val achievementsFlipper = findViewById<DifferenceViewFlipper>(R.id.achievements_screen)
                val statisticsSessionStatButton = statisticsSessionButtons.statisticsButton

                // Shows the loading screen and the loading indicator, blocks all
                // interactive elements during loading

                mainFlipper.displayedChild = MainViewFlipperItems.LOADING
                achievementsFlipper.displayedChild = AchievementsViewFlipperItems.LOADING
                findViewById<View>(R.id.settings_button).isActivated = false
                findViewById<SessionButtonsLayout>(R.id.statistics_session_buttons).setButtonsVisibility(ButtonsVisibility.NOTHING)
                if (statisticsSessionStatButton.isActivated) {
                    statisticsSessionStatButton.setText(R.string.to_session_stat)
                    statisticsSessionStatButton.isActivated = false
                }

                // Plays animation

                InterfaceUtils.playCycledAnimation(
                    searchButton,
                    false
                )

                // Hides keyboard

                imm.hideSoftInputFromWindow(searchField.windowToken, 0)
                searchField.clearFocus()

                // Sets listeners for buttons

                statisticsDetailsButton.setOnClickListener {
                    statisticsScreen.displayedChild = StatisticsViewFlipperItems.DETAILS
                }
                statisticsDetailsBack.setOnClickListener {
                    statisticsScreen.displayedChild = StatisticsViewFlipperItems.STATISTICS
                }
                statisticsSessionButtons.actions.listButtonAction = {
                    statisticsScreen.displayedChild = StatisticsViewFlipperItems.SESSIONS_LIST
                }
                clanMembersButton.setOnClickListener {
                    clanScreen.displayedChild = ClanViewFlipperItems.MEMBERS_LIST
                }
                clanMembersListBackView.setOnClickListener {
                    HapticUtils.performHapticFeedback(clanMembersListBackView)
                    clanScreen.displayedChild = ClanViewFlipperItems.CLAN_INFORMATION
                }
                tanksDetailsBack.setOnClickListener {
                    tanksLayoutsFlipper.displayedChild = 0
                }
                tanksFilters.setOnClickListener {
                    HapticUtils.performHapticFeedback(tanksFilters)
                    tanksLayoutsFlipper.displayedChild = 3
                }
                sessionsReloadButton.setOnClickListener {
                    searchButton.performClick()
                }

                // Set params of tanksList

                tanksList.emptyView = findViewById(R.id.item_nothing_found)

                if (tanksList.footerViewsCount == 0) {
                    val footer = View(this@MainActivity)
                    val width = InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_very_big) * 2
                    footer.layoutParams = AbsListView.LayoutParams(width, (width * 0.15).toInt())
                    tanksList.addFooterView(footer)
                }

                tanksLayoutsFlipper.displayedChild = 2

                // Updates logging display

                updateLoggingDisplay()

                // Does everything to show statistics, but first waits for tokens and
                // ads to be loaded, if necessary

                app.metadataService.addTaskOnEndOfLoad {
                    app.adService.showInterstitial {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {

                                app.userService.clear()
                                try {
                                    app.userService.get(UserService.Arguments(searchField.text.toString()))
                                } catch (serverException: APILoadService.ServerException) {
                                    throw ObjectException(
                                        "${getString(R.string.error)} ${serverException.responseError.code}",
                                        serverException.responseError.message
                                    )
                                }

                                if (app.userService.data!!.data.isEmpty()) {
                                    throw ObjectException(
                                        "${getString(R.string.error)} 404",
                                        getString(R.string.nickname_not_found)
                                            .replace(
                                                "XXX",
                                                app.preferencesService.region!!
                                                    .uppercase()
                                            )
                                    )
                                }

                                // account ID successfully loaded

                                runOnUiThread {
                                    findViewById<EditText>(R.id.search_field).setText(
                                        app.userService.nickname!!,
                                        BufferType.EDITABLE
                                    )
                                }
                                app.recordDatabase.recordDao().addRecord(
                                    Record(
                                        app.userService.accountId!!,
                                        app.userService.nickname!!,
                                        System.currentTimeMillis().toString(),
                                        app.preferencesService.region!!
                                    )
                                )

                                // set player statistics

                                setPlayerStat()
                                setClanStat()
                                setAchievements()
                                app.vehicleSpecsService.addTaskOnEndOfLoad {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        setVehiclesStat()
                                    }
                                }

                            } catch (e: ObjectException) {
                                app.searchProcessingController.stopSearchProcessing()
                                runOnUiThread {
                                    mainFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME
                                    searchField.setText("", BufferType.EDITABLE)

                                    InterfaceUtils.createAlertDialog(
                                        this@MainActivity,
                                        e.title,
                                        e.message
                                    ).show()
                                }
                            }
                        }
                    }
                }

            } else {
                Toast.makeText(this@MainActivity, getString(R.string.please_wait), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Called when the [settings button]
     * [ru.forblitz.statistics.R.id.settings_button] is pressed. Shows the
     * settings screen or returns to the start screen.
     */
    fun onClickSettingsButton(view: View) {

        // Plays animation

        InterfaceUtils.playCycledAnimation(
            view,
            true
        )

        // Shows the settings screen or returns to the start screen

        val mainLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)

        if (!view.isActivated) {

            mainLayoutsFlipper.displayedChild = MainViewFlipperItems.SETTINGS

        } else if (view.isActivated) {

            mainLayoutsFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME
            updateLastSearch(true)

        }

        // Inverses isActivated

        view.isActivated = !view.isActivated

    }

    /**
     * Gets minimal and recommended app version from the server and compares it with the
     * [BuildConfig.VERSION_CODE]. Shows an [AlertDialog][androidx.appcompat.app.AlertDialog] about the update, if necessary.
     */
    private fun versionCheck() {

        // If the version is less than the minimum, it is necessary to show an
        // unclosable AlertDialog about the need for an update. If it is less
        // than recommended, then it is necessary to show a closable
        // AlertDialog that an update is recommended.

        if (BuildConfig.VERSION_CODE in app.metadataService.minimalAppVersion until app.metadataService.currentAppVersion) {

            runOnUiThread {
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
            }

        } else if (BuildConfig.VERSION_CODE < app.metadataService.minimalAppVersion) {

            runOnUiThread {
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

    }

    /**
     * Does all the actions necessary to display player statistics for the
     * entered nickname
     */
    private fun setPlayerStat() {
        val mainLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper)

        CoroutineScope(Dispatchers.IO).launch {

            app.userStatisticsService.clear()
            app.userVehiclesStatisticsService.clear()

            app.userStatisticsService.addTaskOnEndOfLoad {
                runOnUiThread {
                    mainLayoutsFlipper.displayedChild = MainViewFlipperItems.STATISTICS

                    with(findViewById<MaterialButton>(R.id.statistics_toggle_random)) {
                        visibility = if (app.userStatisticsService.randomStatistics.battles != 0) {
                            isChecked = true
                            VISIBLE
                        } else {
                            isChecked = false
                            GONE
                        }
                        addOnCheckedChangeListener { _, _ ->
                            updatePlayerStatistics()
                            updateSessionStatistics(app.sessionService.selectedSessionIndex) }
                    }
                    with(findViewById<MaterialButton>(R.id.statistics_toggle_rating)) {
                        visibility = if (app.userStatisticsService.ratingStatistics.battles != 0) {
                            isChecked = true
                            VISIBLE
                        } else {
                            isChecked = false
                            GONE
                        }
                        addOnCheckedChangeListener { _, _ ->
                            updatePlayerStatistics()
                            updateSessionStatistics(app.sessionService.selectedSessionIndex)
                        }
                    }
                    with(findViewById<MaterialButton>(R.id.statistics_toggle_clan)) {
                        visibility = if (app.userStatisticsService.clanStatistics.battles != 0) {
                            isChecked = true
                            VISIBLE
                        } else {
                            isChecked = false
                            GONE
                        }
                        addOnCheckedChangeListener { _, _ ->
                            updatePlayerStatistics()
                            updateSessionStatistics(app.sessionService.selectedSessionIndex)
                        }
                    }

                    updatePlayerStatistics()
                    app.searchProcessingController.completeStatisticsLoading()
                    updateSessionStatistics(0)
                }
            }

            app.userStatisticsService.get(
                UserStatisticsService.Arguments(app.userService.accountId!!)
            )

        }
    }

    /**
     * Sets values for selected types of player statistics
     */
    private fun updatePlayerStatistics() {
        InterfaceUtils.setStatistics(
            this@MainActivity,
            app.userService.nickname,
            app.userStatisticsService.getStatisticsByEnum(getPlayerStatisticsTypes())
        )
    }

    /**
     * Gets 'session' statistics and sets it for all 'session' elements
     */
    private fun updateSessionStatistics(index: Int) {
        val statisticsSessionButtons = findViewById<SessionButtonsLayout>(R.id.statistics_session_buttons)
        val statisticsFastStat = findViewById<PlayerFastStat>(R.id.statistics_fast_stat)
        val statisticsSessionsList = findViewById<ListView>(R.id.statistics_sessions_list)
        val statisticsSessionStatButton = statisticsSessionButtons.statisticsButton
        val statisticsScreen = findViewById<ViewFlipper>(R.id.fragment_statistics)

        // Updates service with new data

        app.sessionService.update(
            app.userService.accountId!!,
            app.userStatisticsService.timestamp,
            app.userStatisticsService.json!!,
            app.preferencesService.region!!
        )

        // Saves the current session to a file

        app.sessionService.createSessionDir()
        app.sessionService.createSessionFile()

        // Fills in the list of sessions that will be displayed with actions

        val sessions = ArrayList<Session>()
        for (i in 0 until app.sessionService.subList.size) {
            sessions.add(Session().apply {
                this.path = app.sessionService.subList[i].path
                this.set = Runnable {
                    updateSessionStatistics(i)
                    statisticsScreen.displayedChild = StatisticsViewFlipperItems.STATISTICS
                }
                this.delete = Runnable {
                    if (i == index) {
                        InterfaceUtils.createSnackbar(statisticsScreen, getString(R.string.delete_select))
                            .show()
                    } else {
                        InterfaceUtils.createAlertDialog(
                            this@MainActivity,
                            this@MainActivity.getString(R.string.delete),
                            this@MainActivity.getString(R.string.delete_alert),
                            this@MainActivity.getString(R.string.delete),
                            Runnable {
                                if (app.sessionService.subList[i].delete()) {
                                    InterfaceUtils.createSnackbar(statisticsScreen, getString(R.string.delete_successfully))
                                        .show()
                                    if (index != sessions.size - 1) {
                                        updateSessionStatistics(index)
                                    } else {
                                        updateSessionStatistics(index - 1)
                                    }
                                    statisticsScreen.displayedChild = StatisticsViewFlipperItems.STATISTICS
                                } else {
                                    InterfaceUtils.createSnackbar(statisticsScreen, getString(R.string.delete_failed))
                                        .show()
                                }
                            },
                            this@MainActivity.getString(android.R.string.cancel),
                            Runnable {  }
                        ).show()
                    }
                }
                this.isSelected = i == index
            })
        }

        // Displays on the screen

        runOnUiThread {

            statisticsSessionButtons.setButtonsVisibility(
                when(app.sessionService.subList.size) {
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

            statisticsFastStat.setSessionData(
                when(app.sessionService.subList.size) {
                    0 -> {
                        StatisticsData()
                    }
                    else -> {
                        StatisticsDataUtils.calculateSessionDifferences(
                            app.userStatisticsService.getStatisticsByEnum(getPlayerStatisticsTypes()),
                            StatisticsDataUtils.parse(
                                app.sessionService.subList[index].readText(),
                                app.userService.accountId!!,
                                getPlayerStatisticsTypes()
                            )
                        )
                    }
                }
            )
            if (statisticsSessionStatButton.isActivated) {
                statisticsFastStat.setData(
                    app.userService.nickname!!,
                    StatisticsDataUtils.calculateFieldDifferences(
                        app.userStatisticsService.getStatisticsByEnum(getPlayerStatisticsTypes()),
                        StatisticsDataUtils.parse(
                            app.sessionService.subList[index].readText(),
                            app.userService.accountId!!,
                            getPlayerStatisticsTypes()
                        )
                    )
                )
            }
            app.sessionService.selectedSessionIndex = index

            statisticsSessionsList.adapter = SessionAdapter(
                this@MainActivity, sessions
            )

            statisticsSessionButtons.actions.statisticsButtonAction =  {
                statisticsScreen.startAnimation(statisticsScreen.outAnimation)

                if (!statisticsSessionStatButton.isActivated) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        statisticsSessionStatButton.text = getString(R.string.from_session_stat)

                        statisticsFastStat.setData(
                            app.userService.nickname!!,
                            StatisticsDataUtils.calculateFieldDifferences(
                                app.userStatisticsService.getStatisticsByEnum(getPlayerStatisticsTypes()),
                                StatisticsDataUtils.parse(
                                    app.sessionService.subList[index].readText(),
                                    app.userService.accountId!!,
                                    getPlayerStatisticsTypes()
                                )
                            ))

                        statisticsScreen.startAnimation(statisticsScreen.inAnimation)
                        statisticsSessionStatButton.isActivated = true
                    }, animationDurationMillis)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        statisticsSessionStatButton.text = getString(R.string.to_session_stat)

                        statisticsFastStat.setData(
                            app.userService.nickname!!,
                            app.userStatisticsService.getStatisticsByEnum(getPlayerStatisticsTypes())
                        )

                        statisticsScreen.startAnimation(statisticsScreen.inAnimation)
                        statisticsSessionStatButton.isActivated = false
                    }, animationDurationMillis)
                }
            }

            // Displays the loading screen on tanks layout and waits for data loading to finish

            findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper).displayedChild = MainViewFlipperItems.STATISTICS

            app.searchProcessingController.completeSessionsLoading()
        }
    }

    /**
     * Returns a list of selected player statistics types
     */
    private fun getPlayerStatisticsTypes(): Collection<String> {
        return ArrayList<String>().apply {
            if (findViewById<MaterialButton>(R.id.statistics_toggle_random).isChecked) {
                add(PlayerStatisticsTypes.RANDOM)
            }
            if (findViewById<MaterialButton>(R.id.statistics_toggle_rating).isChecked) {
                add(PlayerStatisticsTypes.RATING)
            }
            if (findViewById<MaterialButton>(R.id.statistics_toggle_clan).isChecked) {
                add(PlayerStatisticsTypes.CLAN)
            }
        }
    }

    /**
     * Called when the [apply filters button]
     * [ru.forblitz.statistics.R.id.tanks_apply_filters] or the search
     * button on the keyboard is pressed. Creates a display of suitable vehicle
     * statistics.
     */
    private fun setVehiclesStat() {
        val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)
        val tanksApplyFilters = findViewById<View>(R.id.tanks_apply_filters)

        runOnUiThread {
            tanksApplyFilters.setOnClickListener {
                setVehiclesStat()
                InterfaceUtils.playCycledAnimation(
                    tanksApplyFilters,
                    true
                )
            }
        }

        CoroutineScope(Dispatchers.IO).launch {

            // Creates pairs of vehicles characteristics and vehicles
            // statistics. The key is the ID of the vehicle

            val pairs = HashMap<String, Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>>()

            app.userVehiclesStatisticsService.get(
                app.userService.accountId!!,
                app.vehicleSpecsService.map!!.keys.toList()
            ).values.forEach {
                pairs[it.tankId] = Pair(app.vehicleSpecsService.map!![it.tankId]!!, it)
            }

            // Initializing widget variables

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

            // Sorting and filtering

            val sortedVehicles: ArrayList<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = ArrayList(pairs.values)
            val vehiclesToCreate: ArrayList<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = ArrayList(0)

            val comparatorByBattles: Comparator<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = Comparator {
                    v1, v2 -> v1.second.statistics.battles.compareTo(v2.second.statistics.battles)
            }
            val comparatorByAverageDamage: Comparator<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = Comparator {
                    v1, v2 -> v1.second.statistics.averageDamage!!.compareTo(v2.second.statistics.averageDamage!!)
            }
            val comparatorByWinningPercentage: Comparator<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = Comparator {
                    v1, v2 -> v1.second.statistics.winningPercentage!!.compareTo(v2.second.statistics.winningPercentage!!)
            }
            val comparatorByAverageXp: Comparator<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = Comparator {
                    v1, v2 -> v1.second.statistics.averageXp!!.compareTo(v2.second.statistics.averageXp!!)
            }
            val comparatorByEfficiency: Comparator<Pair<VehicleSpecs, VehiclesStatisticsResponse.VehicleStatistics>> = Comparator {
                    v1, v2 -> v1.second.statistics.efficiency!!.compareTo(v2.second.statistics.efficiency!!)
            }

            val tanksSort = findViewById<RadioGroup>(R.id.tanks_sort)
            when (tanksSort.checkedRadioButtonId) {
                R.id.radio_battles -> {
                    Collections.sort(sortedVehicles, comparatorByBattles)
                }
                R.id.radio_average_damage -> {
                    Collections.sort(sortedVehicles, comparatorByAverageDamage)
                }
                R.id.radio_efficiency -> {
                    Collections.sort(sortedVehicles, comparatorByEfficiency)
                }
                R.id.radio_average_xp -> {
                    Collections.sort(sortedVehicles, comparatorByAverageXp)
                }
                R.id.radio_winning_percentage -> {
                    Collections.sort(sortedVehicles, comparatorByWinningPercentage)
                }
            }

            sortedVehicles.reverse()
            if (sortedVehicles.filter { it.second.statistics.battles > 1 }.size < 2) {
                runOnUiThread { tanksFilters.hide() }
            } else {
                runOnUiThread { tanksFilters.show() }
            }

            sortedVehicles
                .filter { it.second.statistics.battles > 0 }
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

            // Displaying data and ad banners

            runOnUiThread {
                tanksList.adapter = VehicleAdapter(this@MainActivity, vehiclesToCreate)

                val adView = findViewById<BannerAdView>(R.id.tanks_list_banner)
                app.adService.setBanner(
                    InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_big),
                    adView
                )
                adView.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    bottomToBottom = R.id.tanks_list_layout
                }

                tanksLayoutsFlipper.displayedChild = 0
                app.searchProcessingController.completeTanksLoading()
            }
        }
    }

    /**
     * Gets 'clan' statistics and sets it for all 'clan' elements
     */
    private fun setClanStat() {
        val clanScreen = findViewById<ClanScreen>(R.id.fragment_clan)
        val clanBrief = findViewById<ClanBrief>(R.id.statistics_clan)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                runOnUiThread {
                    clanScreen.displayedChild = ClanViewFlipperItems.LOADING
                    clanBrief.displayedChild = ClanBriefFlipperItems.LOADING
                }
                app.userClanService.clear()
                app.clanInformationService.clear()

                UserClanService(app.apiService)
                    .get(UserClanService.Arguments(app.userService.accountId!!))
                    .data[app.userService.accountId]
                    .also { shortClanInfo ->
                        if (shortClanInfo != null) {
                            app.clanInformationService.get(ClanInformationService.Arguments(shortClanInfo.clanId)).data[shortClanInfo.clanId]!!
                                .also { fullClanInfo ->
                                    runOnUiThread {
                                        clanBrief.setData(shortClanInfo)
                                        clanScreen.setData(shortClanInfo, fullClanInfo)
                                        app.searchProcessingController.completeClanLoading()
                                    }
                                }
                        } else {
                            runOnUiThread {
                                clanScreen.setData(null, null)
                                clanBrief.setData(null)
                                app.searchProcessingController.completeClanLoading()
                            }
                        }
                    }
            } catch (e: APILoadService.ServerException) {
                runOnUiThread {
                    clanScreen.setServerException(e)
                    clanBrief.setServerException(e)
                    Firebase.crashlytics.recordException(e)
                    app.searchProcessingController.completeClanLoading()
                }
            }
        }
    }

    private fun setAchievements() {
        val achievementsScreen = findViewById<AchievementsScreen>(R.id.achievements_screen)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                app.userAchievementsService.clear()

                ArrayList<Pair<AchievementInfo, Int>>().apply {

                    app.userAchievementsService
                        .get(UserAchievementsService.Arguments(app.userService.accountId!!))
                        .data[app.userService.accountId!!]!!
                        .achievements
                        .toList()
                        .forEach { currentPair ->
                            app.achievementsInformationService.addTaskOnEndOfLoad { achievementsInformationResponse ->
                                achievementsInformationResponse.data
                                    .toList()
                                    .find { currentPair.first == it.first }
                                    ?.second
                                    ?.let {
                                        this@apply.add(Pair(it, currentPair.second))
                                    }
                            }
                        }

                    app.achievementsInformationService.addTaskOnEndOfLoad {
                        runOnUiThread {
                            achievementsScreen.adapter = AchievementsAdapter(
                                this@MainActivity,
                                this@apply.apply { sortBy { it.second }; reverse() }.chunked(achievementsInRow),
                                (InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_big) * (achievementsInRow + 1)) / achievementsInRow,
                                resources.getDimensionPixelSize(R.dimen.padding_big)
                            ) { achievementRowLayout, viewGroup, achievementPair ->
                                if (achievementRowLayout.expandedChild == -1) {
                                    achievementRowLayout.expand(viewGroup, achievementPair.first)
                                } else {
                                    achievementRowLayout.collapse(
                                        achievementRowLayout
                                            .findViewWithTag<LinearLayout>("row")
                                            .getChildAt(achievementRowLayout.expandedChild) as ViewGroup
                                    )
                                }
                            }
                            app.searchProcessingController.completeAchievementsLoading()
                        }
                    }

                }
            } catch (e: APILoadService.ServerException) {
                runOnUiThread {
                    achievementsScreen.setServerException(e)
                    Firebase.crashlytics.recordException(e)
                    app.searchProcessingController.completeAchievementsLoading()
                }
            }
        }
    }

    /**
     * Change region in [ForBlitzStatisticsApplication.apiService], sets the
     * appropriate item in ['search' region selector]
     * [R.id.search_region_layout] and [settings region selector]
     * [R.id.settings_region_layout]
     */
    private fun setRegion() {
        findViewById<EditText>(R.id.search_field).setText("", BufferType.EDITABLE)
        app.apiService.setRegion(app.preferencesService.region!!)
        findViewById<ExtendedRadioGroup>(R.id.search_region_layout).setCheckedItem(app.preferencesService.region!!)
        findViewById<ExtendedRadioGroup>(R.id.settings_region_layout).setCheckedItem(app.preferencesService.region!!)
    }

    private fun checkIsPrivacyPolicyAccepted() {
        if (!app.preferencesService.isPrivacyPolicyAccepted) {
            InterfaceUtils.createAlertDialog(
                this@MainActivity,
                this@MainActivity.getString(R.string.terms_of_service),
                HtmlCompat.fromHtml(this@MainActivity.getString(R.string.terms_of_service_desc), HtmlCompat.FROM_HTML_MODE_LEGACY),
                this@MainActivity.getString(R.string.accept),
                Runnable { app.preferencesService.isPrivacyPolicyAccepted = true },
                this@MainActivity.getString(R.string.exit),
                Runnable { finish() }
            )
                .show()
                .findViewById<TextView>(android.R.id.message)!!.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    /**
     * Fills [Last search list][R.id.last_searched_list] and sets the
     * appropriate text in ['enter nickname' switcher]
     * [R.id.enter_nickname_text]
     */
    private fun updateLastSearch(needToSetText: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {

            val records =
                app
                    .recordDatabase
                    .recordDao()
                    .getDistinctRecordsByRegion(
                        app.preferencesService.region,
                        3
                    )
                    .toTypedArray()

            runOnUiThread {
                val lastSearchedList = findViewById<ListView>(R.id.last_searched_list)
                val enterNicknameSwitcher = findViewById<TextSwitcher>(R.id.enter_nickname_text)

                if (records.isNotEmpty()) {
                    if (needToSetText) {
                        enterNicknameSwitcher.setText(getString(R.string.enter_nickname_or_select))
                    }
                    lastSearchedList.visibility = VISIBLE

                    lastSearchedList.adapter = LastSearchedAdapter(
                        this@MainActivity,
                        records,
                        (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()
                    )

                } else {
                    if (needToSetText) {
                        enterNicknameSwitcher.setText(getString(R.string.enter_nickname))
                    }
                    lastSearchedList.visibility = GONE
                }
            }

        }
    }

    /**
     * Resets the logs display on the loading screen
     */
    private fun updateLoggingDisplay() {
        val requestLogList = findViewById<ListView>(R.id.request_log_list)

        if (app.setSettings[Constants.PreferencesSwitchesTags.logDisplay] == true) {

            app.requestsService.clearEndedRecords()
            val requestLogAdapter = RequestLogAdapter(
                this@MainActivity,
                app.requestsService.records
            )

            requestLogList.adapter = requestLogAdapter
            requestLogList.deferNotifyDataSetChanged()

            app.requestsService.addOnRequestListChangedListener("updateLoggingDisplay") {
                runOnUiThread {
                    requestLogAdapter.notifyDataSetChanged()
                }
            }

        } else {
            requestLogList.adapter = null
        }
    }

    private fun setLoadingIndicator() {
        app.requestsService.addOnRequestListChangedListener("loadingIndicator") {
            runOnUiThread {
                findViewById<LinearProgressIndicator>(R.id.search_progress_indicator).apply {
                    if (Utils.getWithoutEndedRecords(it).size == 0) {
                        if (isShown) {
                            hide()
                        }
                    } else {
                        if (!isShown) {
                            show()
                        }
                    }
                }
            }
        }
    }

}
