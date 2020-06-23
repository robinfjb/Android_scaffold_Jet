package robin.scaffold.jet.db.dao

import androidx.room.*
import robin.scaffold.jet.db.Book
import robin.scaffold.jet.db.BookWithShop
import robin.scaffold.jet.db.Shop

@Dao
interface BookDao {
//    @Query("SELECT * FROM book")
//    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Book>

    @Query("SELECT * FROM book WHERE name LIKE :name AND price BETWEEN :priceLowest AND :priceHighest")
    fun findByFilter(name: String, priceLowest:Int, priceHighest:Int): List<Book>

    @Delete
    fun delete(user: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(vararg data: Book)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShop(vararg data: Shop)

    @Transaction
    @Query("SELECT * FROM shop")
    fun getAll(): List<BookWithShop>
}