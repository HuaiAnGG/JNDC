package google.architecture.pending.notice;

import google.architecture.coremodel.datamodel.http.entities.TodoData;

/**
 * @description: Todo通知回调
 * @author: HuaiAngg
 * @create: 2019-05-04 18:49
 */
public interface TodoNoticeClickCallback {
    void onClick(TodoData.NoticeResultsBean newsItem);
}
