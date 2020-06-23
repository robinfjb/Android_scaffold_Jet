package robin.scaffold.jet.ui.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoomViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is tools Fragment"
    }
    fun getTex() = _text
}