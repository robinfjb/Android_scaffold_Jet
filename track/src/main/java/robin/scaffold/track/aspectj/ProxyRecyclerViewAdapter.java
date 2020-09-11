package robin.scaffold.track.aspectj;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class ProxyRecyclerViewAdapter extends RecyclerView.Adapter {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter realAdapter;

    public ProxyRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter realAdapter) {
        this.recyclerView = recyclerView;
        this.realAdapter = realAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (realAdapter != null) {
            realAdapter.onBindViewHolder(holder, position, payloads);
        }
        Tracker.INSTANCE.setViewTracker(holder.itemView, Tracker.INSTANCE.getRecyclerViewPath(holder.itemView, recyclerView, position));
    }

    @Override
    public int getItemViewType(int position) {
        if (realAdapter != null) {
            return realAdapter.getItemViewType(position);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        if (realAdapter != null) {
            realAdapter.setHasStableIds(hasStableIds);
        }
    }

    @Override
    public long getItemId(int position) {
        if (realAdapter != null) {
            return realAdapter.getItemId(position);
        } else {
            return super.getItemId(position);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (realAdapter != null) {
            realAdapter.onViewRecycled(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        if (realAdapter != null) {
            return realAdapter.onFailedToRecycleView(holder);
        } else {
            return super.onFailedToRecycleView(holder);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (realAdapter != null) {
            realAdapter.onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (realAdapter != null) {
            realAdapter.onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        if (realAdapter != null) {
            realAdapter.registerAdapterDataObserver(observer);
        }
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        if (realAdapter != null) {
            realAdapter.unregisterAdapterDataObserver(observer);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        if (realAdapter != null) {
            realAdapter.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (realAdapter != null) {
            realAdapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return realAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        realAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return realAdapter.getItemCount();
    }
}
