package google.architecture.todo.notice;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import google.architecture.coremodel.datamodel.http.entities.TodoData;
import google.architecture.todo.R;
import google.architecture.todo.databinding.TodoNoticeItemBinding;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-05 1:42
 */
public class TodoNoticeAdapter extends RecyclerView.Adapter<TodoNoticeAdapter.TodoNoticeViewHolder> {

    List<TodoData.NoticeResultsBean> noticeList;
    TodoNoticeClickCallback noticeClickCallback;

    public TodoNoticeAdapter(TodoNoticeClickCallback callback) {
        this.noticeClickCallback = callback;
    }

    public void setNoticeList(final List<TodoData.NoticeResultsBean> list) {
        if (noticeList == null) {
            noticeList = list;
            notifyItemRangeInserted(0, noticeList.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return noticeList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoData.NoticeResultsBean oldData = noticeList.get(oldItemPosition);
                    TodoData.NoticeResultsBean newData = list.get(newItemPosition);
                    return oldData.getTitle() == newData.getTitle();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoData.NoticeResultsBean oldData = noticeList.get(oldItemPosition);
                    TodoData.NoticeResultsBean newData = list.get(newItemPosition);
                    return oldData.getTitle() == newData.getTitle()
                            && oldData.getIcon() == newData.getIcon()
                            && oldData.getAuthor() == newData.getAuthor()
                            && oldData.getReadNum() == newData.getReadNum()
                            && oldData.getUpdateTime() == newData.getUpdateTime();
                }
            });
            noticeList = list;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public TodoNoticeAdapter.TodoNoticeViewHolder onCreateViewHolder(ViewGroup parent, int positon) {
        TodoNoticeItemBinding  binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.todo_notice_item,
                        parent, false);
        binding.setNoticeCallback(noticeClickCallback);
        return new TodoNoticeAdapter.TodoNoticeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TodoNoticeAdapter.TodoNoticeViewHolder holder, int position) {
        holder.binding.setNoticeItem(noticeList.get(position));
//        holder.binding.todoConsoleListIcon.setImageResource(Constants.TODO_CONSOLE_LIST_ICON[position]);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return noticeList == null ? null : noticeList.size();
    }

    static class TodoNoticeViewHolder extends RecyclerView.ViewHolder {
        TodoNoticeItemBinding binding;

        public TodoNoticeViewHolder(TodoNoticeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
