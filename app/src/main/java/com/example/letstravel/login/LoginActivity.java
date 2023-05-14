package com.example.letstravel.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.letstravel.MainActivity;
import com.example.letstravel.R;
import com.example.letstravel.util.UserPreference;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.model.ClientError;
import com.kakao.sdk.common.model.ClientErrorCause;
import com.kakao.sdk.user.UserApiClient;
import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.OAuthLoginCallback;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private ImageButton btnKakao;
    private ImageButton btnNaver;
    private Context context;
    private ImageView btnBack;
    private TextView login;
    private TextView btnLogout;
    private boolean isKakaoLoginCheck;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        btnKakao = findViewById(R.id.btn_kakao);
        btnNaver = findViewById(R.id.btn_naver);
        btnBack = findViewById(R.id.iv_back);
        login = findViewById(R.id.tv_login);
        btnLogout = findViewById(R.id.btn_logout);


        isKakaoLoginCheck = UserPreference.getKakaoLoginSuccess(context);
        if (isKakaoLoginCheck) {
            setShowLoginUI();
        } else {
            setShowLogoutUI();
        }

        btnKakao.setOnClickListener(v -> kakaoLogin());

        btnNaver.setOnClickListener(v -> naverLogin());

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            startKakaoLogout();
        });


    }

    private void kakaoLogin() {
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(context)) {
            UserApiClient.getInstance().loginWithKakaoTalk(context, (oAuthToken, throwable) -> {
                if (throwable != null) {
                    if (throwable instanceof ClientError && (((ClientError) throwable).getReason()) == ClientErrorCause.Cancelled) {
                        return null;
                    }
                    UserApiClient.getInstance().loginWithKakaoAccount(context, callback);
                } else if (oAuthToken != null) {
                    UserPreference.setKakaoLoginSuccess(context, true);
                    setShowLoginUI();
                }
                return null;
            });
        } else {
            UserApiClient.getInstance().loginWithKakaoAccount(context, callback);

        }
    }

    private Function2 callback = (Function2<OAuthToken, Throwable, Unit>) (oAuthToken, throwable) -> {
        if (throwable != null) {

        } else if (oAuthToken != null) {
            UserPreference.setKakaoLoginSuccess(context, true);
            setShowLoginUI();
        }
        return null;
    };

    private void naverLogin() {
        /**
         * launcher나 OAuthLoginCallback을 authenticate() 메서드 호출 시
         * 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있다.
         * https://developer.android.com/training/basics/intents/result?hl=ko#custom
         */


        OAuthLoginCallback oAuthLoginCallback = new OAuthLoginCallback() {
            @Override
            public void onSuccess() {
                UserPreference.setNaverLoginSuccess(context, true);
            }

            @Override
            public void onFailure(int i, @NonNull String s) {
                String errorCode = NaverIdLoginSDK.INSTANCE.getLastErrorCode().getCode();
                String errorDescription = NaverIdLoginSDK.INSTANCE.getLastErrorDescription();
            }

            @Override
            public void onError(int errorCode, @NonNull String message) {
                onFailure(errorCode, message);
            }
        };
        NaverIdLoginSDK.INSTANCE.authenticate(this, oAuthLoginCallback);

    }


    private void startKakaoLogout() {
        UserApiClient.getInstance().logout(throwable -> {
            if (throwable != null) {
                Log.e("error", "로그아웃 실패 SDK에서 토큰 삭제");
            } else {
                Log.i("error", "로그아웃 성공");
                UserPreference.setKakaoLoginSuccess(context, false);
                setShowLogoutUI();
            }
            return null;
        });
    }


    private void setShowLoginUI() {
        login.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        btnKakao.setVisibility(View.INVISIBLE);
        btnNaver.setVisibility(View.INVISIBLE);
    }

    private void setShowLogoutUI() {
        login.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        btnKakao.setVisibility(View.VISIBLE);
        btnNaver.setVisibility(View.VISIBLE);
    }


}
