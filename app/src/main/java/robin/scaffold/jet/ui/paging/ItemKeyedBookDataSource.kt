package robin.scaffold.jet.ui.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import robin.scaffold.jet.db.Book
import robin.scaffold.jet.db.Producer
import robin.scaffold.jet.net.NetworkState
import robin.scaffold.jet.utils.coroutine
import kotlin.math.pow

class ItemKeyedBookDataSource : ItemKeyedDataSource<Int, Book>() {
    private var startPosition = 0
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    private var retry: (() -> Any)? = null
    fun retryAllFailed(){
        val prevRetry=retry
        retry=null
        prevRetry?.let {
            coroutine {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Book>) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        val items = loadData(startPosition, params.requestedLoadSize)
        callback.onResult(items)
        startPosition = startPosition.plus(items.size)
        networkState.postValue(NetworkState.LOADED)
        initialLoad.postValue(NetworkState.LOADED)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Book>) {
        networkState.postValue(NetworkState.LOADING)
        val items = loadData(startPosition, params.requestedLoadSize)
        callback.onResult(items)
        startPosition = startPosition.plus(items.size)
        networkState.postValue(NetworkState.LOADED)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Book>) {
        // ignored, since we only ever append to our initial load
    }

    override fun getKey(item: Book) = item.id

    private fun loadData(startPosition: Int, limit: Int): List<Book> {
        val list = ArrayList<Book>()
        for (i in 0 until limit) {
            var position=startPosition + i
            val data = Book("名称${position.toString()}",
                    position.toDouble().pow(2.0).toInt(),
                    Producer("pro${position.toString()}"),
            0,
                    position)
            list.add(data)
        }
        return list
    }
}