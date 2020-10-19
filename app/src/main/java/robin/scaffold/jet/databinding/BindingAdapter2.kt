package robin.scaffold.jet.databinding

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapter2 {
    @JvmStatic
    @BindingAdapter("robinTest")
    fun testRobin(textView: TextView, src: String?) {
        if(src != null) {
            Log.e("fjb", "testRobin src = $src")
        }
    }
}