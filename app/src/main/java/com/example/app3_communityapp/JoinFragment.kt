package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app3_communityapp.databinding.FragmentJoinBinding

class JoinFragment : Fragment() { //'회원가입' 프래그먼트
    //바인딩 설정
    lateinit var joinFragmentBinding : FragmentJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩 연결
        joinFragmentBinding = FragmentJoinBinding.inflate(inflater)
        //프래그먼트 상단 '툴바' title 지정
        joinFragmentBinding.joinToolbar.title = "회원가입"

        // 회원가입 화면에서 '다음' 버튼 클릭 시 -> 닉네임 설정 화면으로 전환 이벤트 처리
        joinFragmentBinding.joinNextBtn.setOnClickListener {
            val act = activity as MainActivity
            act.fragmentController("nick_name", true, true)
        }

        return joinFragmentBinding.root
    }

}