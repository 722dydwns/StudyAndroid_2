package com.example.app3_communityapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.app3_communityapp.databinding.ActivityMainBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() { //'메인' 액티비티
    //바인딩 설정
    lateinit var mainActivityBinding : ActivityMainBinding
    // 프래그먼트 담을 변수 선언
    lateinit var currentFragment : Fragment

    //사용자 정보 담을 변수 선언 - 여기에 데이터 담을 예정
    var userId = ""
    var userPw = ""
    var userNickName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        //Preference에 저장된 현재 User 정보를 가져옴
        // + 해당 User의 자동 로그인 희망 여부에 따른 화면 전환 분기문
        val pref = getSharedPreferences("login_data", Context.MODE_PRIVATE)
        val login_user_idx = pref.getInt("login_user_idx", -1)
        val login_auto_login = pref.getInt("login_auto_login", -1) //-1은 deValue

        if(login_auto_login == 1) { //자동로그인 희망 O
            thread {
                //서버로 보낼 데이터 세팅
                val client = OkHttpClient()
                val builder1 = FormBody.Builder()
                builder1.add("login_user_idx", "$login_user_idx")
                val formBody = builder1.build()
                //서버 통신을 위한 세팅
                val site = "http://${ServerInfo.SERVER_IP}:8080/App3_CommunityServer/check_auto_login.jsp"
                val request = Request.Builder().url(site).post(formBody).build()
                val response = client.newCall(request).execute()

                if(response.isSuccessful == true) { //통신 성공 시
                    val result_text = response.body?.string()!!.trim()
                    val chk = Integer.parseInt(result_text)
                    if(chk == 1) {
                        val boardMainIntent = Intent(this, BoardMainActivity::class.java)
                        startActivity(boardMainIntent)
                        finish()
                    }else {
                        fragmentController("login", false, false)
                    }
                }
            }
        }else{
            fragmentController("login", false, false)
        }
    }

    //이 메인 액티비티가 관리할 프래그먼트 화면들을 컨트롤할 메소드
    // (프래그먼트 이름/ 백 스택에 추가 여부 / 애니메이션 동작 사용 여부)
    fun fragmentController(name:String, add:Boolean, animate:Boolean) {

        //1) 띄울 프래그먼트 이름받아서 생성
        when(name) {
            "login" -> {
                currentFragment = LoginFragment()
            }
            "join" -> {
                currentFragment = JoinFragment()
            }
            "nick_name" -> {
                currentFragment = NickNameFragment()
            }
        }
        //트랜잭션으로 띄울 현재의 프래그먼트 띄움
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_container, currentFragment)

        //2) 백 스택에 추가 여부 따라서 ("뒤돌아가기 기능"동작 위해서 사용) - T: 뒤로가기 F:종료
        if(add == true) {
            trans.addToBackStack(name)
        }

        //3) 애니메이션 사용 여부에 따라서
        if(animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit() // 프래그먼트 동적 제어 - 화면에 위의 상황을 적용하는 메소드
    }

}