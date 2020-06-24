package robin.scaffold.jet.ui.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import robin.scaffold.jet.R
import robin.scaffold.jet.component.DataBoundPageListAdapter
import robin.scaffold.jet.component.DataBoundViewHolder
import robin.scaffold.jet.databinding.ItemBookBinding
import robin.scaffold.jet.databinding.NetworkStateBinding
import robin.scaffold.jet.db.Book
import robin.scaffold.jet.net.NetworkState
import robin.scaffold.jet.net.Status

class PagingDataAdpter(private val dataBindingComponent: DataBindingComponent,
                       private val callback: ((Book?, Boolean) -> Unit)?)
    : DataBoundPageListAdapter<Book, ViewDataBinding>(
        diffCallback = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }
        })
{

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : DataBoundViewHolder<ViewDataBinding> {
        val binding = createBinding(parent, viewType)
        return when (viewType) {
            NORMAL_ITEM -> ItemViewHolder(binding)
            NETWORK_ITEM -> NetWorkViewHolder(binding)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return when(viewType) {
            NORMAL_ITEM -> {
                DataBindingUtil.inflate<ItemBookBinding>(
                                LayoutInflater.from(parent.context),
                                R.layout.item_book,
                                parent,
                                false,
                                dataBindingComponent
                        )
            }
            NETWORK_ITEM -> {
                DataBindingUtil
                        .inflate<NetworkStateBinding>(
                                LayoutInflater.from(parent.context),
                                R.layout.network_state,
                                parent,
                                false,
                                dataBindingComponent
                        )
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun bind(binding: ViewDataBinding, item: Book?) {
        when (binding) {
            is ItemBookBinding -> {
                binding.book = item
                binding.root.setOnClickListener {
                    binding.book?.let {
                        callback?.invoke(it, false)
                    }
                }
            }
            is NetworkStateBinding -> {
                binding.network = networkState
                binding.retryButton.setOnClickListener{
                    callback?.invoke(null, true)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_ITEM
        } else {
            NORMAL_ITEM
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED && super.getItemCount() != 0

    fun setNetworkState(newNetworkState: NetworkState?) {
        if(this.networkState == null || this.networkState?.status == Status.INIT) {
            return
        }
        val previousState = this.networkState
        val preExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val nowExtraRow = hasExtraRow()
        if (preExtraRow != nowExtraRow) {
            if (preExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (nowExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class ItemViewHolder(binding: ViewDataBinding): DataBoundViewHolder<ViewDataBinding>(binding)
    class NetWorkViewHolder(binding: ViewDataBinding): DataBoundViewHolder<ViewDataBinding>(binding)

    companion object {
        private const val NORMAL_ITEM = 100
        private const val NETWORK_ITEM = 101
    }

}