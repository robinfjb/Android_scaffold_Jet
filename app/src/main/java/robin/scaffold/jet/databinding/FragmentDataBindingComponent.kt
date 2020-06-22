package robin.scaffold.jet.databinding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

class FragmentDataBindingComponent() : DataBindingComponent {
    private val bindingAdapter = BindingAdapters
    private val swipeRefreshLayoutBinding = SwipeRefreshLayoutBinding

    override fun getBindingAdapters() = bindingAdapter
}