package google.architecture.pending.console.video.videoplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.TextureView;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.NET_DVR_TIME;
import com.hikvision.netsdk.PlaybackCallBack;
import com.hikvision.netsdk.PlaybackControlCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.just.core.common.CoreConfig;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import top.zibin.luban.Luban;

/**
 * <b>
 * IvmsPlayer 海康播放器，直连摄像头/NVR，使用海康提供的SDK播放
 * </b>
 * <pre>
 * 作者: mowanglin@just-tech.com.cn
 * 版权: Copyright (c) 2018
 * 公司: 广州佳时达软件股份有限公司
 * 日期: 2018-04-03
 * </pre>
 */
public class IvmsPlayer extends VideoPlayer {

	public static final String TAG = "--IvmsPlayer";

	protected int port = -1;                      // play port
	protected int logID = -1;                     // return by NET_DVR_Login_v30
	protected int playID = -1;                    // return by NET_DVR_RealPlay_V30
	protected int playbackID = -1;                // return by NET_DVR_PlayBackByTime
	protected int downloadID = -1;                //

	private boolean isPlayingBack = false;

	private Thread playBackThread;

	public IvmsPlayer(TextureView textureView) {
		super(textureView);
	}

	public IvmsPlayer(TextureView textureView, boolean setOnClick) {
		super(textureView, setOnClick);
	}

	@Override
	public void init() {

		// init net sdk
		if (!HCNetSDK.getInstance().NET_DVR_Init()) {
			Log.e("--IvmsPlayer", "HCNetSDK init failed!");

			if (!isPlaySucceed && callback != null) {
				callback.onFail("初始化失败");
			}
		}
	}

	@Override
	public void loginInThread(Object...obj) {
		if (videoData.getIpUsed() == VideoData.IPType.OUTER) {
			if (!videoData.isOutAddrEnable()) {
				if (!isPlaySucceed) {
					postLoginFail("外网地址不可用");
				}
				return;
			}

			if (statusPlay == 2) {
				stopPlay();
				logout();
			}
			isPlaySucceed = false;
			login(videoData.getOutIp(), videoData.getOutPort(), videoData.getUser(), videoData.getPwd());
		}
		else {

			if (!videoData.isInAddrEnable()) {
				if (!isPlaySucceed) {
					postLoginFail("内网地址不可用");
				}
				return;
			}

			if (statusPlay == 2) {
				stopPlay();
				logout();
			}
			isPlaySucceed = false;
			login(videoData.getInIp(), videoData.getInPort(), videoData.getUser(), videoData.getPwd());
		}
	}

	private void login(String ip, int port, String user, String psw) {

		Log.e("--IvmsPlayer", String.format(Locale.CHINA, "login: %s %d %s %s", ip, port, user, psw));

		// init net sdk
		if (!HCNetSDK.getInstance().NET_DVR_Init()) {
			Log.e("--IvmsPlayer", "HCNetSDK init failed!");

			postLoginFail("初始化失败");
			return;
		}

		try {
			// login on the device

			NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
			// call NET_DVR_Login_v30 to login on, port 8000 as default
			logID = HCNetSDK.getInstance().NET_DVR_Login_V30(ip, port, user, psw, m_oNetDvrDeviceInfoV30);
			if (logID < 0) {
				int lastError = HCNetSDK.getInstance().NET_DVR_GetLastError();
				Log.e(TAG, "NET_DVR_Login is failed! Err:" + lastError);

				if (lastError == 1) {
					postLoginFail("用户名或密码错误");
				}
				else if (lastError == 7) {
					postLoginFail("连接设备失败");
				}
				else {
					postLoginFail(String.valueOf(HCNetSDK.getInstance().NET_DVR_GetLastError()));
				}

				return;
			}
//			if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
//				startChan = m_oNetDvrDeviceInfoV30.byStartChan;
//				chanNum = m_oNetDvrDeviceInfoV30.byChanNum;
//			}
//			else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
//				startChan = m_oNetDvrDeviceInfoV30.byStartDChan;
//				chanNum = m_oNetDvrDeviceInfoV30.byIPChanNum + m_oNetDvrDeviceInfoV30.byHighDChanNum * 256;
//			}
			Log.e(TAG, "NET_DVR_Login is Successful!");

			// get instance of exception callback and set
			ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
				public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
					Log.e("--IvmsPlayer", "-------------- recv exception, type:" + iType);
				}
			};

			if (HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oExceptionCbf)) {
				postLoginSuccess();

				if (getStatusPlay() != 3) {
					play(videoData.getStream());	// 登录成功后自动开始播放
				}
			}
			else {
				Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");

				if (!isPlaySucceed) {
					postLoginFail(String.valueOf(HCNetSDK.getInstance().NET_DVR_GetLastError()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
	}

	/**
	 * 登出
	 */
	@Override
	public void logout() {
		super.logout();

		if (logID == -1) {
			return;
		}

		logID = -1;

		HCNetSDK.getInstance().NET_DVR_Logout_V30(logID);
	}

	/**
	 * 按码流播放 / 切换码流
	 */
	@Override
	public void play(VideoData.StreamType streamType) {
		super.play(streamType);

		Log.e("--IvmsPlayer", "play() statusPlay=" + statusPlay);

		startSinglePreview(streamType);
	}

	/**
	 * 开始单窗口预览
	 */
	private void startSinglePreview(VideoData.StreamType streamType) {
		Log.e("--IvmsPlayer", "startSinglePreview()");

		if (logID == -1) {	// 未登录
			login();
			return;
		}

		int ch_id = getCheckChId();
		if (ch_id < 0) {
			return;
		}

		if (playbackID >= 0) {
			Log.e(TAG, "Please stop palyback first");

			if (!isPlaySucceed && callback != null) {
				callback.onFail("请先停止回看");
			}
			return;
		}
		RealPlayCallBack fRealDataCallBack = new RealPlayCallBack() {
			public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
				processRealData(iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
			}
		};
		Log.e(TAG, "startChan:" + videoData.getChid());

		NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
		previewInfo.lChannel = ch_id;
		previewInfo.dwStreamType = streamType.toInt();
		previewInfo.bBlocked = 1;
//         NET_DVR_CLIENTINFO struClienInfo = new NET_DVR_CLIENTINFO();
//         struClienInfo.lChannel = startChan;
//         struClienInfo.lLinkMode = 0;
		// HCNetSDK start preview
		playID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(logID, previewInfo, fRealDataCallBack);
//         playID = HCNetSDK.getInstance().NET_DVR_RealPlay_V30(logID,
//         struClienInfo, fRealDataCallBack, false);
		if (playID < 0) {
			Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());

			if (!isPlaySucceed && callback != null) {
				postFail("播放失败: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
			}
			return;
		}

		isPlaySucceed = true;
		postPlaySuccess();
	}

	/**
	 * 回看
	 * @param calendar 时间
	 */
	@Override
	public void playBack(Calendar calendar) {

		stopPlay();

		int ch_id = getCheckChId();
		if (ch_id < 0) {

			if (callback != null) {
				callback.onPlayBack(-1, null);
			}
			return;
		}

		NET_DVR_TIME struBegin = new NET_DVR_TIME();
		NET_DVR_TIME struEnd = new NET_DVR_TIME();

		struBegin.dwYear = calendar.get(Calendar.YEAR);
		struBegin.dwMonth = calendar.get(Calendar.MONTH) + 1;
		struBegin.dwDay = calendar.get(Calendar.DAY_OF_MONTH);
		struBegin.dwHour = calendar.get(Calendar.HOUR_OF_DAY);
		struBegin.dwMinute = calendar.get(Calendar.MINUTE);
		struBegin.dwSecond = calendar.get(Calendar.SECOND);

		calendar = (Calendar) calendar.clone();

		calendar.add(Calendar.HOUR_OF_DAY, 1);
		if (calendar.getTime().getTime() > System.currentTimeMillis()) {
			calendar.setTime(new Date());
		}

		struEnd.dwYear = calendar.get(Calendar.YEAR);
		struEnd.dwMonth = calendar.get(Calendar.MONTH) + 1;
		struEnd.dwDay = calendar.get(Calendar.DAY_OF_MONTH);
		struEnd.dwHour = calendar.get(Calendar.HOUR_OF_DAY);
		struEnd.dwMinute = calendar.get(Calendar.MINUTE);
		struEnd.dwSecond = calendar.get(Calendar.SECOND);

		playbackID = HCNetSDK.getInstance().NET_DVR_PlayBackByTime(logID, ch_id, struBegin, struEnd);
		Log.e("--IvmsPlayer", "playBack playbackID=" + playbackID);

		if (playbackID >= 0) {

			PlaybackCallBack cbf = new PlaybackCallBack() {
				@Override
				public void fPlayDataCallBack(int iPlaybackHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
					// player channel 1
					processRealData(iDataType, pDataBuffer, iDataSize, Player.STREAM_FILE);
				}
			};

			if (!HCNetSDK.getInstance().NET_DVR_SetPlayDataCallBack(playbackID, cbf)) {
				Log.e(TAG, "Set playback callback failed!");
				return;
			}
			if (!HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(
					playbackID,
					PlaybackControlCommand.NET_DVR_PLAYSTART,
					null, 0, null)) {
				Log.e(TAG, "net sdk playback start failed!");
				return;
			}

			setStatusPlay(2);

			if (playBackThread != null) {
				playBackThread.interrupt();
			}

			isPlayingBack = true;

			playBackThread = new Thread() {
				public void run() {
					int progress;
					boolean callbackStart;
					try {
						callbackStart = false;

						while (isPlayingBack) {
							progress = HCNetSDK.getInstance().NET_DVR_GetPlayBackPos(playbackID);
							Log.e("--IvmsPlayer", "NET_DVR_GetPlayBackPos:" + progress);

							if (progress > 100 && callback != null) {

								CoreConfig.task().post(new Runnable() {
									@Override
									public void run() {
										callback.onPlayBack(-1, "回看出错: "
												+ HCNetSDK.getInstance().NET_DVR_GetLastError());
									}
								});
							}

							if (progress >= 0 && progress < 100) {
								if (!callbackStart && callback != null) {
									callbackStart = true;

									CoreConfig.task().post(new Runnable() {
										@Override
										public void run() {
											callback.onPlayBack(0, null);
										}
									});
								}
							}
							else {
								break;
							}

							Thread.sleep(1000);
						}
					} catch (InterruptedException ignored) {
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (callback != null) {

						CoreConfig.task().post(new Runnable() {
							@Override
							public void run() {
								callback.onPlayBack(1, null);
							}
						});
					}
				}

			};
			playBackThread.start();
		}
		else if (callback != null) {
			setStatusPlay(0);

			callback.onPlayBack(-1, "回看出错: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
			callback.onPlayBack(1, null);
		}
	}

	/**
	 * 下载
	 * @param tag 0:下载视频 1:抓取视频报警
	 */
	@Override
	public void download(Calendar calendarStart, Calendar calendarEnd, final String filePath, final int tag) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

		int ch_id = getCheckChId();
		Log.e("--IvmsPlayer", "Download: ch_id=" + ch_id + " " + format.format(calendarStart.getTime()) + "至"
				+ format.format(calendarEnd.getTime()) + " " + filePath);

		if (ch_id < 0) {
			return;
		}

		NET_DVR_TIME struBegin = new NET_DVR_TIME();
		NET_DVR_TIME struEnd = new NET_DVR_TIME();

		struBegin.dwYear = calendarStart.get(Calendar.YEAR);
		struBegin.dwMonth = calendarStart.get(Calendar.MONTH) + 1;
		struBegin.dwDay = calendarStart.get(Calendar.DAY_OF_MONTH);
		struBegin.dwHour = calendarStart.get(Calendar.HOUR_OF_DAY);
		struBegin.dwMinute = calendarStart.get(Calendar.MINUTE);
		struBegin.dwSecond = calendarStart.get(Calendar.SECOND);

		struEnd.dwYear = calendarEnd.get(Calendar.YEAR);
		struEnd.dwMonth = calendarEnd.get(Calendar.MONTH) + 1;
		struEnd.dwDay = calendarEnd.get(Calendar.DAY_OF_MONTH);
		struEnd.dwHour = calendarEnd.get(Calendar.HOUR_OF_DAY);
		struEnd.dwMinute = calendarEnd.get(Calendar.MINUTE);
		struEnd.dwSecond = calendarEnd.get(Calendar.SECOND);

		downloadID = HCNetSDK.getInstance().NET_DVR_GetFileByTime(logID, ch_id, struBegin, struEnd, filePath);
		if (downloadID < 0) {
			if (callback != null) {
				callback.onDownProgress(-1, tag);
				callback.onFail("下载出错: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
			}
			return;
		}

		boolean b = HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(downloadID, HCNetSDK.NET_DVR_PLAYSTART,
				null, 0, null);

		if (callback == null) {
			return;
		}

		if (!b) {
			callback.onDownProgress(-1, tag);
			return;
		}

		callback.onDownProgress(0, tag);

		new Thread(){
			@Override
			public void run() {

				while (true) {
					final int downloadPos = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(downloadID);
					Log.e("--IvmsPlayer", "downloadPos=" + downloadPos);

					CoreConfig.task().post(new Runnable() {
						@Override
						public void run() {

							callback.onDownProgress(downloadPos, tag);

							if (downloadPos == 100) {
								callback.onDownComp(filePath, tag);
							}
						}
					});

					if (downloadPos < 0 || downloadPos >= 100) {
						break;
					}

					try{
						Thread.sleep(1000);
					}
					catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 取消下载
	 */
	@Override
	public void cancelDownload(){
		if (downloadID > -1){
			HCNetSDK.getInstance().NET_DVR_StopGetFile(downloadID);
		}

		downloadID = -1;
	}

	private void processRealData(int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {

		if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
			if (port >= 0) {
				return;
			}
			port = Player.getInstance().getPort();
			if (port == -1) {
				Log.e(TAG, "getPort is failed with: " + Player.getInstance().getLastError(port));
				return;
			}
			Log.e(TAG, "getPort succ with: " + port);
			if (iDataSize > 0) {
				if (!Player.getInstance().setStreamOpenMode(port, iStreamMode)) { // set stream mode
					Log.e(TAG, "setStreamOpenMode failed");
					return;
				}
				if (!Player.getInstance().openStream(port, pDataBuffer, iDataSize, 2 * 1024 * 1024)) { //open stream
					Log.e(TAG, "openStream failed");
					return;
				}
//				if (!Player.getInstance().play(port, textureView.getHolder())) {
//					Log.e(TAG, "play failed");
//					return;
//				}

				if (!Player.getInstance().playEx(port, textureView.getSurfaceTexture())) {
					Log.e(TAG, "play failed");
					return;
				}

				if (!Player.getInstance().playSound(port)) {
					Log.e(TAG, "playSound failed with error code:" + Player.getInstance().getLastError(port));
				}
			}
		}
		else {
			if (!Player.getInstance().inputData(port, pDataBuffer, iDataSize)) {
				// Log.e(TAG, "inputData failed with: " +
				// Player.getInstance().getLastError(port));
				for (int i = 0; i < 4000 && playbackID >= 0; i++) {
					if (Player.getInstance().inputData(port, pDataBuffer, iDataSize)) {
						break;
					}

					if (i % 100 == 0) {
						Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(port) + ", i:" + i);
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}

	}

	/**
	 * 停止播放
	 */
	@Override
	public void stopPlay() {
		super.stopPlay();

		Log.e("--IvmsPlayer", "stopSinglePreview: playID=" + playID + " playbackID=" + playbackID);

		if (playID >= 0) {

			Log.e("--IvmsPlayer", "stopPreview");

			// net sdk stop preview
			if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(playID)) {
				Log.e(TAG, "StopRealPlay failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());

				playID = -1;
				return;
			}

			playID = -1;
		}

		if (playbackID >= 0) {

			Log.e("--IvmsPlayer", "stopPlayback");

			isPlayingBack = false;

			if (playBackThread != null) {
				playBackThread.interrupt();
			}
			playBackThread = null;

			if (!HCNetSDK.getInstance().NET_DVR_StopPlayBack(playbackID)) {
				Log.e(TAG, "StopPlayBack failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());

				playbackID = -1;
				return;
			}

			playbackID = -1;
		}

		Player.getInstance().stopSound();

		// player stop play
		if (!Player.getInstance().stop(port)) {
			Log.e(TAG, "stop is failed!");
		}

		if (!Player.getInstance().closeStream(port)) {
			Log.e(TAG, "closeStream is failed!");
		}
		if (!Player.getInstance().freePort(port)) {
			Log.e(TAG, "freePort is failed!" + port);
		}
		port = -1;
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

		new Thread(){
			@Override
			public void run() {

				try {
					if (port < 0) {
						Log.e(TAG, "please start preview first");
						return;
					}
					Player.MPInteger stWidth = new Player.MPInteger();
					Player.MPInteger stHeight = new Player.MPInteger();
					if (!Player.getInstance().getPictureSize(port, stWidth, stHeight)) {
						Log.e(TAG, "getPictureSize failed with error code:" + Player.getInstance().getLastError(port));
						return;
					}
					int nSize = 5 * stWidth.value * stHeight.value;
					byte[] picBuf = new byte[nSize];
					Player.MPInteger stSize = new Player.MPInteger();
					if (!Player.getInstance().getBMP(port, picBuf, nSize, stSize)) {
						Log.e(TAG, "getBMP failed with error code:" + Player.getInstance().getLastError(port));
						return;
					}

					final File file = new File(path);
					Bitmap bitmap = BitmapFactory.decodeByteArray(picBuf, 0, nSize);
					FileOutputStream fos = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close();

					//压缩
					try{
						final File cprFile = Luban.with(textureView.getContext()).load(file).get().get(0);
						file.delete();

						textureView.post(new Runnable() {
							@Override
							public void run() {
								if (callback != null) {
									callback.onCaptureDone(cprFile, tag);
								}
							}
						});
					}
					catch (Exception e){
						e.printStackTrace();
					}


				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	/**
	 * 取消静音
	 */
	@Override
	public void unMute() {
		super.unMute();
		Player.getInstance().playSound(port);
	}

	/**
	 * 静音
	 */
	@Override
	public void mute() {
		super.mute();
		Player.getInstance().stopSound();
	}

	@Override
	public void destroy() {

		try {

			stopPlay();
			logout();
			HCNetSDK.getInstance().NET_DVR_Cleanup();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void autoLoginIfNeed(){
		if (logID == -1 && videoData != null) {
			if (videoData.getIpUsed() == VideoData.IPType.INTRANET) {
				login(videoData.getInIp(), videoData.getInPort(), videoData.getUser(), videoData.getPwd());
			}
			else {
				login(videoData.getOutIp(), videoData.getOutPort(), videoData.getUser(), videoData.getPwd());
			}
		}
	}
}
