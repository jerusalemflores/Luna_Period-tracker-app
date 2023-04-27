package database

import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import com.salemflores.luna.Converters

//import database.Entity.Symptom




@Database(entities = [Symptom::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SymptomDatabase : RoomDatabase() {
    abstract fun symptomDao(): SymptomDao

    companion object {
        @Volatile
        private var INSTANCE: SymptomDatabase? = null

        fun getDatabase(context: Context): SymptomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SymptomDatabase::class.java,
                    "symptom_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

