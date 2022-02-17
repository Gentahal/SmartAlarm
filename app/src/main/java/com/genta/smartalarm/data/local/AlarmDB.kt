package com.genta.smartalarm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.genta.smartalarm.data.Alarm


@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDB : RoomDatabase() {
    abstract fun alaramDao(): AlarmDao

    companion object {

    @Volatile
    var instance: AlarmDB? = null

    @JvmStatic
    fun getDataBase(context: Context): AlarmDB {
        if (instance == null) {
            synchronized(AlarmDB::class.java) {
                instance = Room.databaseBuilder(
                    context, AlarmDB::class.java, "Smart_alarm.db"
                ).fallbackToDestructiveMigration()
                   .build()
            }
        }
        return instance as AlarmDB
    }

 }  

}