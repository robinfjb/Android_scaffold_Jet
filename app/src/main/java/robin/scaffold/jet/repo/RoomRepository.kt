package robin.scaffold.jet.repo

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import robin.scaffold.jet.db.AppDatabase
import robin.scaffold.jet.db.Book
import robin.scaffold.jet.db.Shop
import robin.scaffold.jet.utils.ListTypeConverters

class RoomRepository(private val context: Context) {
    private val MIGRATION_1_2:Migration by lazy {
        object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建临时表
                database.execSQL(
                        "CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");
                // 拷贝数据
                database.execSQL(
                        "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
                // 删除老的表
                database.execSQL("DROP TABLE users");
                // 改名
                database.execSQL("ALTER TABLE users_new RENAME TO users");
            }
        }
    }
    private val db : AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "database-robin")
                .addMigrations(MIGRATION_1_2).build()
    }

    suspend fun insertBook(vararg data:Book) {
        db.bookDao().insertBook(*data)
    }

    suspend fun insertShop(vararg data:Shop) {
        db.bookDao().insertShop(*data)
    }

    suspend fun deleteBookById(id:Int) {
        val books = db.bookDao().loadAllByIds(intArrayOf(id))
        books?.apply {
            if(books.isEmpty().not()) {
                db.bookDao().delete(books[0])
            }
        }
    }

    suspend fun queryAll(): String? {
        val books = db.bookDao().getAll()
        val strs = books.map {
            it.toString()
        }
        return ListTypeConverters.strListToString(strs)
    }

    suspend fun queryByFilter(name:String, priceLowest:Int, priceHighest:Int) :String?{
        val books = db.bookDao().findByFilter(name, priceLowest, priceHighest)
        val strs = books.map {
            it.toString()
        }
        return ListTypeConverters.strListToString(strs)
    }
}