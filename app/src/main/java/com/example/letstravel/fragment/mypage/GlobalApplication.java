package com.example.letstravel.fragment.mypage;

import static com.example.letstravel.BuildConfig.KAKAO_APP_KEY;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, KAKAO_APP_KEY);
    }
}