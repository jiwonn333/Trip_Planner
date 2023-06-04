package com.example.letstravel.fragment.mypage;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.letstravel.util.UserPreference;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;


@SuppressLint("StaticFieldLeak")
public class MyPageViewModel extends AndroidViewModel {

    public MutableLiveData isLogin = new MutableLiveData<Boolean>();

    public MutableLiveData<Boolean> getIsLogin() {
        boolean isAlreadyKaKaoLogin = UserPreference.getKakaoLoginSuccess(getApplication().getApplicationContext());
        isLogin.setValue(isAlreadyKaKaoLogin);

        return isLogin;
    }

    public MyPageViewModel(@NonNull Application application) {
        super(application);
    }

    public void kakaoLogin(Context context) {
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {
            UserApiClient.getInstance().loginWithKakaoTalk(context, (oAuthToken, throwable) -> {
                if (throwable != null) {
                    if (throwable instanceof ClientError && (((ClientError) throwable).getReason()) == ClientErrorCause.Cancelled) {
                        Log.e("error", throwable.getMessage());
                        return null;
                    }
                    UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
                } else if (oAuthToken != null) {
                    UserPreference.setKakaoLoginSuccess(context, true);
                    isLogin.setValue(true);
                }
                return null;
            });
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
        }
    }


    private Function2 callback = (Function2<OAuthToken, Throwable, Unit>) (oAuthToken, throwable) -> {
        if (throwable != null) {
            Log.e("error", throwable.getMessage());
            Log.e("error", throwable.toString());
        } else if (oAuthToken != null) {
            UserPreference.setKakaoLoginSuccess(getApplication().getApplicationContext(), true);
            isLogin.setValue(true);
        }
        return null;
    };

    public void kakaoLogout(Context context) {
        UserApiClient.getInstance().logout(throwable -> {
            if (throwable != null) {
                Log.e("error", "로그아웃 실패 SDK에서 토큰 삭제" + throwable.getMessage());
            } else {
                Log.i("error", "로그아웃 성공");
                UserPreference.setKakaoLoginSuccess(context, false);
                isLogin.setValue(false);
            }
            return null;
        });
    }
}