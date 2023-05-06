package edu.quinnipiac.workouttracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var date: String="",
    var name: String="",
    var type: String="",
    var duration: String="",
    var description: String=""
)