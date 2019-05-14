package google.architecture.pending.console.video;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import google.architecture.coremodel.datamodel.http.entities.VideoListData;
import google.architecture.pending.R;
import google.architecture.pending.databinding.VideoListItemBinding;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 10:15
 */
public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoViewHolder> {

    List<VideoListData.BodyBean.Equipment> equipmentList;
    VideoListClickCallback videoListClickCallback;

    public VideoListAdapter(VideoListClickCallback callback) {
        this.videoListClickCallback = callback;
    }

    public void setVideoList(final List<VideoListData.BodyBean.Equipment> list) {
        if (equipmentList == null) {
            equipmentList = list;
            notifyItemRangeInserted(0, equipmentList.size());
        } else {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return equipmentList.size();
                }

                @Override
                public int getNewListSize() {
                    return list.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    VideoListData.BodyBean.Equipment oldData = equipmentList.get(oldItemPosition);
                    VideoListData.BodyBean.Equipment newData = list.get(newItemPosition);
                    return oldData.getDeviceName() == newData.getDeviceName();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    VideoListData.BodyBean.Equipment oldData = equipmentList.get(oldItemPosition);
                    VideoListData.BodyBean.Equipment newData = list.get(newItemPosition);
                    return oldData.getDeviceName() == newData.getDeviceName()
                            && oldData.getDeviceLocation() == newData.getDeviceLocation()
                            && oldData.getFlgDeleted() == newData.getFlgDeleted()
                            && oldData.getResourceId() == newData.getResourceId()
                            && oldData.getSubType() == newData.getSubType()
                            && oldData.getMapPort() == newData.getMapPort();
                }
            });
            equipmentList = list;
            diffResult.dispatchUpdatesTo(this);
        }
    }

    @Override
    public VideoListAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        VideoListItemBinding itemBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.video_list_item,
                        parent, false);
        itemBinding.setVideoItemClickCallback(videoListClickCallback);
        return new VideoViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.binding.setEquipmentItem(equipmentList.get(position));
        holder.binding.setPosition(position);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return equipmentList == null ? 0 : equipmentList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        VideoListItemBinding binding;

        public VideoViewHolder(VideoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
