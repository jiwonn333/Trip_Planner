package com.example.letstravel.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    public static final String PREF_KEY_USER_ID = "userid";
    public static final String PREF_KEY_USER_PW = "userpw";
    public static final String PREF_KEY_KAKAO_USER_NAME = "kakaoname";
    public static final String PREF_KEY_KAKAO_USER_URL = "kakaourl";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("loginPref", MODE_PRIVATE);
    }

    public static boolean getKakaoLoginSuccess(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        boolean getLoginSuccess = pref.getBoolean("kakaoLoginSuccess", DEFAULT_VALUE_BOOLEAN);
        return getLoginSuccess;
    }

    public static void setKakaoLoginSuccess(Context context, boolean loginSuccess) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("kakaoLoginSuccess", loginSuccess);
        editor.commit();
    }


    public static boolean getNaverLoginSuccess(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        boolean getLoginSuccess = pref.getBoolean("naverLoginSuccess", DEFAULT_VALUE_BOOLEAN);
        return getLoginSuccess;
    }

    public static void setNaverLoginSuccess(Context context, boolean loginSuccess) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("naverLoginSuccess", loginSuccess);
        editor.commit();
    }

    public static String getKakaoUserName(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String getUserId = pref.getString(PREF_KEY_KAKAO_USER_NAME, "");

        return getUserId;
    }

    public static void setKakaoUserName(Context context, String userName) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREF_KEY_KAKAO_USER_NAME, userName);
        editor.commit();
    }

    public static String getKakaoUserUrl(Context context) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        String getUserId = pref.getString(PREF_KEY_KAKAO_USER_URL, "");

        return getUserId;
    }

    public static void setKakaoUserUrl(Context context, String userUrl) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(PREF_KEY_KAKAO_USER_URL, userUrl);
        editor.commit();
    }
}
