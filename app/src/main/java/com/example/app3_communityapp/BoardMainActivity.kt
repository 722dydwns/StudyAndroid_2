package com.example.app3_communityapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.app3_communityapp.databinding.ActivityBoardMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread

class BoardMainActivity : AppCompatActivity() { //'게시판 메인' 액티비티

    //바인딩 설정
    lateinit var boardMainActivityBinding : ActivityBoardMainBinding

    //관리할 프래그먼트 화면 변수
    lateinit var currentFragment : Fragment

    //게시글 목록 이름 / idx 받을 List 변수 선언
    val boardNameList = ArrayList<String>()
    val boardIndexList = ArrayList<Int>()
    //현재 선택한 항목 정보 변수
    var selectedBoardType = 0  //초기에는 전체게시판 항목 선택한 상태로 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩
        boardMainActivityBinding = ActivityBoardMainBinding.inflate(layoutInflater)
        setContentView(boardMainActivityBinding.root)

        //전체 게시판 항목 생성
        boardNameList.add("전체 게시판")
        boardIndexList.add(0) //인덱스는 0 으로 세팅

        //서버와의 통신
        thread {
            //통신 정보 세팅
            val client = OkHttpClient()
            val site = "http://${ServerInfo.SERVER_IP}:8080/App3_CommunityServer/get_board_list.jsp"

            //GET 요청 - 가져오기만 할거라서 get / cf. 보내는 것도 필요할 땐 POSt ㅇ청
            val request = Request.Builder().url(site).get().build()
            val response = client.newCall(request).execute() //통신 실행

            if(response.isSuccessful == true) { //정상 통신
                //우선 서버 속 데이터를 공백 제거한 문자열 Text로 받앋고
                val resultText = response.body?.string()!!.trim()
                // 받아온 공백 제거 문자열 Text 데이터를 다시 JSON 형태의 배열에 담는다.
                val root = JSONArray(resultText)
                //반복문 돌리면서
                for(i in 0 until root.length()) {
                    val obj = root.getJSONObject(i)
                    //데이터 전체 받아온 뒤
                    val boardIdx = obj.getInt("board_idx")
                    val boardName = obj.getString("board_name")

                    //여기서 선언해둔 데이터 받을 용도의의 List타입 변수에 차례로 담는다.
                    boardIndexList.add(boardIdx)
                    boardNameList.add(boardName)
                }
            }
        }

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