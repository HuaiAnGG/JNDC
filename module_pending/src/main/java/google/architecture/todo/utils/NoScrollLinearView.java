package google.architecture.todo.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * @description: 禁止上下滑动View
 * @author: HuaiAngg
 * @create: 2019-05-05 2:26
 */
public class NoScrollLinearView extends LinearLayoutManager {

    public NoScrollLinearView(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}
