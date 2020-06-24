package robin.scaffold.jet.ui.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import robin.scaffold.jet.repo.PagingRepository
import robin.scaffold.jet.repo.RoomRepository

class PagingViewModel : ViewModel() {
    private val mText: MutableLiveData<String> = MutableLiveData()
    val text: LiveData<String>
        get() = mText
    private val repository: PagingRepository by lazy {
        PagingRepository()
    }

    private val data = MutableLiveData<String>()
    private val repoResult = Transformations.map(data) {
        repository.getBookList(10)
    }

    val posts = Transformations.switchMap(repoResult) { it.pagedList }!!
    val networkState = Transformations.switchMap(repoResult) { it.networkState }!!
    val refreshState = Transformations.switchMap(repoResult) { it.refreshState }!!

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        val listing = repoResult?.value
        listing?.retry?.invoke()
    }
}