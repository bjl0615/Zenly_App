package com.example.zenly_app

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.zenly_app.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val callback : (OAuthToken?, Throwable?) -> Unit = {token , error ->
        if(error != null) {
            // 로그인 실패
            Log.e("loginAcitivy" , "$error")
        } else if(token != null) {
            Log.e("loginActivity" , "login in with kakao account token = $token")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, "a6fe16c17d01b3ed9d5ddabf6596698d")
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoTalkLoginButton.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                //카카오톡 로그인 실행
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->

                    if(error != null) {
                        // 카카오톡 로그인 실패

                        if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        UserApiClient.instance.loginWithKakaoAccount(this , callback = callback)
                    }else if(token != null) {
                        Log.e("LoginActivity" , "token = $token")
                        // 로그인 성공
                    }

                }
            }else {
                //카카오 계정 로그인

                 UserApiClient.instance.loginWithKakaoAccount(this , callback = callback)
            }
        }


    }
}