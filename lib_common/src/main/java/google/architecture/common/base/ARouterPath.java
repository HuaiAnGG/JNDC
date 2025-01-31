package google.architecture.common.base;

/**
 * Created by danxx on 2017/11/27.
 * 路由path
 *
 * Aty : Activity
 * Fgt : Fragment
 * notice :  the path must be start with '/' and contain more than 2 '/'!
 */

public class ARouterPath {

    /**妹子列表Activity*/
    public static final String GirlsListAty = "/girls/aty/list";

    /**妹子列表动态Activity*/
    public static final String DynaGirlsListAty = "/girls/dynaty/list";

    /**新闻列表Activity*/
    public static final String NewsListAty = "/news/aty/list";

    /**妹子列表Fragment*/
    public static final String GirlsListFgt = "/girls/aty/fgt/list";

    /**新闻列表Fragment*/
    public static final String NewsListFgt = "/news/fgt/list";

    /**关于Fragment*/
    public static final String AboutFgt = "/about/fgt/fragment";

    /**关于登录界面Activity*/
    public static final String LoginAty = "/login/aty/list";

    /**主界面Activity*/
    public static final String MainAty = "/home/aty/list";

    /**工作待办Fragment*/
    public static final String PendingFgt = "/pending/fgt/list";

    /** 控制台 -- >> 视频监控 */
    public static final String CONSOLE_VIDEO_LIST_ATY = "/video/pending/act/list";

    /** 视频监控列表 -->> 详细页**/
    public static final String CONSOLE_VIDEO_DETAIL_ATY = "/detail/video/pending/act/list";
}
