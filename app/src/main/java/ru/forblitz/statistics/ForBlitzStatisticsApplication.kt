package ru.forblitz.statistics

import android.app.Application
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.apiloadservice.AchievementsInformationService
import ru.forblitz.statistics.apiloadservice.ClanInformationService
import ru.forblitz.statistics.apiloadservice.MetadataService
import ru.forblitz.statistics.apiloadservice.UserAchievementsService
import ru.forblitz.statistics.apiloadservice.UserClanService
import ru.forblitz.statistics.apiloadservice.UserService
import ru.forblitz.statistics.apiloadservice.UserStatisticsService
import ru.forblitz.statistics.apiloadservice.VehicleSpecsService
import ru.forblitz.statistics.data.Constants.PreferencesSwitchesTags
import ru.forblitz.statistics.data.RecordDatabase
import ru.forblitz.statistics.service.AdService
import ru.forblitz.statistics.service.ConnectivityService
import ru.forblitz.statistics.service.PreferencesService
import ru.forblitz.statistics.service.RequestsService
import ru.forblitz.statistics.service.SessionService
import ru.forblitz.statistics.service.UserVehiclesStatisticsService

class ForBlitzStatisticsApplication : Application() {

    fun isHapticsEnabled(): Boolean {
        return if (setSettings[PreferencesSwitchesTags.hapticsDisabled] != null) {
            !setSettings[PreferencesSwitchesTags.hapticsDisabled]!!
        } else {
            true
        }
    }

    lateinit var preferencesService: PreferencesService
    val setSettings: HashMap<String, Boolean> = HashMap()

    lateinit var apiService: ApiService
    lateinit var userService: UserService
    lateinit var userStatisticsService: UserStatisticsService
    lateinit var userClanService: UserClanService
    lateinit var clanInformationService: ClanInformationService
    lateinit var sessionService: SessionService
    lateinit var metadataService: MetadataService
    lateinit var vehicleSpecsService: VehicleSpecsService
    lateinit var userVehiclesStatisticsService: UserVehiclesStatisticsService
    lateinit var userAchievementsService: UserAchievementsService
    lateinit var achievementsInformationService: AchievementsInformationService
    lateinit var adService: AdService
    lateinit var connectivityService: ConnectivityService
    lateinit var requestsService: RequestsService
    lateinit var recordDatabase: RecordDatabase

}