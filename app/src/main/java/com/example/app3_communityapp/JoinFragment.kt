package com.example.app3_communityapp

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
            //xml 영역에서 사용자 입력값 ('id/pw') string 형태로 가져옴
            val joinId = joinFragmentBinding.joinId.text.toString()
            val joinPw = joinFragmentBinding.joinPw.text.toString()

            // -> 무입력 상태에 대한 유효성 검사
            //ID 입력값에 대하여
            //가져온 사용자 입력값이 null 이거나 무입력 상태인 경우, -> (알림) 다이얼로그 띄움
           if(joinId == null || joinId.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
               dialogBuilder.setTitle("아이디 입력 오류")
               dialogBuilder.setMessage("아이디를 입력해주세요")
               dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    joinFragmentBinding.joinId.requestFocus() //자동으로 아이디 입력란에 포커싱 주게 설정
               }
               dialogBuilder.show()
               return@setOnClickListener //코틀린에서 람다함수 (setOnClickListener)는 return@로 실행 종료시켜준다.
              }

            //PW 입력값에 대하여
            if(joinPw == null || joinPw.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("비밀번호 입력 오류")
                dialogBuilder.setMessage("비밀번호를 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    joinFragmentBinding.joinPw.requestFocus()
                }
                dialogBuilder.show()
                return@setOnClickListener
            }

            //화면 전환 -> 닉네임 설정 화면
            val act = activity as MainActivity

            //가입화면에서 id/pw 입력 후 전환된 닉네임 입력 프래그먼트에서는 이 데이터값을 서버에 전달할 필요가 있다.
            //따라서 여기서 데이터를 임시로 한 번 담아준다.
            act.userId = joinId
            act.userPw = joinPw

            act.fragmentController("nick_name", true, true)
        }



        return joinFragmentBinding.root
    }

}