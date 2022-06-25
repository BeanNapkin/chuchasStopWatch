package com.gb.stopwatch

class ElapsedTimeCalculator(
    private val timestampProvider: TimestampProvider
) {

    fun calculate(state: State.Running): Long {
        val currentTimestamp = timestampProvider.getMilliseconds()
        val timePassedSinceStart = if (currentTimestamp > state.startTime) {
            currentTimestamp - state.startTime
        } else {
            0
        }
        return timePassedSinceStart + state.elapsedTime
    }
}