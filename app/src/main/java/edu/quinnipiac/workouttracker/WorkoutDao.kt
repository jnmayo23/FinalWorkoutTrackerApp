package edu.quinnipiac.workouttracker

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert
    suspend fun addWorkout(workout: Workout)

    @Delete
    suspend fun deleteWorkout(workout: Workout)

    @Query("SELECT * FROM workout WHERE id =:id")
    fun getWorkout(id:Int) : Flow<Workout>

    @Query("SELECT * FROM workout ORDER BY date DESC")
    fun getAllWorkouts(): Flow<List<Workout>>
}