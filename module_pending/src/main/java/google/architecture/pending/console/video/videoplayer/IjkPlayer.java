package google.architecture.pending.console.video.videoplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.just.core.common.CoreConfig;
import com.just.core.http.HttpEngine;
import com.just.core.http.HttpTask;
import com.just.core.message.ResponseMessage;
//import com.just.iscs.client.app.Cmd;
//import com.just.iscs.client.app.Config;
//import com.just.iscs.client.util.IscsUtil;
//import com.warren.basis.Basis;
//import com.warren.basis.model.AbsResp;
//import com.warren.basis.model.ModelNet;
//import com.warren.basis.util.BitmapUtils;
//import com.warren.basis.util.Dimension;
//import com.warren.basis.util.Run;
//import com.warren.basis.util.TextUtil;

import org.json.ext.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import top.zibin.luban.Luban;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * <b>
 * IjkPlayer B站播放器，使用RTMP协议连接流媒体服务器播放
 * </b>
 * <pre>
 * 作者: mowanglin@just-tech.com.cn
 * 版权: Copyright (c) 2018
 * 公司: 广州佳时达软件股份有限公司
 * 日期: 2018-07-10
 * </pre>
 */
public class IjkPlayer extends VideoPlayer implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener,
		IMediaPlayer.OnErrorListener {

	private IMediaPlayer mediaPlayer = null;

	private String deviceId;		// 设备编号
	private String sessionId;		// 流媒体服务器上的sessionId
	private String playPath;		// RTMP播放地址
	private boolean running;

	private Thread keepAliveThread;
	private ModelNet modelKeepAlive;
	private Surface surface;
	private String serverSupport;	// 转发服务器播放地址 0:支持内网和外网 1:仅内网 2:仅外网

	public IjkPlayer(TextureView textureView) {
		super(textureView);
	}

	public IjkPlayer(TextureView textureView, boolean setOnClick) {
		super(textureView, setOnClick);
	}

	@Override
	public void init() {

		IjkMediaPlayer.loadLibrariesOnce(null);
		IjkMediaPlayer.native_profileBegin("libijkplayer.so");

		if (textureView != null && textureView.getSurfaceTexture() != null) {
			surface = new Surface(textureView.getSurfaceTexture());
		}

//		modelKeepAlive = new ModelNet(ModelNet.B011, Cmd.VIDEO_KEEP_ALIVE, new AbsResp() {
//			@Override
//			public void onSuccess(String str) {
//				super.onSuccess(str);
//			}
//
//			@Override
//			public void onFail(String status, String info) {
//				super.onFail(status, info);
//			}
//		});

		running = true;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * 维持播放状态的心跳包
	 */
	private void startKeepAlive(){

		if (keepAliveThread != null) {
			keepAliveThread.interrupt();
		}

		keepAliveThread = new Thread(){
			@Override
			public void run() {
				try{
					while (running) {

						modelKeepAlive.setParams("sessionid", sessionId);
						modelKeepAlive.doTask();

//						Thread.sleep(Config.PLAYER_KEEP_ALIVE_INTERVAL * 1000);
					}
				}
				catch (Exception ignored){
				}
			}
		};

		keepAliveThread.start();
	}

	@Override
	public void loginInThread(Object...obj) {

		setStatusPlay(2);

//		HttpTask task = new HttpTask(ModelNet.B000, Cmd.GLOBAL_BASECFG);
//		task.setItemParaListWithKeys("own_type", "android", "update_time", "");
//		task.setShowWaitingDlg(false);
//
//		HttpEngine.getInstance().emit(task);
//		ResponseMessage responseMessage = task.getResponseMessage();
//		Log.e("--HttpUtil", "[HTTP RS]" + responseMessage.toJSON());
//
//		String status = responseMessage.header.getStatus();
//		if ("200".equals(status)) {
//			JSONObject body = responseMessage.body.getBody();
//			String str = body.optString("list");
//			Log.e("==============", str );
//			if (TextUtils.isEmpty(str)) {
//				str = body.toString();
//			}
//
//			HashMap<String, String> map = IscsUtil.parseConfig(str);
//			if (map == null) {
//				postLoginFail("获取配置参数出错");
//				return;
//			}
//
//			serverSupport = map.get("media_server_net_support");
//			if (TextUtils.isEmpty(serverSupport)) {
//				postLoginFail("获取配置参数出错");
//				return;
//			}
//		}
//		else {
//
//			postLoginFail("获取配置参数出错");
//			return;
//		}
//
//		task = new HttpTask(ModelNet.B011, Cmd.VIDEO_DEVICE_URL);
//		task.setShowWaitingDlg(false);
//		task.setItemParaListWithKeys(
//				"deviceid", deviceId,
//				"protocol", "RTMP",
//				"code", videoData.getStream().getCode(),
//				"source_type", videoData.getIpUsed() == VideoData.IPType.OUTER ? "public" : "private");
//
//		HttpEngine.getInstance().emit(task);
//		responseMessage = task.getResponseMessage();
//		Log.e("--HttpUtil", "[HTTP RS]" + responseMessage.toJSON());
//
//		status = responseMessage.header.getStatus();
//		if ("200".equals(status)) {
//
//			try{
//				JSONObject json = responseMessage.body.getBody();
//				playPath = json.getString("play_url");
//				sessionId = json.getString("sessionid");
//
//				Log.e("--IjkPlayer", "playPath=" + playPath + " sessionId=" + sessionId);
//
//				postLoginSuccess();
//
//				if (getStatusPlay() != 3) {
//
//					CoreConfig.task().post(new Runnable() {
//						@Override
//						public void run() {
//							startMediaPlay();
//						}
//					});
//				}
//			}
//			catch (Exception e){
//				e.printStackTrace();
//
//				postLoginFail("处理出错");
//			}
//		}
//		else if ("309".equals(status)){
//			setStatusPlay(-1);
//			sessionId = null;
//
//			postLoginFail("未获取到视频信息");
//		}
//		else {
//			String info = responseMessage.header.getInfo();
//
//			setStatusPlay(-1);
//			sessionId = null;
//
//			postLoginFail(info);
//		}
	}

	@Override
	public void logout() {
		super.logout();

		if (keepAliveThread != null) {
			keepAliveThread.interrupt();

			keepAliveThread = null;
		}
	}

	/**
	 * 按码流播放 / 切换码流
	 */
	@Override
	public void play(VideoData.StreamType streamType) {
		logout();

		super.play(streamType);

		login();
	}

	/**
	 * 开始播放
	 */
	private void startMediaPlay(){

		releaseMediaPlayer();

		IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
		IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

		ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 10);	// 解码解不过来时丢掉10帧
		//ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);	// 断开时自动重连
		//ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);// 开启硬解码
		//ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 100 * 1024); //缓冲区为100KB

		mediaPlayer = ijkMediaPlayer;

		try {
			mediaPlayer.setDataSource(playPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (surface == null) {

			if (textureView == null || textureView.getSurfaceTexture() == null) {
				if(callback != null) {
					callback.onFail("播放失败");
				}
				return;
			}

			surface = new Surface(textureView.getSurfaceTexture());
		}

		mediaPlayer.setSurface(surface); // 给mediaPlayer设置视图
		mediaPlayer.prepareAsync();
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnInfoListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	@Override
	public void stopPlay() {
		super.stopPlay();

		Log.e("--IjkPlayer", "stopPlay()");

		releaseMediaPlayer();
		drawBlack();

		releaseSurface();
	}

	@Override
	public boolean onError(IMediaPlayer mp, int what, int extra) {
		Log.e("--IjkPlayer", "onError: what=" + what + " extra=" + extra);
		setStatusPlay(-1);

		if (callback != null) {
			callback.onFail("播放出错: " + what);
		}
		return false;
	}

	@Override
	public boolean onInfo(IMediaPlayer mp, int what, int extra) {
		Log.w("--IjkPlayer", "onInfo: what=" + what + " extra=" + extra);

		if (callback == null) {
			return false;
		}

		if (what == IMediaPlayer.MEDIA_INFO_VIDEO_DECODED_START) {
			isPlaySucceed = true;
			startKeepAlive();
			callback.onPlaySucceed();
		}

		if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
			callback.onBufferStart();
		}

		if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
			callback.onBufferEnd();
		}


		return false;
	}
	@Override
	public void onPrepared(IMediaPlayer mp) {
		mp.start();	// 自动开始播放
	}

	/**
	 * 回看
	 *
	 * @param calendar 时间
	 */
	@Override
	public void playBack(Calendar calendar) {
		if (callback != null) {
			callback.onFail("暂不支持");
		}
	}

	/**
	 * 下载
	 * @param tag 0:下载视频 1:抓取视频报警
	 */
	@Override
	public void download(Calendar calendarStart, Calendar calendarEnd, String filePath, int tag) {
		if (callback != null) {
			callback.onFail("暂不支持");
		}
	}

	/**
	 * 取消下载
	 */
	@Override
	public void cancelDownload() {
		if (callback != null) {
			callback.onFail("暂不支持");
		}
	}

	/**
	 * 抓图
	 *
	 * @param path 保存的路径
	 */
	@Override
	public void capture(final String path, final int tag) {

		if (!isPlaySucceed) {
			return;
		}

		final Bitmap bitmap = textureView.getBitmap();
//		Basis.run(new Run() {
//			@Override
//			public void running() throws Exception {
//
//				File file = new File(path);
//				BitmapUtils.saveBitmap(bitmap, file);
//				File cprFile = Luban.with(textureView.getContext()).load(file).get().get(0);
//
//				post(cprFile);
//			}
//
//			@Override
//			public void runAfterPost(Object... obj) {
//				if (callback != null) {
//					callback.onCaptureDone((File) obj[0], tag);
//				}
//			}
//		});
	}

	@Override
	public void unMute() {
		super.unMute();

		if (mediaPlayer != null) {
			mediaPlayer.setVolume(1f, 1f);
		}
	}

	@Override
	public void mute() {
		super.mute();

		if (mediaPlayer != null) {
			mediaPlayer.setVolume(0f, 0f);
		}
	}

	/**
	 * 画一帧黑色的帧 去除最后一帧
	 */
	public void drawBlack() {

//		try{
//			if (surface != null && surface.isValid()) {
//				int size = Math.max(Dimension.get().getScreenWidth(), Dimension.get().getScreenHeight());
//				Canvas canvas = surface.lockCanvas(new Rect(0, 0, size, size));
//				if (canvas != null) {
//					canvas.drawColor(0xff000000);
//					surface.unlockCanvasAndPost(canvas);
//				}
//			}
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
	}

	@Override
	public void destroy() {

		logout();

		stopPlay();
		IjkMediaPlayer.native_profileEnd();

		running = false;
	}

	private void releaseMediaPlayer() {
		if (mediaPlayer != null) {
			try{
				mediaPlayer.stop();
			}
			catch (Exception e){
				e.printStackTrace();
			}

			mediaPlayer.setDisplay(null);
			mediaPlayer.release();
		}
	}

	public void releaseSurface() {
		if (surface != null) {
			surface.release();
			surface = null;
		}
	}

	@Override
	public boolean isOutAddrEnable() {
		return TextUtils.isEmpty(serverSupport) || "0".equals(serverSupport) || "2".equals(serverSupport);
	}

	@Override
	public boolean isInAddrEnable() {
		return TextUtils.isEmpty(serverSupport) || "0".equals(serverSupport) || "1".equals(serverSupport);
	}

	public String getServerSupport() {
		return serverSupport;
	}
}