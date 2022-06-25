package com.gb.stopwatch

class StopWatchUseCase(
    private val formatter: TimestampMillisecondsFormatter
) {

    fun start(oldState: State): State.Running =
        when (oldState) {
            is State.Running ->
                State.Running(oldState.startTime, System.currentTimeMillis())
            is State.Paused ->
                State.Running(
                    startTime = System.currentTimeMillis(),
                    elapsedTime = oldState.elapsedTime
                )
        }

    fun pause(state: State.Running): State.Paused = State.Paused(System.currentTimeMillis() - state.startTime + state.elapsedTime)

    fun stop(): State.Paused = State.Paused(0)

    fun calculate(state: State): String =
        when (state) {
            is State.Paused -> formatter.format(state.elapsedTime)
            is State.Running -> formatter.format(System.currentTimeMillis() - state.startTime + state.elapsedTime)
        }
}



