package google.architecture.pending.console.video.videolist;

import google.architecture.coremodel.datamodel.http.entities.VideoListData;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 10:43
 */
public interface VideoListClickCallback {

    void onClick(VideoListData.BodyBean.Equipment newsItem, int position);
}
