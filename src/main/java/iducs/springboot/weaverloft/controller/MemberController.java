package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.service.MemberService;
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
        model.addAttribute("member", MemberDTO.builder().build());
        return "/members/regform";
    }

    @PostMapping("")
    public String postMember(@ModelAttribute("member") MemberDTO memberDTO, Model model){
        memberService.create(memberDTO);
        return "redirect:/";
        //return "/members/"+ member.getClass() +"/upform";
    }

    @GetMapping("/{idx}/upform")
    public String getUpform(@PathVariable("idx") Long seq, Model model){
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(seq);
        model.addAttribute("member", memberDTO);
        return "/members/upform"; // view resolving : upform.html
    }

    @GetMapping("/{idx}")
    public String getMember(@PathVariable("idx") Long seq, Model model){
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(seq);
        model.addAttribute("member", memberDTO);
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
    public String putMember(@ModelAttribute("member") MemberDTO memberDTO, Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값
        memberService.update(memberDTO);
        model.addAttribute(memberDTO);
        return "/members/contacts"; // view resolving : update info 확인
    }

    @GetMapping("/{idx}/delform")
    public String getDelform(@PathVariable("idx") Long seq, Model model){
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(seq);
        model.addAttribute("member", memberDTO);
        return "/members/delform"; // view resolving : upform.html
    }

    @DeleteMapping("/{idx}") //삭제 구현
    public String deleteMember(@ModelAttribute("member") MemberDTO memberDTO, Model model, HttpSession session,
                               Long seq){
        memberService.removeWithBoards(memberService.readById(seq).getSeq());
        memberService.delete(memberDTO);
        model.addAttribute(memberDTO);
        session.invalidate();
        return "redirect:/"; //'/members' 요청을 함,
    }

    @GetMapping("/login")
    public String getLoginform(Model model) {
        model.addAttribute("member", MemberDTO.builder().build());
        return "/members/login"; // view resolving
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("member") MemberDTO memberDTO, HttpServletRequest request) {
        MemberDTO dto = null;
        if((dto = memberService.loginByEmail(memberDTO)) != null) {
            HttpSession session = request.getSession();
            session.setAttribute("login", dto);
            session.setAttribute("block",dto.getBlock());
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
