package ru.forblitz.statistics.dto

import com.google.gson.annotations.SerializedName

data class StatisticsDataModern(
    @SerializedName("spotted")
    val spotted: Int,
    @SerializedName("max_frags_tank_id")
    val maxFragsTankId: Int,
    @SerializedName("hits")
    val hits: Int,
    @SerializedName("frags")
    val frags: Int,
    @SerializedName("max_xp")
    val maxXp: Int,
    @SerializedName("max_xp_tank_id")
    val maxXpTankId: Int,
    @SerializedName("wins")
    val wins: Int,
    @SerializedName("losses")
    val losses: Int,
    @SerializedName("capture_points")
    val capturePoints: Int,
    @SerializedName("battles")
    val battles: Int,
    @SerializedName("damage_dealt")
    val damageDealt: Int,
    @SerializedName("damage_received")
    val damageReceived: Int,
    @SerializedName("max_frags")
    val maxFrags: Int,
    @SerializedName("shots")
    val shots: Int,
    @SerializedName("frags8p")
    val frags8p: Int,
    @SerializedName("xp")
    val xp: Int,
    @SerializedName("win_and_survived")
    val winAndSurvived: Int,
    @SerializedName("survived_battles")
    val survivedBattles: Int,
    @SerializedName("dropped_capture_points")
    val droppedCapturePoint: Int
) {

    constructor() : this(
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0
    )

    var winningPercentage: Float? = null
        get() {
            return if (field == null) {
                wins.toFloat() / battles.toFloat() * 100f
            } else {
                field
            }
        }

    var averageDamage: Float? = null
    get() {
        return if (field == null) {
            damageDealt.toFloat() / battles.toFloat()
        } else {
            field
        }
    }

    var efficiency: Float? = null
        get() {
            return if (field == null) {
                damageDealt.toFloat() / damageReceived.toFloat()
            } else {
                field
            }
        }

    var survivedPercentage: Float? = null
        get() {
            return if (field == null) {
                survivedBattles.toFloat() / battles.toFloat() * 100f
            } else {
                field
            }
        }

    var hitsOutOfShots: Float? = null
        get() {
            return if (field == null) {
                hits.toFloat() / shots.toFloat() * 100f
            } else {
                field
            }
        }

    var averageXp: Float? = null
        get() {
            return if (field == null) {
                xp.toFloat() / battles.toFloat()
            } else {
                field
            }
        }

    operator fun plus(other: StatisticsDataModern): StatisticsDataModern {
        return StatisticsDataModern(
        this.spotted + other.spotted,
        this.maxFragsTankId + other.maxFragsTankId,
        this.hits + other.hits,
        this.frags + other.frags,
        this.maxXp + other.maxXp,
        this.maxXpTankId + other.maxXpTankId,
        this.wins + other.wins,
        this.losses + other.losses,
        this.capturePoints + other.capturePoints,
        this.battles + other.battles,
        this.damageDealt + other.damageDealt,
        this.damageReceived + other.damageReceived,
        this.maxFrags + other.maxFrags,
        this.shots + other.shots,
        this.frags8p + other.frags8p,
        this.xp + other.xp,
        this.winAndSurvived + other.winAndSurvived,
        this.survivedBattles + other.survivedBattles,
        this.droppedCapturePoint + other.droppedCapturePoint
        )
    }
    
    operator fun minus(other: StatisticsDataModern): StatisticsDataModern {
        return StatisticsDataModern(
            this.spotted - other.spotted,
            this.maxFragsTankId - other.maxFragsTankId,
            this.hits - other.hits,
            this.frags - other.frags,
            this.maxXp - other.maxXp,
            this.maxXpTankId - other.maxXpTankId,
            this.wins - other.wins,
            this.losses - other.losses,
            this.capturePoints - other.capturePoints,
            this.battles - other.battles,
            this.damageDealt - other.damageDealt,
            this.damageReceived - other.damageReceived,
            this.maxFrags - other.maxFrags,
            this.shots - other.shots,
            this.frags8p - other.frags8p,
            this.xp - other.xp,
            this.winAndSurvived - other.winAndSurvived,
            this.survivedBattles - other.survivedBattles,
            this.droppedCapturePoint - other.droppedCapturePoint
        )
    }
    
}
