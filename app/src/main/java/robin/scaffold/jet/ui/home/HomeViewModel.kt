package robin.scaffold.jet.ui.home

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import robin.scaffold.jet.net.WeatherResult
import robin.scaffold.jet.repo.HomeRepository

class HomeViewModel : ViewModel(){
    private val _text = MutableLiveData<String>()
    private val _textNet = MutableLiveData<String>()
    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val repository: HomeRepository by lazy {
        HomeRepository()
    }
    private var dispose: Disposable? = null

    val text = _text

    val textNet = _textNet

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


    fun getWeather() {
        dispose = repository.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe ({
                    val result = it.weatherinfo
                    _textNet.postValue(result.toString())
                }){
                    _textNet.postValue(it.message)
                }
    }

    private fun postMessage(message: String) {
        val display = "$message".take(10000)
        _text.postValue(display)
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel() // Cancel all coroutines
        if(dispose?.isDisposed == false) {
            dispose?.dispose()
        }
    }
}