package robin.scaffold.jet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import robin.scaffold.jet.db.dao.BookDao

@Database(entities = [Book::class,Shop::class],
    version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun bookDao(): BookDao
}