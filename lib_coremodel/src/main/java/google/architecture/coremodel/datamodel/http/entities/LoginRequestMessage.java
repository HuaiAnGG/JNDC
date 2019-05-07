package google.architecture.coremodel.datamodel.http.entities;

/**
 * 登陆请求信息
 */

public class LoginRequestMessage
{
    private Header header = new Header();

    private Body body = new Body();

    public void setHeader(Header header){
        this.header = header;
    }
    public Header getHeader(){
        return this.header;
    }
    public void setBody(Body body){
        this.body = body;
    }
    public Body getBody(){
        return this.body;
    }

    @Override
    public String toString() {
        return "LoginRequestMessage{" + header.toString() + body.toString() + '}';
    }

    public class Header
    {
        private String appcode;

        private String func;

        private String timestamp;

        private String seqid;

        private int datatype;

        public void setAppcode(String appcode){
            this.appcode = appcode;
        }
        public String getAppcode(){
            return this.appcode;
        }
        public void setFunc(String func){
            this.func = func;
        }
        public String getFunc(){
            return this.func;
        }
        public void setTimestamp(String timestamp){
            this.timestamp = timestamp;
        }
        public String getTimestamp(){
            return this.timestamp;
        }
        public void setSeqid(String seqid){
            this.seqid = seqid;
        }
        public String getSeqid(){
            return this.seqid;
        }
        public void setDatatype(int datatype){
            this.datatype = datatype;
        }
        public int getDatatype(){
            return this.datatype;
        }

        @Override
        public String toString() {
            return "Header{" +
                    "appcode='" + appcode + '\'' +
                    ", func='" + func + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", seqid='" + seqid + '\'' +
                    ", datatype=" + datatype +
                    '}';
        }
    }

    public class Body
    {
        private String userName;

        private String password;

        public void setUserName(String userName){
            this.userName = userName;
        }
        public String getUserName(){
            return this.userName;
        }
        public void setPassword(String password){
            this.password = password;
        }
        public String getPassword(){
            return this.password;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "userName='" + userName + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
