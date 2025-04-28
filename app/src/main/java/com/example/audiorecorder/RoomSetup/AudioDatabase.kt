package com.example.audiorecorder.RoomSetup

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

//possibility we have multiple databases so we use list of DB name, version no shows it's first
// on updating app try different version no
@Database(entities = [Audio::class], version = 1)
abstract class AudioDatabase : RoomDatabase() {

    abstract fun audioDao(): AudioDao

    companion object {
        @Volatile
        private var INSTANCE: AudioDatabase? = null

        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context): AudioDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AudioDatabase::class.java,
                        "audioDB"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}