package com.example.tudou.mymusicss.model;

/**
 * 登录 实体类
 */
public class Login {

    private String LoginMark;//登录方式(Mobile、Email、QQ、WeChat、Alipay)
    private String Account;//登录账户(说明：根据登录方式取对应值)
    private String Password;//登录密码(说明：快捷注册为空)
    private String RoleType;//:角色(0-个人、10-加盟商家、11-特约商家、20-业务员、30-区代、40-市代)
    public Login(String loginMark, String account, String password) {
        LoginMark = loginMark;
        Account = account;
        Password = password;
    }

    public Login(String loginMark, String account, String password, String roleType) {
        LoginMark = loginMark;
        Account = account;
        Password = password;
//        RoleType = roleType;
    }

    public String getRoleType() {
        return RoleType;
    }

    public void setRoleType(String roleType) {
        RoleType = roleType;
    }

    public String getLoginMark() {
        return LoginMark;
    }

    public void setLoginMark(String loginMark) {
        LoginMark = loginMark;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
