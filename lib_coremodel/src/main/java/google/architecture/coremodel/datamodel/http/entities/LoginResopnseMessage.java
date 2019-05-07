package google.architecture.coremodel.datamodel.http.entities;

public class LoginResopnseMessage
{
    private Body body = new Body();

    private Header header = new Header();

    public void setBody(Body body){
        this.body = body;
    }
    public Body getBody(){
        return this.body;
    }
    public void setHeader(Header header){
        this.header = header;
    }
    public Header getHeader(){
        return this.header;
    }


    public class Header
    {
        private String seqid;

        private String status;

        private String func;

        private int datatype;

        private String info;

        public void setSeqid(String seqid){
            this.seqid = seqid;
        }
        public String getSeqid(){
            return this.seqid;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getStatus(){
            return this.status;
        }
        public void setFunc(String func){
            this.func = func;
        }
        public String getFunc(){
            return this.func;
        }
        public void setDatatype(int datatype){
            this.datatype = datatype;
        }
        public int getDatatype(){
            return this.datatype;
        }
        public void setInfo(String info){
            this.info = info;
        }
        public String getInfo(){
            return this.info;
        }
    }

    public class Body
    {
        private String email;

        private String token;

        private String name;

        private String dept;

        private String resourceid;

        private String mobile;

        private String loginName;

        public void setEmail(String email){
            this.email = email;
        }
        public String getEmail(){
            return this.email;
        }
        public void setToken(String token){
            this.token = token;
        }
        public String getToken(){
            return this.token;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setDept(String dept){
            this.dept = dept;
        }
        public String getDept(){
            return this.dept;
        }
        public void setResourceid(String resourceid){
            this.resourceid = resourceid;
        }
        public String getResourceid(){
            return this.resourceid;
        }
        public void setMobile(String mobile){
            this.mobile = mobile;
        }
        public String getMobile(){
            return this.mobile;
        }
        public void setLoginName(String loginName){
            this.loginName = loginName;
        }
        public String getLoginName(){
            return this.loginName;
        }
    }
}
