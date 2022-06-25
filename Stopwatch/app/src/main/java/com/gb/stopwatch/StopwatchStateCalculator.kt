package com.gb.stopwatch

class StopwatchStateCalculator(
    private val timestampProvider: TimestampProvider,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
) {

    fun calculateRunningState(oldState: State): State.Running =
        when (oldState) {
            is State.Running -> oldState
            is State.Paused -> {
                State.Running(
                    startTime = timestampProvider.getMilliseconds(),
                    elapsedTime = oldState.elapsedTime
                )
            }
        }

    fun calculatePausedState(oldState: State): State.Paused =
        when (oldState) {
            is State.Running -> {
                val elapsedTime = elapsedTimeCalculator.calculate(oldState)
                State.Paused(elapsedTime = elapsedTime)
            }
            is State.Paused -> oldState
        }
}