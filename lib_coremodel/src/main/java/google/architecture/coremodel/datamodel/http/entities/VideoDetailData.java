package google.architecture.coremodel.datamodel.http.entities;

import java.util.List;

/**
 * Function:
 * <p>
 * 作者: yanhuaian@just-tech.com.cn
 * 版权: Copyright (c) 2019
 * 公司: 广州佳时达软件股份有限公司
 * 日期: 2019-05-16
 */
public class VideoDetailData {

    private List<EquipmentBean> list;

    public List<EquipmentBean> getList() {
        return list;
    }

    public void setList(List<EquipmentBean> list) {
        this.list = list;
    }

    public static class EquipmentBean {
        /**
         * authPassword : 1
         * authUsername : admin
         * deviceLocation : 广州
         * deviceName : 设备1
         * flgDeleted : N
         * inAddress : /lead_portal_dev/portal/main.lead
         * factoryName : 测试厂区
         * mapPort :
         * outAddress : www.baidu.com
         * port : 8000
         * resourceId : 828080926a011908016a014a26be0000
         * subType : 0
         */

        private String authPassword;
        private String authUsername;
        private String deviceLocation;
        private String deviceName;
        private String flgDeleted;
        private String inAddress;
        private String mapPort;
        private String outAddress;
        private int port;
        private String resourceId;
        private int subType;
        private String factoryName;

        public String getAuthPassword() {
            return authPassword;
        }

        public void setAuthPassword(String authPassword) {
            this.authPassword = authPassword;
        }

        public String getAuthUsername() {
            return authUsername;
        }

        public void setAuthUsername(String authUsername) {
            this.authUsername = authUsername;
        }

        public String getDeviceLocation() {
            return "所属工区：" + deviceLocation;
        }

        public void setDeviceLocation(String deviceLocation) {
            this.deviceLocation = deviceLocation;
        }

        public String getDeviceName() {
            return "设备名称：" + deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getFlgDeleted() {
            return flgDeleted;
        }

        public void setFlgDeleted(String flgDeleted) {
            this.flgDeleted = flgDeleted;
        }

        public String getInAddress() {
            return inAddress;
        }

        public void setInAddress(String inAddress) {
            this.inAddress = inAddress;
        }

        public String getMapPort() {
            return mapPort;
        }

        public void setMapPort(String mapPort) {
            this.mapPort = mapPort;
        }

        public String getOutAddress() {
            return outAddress;
        }

        public void setOutAddress(String outAddress) {
            this.outAddress = outAddress;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public int getSubType() {
            return subType;
        }

        public void setSubType(int subType) {
            this.subType = subType;
        }

        public String getFactoryName() {
            return "所属站点：" + factoryName;
        }

        public void setFactoryName(String factoryName) {
            this.factoryName = factoryName;
        }
    }
}
