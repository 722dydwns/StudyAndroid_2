package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3_communityapp.databinding.BoardMainRecyclerItemBinding
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding

class BoardMainFragment : Fragment() { //'게시판 메인' 프래그먼트
    //바인딩 세팅
    lateinit var boardMainFragmentBinding : FragmentBoardMainBinding

    //Menu 항목 (Dialog) 에 들어갈 부분을 임의로 우선 리스트타입으로 담아둠 -
    // ---> 서버 연동 후 서버에서 가져올 데이터 부분임
    val boardListData = arrayOf(
        "전체글", "게시판1", "게시판2", "게시판3", "게시판4"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardMainFragmentBinding = FragmentBoardMainBinding.inflate(inflater)
        boardMainFragmentBinding.boardMainToolbar.title = "게시판이름"

        //(1) Toolbar 관련 - 메뉴 xml 지정
        boardMainFragmentBinding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
        //(2) Toolbar 메뉴 클릭 이벤트 처리
        boardMainFragmentBinding.boardMainToolbar.setOnMenuItemClickListener {

            when(it.itemId) { //사용자 클릭한 '메뉴' 항목값에 따라 다이얼로그 띄우기
                //메뉴 리스트 클릭 시
                R.id.board_main_menu_board_list -> {
                    val boardListBuilder = AlertDialog.Builder(requireContext())
                    boardListBuilder.setTitle("게시판 목록")
                    boardListBuilder.setNegativeButton("취소", null)
                    boardListBuilder.setItems(boardListData, null)
                    boardListBuilder.show()
                    true
                }
                //글쓰기 메뉴 항목 클릭 시
                R.id.board_main_menu_write -> {
                    val act = activity as BoardMainActivity
                    act.fragmentController("board_write", true, true)
                    true
                }
                else -> false
            }
        }

        //리사이클러뷰 관련 설정
        // 1) 어댑터 객체 생성
        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        boardMainFragmentBinding.boardMainRecycler.adapter = boardMainRecyclerAdapter
        // 2) 레이아웃 매니저로 어댑터로 만든 항목의 레이아웃 배치 설정
        boardMainFragmentBinding.boardMainRecycler.layoutManager = LinearLayoutManager(requireContext())
        // 3) 아이템 데코레이션 - 구분선 생성
        boardMainFragmentBinding.boardMainRecycler.addItemDecoration(DividerItemDecoration(requireContext(), 1))

        return boardMainFragmentBinding.root
    }

    //리사이클러뷰 사용을 위한 클래스  - 내부에서 재정의 필요한 함수 getItemCount()/onCreateViewHolder()/onBindViewHolder()
    inner class BoardMainRecyclerAdapter : RecyclerView.Adapter<BoardMainRecyclerAdapter.ViewHolderClass>(){

        //1) 재정의 : onCreateViewHolder() : 뷰 홀더준비 위해 자동 호출되는 함수
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {

            //바인딩
            val boardMainRecyclerItemBinding = BoardMainRecyclerItemBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(boardMainRecyclerItemBinding) //각 개별 항목 xml 파일을 바인딩

            //각 항목 하나당 레이아웃 크기 설정
            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, //가로 길이
                ViewGroup.LayoutParams.WRAP_CONTENT  //세로 길이
            )
            //위의 설정을 레이아웃에 세팅 해준다.
            boardMainRecyclerItemBinding.root.layoutParams = layoutParams
            //각 항목 터치했을 때 호출될 리스터 설정
            boardMainRecyclerItemBinding.root.setOnClickListener(holder)

            return holder
        }

        //2) 재정의 : onBindViewHolder() 뷰 홀더 각 항목 뷰에 데이터 출력 위해서 자동 호출
        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        }
        //3) 재정의 : getItemCount() 항목 개수 판단을 위해 자동 호출됨
        override fun getItemCount(): Int {
            return 10
        }

        //뷰 홀더 클래스 생성
        inner class ViewHolderClass(boardMainRecyclerItemBinding:BoardMainRecyclerItemBinding)
            : RecyclerView.ViewHolder(boardMainRecyclerItemBinding.root), View.OnClickListener{

            //각 항목 터치됐을 때 자동 호출 메소드 ()
            override fun onClick(v: View?) {
                val act = activity as BoardMainActivity
                act.fragmentController("board_read", true, true)

            }
        }
    }

}