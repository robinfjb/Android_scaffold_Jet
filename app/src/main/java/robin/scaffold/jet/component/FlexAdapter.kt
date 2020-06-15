package robin.scaffold.jet.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import robin.scaffold.jet.R
import robin.scaffold.jet.model.FlexModel


class FlexAdapter(private val list: List<FlexModel>) :
    RecyclerView.Adapter<FlexAdapter.FlexViewHolder>() {

    private var mListener: ((item: FlexModel) -> Unit)? = null

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexViewHolder {
        return when (viewType) {
            COMMON_ITEM -> {
                val item = LayoutInflater.from(parent.context)
                    .inflate(R.layout.flex_item_layout, parent, false)
                FlexViewHolder(item)
            }
            else -> {
                val item = LayoutInflater.from(parent.context)
                    .inflate(R.layout.flex_title_item_layout, parent, false)
                TagFlexViewHolder(item)
            }
        }
    }

    override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {
        holder.bindView(list[position])
        holder.itemView.setOnClickListener {
            mListener?.invoke(list[holder.adapterPosition])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].isTag) {
            TAG_ITEM
        } else {
            COMMON_ITEM
        }
    }

    fun setOnItemClickListener(listener: ((item: FlexModel) -> Unit)) {
        mListener = listener
    }

    open class FlexViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        open fun bindView(model: FlexModel) {
            val mac = view.findViewById<TextView>(R.id.flex_item_name)
            val extra = view.findViewById<TextView>(R.id.flex_item_extra)
            val content = view.findViewById<TextView>(R.id.flex_item_content)
            val count = view.findViewById<TextView>(R.id.flex_item_count)
            mac.text = model.name
            content.text = model.content
            extra.text = model.extra
            count.text = model.count.toString()
        }
    }

    class TagFlexViewHolder(private val view: View) : FlexViewHolder(view) {
        override fun bindView(model: FlexModel) {
            val title = view.findViewById<TextView>(R.id.flex_item_title)
           // val count = view.findViewById<TextView>(R.id.flex_item_count)
            title.text = "${model.name}(${model.count})"
           // count.text = model.signal.toString() + "个"
        }
    }

    companion object {
        private const val COMMON_ITEM = 101
        private const val TAG_ITEM = 102
    }
}