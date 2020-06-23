package robin.scaffold.jet.ui.databind

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BindViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is databinding Fragment"
    }

    fun getText() = _text
}