package com.example.letstravel.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private Intent intent;
    private ImageView btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        btnKakao = findViewById(R.id.btn_kakao);
        btnNaver = findViewById(R.id.btn_naver);
        btnBack = findViewById(R.id.iv_back);

        btnKakao.setOnClickListener(v -> kakaoLogin());

        btnNaver.setOnClickListener(v -> naverLogin());

        btnBack.setOnClickListener(v -> {
            finish();
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
                    startIntent();
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
            startIntent();
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


    private void startIntent() {
        intent = new Intent();
//        intent.putExtra("userId", userId);
//        intent.putExtra("userName", userName);
//        intent.putExtra("userPhone", userPhone);
        setResult(RESULT_OK, intent);
        finish();
    }


}
