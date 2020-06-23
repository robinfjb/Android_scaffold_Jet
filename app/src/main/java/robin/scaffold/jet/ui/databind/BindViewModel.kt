package robin.scaffold.jet.ui.databind

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import robin.scaffold.jet.utils.Preference

class BindViewModel(private val context: Context) : ViewModel() {
    var userId: String by Preference(context, "user_id", "")
    var userName: String by Preference(context, "user_name", "")

    private val _text = MutableLiveData<String>().apply {
        value = "This is databinding Fragment"
    }

    fun getText() = _text

    fun getUser():User {
        val user = User("robin", 777)
        userId = user.userId.toString()
        userName = user.userName
        return user
    }
}