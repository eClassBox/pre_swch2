package com.example.swch

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.swch.databinding.ActivityMainBinding
import com.kakao.sdk.user.UserApiClient
//import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    private val tabBoardFragment by lazy { BoardFragment() }
    private val tabCallFragment by lazy { CallFragment() }
    private val tabMapFragment by lazy { MapFragment() }
    //var firestore : FirebaseFirestore? = null
    var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }

        binding.btnErase.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Toast.makeText(this, "회원 탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "회원 탈퇴 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }
            }
        }
        UserApiClient.instance.me { user, error ->
            binding.kakaoName.setText("환영합니다 : ${user?.kakaoAccount?.profile?.nickname}")
        }
    }

}