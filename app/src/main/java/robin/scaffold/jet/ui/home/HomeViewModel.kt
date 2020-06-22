package robin.scaffold.jet.ui.home

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class HomeViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = ""
    }
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getText() = _text

    fun display() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                postMessage("hello world")
            }
        }
    }

    fun displayArgu(arguments : Bundle?) {
        val msg = arguments?.let { HomeFragmentArgs.fromBundle(it).myArg }
        msg ?.apply {
            postMessage(msg)
        }
    }

    private fun postMessage(message: String) {
        val display = "$message".take(10000)
        _text.postValue(display)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Cancel all coroutines
    }
}