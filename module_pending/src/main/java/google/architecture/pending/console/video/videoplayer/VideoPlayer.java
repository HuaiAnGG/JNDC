package google.architecture.pending.console.video.videoplayer;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.TextureView;
import android.view.View;

import com.just.core.common.CoreConfig;
//import com.just.iscs.client.app.Constants;
//import com.warren.basis.util.Cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

/**
 * <b>
 * VideoPlayer
 * </b>
 * <pre>
 * 作者: mowanglin@just-tech.com.cn
 * 版权: Copyright (c) 2018
 * 公司: 广州佳时达软件股份有限公司
 * 日期: 2018-07-10
 * </pre>
 */
public abstract class VideoPlayer implements View.OnClickListener{

	protected static final String[] CHAN_ARRAY = {"主码流", "次码流", "三码流"};

	protected TextureView textureView;
	protected Callback callback;
	protected VideoData videoData;
	protected int statusPlay = 0;               // 0:未就绪 1:登录中 2:播放中 3:已暂停 -1:加载出错
	protected boolean isPlaySucceed = false;
	protected boolean enableSound = true;		// 静音 true:正常, false:已静音

	public VideoPlayer(TextureView textureView) {
		this(textureView, true);
	}

	public VideoPlayer(TextureView textureView, boolean setOnClick) {
		this.textureView = textureView;

		if (setOnClick) {
			textureView.setOnClickListener(this);
		}

		init();
	}

	public abstract void init();

	@CallSuper
	@Override
	public void onClick(View v) {
		if (v == textureView) {
			if (callback != null) {
				callback.onViewClick();
			}
		}
	}

	public final void login(final Object...obj){

		setStatusPlay(1);

		CoreConfig.task().run(new Runnable() {
			@Override
			public void run() {
				loginInThread(obj);
			}
		});
	}

	public abstract void loginInThread(Object...obj);

	public final void postLoginSuccess() {
		CoreConfig.task().post(new Runnable() {
			@Override
			public void run() {
				if (callback != null) {
					callback.onLoginSuccess();
				}
			}
		});
	}

	public final void postLoginFail(final String info) {
		CoreConfig.task().post(new Runnable() {
			@Override
			public void run() {
				if (callback != null) {
					callback.onLoginFail(info);
				}
			}
		});
	}

	public final void postFail(final String info) {
		CoreConfig.task().post(new Runnable() {
			@Override
			public void run() {
				if (callback != null) {
					callback.onFail(info);
				}
			}
		});
	}

	/**
	 * 登出
	 */
	@CallSuper
	public void logout() {
		isPlaySucceed = false;
		setStatusPlay(0);
	}

	/**
	 * 按码流播放 / 切换码流
	 */
	@CallSuper
	public void play(VideoData.StreamType streamType) {

		if (streamType == null) {
			Log.e("--VideoPlayer", "play: streamType=null!!");
			return;
		}
		videoData.setStream(streamType);

//		Cache.get().put(Constants.DEFAULT_STREAM_SELECT, streamType.toString());

		if (statusPlay == 2) {
			stopPlay();
		}

		setStatusPlay(2);
	}

	public final void postPlaySuccess() {
		CoreConfig.task().post(new Runnable() {
			@Override
			public void run() {
				if (callback != null) {
					callback.onPlaySucceed();
				}
			}
		});
	}

	/**
	 * 停止预览
	 */
	@CallSuper
	public void stopPlay() {
		isPlaySucceed = false;
		setStatusPlay(3);
	}

	/**
	 * 回看
	 * @param calendar 时间
	 */
	public abstract void playBack(Calendar calendar);

	/**
	 * 下载
	 * @param tag 0:下载视频 1:抓取视频报警
	 */
	public abstract void download(Calendar calendarStart, Calendar calendarEnd, String filePath, int tag);

	/**
	 * 取消下载
	 */
	public abstract void cancelDownload();

	/**
	 * 抓图
	 *
	 * @param path 保存的路径
	 */
	public abstract void capture(final String path, final int tag);

	/**
	 * 取消静音
	 */
	@CallSuper
	public void unMute() {
		enableSound = true;
	}

	/**
	 * 静音
	 */
	@CallSuper
	public void mute() {
		enableSound = false;
	}

	public boolean isEnableSound() {
		return enableSound;
	}

	public boolean isOutAddrEnable() {
		return (this instanceof IjkPlayer) || videoData.isOutAddrEnable();
	}

	public boolean isInAddrEnable() {
		return (this instanceof IjkPlayer) || videoData.isInAddrEnable();
	}

	public VideoData getVideoData() {
		return videoData;
	}

	public void setVideoData(VideoData videoData) {
		this.videoData = videoData;
	}

	/**
	 * 播放状态
	 */
	public int getStatusPlay() {
		return statusPlay;
	}

	protected void setStatusPlay(int statusPlay) {
		this.statusPlay = statusPlay;
	}

	/**
	 * 获取码流列表
	 */
	public final ArrayList<String> getChanList() {

//		for (int i = 0; i < videoData.getCh_count(); i++) {
//
//			if (i == CHAN_ARRAY.length) {
//				break;
//			}
//
//			list.add(CHAN_ARRAY[i]);
//		}

		// 固定是2个码流

		return new ArrayList<>(Arrays.asList(CHAN_ARRAY).subList(0, 2));
	}

	/**
	 * 当前播放的码流
	 */
	public final VideoData.StreamType getCurrentStreamType() {
		return (videoData == null || videoData.getStream() == null) ? VideoData.StreamType.MAIN : videoData.getStream();
	}

	protected final int getCheckChId(){

		int ch_id;
		try{
			ch_id = Integer.parseInt(videoData.getChid());
		}
		catch (Exception e){
			e.printStackTrace();

			if (callback != null) {
				callback.onFail("播放失败: chid格式不正确");
			}
			return -1;
		}

		return ch_id;
	}

	public void setTextureView(TextureView textureView) {
		this.textureView = textureView;
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public boolean isPlaySucceed() {
		return isPlaySucceed;
	}

	public abstract void destroy();

	public interface Callback {

		void onLoginSuccess();

		void onLoginFail(String info);

		void onViewClick();

		void onFail(String info);

		/**
		 * 播放成功
		 */
		void onPlaySucceed();

		/**
		 * 下载进度
		 * @param p 下载的进度 -1:失败，0~100:下载的进度，100:下载结束，200:网络异常, 按时间下载时进度只有 0 和 100。
		 * @param tag 0:下载视频 1:抓取视频报警
		 */
		void onDownProgress(int p, int tag);

		/**
		 * 下载完成
		 * @param path 保存的路径
		 * @param tag 0:下载视频 1:抓取视频报警
		 */
		void onDownComp(String path, int tag);

		/**
		 * 抓图
		 */
		void onCaptureDone(File file, int tag);

		/**
		 * 回看
		 * @param status 0:开始 1:停止 -1:出错
		 * @param info 出错信息
		 */
		void onPlayBack(int status, @Nullable String info);

		/**
		 * 正在恢复播放
		 */
		void onResumePlaying();

		/**
		 * 播放期间卡顿 缓冲中
		 */
		void onBufferStart();

		/**
		 * 播放期间卡顿后 缓冲完成,恢复播放
		 */
		void onBufferEnd();
	}
}
