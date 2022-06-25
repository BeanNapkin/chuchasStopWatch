package com.gb.stopwatch.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.stopwatch.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect

class MainActivityViewModel : ViewModel() {

    private val stopWatchUseCase = StopWatchUseCase(TimestampMillisecondsFormatter())

    private val scope = CoroutineScope(Dispatchers.Main)

    private var job: Job? = null

    private val mutableTicker = MutableStateFlow("")
    val ticker: StateFlow<String> = mutableTicker

    private val mutableLiveData: MutableLiveData<String> = MutableLiveData()
    val liveData: LiveData<String> = mutableLiveData

    var currentState: State = State.Paused(0)
        private set

    fun collect() {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            ticker.collect {
                mutableLiveData.value = it
            }
        }
    }

    fun start() {
        if (job == null) {
            currentState = stopWatchUseCase.start(currentState)

            job = scope.launch {
                while (isActive) {
                    mutableTicker.value = stopWatchUseCase.calculate(currentState)
                    delay(20)
                }
            }
        }
    }

    fun pause() {
        if ((currentState as? State.Running) != null){
            currentState = stopWatchUseCase.pause(currentState as State.Running)
            mutableTicker.value = stopWatchUseCase.calculate(currentState)
            scope.coroutineContext.cancelChildren()
            job = null
        }
    }

    fun stop() {
        currentState = stopWatchUseCase.stop()
        scope.coroutineContext.cancelChildren()
        job = null
        mutableTicker.value = "00:00:000"
    }
}
