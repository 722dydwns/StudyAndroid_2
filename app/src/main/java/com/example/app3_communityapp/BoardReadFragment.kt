package com.example.app3_communityapp

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.app3_communityapp.databinding.FragmentBoardReadBinding

class BoardReadFragment : Fragment() { //'게시글 읽기' 화면 프래그먼트

    //바인딩 설정
    lateinit var boardReadFragmentBinding : FragmentBoardReadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //바인딩 처리
        boardReadFragmentBinding = FragmentBoardReadBinding.inflate(inflater)

        //툴바 관련 설정 ->
        boardReadFragmentBinding.boardReadToolbar.title = "게시글 읽기"
        //툴바에 'Back'버튼 아이콘 삽입처리
        val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        boardReadFragmentBinding.boardReadToolbar.navigationIcon = navIcon

        //버전 Q 기준으로. 달라진 툴바의 아이콘 색상 변경 처리 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boardReadFragmentBinding.boardReadToolbar.navigationIcon?.colorFilter = BlendModeColorFilter(
                Color.parseColor("#FFFFFF"), BlendMode.SRC_ATOP)
        }else{
            boardReadFragmentBinding.boardReadToolbar.navigationIcon?.setColorFilter(
                Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        }
        //백버튼 이벤트 처리 - '뒤로가기' 기능 처리
        boardReadFragmentBinding.boardReadToolbar.setNavigationOnClickListener{
            val act = activity as BoardMainActivity
            act.fragmentRemoveBackStack("board_read")
        }
        //툴바 위 메뉴 이벤트 처리 (수정/삭제)
        boardReadFragmentBinding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)
        boardReadFragmentBinding.boardReadToolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.board_read_menu_modify -> { //툴바 메뉴 '수정' 클릭 시
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_modify", true, true)
                    true
                }
                R.id.board_read_menu_delete -> {
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_read")
                    true
                }
                else -> false
            }
        }

        return boardReadFragmentBinding.root
    }


}