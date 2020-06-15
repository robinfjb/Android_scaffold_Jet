package robin.scaffold.jet.ui.tools

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is tools Fragment"
    }
    fun getTex() = _text
}