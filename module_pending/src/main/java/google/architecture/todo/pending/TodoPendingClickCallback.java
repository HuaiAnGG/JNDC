package google.architecture.todo.pending;

import google.architecture.coremodel.datamodel.http.entities.TodoData;

/**
 * @description: Todo待办回调
 * @author: HuaiAngg
 * @create: 2019-05-04 18:49
 */
public interface TodoPendingClickCallback {
    void onClick(TodoData.PendingResultsBean newsItem);
}
