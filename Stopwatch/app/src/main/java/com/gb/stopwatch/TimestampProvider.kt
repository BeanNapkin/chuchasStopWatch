package com.gb.stopwatch

interface TimestampProvider {
    fun getMilliseconds(): Long
}