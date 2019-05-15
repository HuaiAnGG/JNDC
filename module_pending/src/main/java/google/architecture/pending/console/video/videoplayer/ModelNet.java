package google.architecture.pending.console.video.videoplayer;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.just.core.command.ResponseModel;
import com.just.core.common.StatusCode;
import com.just.core.http.HttpEngine;
import com.just.core.http.HttpTask;
import com.just.core.listener.MessageCallbackListener;
import com.just.core.message.ResponseMessage;

import org.json.ext.JSONObject;

/**
 * Created by warren on 2018-01-30.
 * ModelNet
 */
public class ModelNet {

	/**
	 * 基础管理系统
	 */
	public static final String B000 = "B000";

	/**
	 * 报警系统
	 */
	public static final String B002 = "B002";

	/**
	 * 4A用户中心系统
	 */
	public static final String B04A = "B04A";

	/**
	 * 流媒体服务器
	 */
	public static final String B011 = "B011";

	/**
	 * 对讲系统
	 */
	public static final String B012 = "B012";

	/**
	 * 门禁系统
	 */
	public static final String B013 = "B013";

	/**
	 * 人员定位
	 */
	public static final String B015 = "B015";

	/**
	 * 智能眼镜
	 */
	public static final String B017 = "B017";

	protected String appCode;
	protected String cmd;
	protected String[] params;

//	@Nullable
//	protected AbsResp response;

	@Nullable
	protected HttpTask task;

	public ModelNet() {
	}

	public ModelNet(String appCode, String cmd, /*@Nullable AbsResp response, */String...params) {
		this.appCode = appCode;
		this.cmd = cmd;
//		this.response = response;
		this.params = params;
	}


	private void init(){

		try {
			task = new HttpTask(appCode, cmd, new MessageCallbackListener() {
				@Override
				public void onWaiting(HttpTask httpTask) {
				}

				@Override
				public void onMessageResponse(HttpTask httpTask, ResponseMessage responseMessage) {
					Log.e("--HttpUtil", "[HTTP RS]" + responseMessage.toJSON());

//					if (response == null) {
//						return;
//					}

					String status = responseMessage.header.getStatus();

					if ("309".equals(status)) {
//						response.onEmpty();
						return;
					}

					if ("200".equals(status)) {
						JSONObject body = responseMessage.body.getBody();
						String str = body.optString("list");

						if (TextUtils.isEmpty(str)) {
							str = body.toString();
						}
//						response.onSuccess(str);
						return;
					}

					String info = responseMessage.header.getInfo();
					if ("HTTP请求响应数据失败".equals(info)) {
						info = "网络连接出错";
					}
//					response.onFail(status, info);

				}

				@Override
				public void onFinished(HttpTask httpTask) {
//					if (response != null) {
//						response.onFinish();
//					}
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

//			if (response != null) {
//				response.onFail("", "处理出错");
//			}
		}

	}

	public void doTask() {

		if (task == null){
			init();
		}

		Log.e("--HttpUtil", "[HTTP RQ]" + task.getRequestMessage().toJSON());

		HttpEngine.getInstance().emit(task);
	}

	/**
	 * 重置网络请求，重新生成 HttpTask，命令字和参数变化时调用
	 */
	public void reset(){
		task = null;
	}

	/**
	 * 取消网络请求
	 */
	public void cancel(){
		if (task != null){
			task.cancel();
		}
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
		reset();
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String...params) {

		if (params != null && params.length > 1){

			for (int i = 0; i < params.length; i+=2) {
				if (params[i + 1] != null && params[i + 1].indexOf('\'') != -1) {
					params[i + 1] = params[i].replaceAll("'", "\\\\'");
				}
			}
		}

		if (task != null) {
			task.setItemParaListWithKeys(params);
		}

		this.params = params;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

//	@Nullable
//	public AbsResp getResponse() {
//		return response;
//	}
//
//	public void setResponse(@Nullable AbsResp response) {
//		this.response = response;
//		reset();
//	}
}
