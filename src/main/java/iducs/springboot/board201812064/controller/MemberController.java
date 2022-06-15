package iducs.springboot.board201812064.controller;

import iducs.springboot.board201812064.domain.Member;
import iducs.springboot.board201812064.domain.PageRequestDTO;
import iducs.springboot.board201812064.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    // 생성자 주입 : (Constructor Injection) vs. @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/regform")
    public String getRegform(Model model){
        // 정보를 전달받을 객체를 보냄
        model.addAttribute("member", Member.builder().build());
        return "/members/regform";
    }

    @PostMapping("")
    public String postMember(@ModelAttribute("member") Member member, Model model){
        memberService.create(member);
        return "redirect:/";
        //return "/members/"+ member.getClass() +"/upform";
    }

    @GetMapping("/{idx}/upform")
    public String getUpform(@PathVariable("idx") Long seq, Model model){
        // 정보를 전달받을 객체를 보냄
        Member member = memberService.readById(seq);
        model.addAttribute("member", member);
        return "/members/upform"; // view resolving : upform.html
    }

    @GetMapping("/{idx}")
    public String getMember(@PathVariable("idx") Long seq, Model model){
        // 정보를 전달받을 객체를 보냄
        Member member = memberService.readById(seq);
        model.addAttribute("member", member);
        //return "/members/member"; // view resolving : upform.html
        return "/members/contacts";
    }

    @GetMapping("")
    public String getMembers(PageRequestDTO pageRequestDTO, Model model){
        //List<Member> members = memberService.readAll(); // domain 의 리스트, DTO 의 리스트
        //List<MemoEntity> memos = memoService.findAll();
        model.addAttribute("list", memberService.readListBy(pageRequestDTO));
        //return "/members/members";
        return "/members/members";
    }

    @PutMapping("/{idx}")
    public String putMember(@ModelAttribute("member") Member member, Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값
        memberService.update(member);
        model.addAttribute(member);
        return "/members/contacts"; // view resolving : update info 확인
    }

    @GetMapping("/{idx}/delform")
    public String getDelform(@PathVariable("idx") Long seq, Model model){
        // 정보를 전달받을 객체를 보냄
        Member member = memberService.readById(seq);
        model.addAttribute("member", member);
        return "/members/delform"; // view resolving : upform.html
    }

    @DeleteMapping("/{idx}")
    public String deleteMember(@ModelAttribute("member") Member member, Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값
        memberService.delete(member);
        model.addAttribute(member);
        return "redirect:/members"; // /members 요청을 함, view resolving 대신
    }

    @GetMapping("/login")
    public String getLoginform(Model model) {
        model.addAttribute("member", Member.builder().build());
        return "/members/login"; // view resolving
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("member") Member member, HttpServletRequest request) {
        Member dto = null;
        if((dto = memberService.loginByEmail(member)) != null) {
            HttpSession session = request.getSession();
            session.setAttribute("login", dto);
            if(dto.getId().contains("admin"))
                session.setAttribute("isadmin", dto.getId());
            return "redirect:/";
        }
        else
            return "/members/loginfail";

    }
    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/"; // view resolving
    }
}
