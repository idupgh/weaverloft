package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

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
        model.addAttribute("memberDTO", MemberDTO.builder().build());
        return "/members/regform";
    }

    @PostMapping("")
    public String postMember(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "/members/regform";
        }
        try{
            memberService.create(memberDTO);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/members/regform";
        }

        return "redirect:/";
        //return "/members/"+ member.getClass() +"/upform";
    }

    @GetMapping("/{idx}/upform")
    public String getUpform(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if((session.getAttribute("isadmin") == null) && (session.getAttribute("loginSeq") != memberDTO.getId())) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 개인정보만 수정 할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
        }
        return "/members/upform"; // view resolving : upform.html
    }

    @GetMapping("/{idx}")
    public String getMember(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if((session.getAttribute("isadmin") == null) && (session.getAttribute("loginSeq") != memberDTO.getId())) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 개인정보만 볼 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
        }
        return "/members/contacts";
    }

    @GetMapping("/list")
    public String getMembers(PageRequestDTO pageRequestDTO, Model model){
        //List<Member> members = memberService.readAll(); // domain 의 리스트, DTO 의 리스트
        //List<MemoEntity> memos = memoService.findAll();
        model.addAttribute("list", memberService.readListBy(pageRequestDTO));
        //return "/members/members";
        return "/members/members";
    }

    @PutMapping("/{idx}")
    public String putMember(@ModelAttribute("memberDTO") MemberDTO memberDTO, Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값
        memberService.update(memberDTO);
        model.addAttribute(memberDTO);
        return "/members/contacts"; // view resolving : update info 확인
    }

    @GetMapping("/{idx}/delform")
    public String getDelform(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if((session.getAttribute("isadmin") == null) && (session.getAttribute("loginSeq") != memberDTO.getId())) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 개인정보만 삭제 할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
        }
        return "/members/delform"; // view resolving : upform.html
    }

    @DeleteMapping("/{idx}") //삭제 구현
    public String deleteMember(@ModelAttribute("memberDTO") MemberDTO memberDTO, Model model, HttpSession session,
                               String id){
        memberService.removeWithBoards(memberService.readById(id).getId());
        memberService.deleteMember(memberDTO.getId());
        //memberService.delete(memberDTO); > DB 에서 삭제
        model.addAttribute(memberDTO);
        session.invalidate();
        return "redirect:/"; //'/members' 요청을 함,
    }

    @GetMapping("/login")
    public String getLoginform(Model model) {
        model.addAttribute("memberDTO", MemberDTO.builder().build());
        return "/members/login"; // view resolving
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("memberDTO") MemberDTO memberDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MemberDTO dto = null;
        MemberDTO deletedto = null;
        if(((dto = memberService.loginByEmail(memberDTO)) != null)) {
            HttpSession session = request.getSession();
            session.setAttribute("login", dto);
            session.setAttribute("loginSeq", dto.getId());
            session.setAttribute("block",dto.getBlock());
            if(dto.getId().contains("admin")) // ID > ROLE 변경 예정
                session.setAttribute("isadmin", dto.getId());
            return "redirect:/";
        }
        else {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('없는 아이디 입니다.'); location.href='/members/login';</script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기

            return "/members/loginfail";
        }
    }
    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/"; // view resolving
    }
}
