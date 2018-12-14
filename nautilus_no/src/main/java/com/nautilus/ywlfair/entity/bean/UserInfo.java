package com.nautilus.ywlfair.entity.bean;

public class UserInfo extends BaseItem {
    private static final long serialVersionUID = -3230588507333809245L;

    private int userId;
    private String nickname;
    private String phone;
    private String bindPhone = "";
    private String email;
    private int userType;
    private int sex;
    private String birthday;
    private String signature;
    private String avatar;
    private String avatarPath;
    private int userFrom;
    private String regTime;
    private String lastLoginTime;
    private int thirdPartyFlag;
    private String city;
    private int applyVendorStatus;
    private String invitationVendorNickname;
    private String hasInputInvitationCode;

    private int hasPayPassword;

    public int getHasPayPassword() {
        return hasPayPassword;
    }

    public void setHasPayPassword(int hasPayPassword) {
        this.hasPayPassword = hasPayPassword;
    }

    public String getInvitationVendorNickname() {
        return invitationVendorNickname;
    }

    public void setInvitationVendorNickname(String invitationVendorNickname) {
        this.invitationVendorNickname = invitationVendorNickname;
    }

    public String getHasInputInvitationCode() {
        return hasInputInvitationCode;
    }

    public void setHasInputInvitationCode(String hasInputInvitationCode) {
        this.hasInputInvitationCode = hasInputInvitationCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;

    }

    public int getApplyVendorStatus() {
        return applyVendorStatus;
    }


    public void setApplyVendorStatus(int applyVendorStatus) {
        this.applyVendorStatus = applyVendorStatus;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getThirdPartyFlag() {
        return thirdPartyFlag;
    }

    public void setThirdPartyFlag(int thirdPartyFlag) {
        this.thirdPartyFlag = thirdPartyFlag;
    }

    public String getBindPhone() {
        return bindPhone;
    }

    public void setBindPhone(String bindPhone) {
        this.bindPhone = bindPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
