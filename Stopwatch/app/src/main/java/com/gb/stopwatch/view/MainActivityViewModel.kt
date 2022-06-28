package com.gb.stopwatch.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.stopwatch.*
import kotlinx.coroutines.*

class MainActivityViewModel : ViewModel() {

    private val stopWatchUseCase = StopWatchUseCase(TimestampMillisecondsFormatter())

    private val scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    private val mutableLiveData: MutableLiveData<String> = MutableLiveData()
    val liveData: LiveData<String> = mutableLiveData

    var currentState: State = State.Paused(0)
        private set

    fun update() = mutableLiveData

    fun start() {
        if (job == null) {
            currentState = stopWatchUseCase.start(currentState)

            job = scope.launch {
                while (isActive) {
                    mutableLiveData.postValue(stopWatchUseCase.format(currentState))
                    delay(20)
                }
            }
        }
    }

    fun pause() {
        if ((currentState as? State.Running) != null){
            currentState = stopWatchUseCase.pause(currentState as State.Running)
            mutableLiveData.postValue(stopWatchUseCase.format(currentState))
            scope.coroutineContext.cancelChildren()
            job = null
        }
    }

    fun stop() {
        currentState = stopWatchUseCase.stop()
        scope.coroutineContext.cancelChildren()
        job = null
        mutableLiveData.postValue("00:00:000")
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
