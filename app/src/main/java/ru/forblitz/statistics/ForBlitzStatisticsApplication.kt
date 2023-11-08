package ru.forblitz.statistics

import android.app.Application
import android.content.SharedPreferences
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.data.Constants.PreferencesSwitchesTags
import ru.forblitz.statistics.data.RecordDatabase
import ru.forblitz.statistics.service.AchievementsInfoService
import ru.forblitz.statistics.service.AdService
import ru.forblitz.statistics.service.ClanService
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.RequestsService
import ru.forblitz.statistics.service.SessionService
import ru.forblitz.statistics.service.TokensService
import ru.forblitz.statistics.service.UserAchievementsService
import ru.forblitz.statistics.service.UserClanService
import ru.forblitz.statistics.service.UserService
import ru.forblitz.statistics.service.UserStatisticsService
import ru.forblitz.statistics.service.VehicleSpecsService
import ru.forblitz.statistics.service.VehicleStatService
import ru.forblitz.statistics.service.VersionService

class ForBlitzStatisticsApplication : Application() {

    fun isHapticsEnabled(): Boolean {
        return if (setSettings[PreferencesSwitchesTags.hapticsDisabled] != null) {
            !setSettings[PreferencesSwitchesTags.hapticsDisabled]!!
        } else {
            true
        }
    }

    lateinit var preferences: SharedPreferences
    val setSettings: HashMap<String, Boolean> = HashMap()

    lateinit var apiService: ApiService
    lateinit var userService: UserService
    lateinit var userStatisticsService: UserStatisticsService
    lateinit var userClanService: UserClanService
    lateinit var clanService: ClanService
    lateinit var sessionService: SessionService
    lateinit var versionService: VersionService
    lateinit var tokensService: TokensService
    lateinit var vehicleSpecsService: VehicleSpecsService
    lateinit var vehicleStatService: VehicleStatService
    lateinit var userAchievementsService: UserAchievementsService
    lateinit var achievementsInfoService: AchievementsInfoService
    lateinit var adService: AdService
    lateinit var connectivityService: ConnectivityService
    lateinit var requestsService: RequestsService
    lateinit var recordDatabase: RecordDatabase

}