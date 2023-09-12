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
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.LocaleListCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.updateLayoutParams
import androidx.room.Room.databaseBuilder
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import org.jetbrains.anko.contentView
import ru.forblitz.statistics.adapters.LastSearchedAdapter
import ru.forblitz.statistics.adapters.RequestLogAdapter
import ru.forblitz.statistics.adapters.SessionAdapter
import ru.forblitz.statistics.adapters.VehicleAdapter
import ru.forblitz.statistics.adapters.ViewPagerAdapter
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.data.Constants
import ru.forblitz.statistics.data.Constants.ClanViewFlipperItems
import ru.forblitz.statistics.data.Constants.MainViewFlipperItems
import ru.forblitz.statistics.data.Constants.StatisticsViewFlipperItems
import ru.forblitz.statistics.data.Constants.TABS_COUNT
import ru.forblitz.statistics.data.RecordDatabase
import ru.forblitz.statistics.dto.Record
import ru.forblitz.statistics.dto.RequestLogItem
import ru.forblitz.statistics.dto.Session
import ru.forblitz.statistics.dto.StatisticsData
import ru.forblitz.statistics.dto.VehicleSpecs
import ru.forblitz.statistics.dto.VehicleStat
import ru.forblitz.statistics.exception.ObjectException
import ru.forblitz.statistics.service.AdService
import ru.forblitz.statistics.service.ClanService
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.RandomService
import ru.forblitz.statistics.service.RatingService
import ru.forblitz.statistics.service.RequestLogService
import ru.forblitz.statistics.service.SessionService
import ru.forblitz.statistics.service.TokensService
import ru.forblitz.statistics.service.UserClanService
import ru.forblitz.statistics.service.UserService
import ru.forblitz.statistics.service.VehicleSpecsService
import ru.forblitz.statistics.service.VehicleStatService
import ru.forblitz.statistics.service.VersionService
import ru.forblitz.statistics.utils.InterfaceUtils
import ru.forblitz.statistics.utils.ParseUtils
import ru.forblitz.statistics.utils.SessionUtils
import ru.forblitz.statistics.utils.Utils
import ru.forblitz.statistics.widget.common.DifferenceViewFlipper
import ru.forblitz.statistics.widget.common.ExtendedRadioGroup
import ru.forblitz.statistics.widget.data.ClanBrief
import ru.forblitz.statistics.widget.data.ClanScreen
import ru.forblitz.statistics.widget.data.PlayerFastStat
import ru.forblitz.statistics.widget.data.SessionButtonsLayout
import ru.forblitz.statistics.widget.data.SessionButtonsLayout.ButtonsVisibility
import ru.forblitz.statistics.widget.data.SettingsSwitchesList
import java.io.File
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
            if (tabLayout.getTabAt(i) == null) {
                tabLayout.addTab(tabLayout.newTab().setCustomView(view))
            } else {
                tabLayout.getTabAt(i)?.customView = view
            }
        }

        viewPager.offscreenPageLimit = 3

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
        app.requestLogService = RequestLogService()
        app.tokensService = TokensService(
            app.connectivityService,
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        app.apiService = ApiService(
            app.connectivityService,
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
            app.requestLogService,
            app.tokensService.tokens
        )
        app.userService = UserService(this@MainActivity, app.apiService)
        app.randomService = RandomService(app.apiService)
        app.ratingService = RatingService(app.apiService)
        app.userClanService = UserClanService(app.apiService)
        app.clanService = ClanService(app.apiService)
        app.sessionService = SessionService(applicationContext)
        app.versionService = VersionService(
            app.connectivityService,
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        app.vehicleSpecsService = VehicleSpecsService(app.apiService)
        app.vehicleStatService = VehicleStatService(app.apiService)
        app.adService = AdService(
            this@MainActivity,
            app.tokensService.tokens
        )
        app.recordDatabase = databaseBuilder(
            applicationContext,
            RecordDatabase::class.java, "history-database"
        ).build()

        // Creates a session directory

        app.sessionService.createSessionDir()

        // Initialization of app.preferences

        app.preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        // Getting the set settings

        Constants.preferencesTags.forEach {
            app.setSettings[it] = app.preferences.getBoolean(it, false)
        }

        // Sets app.preferences listeners

        val searchRegionLayout = findViewById<ExtendedRadioGroup>(R.id.search_region_layout)
        val settingsRegionLayout = findViewById<ExtendedRadioGroup>(R.id.settings_region_layout)
        val settingsLocaleLayout = findViewById<ExtendedRadioGroup>(R.id.settings_locale_layout)

        for (i in 0 until searchRegionLayout.childCount) {
            val view = searchRegionLayout.getChildAt(i)
            view.setOnClickListener {
                if (Constants.baseUrl.containsKey((view.tag.toString()))) {
                    app.preferences.edit().putString("region", view.tag.toString()).apply()
                    setRegion()
                    updateLastSearch(false)
                }
            }
        }
        for (i in 0 until settingsRegionLayout.childCount) {
            val view = settingsRegionLayout.getChildAt(i)
            view.setOnClickListener {
                if (Constants.baseUrl.containsKey((view.tag.toString()))) {
                    app.preferences.edit().putString("region", view.tag.toString()).apply()
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

        // Configured settings dimensions

        settingsRegionLayout.childHeight = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()
        settingsLocaleLayout.childHeight = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()

        findViewById<View>(R.id.settings_region).updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.05).toInt()
        }

        findViewById<View>(R.id.settings_locale).updateLayoutParams<LinearLayout.LayoutParams> {
            height = (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.05).toInt()
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
                    app.preferences.edit().putBoolean(it.first, isChecked).apply()
                    app.setSettings.replace(it.first, isChecked)
                }
        }

        // Sets onBackPressed

        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val randomLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.random_layouts_flipper)
                val ratingLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.rating_layouts_flipper)
                val clanLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.clan_layouts_flipper)
                val tanksLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.tanks_layouts_flipper)

                if (mainLayoutsFlipper.displayedChild == MainViewFlipperItems.SETTINGS) {
                    findViewById<View>(R.id.settings_button).performClick()
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

        // Initialization of ads

        MobileAds.initialize(this) { Log.i("YandexMobileAds", "SDK initialized") }

    }

    override fun onStart() {
        super.onStart()
        app.connectivityService.subscribe(this)

        // Set keyboard visibility listener

        contentView!!.viewTreeObserver.addOnGlobalLayoutListener {
            val displayFrameRect = Rect()
            contentView?.getWindowVisibleDisplayFrame(displayFrameRect)
            val screenHeight = contentView!!.rootView.height

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

        // Set region

        setRegion()
        updateLastSearch(true)

        // Displays the selected locale in the settings

        val prefLocale = app.preferences.getString("locale", "notSpecified")
        if (prefLocale != "notSpecified" && prefLocale != null) {
            findViewById<ExtendedRadioGroup>(R.id.settings_locale_layout).setCheckedItem(prefLocale)
        }

        // Check version and fill vehicles specifications

        CoroutineScope(Dispatchers.IO).launch {
            // Checks if the app version is up-to-date
            versionCheck()
            // Loads tokens
            app.tokensService.updateTokens()
            app.tokensService.addTaskOnEndOfLoad {
                CoroutineScope(Dispatchers.IO).launch {
                    // Get vehicle specifications
                    app.vehicleSpecsService.getVehiclesSpecsList()
                }
            }

        }
    }

    /**
     * Restarts the activity with the locale set
     * @param locale Locale code to be applied
     */
    private fun changeLocale(locale: String) {
        app.preferences.edit().putString("locale", locale).apply()

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
        val searchProgressIndicator = findViewById<LinearProgressIndicator>(R.id.search_progress_indicator)

        // Shows the loading screen and the loading indicator, blocks all
        // interactive elements during loading

        mainFlipper.displayedChild = MainViewFlipperItems.LOADING
        searchButton.isClickable = false
        findViewById<View>(R.id.settings_button).isActivated = false
        findViewById<SessionButtonsLayout>(R.id.random_session_buttons).setButtonsVisibility(ButtonsVisibility.NOTHING)
        searchProgressIndicator.show()

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

        // Set params of tanksList

        tanksList.emptyView = findViewById(R.id.item_nothing_found)

        if (tanksList.footerViewsCount == 0) {
            val footer = View(this@MainActivity)
            val width = InterfaceUtils.getX() - resources.getDimensionPixelSize(R.dimen.padding_very_big) * 2
            footer.layoutParams = AbsListView.LayoutParams(width, (width * 0.15).toInt())
            tanksList.addFooterView(footer)
        }

        // Updates logging display

        updateLoggingDisplay()

        // Does everything to show statistics, but first waits for tokens and
        // ads to be loaded, if necessary

        app.tokensService.addTaskOnEndOfLoad {
            app.adService.showInterstitial {
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        app.userService.clear()
                        app.userService.getUserID(searchField.text.toString())

                        findViewById<EditText>(R.id.search_field).setText(
                            app.userService.getNickname(),
                            TextView.BufferType.EDITABLE
                        )
                        app.recordDatabase.recordDao().addRecord(
                            Record(
                                app.userService.getUserID(),
                                app.userService.getNickname(),
                                System.currentTimeMillis().toString(),
                                app.preferences.getString("region", "notSpecified")!!
                            )
                        )

                        // set player statistics

                        app.sessionService.clear()
                        app.vehicleStatService.clear()

                        setRandomStat()
                        setRatingStat()
                        setClanStat()
                        app.vehicleSpecsService.addTaskOnEndOfLoad {
                            CoroutineScope(Dispatchers.IO).launch {
                                setVehiclesStat()
                            }
                        }

                    } catch (e: ObjectException) {
                        runOnUiThread {
                            mainFlipper.displayedChild = MainViewFlipperItems.ENTER_NICKNAME
                            searchButton.isClickable = true
                            searchField.setText("", TextView.BufferType.EDITABLE)
                            searchProgressIndicator.hide()

                            InterfaceUtils.createAlertDialog(
                                this@MainActivity,
                                getString(R.string.error) + " " + e.error.code,
                                e.message.toString().replace("XXX", app.preferences.getString("region", "notSpecified")!!.uppercase()),
                            ).show()
                        }
                    }
                }
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
    private suspend fun versionCheck() {

        // If the version is less than the minimum, it is necessary to show an
        // unclosable AlertDialog about the need for an update. If it is less
        // than recommended, then it is necessary to show a closable
        // AlertDialog that an update is recommended.

        if (BuildConfig.VERSION_CODE in app.versionService.getMinimalAppVersion() until app.versionService.getCurrentAppVersion()) {

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

        } else if (BuildConfig.VERSION_CODE < app.versionService.getMinimalAppVersion()) {

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
     * Gets 'random' statistics and sets it to the 'random' screen. At the end
     * of the work, it calls 'setSessionStat()'
     */
    private fun setRandomStat() {
        CoroutineScope(Dispatchers.IO).launch {

            app.randomService.clear()
            try {
                InterfaceUtils.setBaseStatistics(
                    this@MainActivity,
                    app.randomService.getStatisticsData(
                        app.userService.getUserID(),
                        app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
                    ),
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

    /**
     * Gets 'rating' statistics and sets it to the 'rating' screen. At the end
     * of the work, it calls 'setSessionStat()'
     */
    private fun setRatingStat() {
        CoroutineScope(Dispatchers.IO).launch {

            app.ratingService.clear()
            InterfaceUtils.setRatingStatistics(this@MainActivity,
                app.ratingService.getStatisticsData(
                    app.userService.getUserID(),
                    app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
                ),
                true)
            setSessionStat(0)

        }
    }

    /**
     * Gets 'session' statistics and sets it for all 'session' elements
     */
    private fun setSessionStat(index: Int) {

        // If the statistics data 'random' or statistics data 'rating' have not
        // been loaded yet, then there is nothing to compare with, the function
        // is terminated. If session statistics are already set, then the
        // function is also terminated.

        if (app.randomService.getStatisticsData() == null || app.ratingService.getStatisticsData() == null || app.sessionService.alreadySet) {
            return
        }
        app.sessionService.alreadySet = true

        // Saves the current session to a file

        app.sessionService.createSessionFile(
            app.randomService.getJson(),
            app.userService.getUserID(),
            ParseUtils.parseTimestamp(app.randomService.getJson(), false),
            app.preferences.getString("region", "notSpecified")!!
        )
        app.sessionService.getSessionsList(app.userService.getUserID(), app.preferences.getString("region", "notSpecified")!!)

        // Checks whether the current session matches the last one. If yes,
        // then removes it from the list.

        if (
            ParseUtils.parseTimestamp(app.randomService.getJson(), false)
            ==
            ParseUtils.parseTimestamp(app.sessionService.getSessionsList()[0], true)
        ) {
            app.sessionService.getSessionsList().removeAt(0)
        }

        // Fills in the list of sessions that will be displayed with actions

        val randomLayoutsFlipper = findViewById<DifferenceViewFlipper>(R.id.random_layouts_flipper)

        val sessions = ArrayList<Session>(0)
        for (i in 0 until app.sessionService.getSessionsList().size) {
            val session = Session()
            session.path = app.sessionService.getSessionsList()[i]
            session.set = Runnable {
                app.sessionService.clear()
                setSessionStat(i)
                randomLayoutsFlipper.displayedChild = 0
            }
            session.delete = Runnable {
                if (i == index) {
                    Snackbar.make(randomLayoutsFlipper, getString(R.string.delete_select), Snackbar.LENGTH_SHORT).show()
                } else {
                    InterfaceUtils.createAlertDialog(
                        this@MainActivity,
                        this@MainActivity.getString(R.string.delete),
                        this@MainActivity.getString(R.string.delete_alert),
                        this@MainActivity.getString(R.string.delete),
                        Runnable {
                            if (File(app.sessionService.getSessionsList()[i]).delete()) {
                                Snackbar.make(randomLayoutsFlipper, getString(R.string.delete_successfully), Snackbar.LENGTH_SHORT).show()
                                if (index != sessions.size - 1) {
                                    app.sessionService.clear()
                                    setSessionStat(index)
                                } else {
                                    app.sessionService.clear()
                                    setSessionStat(index - 1)
                                }
                                randomLayoutsFlipper.displayedChild = 0
                            } else {
                                Snackbar.make(randomLayoutsFlipper, getString(R.string.delete_failed), Snackbar.LENGTH_SHORT).show()
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

        // Displays on the screen

        val randomSessionButtons = findViewById<SessionButtonsLayout>(R.id.random_session_buttons)
        val randomFastStat = findViewById<PlayerFastStat>(R.id.random_fast_stat)
        val ratingFastStat = findViewById<PlayerFastStat>(R.id.rating_fast_stat)
        val randomSessionsList = findViewById<ListView>(R.id.random_sessions_list)
        val randomSessionStatButton = findViewById<TextView>(R.id.random_session_stat_button)
        val fragmentRandom = findViewById<ViewFlipper>(R.id.fragment_random)
        val fragmentRating = findViewById<ViewFlipper>(R.id.fragment_rating)

        runOnUiThread {

            randomSessionButtons.setButtonsVisibility(
                when(app.sessionService.getSessionsList().size) {
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
                when(app.sessionService.getSessionsList().size) {
                    0 -> {
                        StatisticsData()
                    }
                    else -> {
                        SessionUtils.calculateSessionDifferences(
                            app.randomService.getStatisticsData(),
                            ParseUtils.parseStatisticsData(
                                File(app.sessionService.getSessionsList()[index]).readText(),
                                "all",
                                app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
                            )
                        )
                    }
                }
            )

            ratingFastStat.setSessionData(
                when(app.sessionService.getSessionsList().size) {
                    0 -> {
                        StatisticsData()
                    }
                    else -> {
                        SessionUtils.calculateSessionDifferences(
                            app.ratingService.getStatisticsData(),
                            ParseUtils.parseStatisticsData(
                                File(app.sessionService.getSessionsList()[index]).readText(),
                                "rating",
                                app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
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
                randomFastStat.setData(app.randomService.getStatisticsData())
                ratingFastStat.setData(app.ratingService.getStatisticsData())
                randomSessionStatButton.isActivated = false
            }
            randomSessionStatButton.setOnClickListener {
                fragmentRandom.startAnimation(fragmentRandom.outAnimation)
                fragmentRating.startAnimation(fragmentRating.outAnimation)

                if (!randomSessionStatButton.isActivated) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        randomSessionStatButton.text = getString(R.string.from_session_stat)

                        randomFastStat.setData(SessionUtils.calculateSession(
                            app.randomService.getStatisticsData(),
                            ParseUtils.parseStatisticsData(
                                File(app.sessionService.getSessionsList()[index]).readText(),
                                "all",
                                app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
                            )
                        ))
                        ratingFastStat.setData(SessionUtils.calculateSession(
                            app.ratingService.getStatisticsData(),
                            ParseUtils.parseStatisticsData(
                                File(app.sessionService.getSessionsList()[index]).readText(),
                                "rating",
                                app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
                            )
                        ))

                        fragmentRandom.startAnimation(fragmentRandom.inAnimation)
                        fragmentRating.startAnimation(fragmentRating.inAnimation)
                        randomSessionStatButton.isActivated = true
                    }, 125)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        randomSessionStatButton.text = getString(R.string.to_session_stat)

                        randomFastStat.setData(app.randomService.getStatisticsData())
                        ratingFastStat.setData(app.ratingService.getStatisticsData())

                        fragmentRandom.startAnimation(fragmentRandom.inAnimation)
                        fragmentRating.startAnimation(fragmentRating.inAnimation)
                        randomSessionStatButton.isActivated = false
                    }, 125)
                }
            }

            // Displays the loading screen on tanks layout and waits for data loading to finish

            findViewById<DifferenceViewFlipper>(R.id.main_layouts_flipper).displayedChild = MainViewFlipperItems.STATISTICS
            findViewById<View>(R.id.search_button).isClickable = true
            findViewById<LinearProgressIndicator>(R.id.search_progress_indicator).hide()
        }
    }

    /**
     * Called when the [apply filters button]
     * [ru.forblitz.statistics.R.id.tanks_apply_filters] or the search
     * button on the keyboard is pressed. Creates a display of suitable vehicle
     * statistics.
     */
    private fun setVehiclesStat() {
        if (app.vehicleSpecsService.getListSize() != 0) {

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

                // Creates pairs of vehicles characteristics and vehicles
                // statistics. The key is the ID of the vehicle

                val pairs = HashMap<String, Pair<VehicleSpecs, VehicleStat>>()

                app.vehicleStatService.getVehicleStat(
                    app.userService.getUserID(),
                    app.vehicleSpecsService.getVehiclesSpecsList().keys.toTypedArray(),
                    app.setSettings[Constants.PreferencesSwitchesTags.averageDamageRounding]!!
                ).forEach {
                    pairs[it.key] = Pair(app.vehicleSpecsService.getVehiclesSpecsList()[it.key]!!, it.value)
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

                val sortedVehicles: ArrayList<Pair<VehicleSpecs, VehicleStat>> = ArrayList(pairs.values)
                val vehiclesToCreate: ArrayList<Pair<VehicleSpecs, VehicleStat>> = ArrayList(0)

                val comparatorByBattles: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator {
                        v1, v2 -> v1.second.all.battles.toInt().compareTo(v2.second.all.battles.toInt())
                }
                val comparatorByAverageDamage: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator {
                        v1, v2 -> v1.second.all.averageDamage.toDouble().compareTo(v2.second.all.averageDamage.toDouble())
                }
                val comparatorByWinRate: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator {
                        v1, v2 -> v1.second.all.winRate.toDouble().compareTo(v2.second.all.winRate.toDouble())
                }
                val comparatorByAverageXp: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator {
                        v1, v2 -> v1.second.all.averageXp.toDouble().compareTo(v2.second.all.averageXp.toDouble())
                }
                val comparatorByEfficiency: Comparator<Pair<VehicleSpecs, VehicleStat>> = Comparator {
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
                }
            }
        }
    }

    /**
     * Gets 'clan' statistics and sets it for all 'clan' elements
     */
    private fun setClanStat() {
        CoroutineScope(Dispatchers.IO).launch {

            app.userClanService.clear()
            app.clanService.clear()

            if (app.userClanService.getShortClanInfo(app.userService.getUserID()) != null) {
                runOnUiThread {
                    findViewById<ClanBrief>(R.id.random_clan).setData(app.userClanService.getShortClanInfo())
                    findViewById<ClanBrief>(R.id.rating_clan).setData(app.userClanService.getShortClanInfo())
                }
                app.clanService.getFullClanInfo(app.userClanService.getShortClanInfo())
                runOnUiThread {
                    findViewById<ClanScreen>(R.id.fragment_clan).setData(app.userClanService.getShortClanInfo(), app.clanService.getFullClanInfo())
                }
            } else {
                runOnUiThread {
                    findViewById<ClanScreen>(R.id.fragment_clan).setData(null, app.clanService.getFullClanInfo())
                    findViewById<ClanBrief>(R.id.random_clan).setData(null)
                    findViewById<ClanBrief>(R.id.rating_clan).setData(null)
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
        findViewById<EditText>(R.id.search_field).setText("", TextView.BufferType.EDITABLE)

        // If the region is not set, it means that the application is launched
        // for the first time. An AlertDialog about the privacy policy is
        // created, and the region is automatically set to 'ru'. In any other
        // case, the specified region is set and updateLastSearch() is called
        // (because the region has changed, the search history has changed)

        if (app.preferences.getString("region", "notSpecified") == "notSpecified") {
            app.preferences.edit().putString("region", "ru").apply()
            app.apiService.setRegion(app.preferences.getString("region", "notSpecified")!!)

            InterfaceUtils.createAlertDialog(
                this@MainActivity,
                this@MainActivity.getString(R.string.terms_of_service),
                HtmlCompat.fromHtml(this@MainActivity.getString(R.string.terms_of_service_desc), HtmlCompat.FROM_HTML_MODE_LEGACY),
                this@MainActivity.getString(R.string.accept),
                Runnable {  },
                this@MainActivity.getString(R.string.exit),
                Runnable { finish() }
            )
                .show()
                .findViewById<TextView>(android.R.id.message)!!.movementMethod = LinkMovementMethod.getInstance()

            setRegion()
        } else {
            app.apiService.setRegion(app.preferences.getString("region", "notSpecified")!!)
            findViewById<ExtendedRadioGroup>(R.id.search_region_layout).setCheckedItem(app.preferences.getString("region", "notSpecified")!!)
            findViewById<ExtendedRadioGroup>(R.id.settings_region_layout).setCheckedItem(app.preferences.getString("region", "notSpecified")!!)
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
                        app.preferences.getString("region", "notSpecified"),
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
                    lastSearchedList.visibility = View.VISIBLE

                    lastSearchedList.adapter = LastSearchedAdapter(
                        this@MainActivity,
                        records,
                        (InterfaceUtils.getY(this@MainActivity) * 0.905 * 0.1).toInt()
                    )

                } else {
                    if (needToSetText) {
                        enterNicknameSwitcher.setText(getString(R.string.enter_nickname))
                    }
                    lastSearchedList.visibility = View.GONE
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

            val requestLogAdapter = RequestLogAdapter(
                this@MainActivity,
                ArrayList<RequestLogItem>()
            )

            requestLogList.adapter = requestLogAdapter
            requestLogList.deferNotifyDataSetChanged()

            app.requestLogService.setOnRecordAddedListener { requestLogItem ->
                runOnUiThread {
                    requestLogAdapter.add(requestLogItem, requestLogList)
                }
            }

        } else {
            requestLogList.adapter = null
        }
    }

}
