package google.architecture.pending.console.video.videoplayer;

import android.text.TextUtils;

import com.just.iscs.client.app.Constants;
import com.warren.basis.util.Cache;

/**
 * <b>
 * VideoData
 * </b>
 * <pre>
 * 作者: mowanglin@just-tech.com.cn
 * 版权: Copyright (c) 2018
 * 公司: 广州佳时达软件股份有限公司
 * 日期: 2018-04-03
 * </pre>
 */
public class VideoData {

	private String inIp, outIp, user, pwd;
	private int inPort, outPort;
	private String chid;							// varchar(16)	通道编号
	private String ch_name;							// varchar(64)	通道名称

	private IPType ipUsed;							// 使用内网/外网地址播放
	private StreamType stream;						// 码流
	private boolean isPause;						// true:已暂停

	public VideoData() {

		ipUsed = "out".equals(Cache.get().getString(Constants.DEFAULT_IP_SELECT)) ? IPType.OUTER : IPType.INTRANET;
		stream = StreamType.valueOf(Cache.get().getString(Constants.DEFAULT_STREAM_SELECT, StreamType.MAIN.toString()));
	}

	/**
	 * @param inIp 内网IP
	 * @param outIp 外网IP
	 * @param user 认证用户名
	 * @param pwd 认证密码
	 */
	public VideoData(String inIp, int inPort, String outIp, int outPort, String user, String pwd, String chid,
					 String ch_name){

		this.inIp = inIp;
		this.inPort = inPort;
		this.outIp = outIp;
		this.outPort = outPort;
		this.user = user;
		this.pwd = pwd;
		this.chid = chid;
		this.ch_name = ch_name;

		ipUsed = "out".equals(Cache.get().getString(Constants.DEFAULT_IP_SELECT)) ? IPType.OUTER : IPType.INTRANET;
		stream = StreamType.valueOf(Cache.get().getString(Constants.DEFAULT_STREAM_SELECT, StreamType.MAIN.toString()));
	}

	public String getInIp() {
		return inIp;
	}

	public void setInIp(String inIp) {
		this.inIp = inIp;
	}

	public String getOutIp() {
		return outIp;
	}

	public void setOutIp(String outIp) {
		this.outIp = outIp;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getInPort() {
		return inPort;
	}

	public void setInPort(int inPort) {
		this.inPort = inPort;
	}

	public int getOutPort() {
		return outPort;
	}

	public void setOutPort(int outPort) {
		this.outPort = outPort;
	}

	public boolean isInAddrEnable() {
		return !TextUtils.isEmpty(inIp) && inPort > 0;
	}

	public boolean isOutAddrEnable() {
		return !TextUtils.isEmpty(outIp) && outPort > 0;
	}

	public IPType getIpUsed() {
		return ipUsed;
	}

	public void setIpUsed(IPType ipUsed) {
		this.ipUsed = ipUsed;
	}

	public String getChid() {
		return chid;
	}

	public void setChid(String chid) {
		this.chid = chid;
	}

	public String getCh_name() {
		return ch_name;
	}

	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}

	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean pause) {
		isPause = pause;
	}

	public StreamType getStream() {
		return stream;
	}

	public void setStream(StreamType stream) {
		this.stream = stream;
	}

	public enum IPType {

		UNSPECIFIC,			// 未指定

		INTRANET,			// 内网

		OUTER,				// 外网
	}

	public enum StreamType {

		MAIN,				// 主码流

		SUB,				// 次码流

		THIRD;				// 三码流

		public String getName(){
			switch (ordinal()) {
				case 0 :
					return "主码流";
				case 1 :
					return "次码流";
				case 2 :
					return "三码流";
				default:
					return "主码流";
			}
		}

		public String getCode(){
			switch (ordinal()) {
				case 0 :
					return "main";
				case 1 :
					return "sub";
				case 2 :
					return "";
				default:
					return "main";
			}
		}

		public int toInt(){
			return ordinal();
		}

		public static StreamType valueOf(int value){
			switch (value) {
				case 0 :
					return MAIN;
				case 1 :
					return SUB;
				case 2 :
					return THIRD;
				default:
					return MAIN;
			}
		}
	}
}
