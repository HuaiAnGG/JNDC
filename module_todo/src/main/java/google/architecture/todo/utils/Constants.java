package google.architecture.todo.utils;

import java.util.List;

import google.architecture.todo.R;

/**
 * @description: Constants
 * @author: HuaiAngg
 * @create: 2019-05-02 21:21
 */
public class Constants {

    /**
     * 控制台列表名称
     */
    public static final String[] TODO_CONSOLE_LIST_NAME = {
            "集中待办", "视频监控", "生产早会", "生产指标",
            "粤电负荷", "工单管理", "运行日志", "更多"
    };

    /**
     * 控制台列表图表
     */
    public static final int[] TODO_CONSOLE_LIST_ICON = {
            R.drawable.workpending_pending_ic, R.drawable.workpending_video_ic,
            R.drawable.workpending_production_meeting_ic, R.drawable.workpending_production_index_ic,
            R.drawable.workpending_electric_load_ic, R.drawable.workpending_order_management_ic,
            R.drawable.workpending_operation_log_ic, R.drawable.workpending_more_ic
    };
}
