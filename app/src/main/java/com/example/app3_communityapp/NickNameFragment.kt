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
import com.example.app3_communityapp.databinding.FragmentNickNameBinding
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class NickNameFragment : Fragment() { // '닉네임 입력' 프래그먼트
    //바인딩 설정
    lateinit var nickNameFragmentBinding : FragmentNickNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        nickNameFragmentBinding = FragmentNickNameBinding.inflate(inflater)
        // -> 툴바 제목 설정
        nickNameFragmentBinding.nicknameToolbar.title = "닉네임 설정"
        // 백 스택에 저장된 프래그먼트들을 완전히 종료하기 위함
        // 로그인 시도가 완료된 뒤에는 '뒤로가기' 를 눌러도 액티비티가 종료되어야 한다.
        // -> 닉네임 설정 버튼 클릭 시 -> 새로운 주기를 갖는 메인 액티비티를 다시 띄워주어야 함
        nickNameFragmentBinding.nicknameJoinBtn.setOnClickListener {
            //사용자 입력값 가져오기
            val nickNameNickName = nickNameFragmentBinding.nicknameNickname.text.toString()
            //유효성 검사
            if(nickNameNickName == null || nickNameNickName.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("닉네임 입력 오류")
                dialogBuilder.setMessage("닉네임을 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    nickNameFragmentBinding.nicknameNickname.requestFocus()
                }
                dialogBuilder.show()
                return@setOnClickListener
            }

            //사용자가 여기서 입력한 '닉네임' 데이터도 다시 서버로 보낼 용도로 여기서 담아준다.
            val act = activity as MainActivity
            act.userNickName = nickNameNickName

            //Log.d("test", "${act.userId}")
            //Log.d("test", "${act.userPw}")
            //Log.d("test", "${act.userNickName}")

           thread {
               val client = OkHttpClient()
               val site = "http://172.30.1.27:8080/App3_CommunityServer/join_user.jsp"

               //서버로 보낼 데이터 세팅 - 넘어갈 데이터 변수 이름 일치 시켜주기
               val builder1 = FormBody.Builder()
               builder1.add("user_id", act.userId)
               builder1.add("user_pw", act.userPw)
               builder1.add("user_nick_name", act.userNickName)
               val formBody = builder1.build()

               //Request 생성 - Post 방식으로
               val request = Request.Builder().url(site).post(formBody).build()
               val response = client.newCall(request).execute()

               //가입 완료 처리
               if(response.isSuccessful == true){
                   activity?.runOnUiThread {
                       val dialogBuilder = AlertDialog.Builder(requireContext())
                       dialogBuilder.setTitle("가입 완료")
                       dialogBuilder.setMessage("가입이 완료되었습니다")
                       dialogBuilder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                           val mainIntent = Intent(requireContext(), MainActivity::class.java)
                           startActivity(mainIntent)
                           activity?.finish()
                       }
                       dialogBuilder.show()
                   }
               } else {
                   activity?.runOnUiThread {
                       val dialogBuilder = AlertDialog.Builder(requireContext())
                       dialogBuilder.setTitle("가입오류")
                       dialogBuilder.setMessage("가입 오류가 발생하였습니다")
                       dialogBuilder.setPositiveButton("확인", null)
                       dialogBuilder.show()
                   }
               }
           }

        }

        return nickNameFragmentBinding.root
    }

}