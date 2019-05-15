package google.architecture.pending.console.video.videoplayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.just.iscs.client.bean.ImgCapture;
import com.just.iscs.client.util.IscsUtil;
import com.warren.basis.Basis;
import com.warren.basis.util.BitmapUtils;
import com.warren.basis.util.Dimension;
import com.warren.basis.util.Run;
import com.warren.basis.util.TextUtil;
import com.warren.basis.util.UuidUtil;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

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
public class IjkSimplePlayer extends VideoPlayer implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener,
		IMediaPlayer.OnErrorListener {

	private IMediaPlayer mediaPlayer = null;

	private String playPath;		// RTMP播放地址
	private boolean running;

	private Surface surface;
	private String serverSupport;	// 转发服务器播放地址 0:支持内网和外网 1:仅内网 2:仅外网
	private File cacheDir;

	public IjkSimplePlayer(TextureView textureView) {
		super(textureView);
	}

	public IjkSimplePlayer(TextureView textureView, boolean setOnClick) {
		super(textureView, setOnClick);
	}

	@Override
	public void init() {

		IjkMediaPlayer.loadLibrariesOnce(null);
		IjkMediaPlayer.native_profileBegin("libijkplayer.so");

		if (textureView != null && textureView.getSurfaceTexture() != null) {
			surface = new Surface(textureView.getSurfaceTexture());

			cacheDir = IscsUtil.getVideoCatchPhotoDir(textureView.getContext());
		}


		running = true;
	}

	@Override
	public void loginInThread(Object...obj) {

		setStatusPlay(2);
	}

	@Override
	public void logout() {
		super.logout();
	}

	/**
	 * 按码流播放 / 切换码流
	 */
	@Override
	public void play(VideoData.StreamType streamType) {
		super.play(streamType);

		Log.e("--IjkSimplePlayer", "play: " + playPath);

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

			if (textureView == null) {
				Log.e("--IjkSimplePlayer", "textureView null!");
			}
			else if (textureView.getSurfaceTexture() == null){
				Log.e("--IjkSimplePlayer", "surface null!");
			}

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

		Log.e("--IjkSimplePlayer", "stopPlay()");

		releaseMediaPlayer();
		drawBlack();

		releaseSurface();
	}

	@Override
	public boolean onError(IMediaPlayer mp, int what, int extra) {
		Log.e("--IjkSimplePlayer", "onError: what=" + what + " extra=" + extra);
		setStatusPlay(-1);

		if (callback != null) {
			callback.onFail("播放出错: " + what);
		}
		return false;
	}

	@Override
	public boolean onInfo(IMediaPlayer mp, int what, int extra) {
		Log.w("--IjkSimplePlayer", "onInfo: what=" + what + " extra=" + extra);

		if (callback == null) {
			return false;
		}

		if (what == IMediaPlayer.MEDIA_INFO_VIDEO_DECODED_START) {
			isPlaySucceed = true;
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
	 * @param tag -1: 添加到历史记录
	 */
	@Override
	public void capture(final String path, final int tag) {

		if (!isPlaySucceed) {
			return;
		}

		final Bitmap bitmap = textureView.getBitmap();
		Basis.run(new Run() {
			@Override
			public void running() throws Exception {

				File file = new File(path);
				BitmapUtils.saveBitmap(bitmap, file);
				File cprFile = Luban.with(textureView.getContext()).load(file).get().get(0);

				post(cprFile);
			}

			@Override
			public void runAfterPost(Object... obj) {
				if (callback != null) {
					callback.onCaptureDone((File) obj[0], tag);
				}
			}
		});
	}

	/**
	 * 抓图
	 */
	public void capture(final ImgCapture imgCapture) {

		final Bitmap bitmap = textureView.getBitmap();
		Basis.run(new Run() {
			@Override
			public void running() throws Exception {

				if (cacheDir == null) {
					cacheDir = IscsUtil.getVideoCatchPhotoDir(textureView.getContext());
				}

				File file = new File(cacheDir, UuidUtil.randomUUID() + ".jpg");
				BitmapUtils.saveBitmap(bitmap, file);
				File cprFile = Luban.with(textureView.getContext()).load(file).get().get(0);


				imgCapture.setStatus(1);
				imgCapture.setPath(cprFile.getAbsolutePath());
				post(cprFile);
			}

			@Override
			public void runAfterPost(Object... obj) {
				if (callback != null) {
					callback.onCaptureDone((File) obj[0], 1);
				}
			}
		});
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

		try{
			if (surface != null && surface.isValid()) {
				int size = Math.max(Dimension.get().getScreenWidth(), Dimension.get().getScreenHeight());
				Canvas canvas = surface.lockCanvas(new Rect(0, 0, size, size));
				if (canvas != null) {
					canvas.drawColor(0xff000000);
					surface.unlockCanvasAndPost(canvas);
				}
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setPlayPath(String playPath) {
		this.playPath = playPath;
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
