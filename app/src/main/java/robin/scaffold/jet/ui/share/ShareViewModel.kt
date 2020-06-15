package robin.scaffold.jet.ui.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is share Fragment"
    }

    fun getText() = _text
}