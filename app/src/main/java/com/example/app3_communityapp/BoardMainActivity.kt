package com.example.app3_communityapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.app3_communityapp.databinding.ActivityBoardMainBinding

class BoardMainActivity : AppCompatActivity() { //'게시판 메인' 액티비티

    //바인딩 설정
    lateinit var boardMainActivityBinding : ActivityBoardMainBinding

    //관리할 프래그먼트 화면 변수
    lateinit var currentFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩
        boardMainActivityBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(boardMainActivityBinding.root)

        //가장 초기 컨트롤 메소드
        fragmentController("board_main", false, false)
    }

    //(1) 프래그먼트 '컨트롤' 메소드 (현재 프래그먼트이름/백스택 추가여부/애니메이션 동작 여부)
    fun fragmentController(name:String, add:Boolean, animate:Boolean){
        // 1) 현재 프래그먼트 이름값에 따라 분기
        when(name){
            "board_main" -> {
                currentFragment = BoardMainFragment()
            }
            "board_read" -> {
                currentFragment = BoardReadFragment()
            }
            "board_write" -> {
                currentFragment = BoardWriteFragment()
            }
            "board_modify" -> {
                currentFragment = BoardModifyFragment()
            }
        }
        // 프래그먼트 관리자로 관리
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.board_main_container, currentFragment)

        // 2) 백스택 여부에 따라 분기
        if (add == true) {
            //현재 이름 프래그먼트를 백스택에 add 처리함 : 뒤로가기 대비
            trans.addToBackStack(name)
        }
        // 3) 애니메이션 사용 여부 따라 분기
        if(animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit() //화면에 적용

    }

    // (2) 프래그먼트를 백스택에서 제거하는 메소드
    fun fragmentRemoveBackStack(name:String) {
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE) //프래그먼트 매니저->제거 호출
    }



}