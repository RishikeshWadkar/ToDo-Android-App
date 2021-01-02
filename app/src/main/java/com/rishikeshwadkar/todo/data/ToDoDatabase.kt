package com.rishikeshwadkar.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rishikeshwadkar.todo.data.models.ToDoData

@Database(entities = [ToDoData::class],version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase: RoomDatabase() {

    abstract fun todoDao(): ToDoDao

    // Singleton prevents multiple instances of database opening at the
    // same time.
    companion object{
        @Volatile
        var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase{
            // if the INSTANCE is not null then return it.
            // if it is null then create the database
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoDatabase::class.java,
                        "todo_database"
                ).build()
                INSTANCE = instance

                // return instance
                instance
            }
        }
    }
}