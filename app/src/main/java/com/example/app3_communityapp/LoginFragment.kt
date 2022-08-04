package com.example.app3_communityapp

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

            //화면 전환 -> 보드 메인
           // val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
           // startActivity(boardMainIntent)
           // activity?.finish()
        }

        return loginFragmentBinding.root
    }


}