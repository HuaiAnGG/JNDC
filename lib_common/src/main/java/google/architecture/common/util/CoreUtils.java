package google.architecture.common.util;

import android.text.TextUtils;
import android.util.Log;

import com.just.core.http.HttpEngine;
import com.just.core.http.HttpTask;
import com.just.core.listener.MessageCallbackListener;
import com.just.core.message.ResponseMessage;

import org.json.ext.JSONObject;

public class CoreUtils {

    /*private void init(){

        try {
            HttpTask task = new HttpTask(appCode, cmd, new MessageCallbackListener() {
                @Override
                public void onWaiting(HttpTask httpTask) {
                }

                @Override
                public void onMessageResponse(HttpTask httpTask, ResponseMessage responseMessage) {
                    Log.e("--HttpUtil", "[HTTP RS]" + responseMessage.toJSON());

                    if (response == null) {
                        return;
                    }

                    String status = responseMessage.header.getStatus();

                    if ("309".equals(status)) {
                        response.onEmpty();
                        return;
                    }

                    if ("200".equals(status)) {
                        JSONObject body = responseMessage.body.getBody();
                        String str = body.optString("list");

                        if (TextUtils.isEmpty(str)) {
                            str = body.toString();
                        }
                        response.onSuccess(str);
                        return;
                    }

                    String info = responseMessage.header.getInfo();
                    if ("HTTP请求响应数据失败".equals(info)) {
                        info = "网络连接出错";
                    }
                    response.onFail(status, info);

                }

                @Override
                public void onFinished(HttpTask httpTask) {
                    if (response != null) {
                        response.onFinish();
                    }
                }

                @Override
                public void onUploading(HttpTask httpTask, long l, long l1) {

                }
            });

            if (params != null && params.length > 1){

                for (int i = 0; i < params.length; i+=2) {
                    if (params[i + 1] != null && params[i + 1].indexOf('\'') != -1) {
                        params[i + 1] = params[i].replaceAll("'", "\\\\'");
                    }
                }

                task.setItemParaListWithKeys(params);
            }
            task.setShowWaitingDlg(false);

        } catch (Exception e) {
            e.printStackTrace();

            if (response != null) {
                response.onFail("", "处理出错");
            }
        }

    }

    public void doTask() {

        if (task == null){
            init();
        }

        Log.e("--HttpUtil", "[HTTP RQ]" + task.getRequestMessage().toJSON());

        HttpEngine.getInstance().emit(task);
    }
*/
}
