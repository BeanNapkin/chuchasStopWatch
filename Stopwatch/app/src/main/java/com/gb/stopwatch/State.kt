package com.gb.stopwatch

sealed class State {
    data class Paused(
        val elapsedTime: Long
    ) : State()

    data class Running(
        val startTime: Long,
        val elapsedTime: Long
    ) : State()
}