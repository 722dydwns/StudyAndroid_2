package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.app3_communityapp.databinding.FragmentBoardModifyBinding

class BoardModifyFragment : Fragment() { //게시글 수정 프래그먼트
    //바인딩 설정
    lateinit var boardModifyFragmentBinding : FragmentBoardModifyBinding

    //Spinner 목록- 나중에 서버로 받아올 부분 임시로 arrayOf 선언
    val spinnerData = arrayOf("게시판1", "게시판2", "게시판3", "게시판4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardModifyFragmentBinding = FragmentBoardModifyBinding.inflate(inflater)
        //툴바 관련 설정
        boardModifyFragmentBinding.boardModifyToolbar.title = "글 수정 "

        //툴바 메뉴 관련 설정
        boardModifyFragmentBinding.boardModifyToolbar.inflateMenu(R.menu.board_modify_menu)
        boardModifyFragmentBinding.boardModifyToolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.board_modify_menu_camera -> {
                    true
                }
                R.id.board_modify_menu_gallery -> {
                    true
                }
                R.id.board_modify_menu_upload -> {
                    val act = activity as BoardMainActivity
                    act.fragmentRemoveBackStack("board_modify")
                    true
                }
                else -> false
            }
        }

        //Spinner 구성
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerData)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        boardModifyFragmentBinding.boardModifyType.adapter = spinnerAdapter

        return boardModifyFragmentBinding.root
    }

}