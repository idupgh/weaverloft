package iducs.springboot.weaverloft.controller;

import iducs.springboot.weaverloft.domain.ExcelData;
import iducs.springboot.weaverloft.domain.MemberDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.service.MemberService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        // *주의* BindingResult 를 검증할 객체 바로 뒤에 사용하여야 한다.
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

    @GetMapping("/{idx}")
    public String getMember(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if(!(session.getAttribute("loginid").equals(memberDTO.getId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/members/contacts";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 개인정보만 볼 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 개인정보만 볼 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/members/contacts";
    }

    @GetMapping("/list")
    public String getMembers(PageRequestDTO pageRequestDTO, Model model){
        //List<Member> members = memberService.readAll(); // domain 의 리스트, DTO 의 리스트
        //List<MemoEntity> memos = memoService.findAll();
        model.addAttribute("list", memberService.readListBy(pageRequestDTO));
        model.addAttribute("count",memberService.readListBy(pageRequestDTO).getTotalSize());
        //return "/members/members";
        return "/members/members";
    }

    @GetMapping("/upform/{idx}")
    public String getUpform(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if(!(session.getAttribute("loginid").equals(memberDTO.getId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/members/upform";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 개인정보만 수정할 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 개인정보만 수정할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/members/upform"; // view resolving : upform.html
    }

    @GetMapping("/pwupform/{idx}")
    public String getPwUpform(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if(!(session.getAttribute("loginid").equals(memberDTO.getId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/members/pwupform";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 비밀번호만 수정할 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 비밀번호만 수정할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/members/pwupform"; // view resolving : upform.html
    }
    @GetMapping("/pwupform2/{idx}")
    public String getPwUpform2(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if(!(session.getAttribute("loginid").equals(memberDTO.getId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/members/pwupform2";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 비밀번호만 수정할 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 비밀번호만 수정할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/members/pwupform2"; // view resolving : upform.html
    }

    @PutMapping("/update/{idx}")
    public String putMember(@Valid @ModelAttribute("memberDTO") MemberDTO memberDTO, BindingResult bindingResult ,Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값

        if(bindingResult.hasErrors()){
            return "/members/upform";
        }
        try{
            memberService.update(memberDTO);
            model.addAttribute(memberDTO);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/members/upform";
        }

        return "/members/contacts"; // view resolving : update info 확인
    }

    @PutMapping("/pwupdate/{idx}")
    public String putMemberPw(@Valid @ModelAttribute("memberDTO") MemberDTO memberDTO, BindingResult bindingResult ,Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값

        if(bindingResult.hasErrors()){
            return "/members/pwupform";
        }
        try{
            memberService.pwupdate(memberDTO);
            model.addAttribute(memberDTO);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/members/pwupform";
        }

        return "/members/contacts"; // view resolving : update info 확인
    }

    @PutMapping("/pwupdate2/{idx}")
    public String putMemberPw2(@ModelAttribute("memberDTO") MemberDTO memberDTO, BindingResult bindingResult ,Model model) {
        // HTML 에서 전달된 model 객체를 전달 받음 : member 라는 애트리뷰트 명 th:object 애트리뷰트 값
        memberService.dateupdate(memberDTO);
        model.addAttribute(memberDTO);

        return "/members/contacts"; // view resolving : update info 확인
    }

    @GetMapping("/{idx}/delform")
    public String getDelform(@PathVariable("idx") String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {
        // 정보를 전달받을 객체를 보냄
        MemberDTO memberDTO = memberService.readById(id);
        model.addAttribute("memberDTO", memberDTO);
        if(!(session.getAttribute("loginid").equals(memberDTO.getId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/members/delform";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 개인정보만 삭제할 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 개인정보만 삭제할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/members/delform"; // view resolving : upform.html
    }

    @DeleteMapping("/{idx}") //삭제 구현
    public String deleteMember(@ModelAttribute("memberDTO") MemberDTO memberDTO, Model model, HttpSession session,
                               String id){
        memberService.removeWithBoards(memberService.readById(id).getId());
        memberService.deleteMember(memberDTO.getId());
        //memberService.delete(memberDTO); > DB 에서 삭제
        model.addAttribute(memberDTO);
        if(session.getAttribute("isadmin") == null) {
            session.invalidate();
        }
        return "redirect:/"; //'/members' 요청을 함,
    }

    @GetMapping("/login")
    public String getLoginform(Model model) {
        model.addAttribute("memberDTO", MemberDTO.builder().build());
        return "/members/login"; // view resolving
    }
    @PostMapping("/login")
    public String postLogin(@ModelAttribute("memberDTO") MemberDTO memberDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        MemberDTO deletedto = null;

        // 만약 로그인 한 날짜가 upDate 보다

        if(((memberService.loginById(memberDTO)) == null)) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('정보가 올바르지 않습니다.'); location.href='/members/login';</script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기

            return "/members/loginfail";
        }
        else if (Objects.equals(memberService.loginById(memberDTO).getDelete_yn(), String.valueOf('y'))) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('탈퇴한 사용자 입니다.'); location.href='/';</script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기

            return "/";
        }
        else if(((memberDTO = memberService.loginById(memberDTO)) != null)) {
            String id = memberDTO.getId();
            HttpSession session = request.getSession();
            session.setAttribute("login", memberDTO);
            session.setAttribute("loginid", memberDTO.getId());
            session.setAttribute("block",memberDTO.getBlock());
            session.setAttribute("delete",memberDTO.getDelete_yn());
            LocalDateTime lastDate = memberDTO.getPwUpdateDate(); // 비밀번호 변경한 날짜
            LocalDateTime upDate = lastDate.plusDays(90); // 그로부터 90일 이후
            LocalDateTime toDay = LocalDateTime.now(); // 로그인한 날짜
            if(memberDTO.getRole().contains("admin")) // ID > ROLE 변경 예정
                session.setAttribute("isadmin", memberDTO.getId());
            if(upDate.isBefore(toDay)){
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('비밀번호 변경 대상자 입니다');location.href='/members/pwupform2/"+ id +"';</script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            return "redirect:/";
        }
        return "redirect:/";
    }
    @GetMapping("/logout")
    public String getLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/"; // view resolving
    }

    //Excel
    @GetMapping("/excel/download")
    public void excelDownload(HttpServletResponse response, MemberDTO memberDTO, PageRequestDTO pageRequestDTO) throws IOException {
//        Workbook wb = new HSSFWorkbook();
        pageRequestDTO.setSize((int) memberService.readListBy(pageRequestDTO).getTotalSize());
        List<MemberDTO> members = memberService.readListBy(pageRequestDTO).getDtoList(); // domain 의 리스트, DTO 의 리스트
        int count = pageRequestDTO.getSize();
        int j = pageRequestDTO.getPage();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("첫번째 시트");
        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        sheet.setColumnWidth(0, 3500); // cell 넓이 설정
        sheet.setColumnWidth(1, 3500);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 8000);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        // Header
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("Id");
        cell = row.createCell(1);
        cell.setCellValue("이름");
        cell = row.createCell(2);
        cell.setCellValue("Email");
        cell = row.createCell(3);
        cell.setCellValue("주소");
        cell = row.createCell(4);
        cell.setCellValue("탈퇴여부");

        // Body

        for (int i = 0; i < count; i++) {
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(members.get(i).getId());
            cell = row.createCell(1);
            cell.setCellValue(members.get(i).getName());
            cell = row.createCell(2);
            cell.setCellValue(members.get(i).getEmail());
            cell = row.createCell(3);
            cell.setCellValue(members.get(i).getAddress());
            cell = row.createCell(4);
            cell.setCellValue(members.get(i).getDelete_yn());
        }


        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
//        response.setHeader("Content-Disposition", "attachment;filename=example.xls");
        String fileName = "회원목록" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 엑셀 다운로드시 엑셀 이름 한글 깨짐 처리
        String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");
        response.setHeader("Content-Disposition", "attachment;filename=" + outputFileName + ".xlsx");
        response.setStatus(200);

        // Excel File Output
        wb.write(response.getOutputStream());
        wb.close();
    }

    @PostMapping("/excel/read")
    public String readExcel(@RequestParam("file") MultipartFile file, Model model) throws IOException {

//        ExcelData dataList = Excel 값 담을 리스트
//        MemeberDTO memberDTOList = 모든 멤버 리스트
//        Object memberid = 비교를 위한 멤버 형변환 리스트
//        String dataid = 비교를 위한 Excel 형변환 리스트
//        Object result = 멤버 형변환 리스트와 Excel 형변환 리스트의 교집합 리스트
//        MembetDTO resultid = 교집합 리스트를 MemberDTO로 전환


        List<ExcelData> dataList = new ArrayList<>();
        // 모든 멤버 리스트
        List<MemberDTO> memberDTOList = memberService.readAll();
        // 비교하기 위해 변환
        List<Object> memberid = memberDTOList.stream().map(MemberDTO::getId).collect(Collectors.toCollection(ArrayList::new));



        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

            Row row = worksheet.getRow(i);

            ExcelData data = new ExcelData();

            data.setId(row.getCell(0).getStringCellValue());

            dataList.add(data); // 엑셀 데이터를 리스트로
        }
        List<String> dataid = dataList.stream().map(ExcelData::getId).collect(Collectors.toCollection(ArrayList::new));
        // 비교하기 위해 변환

        model.addAttribute("datas", dataList); //
        // -------------------------excel 파일 데이터 리스트

        Set<Object> result = new HashSet<>(memberid);
        result.retainAll(dataid); // 공통요소만 남기고 제거
        List<Object> commonList = new ArrayList<>(result); // List 로 변환
        List<MemberDTO> resultid = (List<MemberDTO>) (Object) commonList; // MemberDTO로 변환 >> 멤버리스트와 엑셀 리스트의 공통 리스트
        // 실패목록 리스트
        List<String> dupfailList = new ArrayList<>(); // 중복되서 실패

        Set<Object> subResult = new HashSet<>(dataid);
        subResult.removeAll(result);
        List<Object> subList = new ArrayList<>(subResult); // 없는 아이디여서 실패
        List<String> nofailList = (List<String>) (Object) subList;

        int succount = 0;

        for(int i = 0; i< memberDTOList.size(); i++) {
            for(int j = 0; j<result.size(); j++) {
                if (memberDTOList.get(i).getId().contains((CharSequence) resultid.get(j))){
                    //newList.add(memberService.readById((String) result.get(j)));
                    MemberDTO memberDTO = memberService.readById(String.valueOf(resultid.get(j)));
                    if(!memberDTO.getDelete_yn().equals("y")){
                        memberDTO.setDelete_yn("y");
                        memberService.update(memberDTO);
                        succount++;
                    }else{
                        dupfailList.add(memberDTO.getId()); // 목록중에 중복되서 실패한 리스트
                    }
                }

            }
//            resultid.add(memberService.readById(resultid.get(i).getId()));
        }
        nofailList.removeAll(dupfailList);
        int dulcount = resultid.size()-succount;
        model.addAttribute("allcount", dataList.size());
        model.addAttribute("succount", succount);
        model.addAttribute("facount", dataList.size() - succount);

//        for(int i = 0; i < result.size(); i++) {
//            MemberDTO memberDTO = memberService.readById(resultid.get(i).getId());
//            memberDTO.setDelete_yn("y");
//            memberService.update(memberDTO);
//        }

        String strdupfailList = String.join(",", dupfailList);
        String strnofailList = String.join(",", nofailList);

//        return "redirect:/members/list";
        return "redirect:/members/list?allcount="+dataList.size()+"&succount="+succount+"&facount="+(dataList.size() - succount)+"&dulcount="+dulcount+"&dupfailList="+strdupfailList+"&nofailList="+strnofailList;

    }
}
