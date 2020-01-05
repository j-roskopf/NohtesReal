package joetr.com.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import joetr.com.data.dao.LabelDao
import joetr.com.data.dao.NoteDao
import joetr.com.data.entities.LabelEntity
import joetr.com.data.entities.NoteEntity
import java.util.concurrent.Executors

@Database(entities = [NoteEntity::class, LabelEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun labelDao(): LabelDao

    companion object {

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) : AppDatabase {
            val db = Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, "notes.db")

            return db.addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {
                        getInstance(context).labelDao().insertAllLabels(listOf(
                            LabelEntity(
                                label = "Personal"
                            ),
                            LabelEntity(
                                label = "Work"
                            ),
                            LabelEntity(
                                label = "Shopping"
                            )
                        ))
                    }
                }
            }).build()
        }
    }
}

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f: () -> Unit) {
    IO_EXECUTOR.execute(f)
}
