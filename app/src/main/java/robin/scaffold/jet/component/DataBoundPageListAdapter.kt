package robin.scaffold.jet.component

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil

abstract class DataBoundPageListAdapter<T, V : ViewDataBinding>(
        diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, DataBoundViewHolder<V>>(
        AsyncDifferConfig.Builder<T>(diffCallback)
                //.setBackgroundThreadExecutor(appExecutors.diskIO())
                .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent, viewType)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup, viewType: Int): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        onBind(holder)
        val size = currentList?.size ?: 0
        if(size > 0 && position < size) {
            getItem(position)?.let {
                bind(holder.binding, it)
            }
        } else {
            bind(holder.binding, null)
        }
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T?)

    open fun onBind(holder: DataBoundViewHolder<V>) {

    }
}