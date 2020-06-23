package robin.scaffold.jet.db

import androidx.room.*

@Entity(tableName = "book")
data class Book(
        @ColumnInfo(name = "name")
        val name:String,
        @ColumnInfo(name = "price")
        val price:Int,
        @Embedded
        val producer: Producer,
        val shopCreatorId: Int,
        @PrimaryKey(autoGenerate = true)
        val id:Int = 0
){
        @Ignore
        val temp: String = ""
}

data class Producer(
        @ColumnInfo(name = "produce_name")
        val name:String
)

@Entity(tableName = "shop")
data class Shop(
        @PrimaryKey
        val shopId:Int,
        @ColumnInfo(name = "name")
        val name:String,
        @ColumnInfo(name = "address")
        val address: String
)

data class BookWithShop(
        @Embedded val shop: Shop,
        @Relation(
                parentColumn = "shopId",
                entityColumn = "shopCreatorId"
        )
        val books: List<Book>
)