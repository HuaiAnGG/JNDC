package google.architecture.coremodel.datamodel.http.entities;

import java.util.List;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 10:45
 */
public class VideoListData {
    /**
     * body : {"list":[{"authPassword":"1","authUsername":"admin","deviceLocation":"广州","deviceName":"设备1",
     * "flgDeleted":"N","inAddress":"/lead_portal_dev/portal/main.lead","mapPort":"","outAddress":"www.baidu.com",
     * "port":8000,"resourceId":"828080926a011908016a014a26be0000","subType":0},{"authPassword":"1",
     * "authUsername":"admin","deviceLocation":"黄浦区","deviceName":"门户","flgDeleted":"N",
     * "inAddress":"http://localhost:8080/lead_portal_dev/portal/index.lead","outAddress":"","port":8000,
     * "resourceId":"828080926a011908016a014aeaf80001","subType":0}],"pageinfo":{"pagecount":1,"pageno":1,
     * "pagesize":10,"totalcount":2}}
     * header : {"datatype":0,"func":"","info":"操作成功","seqid":"","status":"200"}
     */

    private BodyBean body;
    private HeaderBean header;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public static class BodyBean {
        /**
         * list : [{"authPassword":"1","authUsername":"admin","deviceLocation":"广州","deviceName":"设备1",
         * "flgDeleted":"N","inAddress":"/lead_portal_dev/portal/main.lead","mapPort":"","outAddress":"www.baidu.com",
         * "port":8000,"resourceId":"828080926a011908016a014a26be0000","subType":0},{"authPassword":"1",
         * "authUsername":"admin","deviceLocation":"黄浦区","deviceName":"门户","flgDeleted":"N",
         * "inAddress":"http://localhost:8080/lead_portal_dev/portal/index.lead","outAddress":"","port":8000,
         * "resourceId":"828080926a011908016a014aeaf80001","subType":0}]
         * pageinfo : {"pagecount":1,"pageno":1,"pagesize":10,"totalcount":2}
         */

        private PageinfoBean pageinfo;
        private List<Equipment> list;

        public PageinfoBean getPageinfo() {
            return pageinfo;
        }

        public void setPageinfo(PageinfoBean pageinfo) {
            this.pageinfo = pageinfo;
        }

        public List<Equipment> getList() {
            return list;
        }

        public void setList(List<Equipment> list) {
            this.list = list;
        }

        public static class PageinfoBean {
            /**
             * pagecount : 1
             * pageno : 1
             * pagesize : 10
             * totalcount : 2
             */

            private int pagecount;
            private int pageno;
            private int pagesize;
            private int totalcount;

            public int getPagecount() {
                return pagecount;
            }

            public void setPagecount(int pagecount) {
                this.pagecount = pagecount;
            }

            public int getPageno() {
                return pageno;
            }

            public void setPageno(int pageno) {
                this.pageno = pageno;
            }

            public int getPagesize() {
                return pagesize;
            }

            public void setPagesize(int pagesize) {
                this.pagesize = pagesize;
            }

            public int getTotalcount() {
                return totalcount;
            }

            public void setTotalcount(int totalcount) {
                this.totalcount = totalcount;
            }
        }

        public static class Equipment {
            /**
             * authPassword : 1
             * authUsername : admin
             * deviceLocation : 广州
             * deviceName : 设备1
             * flgDeleted : N
             * inAddress : /lead_portal_dev/portal/main.lead
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
                return deviceLocation;
            }

            public void setDeviceLocation(String deviceLocation) {
                this.deviceLocation = deviceLocation;
            }

            public String getDeviceName() {
                return deviceName;
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
        }
    }

    public static class HeaderBean {
        /**
         * datatype : 0
         * func :
         * info : 操作成功
         * seqid :
         * status : 200
         */

        private int datatype;
        private String func;
        private String info;
        private String seqid;
        private String status;

        public int getDatatype() {
            return datatype;
        }

        public void setDatatype(int datatype) {
            this.datatype = datatype;
        }

        public String getFunc() {
            return func;
        }

        public void setFunc(String func) {
            this.func = func;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getSeqid() {
            return seqid;
        }

        public void setSeqid(String seqid) {
            this.seqid = seqid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
