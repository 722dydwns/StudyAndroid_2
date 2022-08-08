package com.example.app3_communityapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.app3_communityapp.databinding.FragmentLoginBinding
import okhttp3.FormBody
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread


class LoginFragment : Fragment() { //'로그인' 프래그먼트
    //바인딩 설정
    lateinit var loginFragmentBinding : FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩 연결
        loginFragmentBinding = FragmentLoginBinding.inflate(inflater)
        //프래그먼트 상단 '툴바' title 지정
        loginFragmentBinding.loginToolbar.title = "로그인"
        // 현재 로그인 화면에서 '회원가입' 버튼 클릭 시 이벤트 처리
        loginFragmentBinding.loginJoinbtn.setOnClickListener {
            //액티비티 추출 - 현재의 프래그먼트를 소유 중인 액티비티 추출
            val act = activity as MainActivity //추출한 액티비티를 MainActiity로 변환
            //추출한 액티비티에서 현재의 프래그먼트 컨트롤 메소드 호출
            act.fragmentController("join", true, true)
        }
        // '로그인' 버튼 클릭 시, 이벤트 처리 :
        loginFragmentBinding.loginLoginbtn.setOnClickListener {

            //사용자 입력 데이터 받기
            val loginId = loginFragmentBinding.loginId.text.toString()
            val loginPw = loginFragmentBinding.loginPw.text.toString()
            val chk = loginFragmentBinding.loginAutologin.isChecked //자동 로그인 체크 여부

            var loginAutoLogin = 0 //자동 로그인 여부 분기 처리
            if(chk == true) {
                loginAutoLogin = 1
            }else {
                loginAutoLogin = 0
            }
            //유효성 검사 - 입력 여부에 따른 분기
            //로그인 ID 값
            if(loginId == null || loginId.length == 0 ){
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("아이디 입력 오류")
                dialogBuilder.setMessage("아이디를 입력해주세요 ")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    loginFragmentBinding.loginId.requestFocus() //포커스 주기
                }
                dialogBuilder.show()
                return@setOnClickListener
            }
            //로그인 PW 값
            if(loginPw == null || loginPw.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("비밀번호 입력 오류")
                dialogBuilder.setMessage("비밀번호를 입력해주세요 ")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    loginFragmentBinding.loginPw.requestFocus()
                }
                dialogBuilder.show()
                return@setOnClickListener
            }
            //데이터 잘 받아졌는지 Log 찍기
            Log.d("test", loginId)
            Log.d("test", loginPw)
            Log.d("test", "$loginAutoLogin")

            //-> 서버와 통신 처리
            thread {
                val client = OkHttpClient()
                val site = "http://${ServerInfo.SERVER_IP}/App3_CommunityServer/login_user.jsp"

                //서버로 전달할 데이터 몸통 세팅 처리 - id/pw 값을 전달하고
                val builder1 = FormBody.Builder()
                builder1.add("user_id", loginId)
                builder1.add("user_pw", loginPw)
                builder1.add("user_autologin", "$loginAutoLogin")
                val formBody = builder1.build()

                val request = Request.Builder().url(site).post(formBody).build()
                val response = client.newCall(request).execute() //요청 수행

                //통신 성공 여부에 따라 분기
                if(response.isSuccessful == true) { //통신 정상 연결
                    //응답 결과를 추출
                    val result_text = response.body?.string()!!.trim()

                    if(result_text == "FAIL" ) { //로그인 실패 시 처리
                        activity?.runOnUiThread{
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setTitle("로그인 실패")
                            dialogBuilder.setMessage("아이디나 비번 잘못되었습니다.")
                            dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                                //입력한 칸 비워두고 - 자동로그인 체크 해제 - 자동 아이디 포커싱
                                loginFragmentBinding.loginId.setText("")
                                loginFragmentBinding.loginPw.setText("")
                                loginFragmentBinding.loginAutologin.isChecked = false
                                loginFragmentBinding.loginId.requestFocus()
                            }
                            dialogBuilder.show()
                        }
                    } else{ //로그인 성공 시 처리
                        activity?.runOnUiThread {
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setTitle("로그인 성공")
                            dialogBuilder.setMessage("로그인에 성공하였습니다.")
                            dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->

                                // 사용자 정보를 'preferences' 에 저장한다.
                                val pref = activity?.getSharedPreferences("login_data", Context.MODE_PRIVATE)
                                val editor = pref?.edit()
                                //서버로부터 전달받는 데이터는 모두 String 형태이므로 받을 때 정수형 Integer로 형변환시켜서 받음
                                //putString(키, 값) , putInt(키, 값) 형태
                                editor?.putInt("login_user_idx", Integer.parseInt(result_text)) //해당 사용자 idx
                                editor?.putInt("login_auto_login", loginAutoLogin) //자동로그인 체크 여부
                                editor?.commit()

                                //로그인 성공 후 -> 화면 전환처리
                                val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
                                startActivity(boardMainIntent)
                                activity?.finish()
                            }
                            dialogBuilder.show() //알림 띄우기
                        }
                    }
                }else{ //통신 비정상인 경우
                    val dialogBuilder = AlertDialog.Builder(requireContext())
                    dialogBuilder.setTitle("로그인 오류")
                    dialogBuilder.setMessage("로그인 오류 발생")
                    dialogBuilder.setPositiveButton("확인", null)
                    dialogBuilder.show()
                }
            }
        }
        return loginFragmentBinding.root
    }
}