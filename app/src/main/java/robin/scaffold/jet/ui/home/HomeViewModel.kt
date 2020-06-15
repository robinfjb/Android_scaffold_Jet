package robin.scaffold.jet.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import robin.scaffold.jet.utils.utc2Local
import java.io.*

class HomeViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "world"
    }
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getText() = _text

    fun display() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                postMessage("hello")
            }
        }
    }

    private fun postMessage(message: String) {
        val display = "$message  \n ${_text.value}".take(10000)
        _text.postValue(display)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Cancel all coroutines
    }
}