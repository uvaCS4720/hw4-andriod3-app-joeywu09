package edu.nd.pmcburne.hello

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.nd.pmcburne.hello.data.LocationEntity
import edu.nd.pmcburne.hello.data.LocationTagEntity

@Database(
    entities = [LocationEntity::class, LocationTagEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CampusDatabase : RoomDatabase() {
    abstract fun dao(): CampusDao

    companion object {
        fun create(context: Context): CampusDatabase =
            Room.databaseBuilder(
                context,
                CampusDatabase::class.java,
                "campus_maps.db",
            ).build()
    }
}
