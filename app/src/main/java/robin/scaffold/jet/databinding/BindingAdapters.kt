package robin.scaffold.jet.databinding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.w3c.dom.Text

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter("image_src")
    fun bindImage(imageView: ImageView, @DrawableRes src: Int) {
        if(src != 0) {
            Glide.with(imageView.context).load(src).into(imageView)
        }
    }
}