package ru.forblitz.statistics

import android.app.Application
import android.content.SharedPreferences
import ru.forblitz.statistics.api.ApiService
import ru.forblitz.statistics.data.RecordDatabase
import ru.forblitz.statistics.service.*

class ForBlitzStatisticsApplication : Application() {

    lateinit var preferences: SharedPreferences
    val setSettings: HashMap<String, Boolean> = HashMap()

    lateinit var apiService: ApiService
    lateinit var userService: UserService
    lateinit var randomService: RandomService
    lateinit var ratingService: RatingService
    lateinit var userClanService: UserClanService
    lateinit var clanService: ClanService
    lateinit var sessionService: SessionService
    lateinit var versionService: VersionService
    lateinit var vehicleSpecsService: VehicleSpecsService
    lateinit var vehicleStatService: VehicleStatService
    lateinit var adService: AdService
    lateinit var connectivityService: ConnectivityService
    lateinit var recordDatabase: RecordDatabase

}