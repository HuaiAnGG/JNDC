package google.architecture.pending.console;

import google.architecture.coremodel.datamodel.http.entities.TodoData;

/**
 * @description: Todo控制台回调
 * @author: HuaiAngg
 * @create: 2019-05-04 18:49
 */
public interface TodoConsoleClickCallback {
    void onClick(TodoData.ConsoleResultsBean newsItem, int position);
}
