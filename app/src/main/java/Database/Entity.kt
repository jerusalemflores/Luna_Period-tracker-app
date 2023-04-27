package database
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.*
import java.util.*
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "symptom_database")
data class Symptom(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val date: String,
    val symptom: String
    ): Parcelable

@Dao
interface SymptomDao {

    @Update
    suspend fun update(symptom: Symptom)

    @Delete
    suspend fun delete(symptom: Symptom)

    @Query("SELECT * FROM symptom_database")
    suspend fun getAllSymptoms(): List<Symptom>

    @Query("SELECT * FROM symptom_database WHERE id = :id")
    suspend fun getSymptomById(id: Int): Symptom?

    @Insert
    fun insertAll(vararg symptoms: Symptom)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymptom(symptom: Symptom) {
        Log.d("SymptomDao", "Inserting symptom: $symptom")

    }

    suspend fun insertWithList(symptoms: List<Symptom>) {
        symptoms.forEach { insertSymptom(it) }
        var symptomsList = getAllSymptoms()
    }

    @Query("SELECT * FROM symptom_database WHERE date = :date")
    suspend fun getSymptomsByDate(date: String): List<Symptom>

    @Query("SELECT * FROM symptom_database WHERE date = :date")
    suspend fun getSymptomsByDateString(date: String): List<Symptom>


    @Query("DELETE FROM symptom_database WHERE date = :date")
    suspend fun deleteSymptomsByDate(date: String)

}


