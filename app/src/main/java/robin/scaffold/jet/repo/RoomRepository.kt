package robin.scaffold.jet.repo

import android.content.Context
import androidx.room.Room
import robin.scaffold.jet.db.AppDatabase
import robin.scaffold.jet.db.Book
import robin.scaffold.jet.db.Shop
import robin.scaffold.jet.utils.ListTypeConverters

class RoomRepository(private val context: Context) {
    private val db : AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "database-robin")
                .fallbackToDestructiveMigration().build()
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