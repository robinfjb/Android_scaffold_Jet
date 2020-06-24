package robin.scaffold.jet.databinding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

@BindingAdapter("load_asset")
fun loadAsset(imageView: ImageView, id: Int) =
        Glide.with(imageView.context).load(id).into(imageView)

@BindingAdapter("load_image")
fun loadImage(imageView: ImageView, path: String?) =
        Glide.with(imageView.context)
                .load(path)
                .into(imageView)


@BindingAdapter("load_res")
fun loadImage(imageView: ImageView, @DrawableRes resId: Int){
    val cropOptions = RequestOptions().centerCrop()
    Glide.with(imageView.context).load(resId).apply(cropOptions)
            .into(imageView)
}

@BindingAdapter("load_url")
fun loadImage(imageView: ImageView, url: String){
    if(url.isNotEmpty()) {
        val cropOptions = RequestOptions().centerCrop()
        Glide.with(imageView.context).load(url)
            .apply(cropOptions)
            .into(imageView)
    }
}
