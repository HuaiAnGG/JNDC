package google.architecture.common.util;

import com.showjoy.android.storage.SHStorageManager;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 18:00
 */
public class SPUtils {

    /**
     * 存储内存缓存
     * @param module 文件名
     * @param key 关键字
     * @param value 值
     */
    public static <T> void saveInCache(String module, String key, T value){
        SHStorageManager.putToCache(module, key, value);
    }


    /**
     * 存储到disk的数据，这里会先存到cache，再存储到disk
     * @param module 文件名
     * @param key 关键字
     * @param value 值
     */
    public static <T> void saveInDisk(String module, String key, T value){
        SHStorageManager.putToDisk(module, key, value);
    }

    /**
     * 读取int型数据
     * @param module
     * @param key
     * @param defValue
     * @return
     */
    public static int getIntInCache(String module, String key,int defValue) {
        return SHStorageManager.get(module, key, defValue);
    }

    /**
     * 读取String型数据
     * @param module
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringInCache(String module, String key,String defValue) {
        return SHStorageManager.get(module, key, defValue);
    }

    /**
     * 读取int型数据
     * @param module
     * @param key
     * @param defValue
     * @return
     */
    public static String getIntInCache(String module, String key,String defValue) {
        return SHStorageManager.get(module, key, defValue);
    }

    /**
     * 清除所有缓存
     */
    public static void clearAllCache() {
        //清除所有缓存
        SHStorageManager.clearCache();
    }

    public static void clearModuleCache(String module, String key) {
        //清除指定模块的缓存
        SHStorageManager.removeFromCache(module, key);
    }

}
