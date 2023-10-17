package ru.forblitz.statistics.service

open class DeferredTasksService {

    protected var isRequestLoaded: Boolean = false

    private val taskQueue: ArrayList<Runnable> = ArrayList()

    fun addTaskOnEndOfLoad(runnable: Runnable) {
        if (isRequestLoaded) {
            runnable.run()
        } else {
            taskQueue.add(runnable)
        }
    }

    protected fun onEndOfLoad() {
        isRequestLoaded = true
        taskQueue.forEach { it.run() }
        taskQueue.clear()
    }

}