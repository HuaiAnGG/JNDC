package google.architecture.pending.pending;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import google.architecture.coremodel.datamodel.http.entities.TodoData;
import google.architecture.pending.R;
import google.architecture.pending.databinding.TodoPendingItemBinding;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-05 2:53
 */
public class TodoPendingAdapter extends RecyclerView.Adapter<TodoPendingAdapter.PendingViewHolder> {

    private List<TodoData.PendingResultsBean> pendingList;
    private TodoPendingClickCallback pendingClickCallback;

    public TodoPendingAdapter(TodoPendingClickCallback callback) {
        this.pendingClickCallback = callback;
    }

    public void setPendingList(final List<TodoData.PendingResultsBean> list) {
        if (pendingList == null) {
            pendingList = list;
            notifyItemRangeInserted(0, pendingList.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return pendingList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoData.PendingResultsBean oldData = pendingList.get(oldItemPosition);
                    TodoData.PendingResultsBean newData = list.get(newItemPosition);
                    return oldData.getTitle().equals(newData.getTitle());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoData.PendingResultsBean oldData = pendingList.get(oldItemPosition);
                    TodoData.PendingResultsBean newData = list.get(newItemPosition);
                    return oldData.getTitle().equals(newData.getTitle())
                            && oldData.getIcon() == newData.getIcon()
                            && oldData.getAuthor().equals(newData.getAuthor())
                            && oldData.getUpdateTime().equals(newData.getUpdateTime());
                }
            });
            pendingList = list;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public PendingViewHolder onCreateViewHolder(ViewGroup parent, int positon) {
        TodoPendingItemBinding  binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.todo_pending_item,
                        parent, false);
        binding.setPendingCallback(pendingClickCallback);
        return new TodoPendingAdapter.PendingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TodoPendingAdapter.PendingViewHolder holder, int position) {
        holder.binding.setPendingItem(pendingList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return pendingList == null ? 0 : pendingList.size();
    }

    static class PendingViewHolder extends RecyclerView.ViewHolder {
        TodoPendingItemBinding binding;

        PendingViewHolder(TodoPendingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
