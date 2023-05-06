package edu.quinnipiac.workouttracker

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Workout::class] , version = 1)
abstract class WorkoutDatabase : RoomDatabase() {

    abstract fun getWorkoutDao() : WorkoutDao

    companion object{
        private const val DB_NAME = "Workout-Database.db"

        @Volatile
        private var instance : WorkoutDatabase? = null

        fun getDatabase(context: Context) : WorkoutDatabase{
            return instance ?: synchronized( this){
                var instance = Room.databaseBuilder(
                    context.applicationContext.applicationContext,
                    WorkoutDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instance= instance
                return instance
            }
        }
    }

    private val roomCallback: Callback = object : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // this method is called when database is created
            // and below line is to populate our data.
            PopulateDbAsyncTask(instance).execute()
        }
    }

    // we are creating an async task class to perform task in background.
    private class PopulateDbAsyncTask internal constructor(instance: WorkoutDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        init {
            val workoutDao: WorkoutDao? = instance?.getWorkoutDao()
        }

//        protected fun doInBackground(vararg voids: Void): Void? {
//            return null
//        }

        override fun doInBackground(vararg p0: Void?): Void? {
            return null
        }
    }
}