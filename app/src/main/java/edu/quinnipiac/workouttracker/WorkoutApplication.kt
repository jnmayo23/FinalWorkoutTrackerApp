package edu.quinnipiac.workouttracker

import android.app.Application

class WorkoutApplication : Application(){
    val intervalWorkoutDatabase : IntervalWorkoutDatabase by lazy {IntervalWorkoutDatabase.getDatabase(this)}
    val workoutDatabase : WorkoutDatabase by lazy {WorkoutDatabase.getDatabase(this)}
}