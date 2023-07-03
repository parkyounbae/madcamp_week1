package com.example.myapplication

class DataManager {
    private val dataObservers = mutableListOf<DataObserver>()
    private var sharedData: String = ""

    fun registerObserver(observer: DataObserver) {
        dataObservers.add(observer)
    }

    fun unregisterObserver(observer: DataObserver) {
        dataObservers.remove(observer)
    }

    fun setData(newData: String) {
        sharedData = newData
        notifyObservers()
    }

    private fun notifyObservers() {
        dataObservers.forEach { observer ->
            observer.onDataChanged()
        }
    }
}

interface DataObserver {
    fun onDataChanged()
}