Android开发框架说明：

基础包	
	core_basic：
		主要提供框架基础支持：
			1、Activity主要有BaiscActivity、SwipeBackActivity、ToolbarActivity;
			2、Fragment支持及管理;
			3、AndroidUtil工具类。
		版本说明：
			V0.2在V0.1基础上增加Fragment支持：
			1、解决多个同级Fragment在内存不足时出现重叠现象的问题;
			2、增加单Activity多Fragment开发的支持;
			3、V0.2需要新增'me.yokeyword:fragmentation:0.7.7'依赖。   			
			V0.3
				1、兼容0.2
				2、状态栏沉浸式使用新的处理方式，并进行进一步封装；
				3、滑动返回使用新的库，处理了V0.2滑动返回的BUG
				4、需要加入compile 'com.r0adkll:slidableactivity:2.0.5'
			
		注意：
			如果不需要使用Fragment支持时，可以使用V0.1版本，但如无特殊要求（如：对包大小有要求的）建议都使用V0.2版本及以上。
		
	swipebacklayoutlib：
		主要提供滑动返回的支持。
		
	xutils：
		基于xutils3框架修改，修复内容：
			1、HTTP带文件上传时各个属性出现重排的问题。
			2、图片不能周边满屏显示
扩展包
	core_message：
		主要提供消息通讯的支持。
		更新说明：
			2016-08-15：
				HttpTask增加url参数，在HttpEngine中，默认以该参数作为目标地址，如果为空时，则切换为CoreConfig中对应的domainUrl;
	
	core_location：
		提供定位服务。
		
	core_push：
		基于mqttv3框架提供消息推送服务。
	
	core_socia：
		提供主要的社会化分享的服务，包括微信（微信、微信朋友圈）、QQ（QQ、QQ空间）、新浪微博等。
	
	multi-image-selector：
		提供图片选择支持。
		
	refreshlayout：
		提供下拉刷新支持：
		修改内容：
			1、修复RecyclerView一个条目刚好占满整个屏幕时不能显示加载更多问题。
	
	fragmentation：
		提供Fragment支持，解决原生Fragment重叠问题，实现Fragment懒加载等
			
第三方依赖包
	
	
		