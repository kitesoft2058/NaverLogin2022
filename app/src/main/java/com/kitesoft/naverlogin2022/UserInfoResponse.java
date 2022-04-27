package com.kitesoft.naverlogin2022;

public class UserInfoResponse {
    String resultcode;
    String message;
    User response;
}

class User{
    String id;
    String nickname;
    String profile_image;
    String email;
}
