package com.example.trailx

// Will listen to step alerts
interface StepListener {
    fun step(timeNs: Long)
}