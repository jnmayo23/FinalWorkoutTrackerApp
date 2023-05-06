import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import edu.quinnipiac.workouttracker.Workout
import edu.quinnipiac.workouttracker.WorkoutDao
import edu.quinnipiac.workouttracker.WorkoutDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkoutDataEntityTest {


    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutDatabase: WorkoutDatabase
    private var workout1 = Workout(1, "05-05-2023", "Test1", "Cardio", "20 min", "Went on a jog")
    private var workout2 = Workout(2, "05-05-2023", "Test2", "Lift", "1 hour", "Lifted weights")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        workoutDatabase = Room.inMemoryDatabaseBuilder(context, WorkoutDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        workoutDao = workoutDatabase.getWorkoutDao()
    }
    private suspend fun addOneWorkoutToDb() {
        workoutDao.addWorkout(workout1)
    }

    private suspend fun addTwoWorkoutsToDb() {
        workoutDao.addWorkout(workout1)
        workoutDao.addWorkout(workout2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsWorkoutsIntoDB() = runBlocking {
        addOneWorkoutToDb()
        val allWorkouts = workoutDao.getAllWorkouts().first()
        assertEquals(allWorkouts[0], workout1)
    }
    @Test
    @Throws(Exception::class)
    fun daoGetAllWorkouts_returnsAllWorkoutsFromDB() = runBlocking {
        addTwoWorkoutsToDb()
        val allWorkouts = workoutDao.getAllWorkouts().first()
        Assert.assertEquals(allWorkouts[0], workout1)
        Assert.assertEquals(allWorkouts[1], workout2)
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        workoutDatabase.close()
    }

}
