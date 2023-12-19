package ru.forblitz.statistics.helpers

class SearchProcessingController {

    var searchProcessing: Boolean = false
        private set

    private var isStatisticsLoaded: Boolean = false
    private var isAchievementsLoaded: Boolean = false
    private var isClanLoaded: Boolean = false
    private var isTanksLoaded: Boolean = false
    private var isSessionsLoaded: Boolean = false

    fun startSearchProcessing() {
        searchProcessing = true
        isStatisticsLoaded = false
        isAchievementsLoaded = false
        isClanLoaded = false
        isTanksLoaded = false
        isSessionsLoaded = false
    }
    
    fun stopSearchProcessing() {
        searchProcessing = false
    }

    fun completeStatisticsLoading() {
        isStatisticsLoaded = true
        update()
    }

    fun completeAchievementsLoading() {
        isAchievementsLoaded = true
        update()
    }

    fun completeClanLoading() {
        isClanLoaded = true
        update()
    }

    fun completeTanksLoading() {
        isTanksLoaded = true
        update()
    }

    fun completeSessionsLoading() {
        isSessionsLoaded = true
        update()
    }

    private fun update() {
        if (isStatisticsLoaded and isAchievementsLoaded and isClanLoaded and isTanksLoaded and isSessionsLoaded) {
            searchProcessing = false
        }
    }

}