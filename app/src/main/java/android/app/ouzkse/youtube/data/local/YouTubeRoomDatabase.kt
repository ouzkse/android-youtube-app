package android.app.ouzkse.youtube.data.local

import android.app.ouzkse.youtube.data.model.Item
import android.app.ouzkse.youtube.data.model.SearchHistoryModel
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class, SearchHistoryModel::class], version = 1, exportSchema = false)
abstract class YouTubeRoomDatabase : RoomDatabase() {

    abstract fun youtubeDao(): YouTubeDao

    companion object {

        @Volatile
        private var INSTANCE: YouTubeRoomDatabase? = null

        fun getDatabase(context: Context): YouTubeRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YouTubeRoomDatabase::class.java,
                    "_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}