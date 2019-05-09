package google.architecture.login.bean;

public class HeaderList {
    /**
     * email : 1231@123.ccc
     * token : 67f05826-6f2a-42dd-9b7d-0dfceba77038
     * name : 超级管理员
     * dept :
     * resourceid : 4028812444b551e50144b5520bf30024
     * mobile : 13430251012
     * loginName : admin
     */

    private String email;
    private String token;
    private String name;
    private String dept;
    private String resourceid;
    private String mobile;
    private String loginName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getResourceid() {
        return resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
