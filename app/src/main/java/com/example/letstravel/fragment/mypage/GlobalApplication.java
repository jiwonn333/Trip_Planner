package com.example.letstravel.fragment.mypage;

import android.app.Application;

import com.example.letstravel.BuildConfig;
import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY);
    }
}