package com.example.app3_communityapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
            startActivity(boardMainIntent)
            activity?.finish()
        }

        return loginFragmentBinding.root
    }


}