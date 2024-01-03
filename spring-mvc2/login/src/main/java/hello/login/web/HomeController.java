package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {

        return "home";
    }

    @GetMapping("/") //로그인 처리 후 화면 : 쿠키를 받아와서 로그인 이후 화면을 보여주게된다
    public String homeLogin(@CookieValue(name = "memberId", required = false/*로그인 안한 사용자도 들어와야하니까*/)Long memberId,
                            Model model){ //Cookie값은 String인데 id의 값이 Long인 경우 스프링의 타입컨버팅으로 해결된다. 추후 설명

        if(memberId == null){
            return "home";
        }

        //로그인한 사용자
        Member loginMember = memberRepository.findById(memberId);

        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}