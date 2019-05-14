package google.architecture.pending.console;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import google.architecture.coremodel.datamodel.http.entities.TodoData;
import google.architecture.pending.R;
import google.architecture.pending.databinding.TodoConsoleItemBinding;
import google.architecture.pending.utils.Constants;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-04 20:36
 */
public class TodoConsoleAdapter extends RecyclerView.Adapter<TodoConsoleAdapter.TodoConsoleViewHolder>
implements View.OnClickListener{

    private List<TodoData.ConsoleResultsBean> consoleList;
    private TodoConsoleClickCallback consoleItemClickCallback;

    public TodoConsoleAdapter(TodoConsoleClickCallback consoleItemClickCallback) {
        this.consoleItemClickCallback = consoleItemClickCallback;
    }

    public void setConsoleList(final List<TodoData.ConsoleResultsBean> list) {
        if (consoleList == null) {
            consoleList = list;
            notifyItemRangeInserted(0, consoleList.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return consoleList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoData.ConsoleResultsBean oldData = consoleList.get(oldItemPosition);
                    TodoData.ConsoleResultsBean newData = list.get(newItemPosition);
                    return oldData.getName().equals(newData.getName());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoData.ConsoleResultsBean oldData = consoleList.get(oldItemPosition);
                    TodoData.ConsoleResultsBean newData = list.get(newItemPosition);
                    return oldData.getName().equals(newData.getName())
                            && oldData.getIcon() == newData.getIcon();
                }
            });
            consoleList = list;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public TodoConsoleViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        TodoConsoleItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.todo_console_item,
                        parent, false);
        binding.setConsoleCallback(consoleItemClickCallback);
        return new TodoConsoleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TodoConsoleViewHolder holder, int position) {
        holder.binding.setConsoleItem(consoleList.get(position));
        holder.binding.todoConsoleListIcon.setImageResource(Constants.TODO_CONSOLE_LIST_ICON[position]);
        holder.binding.setPosition(position);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return consoleList == null ? 0 : consoleList.size();
    }

    @Override
    public void onClick(View view) {

    }

    static class TodoConsoleViewHolder extends RecyclerView.ViewHolder {
        TodoConsoleItemBinding binding;

        TodoConsoleViewHolder(TodoConsoleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
