import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.quinnipiac.workouttracker.IntervalWorkout
import edu.quinnipiac.workouttracker.IntervalWorkoutDao
import edu.quinnipiac.workouttracker.IntervalWorkoutDatabase
import org.junit.runner.RunWith
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class IntervalWorkoutDataEntityTest {


    private lateinit var intervalWorkoutDao: IntervalWorkoutDao
    private lateinit var intervalWorkoutDatabase: IntervalWorkoutDatabase
    private var intervalWorkout1 = IntervalWorkout(1, "Test1", 3, 30, 20, 20, 30)
    private var intervalWorkout2 = IntervalWorkout(2, "Test2", 2, 10, 30, 15, 10)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        intervalWorkoutDatabase = Room.inMemoryDatabaseBuilder(context, IntervalWorkoutDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        intervalWorkoutDao = intervalWorkoutDatabase.getIntervalWorkoutDao()
    }
    private suspend fun addOneIntervalWorkoutToDb() {
        intervalWorkoutDao.addIntervalWorkout(intervalWorkout1)
    }

    private suspend fun addTwoIntervalWorkoutsToDb() {
        intervalWorkoutDao.addIntervalWorkout(intervalWorkout1)
        intervalWorkoutDao.addIntervalWorkout(intervalWorkout2)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsIntervalWorkoutIntoDB() = runBlocking {
        addOneIntervalWorkoutToDb()
        val allIntervalWorkouts = intervalWorkoutDao.getAllIntervalWorkouts().first()
        assertEquals(allIntervalWorkouts[0], intervalWorkout1)
    }
    @Test
    @Throws(Exception::class)
    fun daoGetAllIntervalWorkouts_returnsAllIntervalWorkoutsFromDB() = runBlocking {
        addTwoIntervalWorkoutsToDb()
        val allIntervalWorkouts = intervalWorkoutDao.getAllIntervalWorkouts().first()
        Assert.assertEquals(allIntervalWorkouts[0], intervalWorkout2)
        Assert.assertEquals(allIntervalWorkouts[1], intervalWorkout1)
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        intervalWorkoutDatabase.close()
    }

}
