package edu.quinnipiac.workouttracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import edu.quinnipiac.workouttracker.Workout
import edu.quinnipiac.workouttracker.WorkoutDao
import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope


class WorkoutViewModel(private val workoutDao: WorkoutDao) : ViewModel() {

    val allWorkouts : LiveData<List<Workout>> = workoutDao.getAllWorkouts().asLiveData()

    fun addNewWorkout(date: String, name: String, type: String, duration: String, description: String) {
        val newWorkout = getNewWorkoutEntry(date, name, type, duration, description)
        insertWorkout(newWorkout)
    }

    private fun insertWorkout(newWorkout: Workout) {
        viewModelScope.launch {
            workoutDao.addWorkout(newWorkout)
        }
    }

    fun retrieveWorkout(id: Int): LiveData<Workout> {
        return workoutDao.getWorkout(id).asLiveData()
    }

    private fun getNewWorkoutEntry(date: String, name: String, type: String, duration: String, description: String): Workout {
        return(
                Workout(
                    date = date,
                    name = name,
                    type = type,
                    duration = duration,
                    description = description
                ))
    }

    fun isEntryValid(date: String, name: String, type: String, duration: String, description: String): Boolean{
        if (date.isBlank() || name.isBlank() || type.isBlank() || duration.isBlank() || description.isBlank()){
            return false
        }
        return true
    }
}
class WorkoutViewModelFactory (private  val workoutDao: WorkoutDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(workoutDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


