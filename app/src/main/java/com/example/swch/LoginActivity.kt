package com.example.swch

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import io.reactivex.Scheduler

import com.example.swch.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause.*
import com.kakao.sdk.user.UserApiClient
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLoginBinding.inflate(layoutInflater)
        val auth = FirebaseAuth.getInstance()
        setContentView(binding.root)




        //디버그 키 해시 확인
        /*
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        */

        // 로그인 정보 확인 (자동 로그인)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)
            }
            else if (tokenInfo != null) {
                Log.i(TAG, "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")
                //auth.sign
                Toast.makeText(this, "자동로그인 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AccessDenied.toString() -> {
                        Log.e("Tag", "접근이 거부됨(동의 취소)")
                    }
                    error.toString() == InvalidClient.toString() -> {
                        Log.e("Tag", "유효하지 않은 앱")
                    }
                    error.toString() == InvalidGrant.toString() -> {
                        Log.e("Tag", "인증수단이 유효하지 않음")
                    }
                    error.toString() == InvalidRequest.toString() -> {
                        Log.e("Tag", "요청 파라미터 오류")
                    }
                    error.toString() == InvalidScope.toString() -> {
                        Log.e("Tag", "유효하지 않은 ID")
                    }
                    error.toString() == Misconfigured.toString() -> {
                        Log.e("Tag", "설정이 올바르지 않음")
                    }
                    error.toString() == ServerError.toString() -> {
                        Log.e("Tag", "서버 내부 에러")
                    }
                    error.toString() == Unauthorized.toString() -> {
                        Log.e("Tag", "앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        Log.e("Tag", "기타 에러")
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                val user = Firebase.auth.currentUser
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        binding.btnLogin.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }
}