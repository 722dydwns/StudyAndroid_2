# # PRJ_3. 커뮤니티 게시판 앱

## 🟦 31강, 32강 프로젝트 생성

### ▶️ViewBinding 설정

- app 수준의 build.gradle파일에 다음을 추가

```kotlin
buildFeatures{
viewBinding = true
}
```

- MainActivity 에서 바인딩 변수 선언 후 onCreate() 에서 연결

```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var mainActivityBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

    }
}
```

## 🟦 34강. 로그인 화면 구성

### ▶️  **‘**로그인’ 화면 구성

- Splash 화면 끝난 후 곧바로 로그인 화면이 나타남
- 로그인 화면 속에는 회원 가입 버튼 존재
- 로그인 후 게시판의 첫 화면 등장하도록 할 것
- ‘프래그먼트’ 화면들로 만들 예정

### **📍theme.xml**

- 먼저 액션바를 없앤다
- 각 프래그먼트 별 ‘툴바’ 배치해서 사용할 목적
    
    ```xml
    <!-- 기본 액션바 없애기 -->
    <item name = "windowActionBar">false</item>
    <item name = "windowNoTitle">true</item>
    ```
    

### **🟧** **액션바 VS 툴바**

- **액션바 : 액티비티에 자동 포함되는 구성요소**
- **툴바 : 개발자가 직접 제어하는 ‘뷰’ 속성**
    
    ![액션바툴바비교.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ae7f7ba7-94ee-4ce0-9182-a93eb95d94cb/%EC%95%A1%EC%85%98%EB%B0%94%ED%88%B4%EB%B0%94%EB%B9%84%EA%B5%90.png)
    

### **🟧 애플리케이션 구성**

 각 액티비티 규격 안에 여러 개의 프래그먼트 화면들을 담는 형태.

- 애플리케이션의 모든 화면은 Fragment(프래그먼트)로 구성한다.
- Activity(액티비티)는 Fragement를 관리하는 역할을 담당한다.
- 하나의 기능이 완료되면 새로운 Activity를 실행하는 구조로 처리한다.
    
    ![프래그먼트 관련 .png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/9df72dc0-3ff9-44b8-8c54-90108840dc8e/%ED%94%84%EB%9E%98%EA%B7%B8%EB%A8%BC%ED%8A%B8_%EA%B4%80%EB%A0%A8_.png)
    

## **🟧** Task, Activity, 백 스택

---

Task는 사용자가 특정 작업을 할 때 상호작용하는 Activity의 집합입니다. 하나의 Task에는 액티비티 집합을 열린 순서대로 정렬해놓는 **백 스택이 존재**합니다. **백 스택**은 **Stack의 성질**을 가지고 있으며 가장 최신에 열린 액티비티가 가장 맨위로 위치하게 됩니다.

 예를 들어서 이메일 앱에는 새 메시지 목록을 표시하는 액티비티가 존재하고, 사용자가 메시지 목록에서 메시지를 하나 클릭하면 메시지의 내용을 상세하게 볼 수 있도록 새로운 액티비티가 열리게 됩니다. 이때 메시지 상세보기 액티비티는 백 스택에 추가되며, 만약에 사용자가 뒤로가기 버튼을 탭하면 메시지 상세보기 액티비티는 백 스택에서 pop됩니다.

 사용자가 계속 뒤로 버튼을 누르면 계속 이전 액티비티가 나타나며, 만약 모든 액티비티가 스택에서 삭제되면 Task는 더 이상 존재하지 않게 됩니다. 사용자가 새 Task를 시작하거나 홈 버튼을 통해 홈 화면으로 이동할 때 현재 Task는 통째로 백그라운드로 이동합니다. Task의 모든 액티비티는 백그라운드에 있는 동안 모두 중지되지만, Task의 백 스택은 그대로 유지됩니다. 이후에 Task가 다시 포그라운드로 돌아가게 되면 백 스택이 유지 되어있기에 사용자는 스택의 가장 상단에 있는 액티비티를 그대로 이어나갈 수 있습니다.

◾ MainActivity.kt

```kotlin
package com.example.app3_communityapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.app3_communityapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //바인딩 설정
    lateinit var mainActivityBinding : ActivityMainBinding
    // 프래그먼트 담을 변수 선언
    lateinit var currentFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        //프래그먼트 컨트롤 메소드
        fragmentController("login", false, false)
    }

    //이 메인 액티비티가 관리할 프래그먼트 화면들을 컨트롤할 메소드
    // (프래그먼트 이름/ 백 스택에 추가 여부 / 애니메이션 동작 사용 여부)
    fun fragmentController(name:String, add:Boolean, animate:Boolean) {

        //1) 띄울 프래그먼트 이름받아서 생성
        when(name) {
            "login" -> {
                currentFragment = LoginFragment()
            }
        }
        //트랜잭션으로 띄울 현재의 프래그먼트 띄움
        val trans =supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_container, currentFragment)

        //2) 백 스택에 추가 여부 따라서 ("뒤돌아가기 기능"동작 위해서 사용) - T: 뒤로가기 F:종료
        if(add == true) {
            trans.addToBackStack(name)
        }

        //3) 애니메이션 사용 여부에 따라서
        if(animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit()

    }

}
```

◾ LoginFragment.kt

```kotlin
package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app3_communityapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
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

        loginFragmentBinding = FragmentLoginBinding.inflate(inflater)
        loginFragmentBinding.loginToolbar.title= "로그인"

        return loginFragmentBinding.root
}
```

## 🟦 35강. 회원가입 화면 구성

### ▶️  **‘회원 가입’ 화면 구성**

- 로그인 화면 속 ‘회원가입’ 버튼 클릭 시, → 회원가입 화면으로 전환
- 사용자로부터 아이디와 비밀번호만 입력받음
- ‘다음’ 버튼 클릭 시, → 닉네임 설정 화면으로 전환

◾ JoinFragment.kt

```kotlin
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
        joinFragmentBinding.joinToolbar.title= "회원가입"

        // 회원가입 화면에서 '다음' 버튼 클릭 시 -> 닉네임 설정 화면으로 전환 이벤트 처리
        joinFragmentBinding.joinNextBtn.setOnClickListener{
val act =activityas MainActivity
            act.fragmentController("nick_name", true, true)
}

return joinFragmentBinding.root
}

}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c130cc62-4b30-4282-87d7-d3d2c13676b6/Untitled.png)

### ▶️  **‘닉네임 입력’ 화면 구성**

- 닉네임 입력 후 ‘가입 완료’ 버튼 클릭 시, → 로그인 화면으로 전환

◾NickNameFragment.kt

```kotlin
package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app3_communityapp.databinding.FragmentNickNameBinding

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
        //툴바 제목 설정
        nickNameFragmentBinding.nicknameToolbar.title= "닉네임 설정"

        return nickNameFragmentBinding.root
}

}
```

◾MainActivity.kt

- 이 액티비티 객체 속에 만들어뒀던 fragmentController()

```
class MainActivity : AppCompatActivity() { //'메인' 액티비티
    //바인딩 설정
    lateinit var mainActivityBinding : ActivityMainBinding
    // 프래그먼트 담을 변수 선언
    lateinit var currentFragment : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        //프래그먼트 컨트롤 메소드
        fragmentController("login", false, false)
    }

    //이 메인 액티비티가 관리할 프래그먼트 화면들을 컨트롤할 메소드
    // (프래그먼트 이름/ 백 스택에 추가 여부 / 애니메이션 동작 사용 여부)
    fun fragmentController(name:String, add:Boolean, animate:Boolean) {

        //1) 띄울 프래그먼트 이름받아서 생성
        when(name) {
            "login" -> {
                currentFragment = LoginFragment()
            }
            "join" -> {
                currentFragment = JoinFragment()
            }
            "nick_name" -> {
                currentFragment = NickNameFragment()
            }
        }
        //트랜잭션으로 띄울 현재의 프래그먼트 띄움
        val trans =supportFragmentManager.beginTransaction()
        trans.replace(R.id.main_container, currentFragment)

        //2) 백 스택에 추가 여부 따라서 ("뒤돌아가기 기능"동작 위해서 사용) - T: 뒤로가기 F:종료
        if(add == true) {
            trans.addToBackStack(name)
        }

        //3) 애니메이션 사용 여부에 따라서
        if(animate == true) {
            trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        trans.commit() // 프래그먼트 동적 제어 - 화면에 위의 상황을 적용하는 메소드

    }

}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/cc3e69a0-328a-4de4-bc42-f3415c337417/Untitled.png)

### **🟧 ‘닉네임 설정’ 버튼 후 → 액티비티 완전 종료 처리**

- 현재 앱은 로그인 시도 or 회원가입 시도 후 닉네임 설정까지 완료되면 ‘뒤로 가기’ 버튼 눌러도 완전히 새로운 액티비티 창을 켜야 하기 때문
    
    ◾NickNameFragment.kt 속 onCreateView() 메소드 내부 
    
    ```kotlin
    // 백 스택에 저장된 프래그먼트들을 완전히 종료하기 위함
    // 로그인 시도가 완료된 뒤에는 '뒤로가기' 를 눌러도 액티비티가 종료되어야 한다.
    nickNameFragmentBinding.nicknameJoinBtn.setOnClickListener{
    val mainIntent = Intent(requireContext(), MainActivity::class.java)
        startActivity(mainIntent)
    activity?.finish()
    }
    ```
    

### **🟧 ‘로그인’ 버튼 클릭 시 → 액티비티 완전 종료 처리**

- ‘로그인’ 버튼 클릭 후에는 ‘게시판 메인’ 액티비티로 새롭게 화면 전환되어야 함.
    
    ◾LoginFragment.kt 속 onCreateView() 메소드 내부에 추가
    

```
// '로그인' 버튼 클릭 시, 이벤트 처리 :
loginFragmentBinding.loginLoginbtn.setOnClickListener{
val boardMainIntent = Intent(requireContext(), BoardMainActivity::class.java)
    startActivity(boardMainIntent)
activity?.finish()
}

```

## 🟦 36강. 게시글 Main 액티비티 화면 구성

### ▶️ ‘게시글 메인’ 액티비티 화면 구성

- 게시글 메인 액티비티 구성
- MainActicity 처럼 **여러 개의 Fragment 관리할 수 있도록 구성**

◾BoardMainActivity.kt 

- 게시판 관련 프래그먼트 총 관리할 액티비티

```kotlin
package com.example.app3_communityapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
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

    //프래그먼트 컨트롤 메소드 (현재 프래그먼트이름/백스택 추가여부/애니메이션 동작 여부)
    fun fragmentController(name:String, add:Boolean, animate:Boolean){
        // 1) 현재 프래그먼트 이름값에 따라 분기
        when(name){
            "board_main" -> {
                currentFragment = BoardMainFragment()
            }
        }
        // 프래그먼트 관리자로 관리
        val trans =supportFragmentManager.beginTransaction()
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

}
```

### **🟧 ‘게시판 메인’ 프래그먼트 화면 생성**

- 게시판 관리하는 BoardMainActivity 속에 포함되는 가장 Main 게시판 프래그먼트

◾BoardMainFragment.kt

```kotlin
package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding

class BoardMainFragment : Fragment() { //'게시판 메인' 프래그먼트

    //바인딩 세팅
    lateinit var boardMainFragmentBinding : FragmentBoardMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardMainFragmentBinding = FragmentBoardMainBinding.inflate(inflater)

        return boardMainFragmentBinding.root
}

}
```

## 🟦 37강. RecyclerViw : 목록 화면 구성

### ▶️ RecyclerView 구성

- 현재 게시판 글 목록을 RecyclerView를 이용하여 구성.

### **🟧 RecyclerView 리사이클러 뷰 - 목록 화면**

- **리사이클러 뷰**  : 여러 항목 나열한 목록 화면
- **내부 구성요소 (4가지)**

1) ViewHolder(필수) : 항목 뷰 객체 가짐

2) Adapter(필수) : 각 항목을 구성

3) LayoutManager(필수) : 항목 배치

4) ItemDecoration(선택) : 항목 꾸미기 

- **리사이클러 뷰 사용법**

(1) build.gradle 파일 dependencies 항목에 다음 의존 추가 

```kotlin
implementation ‘androidx.recyclerview:recyclerview:1.2.1’
```

(2) 리사이클러 뷰를 레이아웃 XML 파일에 등록

(3) 각 항목 레이아웃 XML 파일도 필요 

- **목록화면 전체 구성 순서**

(1) **뷰 홀더** 준비

(2) **어댑터**로 뷰 홀더 속 각 항목 뷰 객체에 데이터 대입하여 각 항목 구성

(3) **레이아웃 매니저**는 어댑터가 만든 항목들의 배치 결정

(4) **리사이클러 뷰**에 위의 내용을 최종 출력 

**◾ 1) 각 항목의 규격을 갖는 xml 파일 생성**

  **board_main_recycler_item.xml**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0cb78ae7-e44a-425e-8ef0-988a41c773f4/Untitled.png)

**◾ 2) 각 항목들을 띄울 ‘BoardMainFragment.kt’ 에서 리사이클러 뷰 설정해준 뒤 바인딩 처리** 

 BoardMainFragment.kt

```kotlin
package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app3_communityapp.databinding.BoardMainRecyclerItemBinding
import com.example.app3_communityapp.databinding.FragmentBoardMainBinding

class BoardMainFragment : Fragment() { //'게시판 메인' 프래그먼트
    //바인딩 세팅
    lateinit var boardMainFragmentBinding : FragmentBoardMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardMainFragmentBinding = FragmentBoardMainBinding.inflate(inflater)
        boardMainFragmentBinding.boardMainToolbar.title= "게시판이름"

        //리사이클러뷰 관련 설정
        // 1) 어댑터 객체 생성
        val boardMainRecyclerAdapter = BoardMainRecyclerAdapter()
        boardMainFragmentBinding.boardMainRecycler.adapter= boardMainRecyclerAdapter
        // 2) 레이아웃 매니저로 어댑터로 만든 항목의 레이아웃 배치 설정
        boardMainFragmentBinding.boardMainRecycler.layoutManager= LinearLayoutManager(requireContext())
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
                ViewGroup.LayoutParams.WRAP_CONTENT//세로 길이
            )
            //위의 설정을 레이아웃에 세팅 해준다.
            boardMainRecyclerItemBinding.root.layoutParams= layoutParams
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

            }
        }
    }

}
```

## **최종 모습**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8b95e1ce-ae07-424c-8d7d-4f2569e34080/Untitled.png)

## 🟦 38강. 게시판 목록 메뉴 구성

### ▶️ 게시판 목록 메뉴 구성

- 화면 상단 Toolbar에 메뉴를 적용한다.
- 해당 메뉴 클릭 시, 각 카테고리별 게시판 종류를 선택할 수 있도록 한다.

**◾ 1) Android Resource 파일 - Menu 규격의 xml 파일 생성**

- board_main_menu.xml 생성
- showAsAction 항목 - ‘always’ 지정

![과정1.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d356ef51-57f6-440c-974d-533621a5d3ea/%EA%B3%BC%EC%A0%951.png)

**◾2) 위 메뉴 xml을 BoardMainFragment.kt에서 onCreateView()에 바인딩 처리해준다.** 

- **(1) Toolbar에 메뉴 xml 파일 inflateMenu()로 바인딩 처리**
- **(2) Toolbar에서 사용자가 클릭한 각 항목 메뉴별 이벤트 처리**

      -이를 위해서 변수 임시 선언해뒀다. **임의로** arrayOf()로 항목 데이터 담아뒀다. 

      -추후, 이 부분은 서버와 연동하여 데이터 가져올 부분이다. 

```kotlin
//Menu 항목 (Dialog) 에 들어갈 부분을 임의로 우선 리스트타입으로 담아둠 -
// ---> 서버 연동 후 서버에서 가져올 데이터 부분임
val boardListData =arrayOf(
    "전체글", "게시판1", "게시판2", "게시판3", "게시판4"
)
```

```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

   ...
				//(1) Toolbar 관련 - 메뉴 xml 지정
		        boardMainFragmentBinding.boardMainToolbar.inflateMenu(R.menu.board_main_menu)
        //(2) Toolbar 메뉴 클릭 이벤트 처리
        boardMainFragmentBinding.boardMainToolbar.setOnMenuItemClickListener {

            when(it.itemId) { //사용자 클릭한 메뉴 항목값에 따라 다이얼로그 띄우기
                R.id.board_main_menu_board_list -> {
                    val boardListBuilder = AlertDialog.Builder(requireContext())
                    boardListBuilder.setTitle("게시판 목록")
                    boardListBuilder.setNegativeButton("취소", null)
                    boardListBuilder.setItems(boardListData, null)
                    boardListBuilder.show()
                    true
                }
                else -> false
            }
        }
 
    //리사이클러뷰 관련 설정
   ...
}
```

## **최종 모습**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e4ce6435-4dd1-4c01-be85-f608be9bbc42/Untitled.png)

## 🟦 39강. 게시글 읽기 화면 구성

### ▶️ 게시글 읽기 화면 구성하기

- 게시글 리스트에서 각 항목 선택 시 나타나는 게시글 각각의 읽기 화면을 구성한다.
- Back Button 클릭 시, 게시글 리스트 화면으로 이동한다.

### **🟧 ScrollView 를 사용**

- 읽을 게시글 내용이 많을 수 있기 때문에 스크롤을 내릴 수 있는 View를 컨테이너로 사용한다.

### **🟧 Toolbar에 ‘뒤로가기’ 기능 추가**

BoardMainActivity.kt

- 이 액티비티에서 게시판 관련 프래그먼트들을 관리함
- 따라서, 이 객체 안에 ‘백스택’에서 프래그먼트 제거하는 메소드를 새로 생성해주었다.

```kotlin
// (2) 프래그먼트를 백스택에서 제거하는 메소드
fun fragmentRemoveBackStack(name:String) {
supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE) //프래그먼트 매니저->제거 호출
}
```

◾ BoardReadFragment.kt

- BoardMainActivity 속 프래그먼트 제거 메소드를 사용하여 ‘뒤로가기’ 기능을 구현함

```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    // Inflate the layout for this fragment
  . . . 
    //툴바에 'Back'버튼 아이콘 삽입처리
    val navIcon = requireContext().getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
    boardReadFragmentBinding.boardReadToolbar.navigationIcon= navIcon

    //버전 Q 기준으로. 달라진 툴바의 아이콘 색상 변경 처리 분기
    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
        boardReadFragmentBinding.boardReadToolbar.navigationIcon?.colorFilter= BlendModeColorFilter(
            Color.parseColor("#FFFFFF"), BlendMode.SRC_ATOP)
    }else{
        boardReadFragmentBinding.boardReadToolbar.navigationIcon?.setColorFilter(
            Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
    }

    //백버튼 이벤트 처리 - '뒤로가기' 기능 처리
    boardReadFragmentBinding.boardReadToolbar.setNavigationOnClickListener{
val act =activityas BoardMainActivity
        act.fragmentRemoveBackStack("board_read")
}
return boardReadFragmentBinding.root
}
```

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d52cd273-c7e8-450b-9ac0-b1b9ca04dff7/Untitled.png)

## 🟦 40강. 게시글 작성 화면 구성

### ▶️ 게시글 작성 화면 구성

- **게시글 리스트 화면에서 메뉴 클릭 시, 게시글 작성하는 화면 나타나도록** 한다.
- 여기서는 **이미지 첨부가 가능하도록 구현**할 것이다.

### **🟧 스피너 Spinner란?**

- **스피너 : 값 집합에서 하나의 값을 선택할 수 있는 방법을 제공하는 것.**
- 사용자가 스피너 클릭 시, 사용한 모든 값을 ‘드롭 다운’ 메뉴 형태로 내놓는다. 여기서 사용자가 원하는 값을 선택할 수 있다.
- 여기서는 사용자가 글 작성 시, 게시판 종류를 고를 수 있도록 하기 위해 사용한다.

![최종.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/4d45a486-e9b4-4f3f-837d-0ac4620b0e26/%EC%B5%9C%EC%A2%85.png)

◾BoardWriteFragment.kt

- 1) Spinner 생성해서 어댑터 연결한 뒤 이벤트 처리했다.
- 2) 이 글쓰기 프래그먼트의 툴바에 ‘메뉴’ 배치하여 이벤트 처리 (카메라/갤러리/업로드)
- 3) 만약 사용자가 업로드 버튼을 클릭하게 되면 백스택에 이전 기록을 삭제하기 위하여 fragmentRemoveBackStack() 호출하였다.

     - 만약 사용자가 글 작성 후 업로드 처리하고 ‘뒤로가기’ 클릭 시, 곧장 읽기 화면으로 가도록 하기 위함.

```kotlin
package com.example.app3_communityapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.app3_communityapp.databinding.FragmentBoardWriteBinding

class BoardWriteFragment : Fragment() { //'게시글' 쓰기 프래그먼트 화면

    //바인딩 설정
    lateinit var boardWriteFragmentBinding: FragmentBoardWriteBinding

    val spinner_data =arrayOf("게시판1", "게시판2", "게시판3", "게시판4")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        boardWriteFragmentBinding = FragmentBoardWriteBinding.inflate(inflater)
        //게시글 작성 프래그먼트의 Toolbar 관련 처리
        boardWriteFragmentBinding.boardWriteToolbar.title= "게시글 작성"
        boardWriteFragmentBinding.boardWriteToolbar.inflateMenu(R.menu.board_write_menu)
        boardWriteFragmentBinding.boardWriteToolbar.setOnMenuItemClickListener{
when (it.itemId) {
                R.id.board_write_menu_camera-> {
                    true
                }
                R.id.board_write_menu_gallery-> {
                    true
                }
                R.id.board_write_menu_upload-> { //사용자가 업로드 버튼 클릭 시
                    val act =activityas BoardMainActivity
                    act.fragmentRemoveBackStack("board_write")
                    act.fragmentController("board_read", true, true)
                    true
                }
                else -> false
            }
}
//spinner 어댑터 생성
            val spinnerAdapter = ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, spinner_data
            )

            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            boardWriteFragmentBinding.boardWriteType.adapter= spinnerAdapter

            return boardWriteFragmentBinding.root
	}
}
```

## **🟧**최종 모습

![찐최종.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/a6e5a8da-55f4-4d8a-9541-ea79fdd821ab/%EC%B0%90%EC%B5%9C%EC%A2%85.png)

## 🟦 41강. 글 읽는 화면 메뉴 구성

### ▶️ 게시글 읽기 화면 속 ‘메뉴’ 구성

- 글 읽는 화면의 메뉴를 구성한다.
- ‘수정’과 ‘삭제’ 메뉴를 둘 것
- + 이 메뉴는 해당 글을 작성한 사람에게만 나타나도록 구성.

◾ BoardReadFragment.kt

```kotlin
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
          . . . 

				// -> 툴바 위 메뉴 이벤트 처리 (수정/삭제)
        boardReadFragmentBinding.boardReadToolbar.inflateMenu(R.menu.board_read_menu)
        boardReadFragmentBinding.boardReadToolbar.setOnMenuItemClickListener{
							when(it.itemId) {
							
                R.id.board_read_menu_modify-> { //'수정'하기 클릭 시
                    true
                }
                R.id.board_read_menu_delete-> { //'삭제'하기 클릭 시 
                    val act =activityas BoardMainActivity
                    act.fragmentRemoveBackStack("board_read") //백 스택에 기록 삭제
                    true
                }
                else -> false
            }
}

return boardReadFragmentBinding.root
	}
}
```

## **🟧**최종 모습

![찐.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/aa13abcb-d0e9-4922-8faa-dcfd48dfd0de/%EC%B0%90.png)

## 🟦 42강. 게시글 수정 화면 구성

### ▶️ 게시글 수정 화면 구성

- 글 읽는 화면의 메뉴에서 **‘수정’ 메뉴 클릭 시, → 게시글 수정 화면으로 전환되도록 이벤트 처리**하기
- 수정 화면에는 **이전에 작성한 글 내용이 나타나도록** 한다.

◾ BoardModifyFragment.kt

- 1) Spinner에 임시 배열로 글목록 담아놓고 바인딩 처리하여 화면에 등장시킨다.
- 2) 툴바에 메뉴 담아서 해당 메뉴 클릭 시 이벤트 처리를 한다.

```kotlin
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
    val spinnerData =arrayOf("게시판1", "게시판2", "게시판3", "게시판4")

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
        boardModifyFragmentBinding.boardModifyToolbar.title= "글 수정 "

        //툴바 메뉴 관련 설정
        boardModifyFragmentBinding.boardModifyToolbar.inflateMenu(R.menu.board_modify_menu)
        boardModifyFragmentBinding.boardModifyToolbar.setOnMenuItemClickListener{
when(it.itemId){
                R.id.board_modify_menu_camera-> {
                    true
                }
                R.id.board_modify_menu_gallery-> {
                    true
                }
                R.id.board_modify_menu_upload-> {
                    val act =activityas BoardMainActivity
                    act.fragmentRemoveBackStack("board_modify")
                    true
                }
                else -> false
            }
}

//Spinner 구성
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerData)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        boardModifyFragmentBinding.boardModifyType.adapter= spinnerAdapter

        return boardModifyFragmentBinding.root
}

}
```

## **🟧**최종 모습

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/93d657c5-41ce-47bf-825d-741ddf130eda/Untitled.png)

## 🟦 43강. 서버 프로그래밍 준비

### ▶️ 서버 프로그래밍 개요

- 안드로이드 애플리케이션과 통신할 서버 프로그램 구현 위한 준비 작업 수행
- 서버는 jsp, spring, nodejs, python 등 웹 서비스를 위해 **백 엔드 개발을 할 수 있는 것 중 편한 것 사용**
- 여기서는 jsp를 활용한다.

### **🟧 설치 소프트웨어**

- Java Development Kit : 8버전
- Eclipse
- Apache-Tomcat : 9버전
- MySQL : 데이터베이스

## 🟦 44강. 데이터베이스 생성

### ▶️ 데이터베이스 테이블 구조

**1) user_table : 사용자 회원 정보 테이블**

![회원.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/70e83b3d-c258-4d8b-bf8e-deeaf179c8f6/%ED%9A%8C%EC%9B%90.png)

**2) board_table : 게시판 정보**

![개시판정보.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2afa45ea-98c2-462a-84b9-83532bfffd53/%EA%B0%9C%EC%8B%9C%ED%8C%90%EC%A0%95%EB%B3%B4.png)

**3) content_table : 게시글 내용 정보** 

![s내용.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/adc0f7b0-68f8-4ebf-b824-2de4565faea8/s%EB%82%B4%EC%9A%A9.png)

### **🟧 전체 테이블 구조 관계도**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/657094b1-fe4c-4a67-8b8e-1860b4c2ac3e/Untitled.png)

### **🟧 MySQL 에 Sql 쿼리문 작성**

```sql
create database app3_community_db;

use app3_community_db;

create table user_table(
user_idx int not null primary key auto_increment,
user_id varchar(100) not null unique,
user_pw varchar(100) not null,
user_autologin int not null check(user_autologin in(0, 1)),
user_nick_name varchar(100) not null unique
);

create table board_table(
board_idx int not null primary key auto_increment,
board_name varchar(100) not null unique
);

insert into board_table (board_name) values ("게시판1");
insert into board_table (board_name) values ("게시판2");
insert into board_table (board_name) values ("게시판3");
insert into board_table (board_name) values ("게시판4");

create table content_table(
content_idx int not null primary key auto_increment,
content_board_idx int not null references board_table(board_idx),
content_writer_idx int not null references user_table(user_idx),
content_subject varchar(500) not null,
content_write_date datetime not null,
content_text longtext not null,
content_image varchar(500)
);

commit;
```

## 🟦 45강. 이클립스 설정

### ▶️ 이클립스 설정

- 서버 프로그램 구현을 위해 사용할 Eclipse 기본 설정 수행
- Apache-Tomcat 서버와의 연동 설정을 수행
- 프로젝트를 생성하고 실행 테스트를 수행

## 🟦 46강. OkHttp 라이브러리 사용

### ▶️ OkHttp 라이브러리

- REST API, HTTP 통신을 간편하게 구현할 수 있도록 다양한 기능 제공하는 라이브러리

### **🟧 사용을 위한 세팅**

**1) 뷰 바인딩 설정**

- Module 수준의 build.gradle 파일에 viewBinding 설정 true 준다.

```php
buildFeatures{
viewBinding = true
}
```

**2) OkHttp라이브러리 사용을 위해 dependencies에 의존 추가한다.**

```php
implementation 'com.squareup.okhttp3:okhttp:4.9.0'
```

**3) 네트워크 사용을 위해 ‘인터넷 권한’을 추가한다.**

◾ AndroidManifest.xml 

`<uses-permission android:name = "android.permission.INTERNET"/>`

**4) AndroidManifest.xml 에 다음을 추가**

- Http 사용 시, 보안 문제 때문에 다음을 추가한다.

`android:usesCleartextTraffic="true"`

**5) 네트워크 관련 처리는 반드시 ‘쓰레드’로 동작 처리 필수**

◾ MainActivity.kt 

```kotlin
package com.example.okhttpapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.okhttpapplication.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() { //'메인' 액티비티

    //뷰 바인딩 설정
    lateinit var mainActivityBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //뷰 바인딩 설정
        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        //버튼 이벤트 처리
        mainActivityBinding.connectBtn.setOnClickListener{
thread{//쓰레드로 동작해야 네트워크 관련 처리 가능
               //localhost 부분에 서버 Ip 주소 담기
               val site = "http://172.30.1.9:8080/App3_CommunityServer/test.jsp"

               //okHttp 객체
               val client = OkHttpClient()

               val request = Request.Builder().url(site).get().build()
               val response = client.newCall(request).execute() //접속됨

               // 만약 서버와 통신 성공 시
               if(response.isSuccessful == true) {
                   val result = response.body?.string() //서버로부터 받은 데이터를 받아올 수 있다.
                   runOnUiThread{
mainActivityBinding.resultText.text= result
}
}
}
        }
}
}
```

**→ 여기서 서버 연동할 site 주소 속 localhost는 자신의 컴퓨터 ip 주소로 대체해야 한다.**

**→ 명령 프롬포트에서 ipconfig 명령어 입력 시 등장하는 ip 주소 가져올 것** 

![명령 아잉피 주소.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/eee5614a-0871-4cea-aa89-0e6e61215531/%EB%AA%85%EB%A0%B9_%EC%95%84%EC%9E%89%ED%94%BC_%EC%A3%BC%EC%86%8C.png)

### **🟧 최종 모습**

**1) 서버 위에 올라간 test.jsp 파일 속 내용물**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/99bc964a-f571-4ccb-8641-f3ffbaf9a885/Untitled.png)

**2) 위 url 주소에 ip주소 혼합시켜서 ‘안드로이드 앱’에 데이터 가져옴**

- 사용자가 버튼 클릭 시, 가져올 수 있도록 이벤트 처리되어 있다.
    
    ![찐최종.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/11cc2f90-93b5-4777-bdd1-930337299755/%EC%B0%90%EC%B5%9C%EC%A2%85.png)
    

## 🟦 47강. 회원 가입 기본 기능 구현

### ▶️ 회원 가입 기본 기능

- 회원 가입 화면에서 **아이디/비번/닉네임에 대한 유효성 검사 코드를 작성**해준다.
- 우선, **입력하지 않았을 경우의 유효성 검사**를 먼저 처리한다.

### **🟧 1) MainActivity.kt**

- 사용자 입력값 id/pw 를 담을 변수를 미리 선언해둔다.

```kotlin
//사용자 정보 담을 변수 선언 - 여기에 데이터 담을 예정
var userId = ""
var userPw = ""
var userNickName = ""
```

### **🟧 2) JoinFragment.kt**

- 우선 무입력 상태에서 ‘유효성 검사’ 를 실시해야 한다.
- JoinFragment 뷰에서 사용자가 입력한 값들을 toString() 형태로 받아두고,
- 사용자가 ID/PW 입력 없이 ‘회원가입’ 버튼 클릭할 경우,
    
    유효성 검사를 거친 뒤 → DIalog 알림을 띄우고 해당 입력란에 Focus()를 마치도록 이벤트 처리를 해둔다.
    

```kotlin
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
        joinFragmentBinding.joinToolbar.title= "회원가입"

        // 회원가입 화면에서 '다음' 버튼 클릭 시 -> 닉네임 설정 화면으로 전환 이벤트 처리
        joinFragmentBinding.joinNextBtn.setOnClickListener{
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
               dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
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
                dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
joinFragmentBinding.joinPw.requestFocus()
}
dialogBuilder.show()
                return@setOnClickListener
            }

            //화면 전환 -> 닉네임 설정 화면
            val act =activityas MainActivity

            //가입화면에서 id/pw 입력 후 전환된 닉네임 입력 프래그먼트에서는 이 데이터값을 서버에 전달할 필요가 있다.
            //따라서 여기서 데이터를 임시로 한 번 담아준다.
            act.userId = joinId
            act.userPw = joinPw

            act.fragmentController("nick_name", true, true)
}

return joinFragmentBinding.root
}

}
```

### **🟧 3) NickNameFragment.kt**

- 사용자가 닉네임 입력하지 않고 확인 누르면, 유효성 검사를 거쳐서 알림이 뜨고 입력란에 자동 포커싱하도록 이벤트 처리.

```kotlin
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
        nickNameFragmentBinding.nicknameToolbar.title= "닉네임 설정"
        // 백 스택에 저장된 프래그먼트들을 완전히 종료하기 위함
        // 로그인 시도가 완료된 뒤에는 '뒤로가기' 를 눌러도 액티비티가 종료되어야 한다.
        // -> 닉네임 설정 버튼 클릭 시 -> 새로운 주기를 갖는 메인 액티비티를 다시 띄워주어야 함
        nickNameFragmentBinding.nicknameJoinBtn.setOnClickListener{
//사용자 입력값 가져오기
            val nickNameNickName = nickNameFragmentBinding.nicknameNickname.text.toString()
            //유효성 검사
            if(nickNameNickName == null || nickNameNickName.length == 0) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("닉네임 입력 오류")
                dialogBuilder.setMessage("닉네임을 입력해주세요")
                dialogBuilder.setPositiveButton("확인"){dialogInterface: DialogInterface, i: Int->
nickNameFragmentBinding.nicknameNickname.requestFocus()
}
dialogBuilder.show()
                return@setOnClickListener
            }

            //사용자가 여기서 입력한 '닉네임' 데이터도 다시 서버로 보낼 용도로 여기서 담아준다.
            val act =activityas MainActivity
            act.userNickName = nickNameNickName

            Log.d("test", "${act.userId}")
            Log.d("test", "${act.userPw}")
            Log.d("test", "${act.userNickName}")

            val mainIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(mainIntent)
activity?.finish() //기존 액티비티 종료
}

return nickNameFragmentBinding.root
}

}
```

- 또한, 여기서 입력한 닉네임값과 앞서 입력하여 넘겨받은 ID/PW 값은 서버와 DB에도 보내야 하기 때문에 임시로 담아두고 Log.d로 잘 담겼는지 확인을 거친다.
    
    ![로그 확인.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/ab574867-ef18-4950-9628-8a7a50ea653a/%EB%A1%9C%EA%B7%B8_%ED%99%95%EC%9D%B8.png)
    

### **🟧 최종 실행 - ID/PW 관련**

![회원가입 관련 최종.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3e100727-0605-443a-8973-dfa47ba559e1/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85_%EA%B4%80%EB%A0%A8_%EC%B5%9C%EC%A2%85.png)

 ****

### **🟧 최종 실행 - 닉네임 관련**
