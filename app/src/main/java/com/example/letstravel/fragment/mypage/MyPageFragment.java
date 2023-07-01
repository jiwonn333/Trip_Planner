package com.example.letstravel.fragment.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.letstravel.databinding.FragmentMypageBinding;
import com.example.letstravel.util.UserPreference;

public class MyPageFragment extends Fragment {

    private FragmentMypageBinding binding;
    private MyPageViewModel myPageViewModel;
    private boolean isLogin;

    // view 레이아웃 파일 연결
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMypageBinding.inflate(inflater, container, false);

        // binding 사용
        binding.btnKakao.setOnClickListener(v -> myPageViewModel.kakaoLogin(getContext()));
        binding.btnLogout.setOnClickListener(v -> myPageViewModel.kakaoLogout(getContext()));

        return binding.getRoot();
    }


    // 프레그먼트가 보여줄 뷰가 만들어진 후 자동으로 실행되는 라이프사이클 콜백메소드
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isLogin = UserPreference.getKakaoLoginSuccess(getContext());
        myPageViewModel = new ViewModelProvider(this).get(MyPageViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setViewmodel(myPageViewModel);

        /*myPageViewModel.getIsLogin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
//                // UI 작업
//                if (aBoolean) {
//                    // 로그인
//                    binding.btnKakao.setVisibility(View.INVISIBLE);
//                    binding.btnNaver.setVisibility(View.INVISIBLE);
//                    binding.btnLogout.setVisibility(View.VISIBLE);
//                    Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
//                } else {
//                    // 로그아웃
//                    binding.btnKakao.setVisibility(View.VISIBLE);
//                    binding.btnNaver.setVisibility(View.VISIBLE);
//                    binding.btnLogout.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getContext(), "else", Toast.LENGTH_SHORT).show();
//                }
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}