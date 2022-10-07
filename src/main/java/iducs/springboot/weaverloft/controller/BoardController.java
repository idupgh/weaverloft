package iducs.springboot.weaverloft.controller;



import iducs.springboot.weaverloft.domain.*;
import iducs.springboot.weaverloft.service.BoardService;
import iducs.springboot.weaverloft.service.FileService;
import iducs.springboot.weaverloft.service.ReplyService;
import iducs.springboot.weaverloft.util.MD5Generator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/boards")
public class BoardController {
    // 생성자 주입 : Spring Framework <- Autowired (필드 주입)
    private final BoardService boardService;
    private final FileService fileService;
    private final ReplyService replyService;
    public BoardController(BoardService boardService, ReplyService replyService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
        this.replyService = replyService;
    }

    @GetMapping("/regform")
    public String getRegform(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("boardDTO", BoardDTO.builder().build()); // 빈 Board 객체 생성
        return "/boards/regform"; // boards/regform.html 전달
    }

    // 게시글 등록
    @PostMapping("")
    public String post(@RequestParam(value = "file",required = false)List<MultipartFile> files, @Valid BoardDTO boardDTO, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            return "/boards/regform";
        }
        try{
            // 원래 있던 부분
            long bno = boardService.register(boardDTO);

            try {
                for(MultipartFile file : files) {
                    String origFilename = file.getOriginalFilename();
                    if(!origFilename.isEmpty()){
                        String uuid = UUID.randomUUID().toString();
                        String[] filenameArray = origFilename.split("\\.");
                        String filename = new MD5Generator(uuid + filenameArray[0]).toString() + "." + filenameArray[1];

                        /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
                        String savePath = (file.getContentType().startsWith("image") == true) ? "C:\\Users\\GeunHyeong\\IdeaProjects\\weaverloft\\src\\main\\resources\\static\\images" : System.getProperty("user.dir") + "\\files";
                        /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                        if (!new File(savePath).exists()) {
                            try {
                                new File(savePath).mkdir();
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                        }
                        String filePath = savePath + "\\" + filename;
                        file.transferTo(new File(filePath));

                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setOrigFilename(origFilename);
                        fileDTO.setFilename(filename);
                        fileDTO.setFilePath(filePath);
                        fileDTO.setBno(bno);
                        model.addAttribute("filename", filename);
                        fileService.saveFile(fileDTO);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } // 원래 있던 부분 종점
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/boards/regform";
        }
        return "redirect:/boards/"; // 등록 후 상세보기
        // return "redirect:/boards/" + bno; >> 등록 후 바로 상세보기 띄우기였음
    }

    @GetMapping("")
    public String getList(PageRequestDTO pageRequestDTO, Model model, Long bno, HttpServletResponse response) throws IOException {


        model.addAttribute("list", boardService.getList(pageRequestDTO));
        model.addAttribute("count", boardService.getList(pageRequestDTO).getTotalSize());

        return "/boards/list"; // boards/list.html 전달
    }

    // 조회수
    @GetMapping("/{bno}")
    public String getBoard(@PathVariable("bno") Long bno, Model model) {

        BoardDTO boardDTO = boardService.getById(bno);
        boardService.updateView(bno);

        model.addAttribute("fileList", fileService.getList(bno));
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("replyList", replyService.getList(bno));

        return "/boards/read";
    }

    @GetMapping("/update/{bno}") //업데이트폼
    public String getUpform(@PathVariable("bno") Long bno, Model model, HttpSession session, HttpServletResponse response)
            throws IOException {
        BoardDTO boardDTO = boardService.getById(bno);

        model.addAttribute("fileList", fileService.getList(bno));
        model.addAttribute("boardDTO", boardDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님

        if(!(session.getAttribute("loginid").equals(boardDTO.getWriterId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/boards/upform";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 작성한 글만 수정할 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 작성한 글만 수정할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/boards/upform"; // view resolving : upform.html
    }

    @PutMapping("/update/{bno}") //업데이트 구현
    public String putBoard(@RequestParam(value = "file", required = false)
                               List<MultipartFile> files, @RequestParam(value = "deleteFileId", required = false)
            String deleteFileId, Long bno,@Valid BoardDTO boardDTO, BindingResult bindingResult, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)

        Long id = boardDTO.getFileId();

        if(bindingResult.hasErrors()){
            return "/boards/upform";
        }
        try{
            try {
                if(!deleteFileId.equals("")){
                    String[] strArr = deleteFileId.split(",");
                    for(int i=0; i < strArr.length; i++){
                        Long delid = Long.parseLong(strArr[i]);
                        fileService.deleteFile(delid, bno);
                    }
                }

            }catch (Exception e) {

                e.printStackTrace();
            }

            try {

                for(MultipartFile file : files) {
                    String origFilename = file.getOriginalFilename();
                    if(!origFilename.isEmpty()){
                        String[] filenameArray = origFilename.split("\\.");
                        String filename = new MD5Generator(filenameArray[0]).toString() + "." + filenameArray[1];

                        /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
                        String savePath = (file.getContentType().startsWith("image") == true) ? "C:\\Users\\GeunHyeong\\IdeaProjects\\weaverloft\\src\\main\\resources\\static\\images" : System.getProperty("user.dir") + "\\files";
                        /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                        if (!new File(savePath).exists()) {
                            try {
                                new File(savePath).mkdir();
                            } catch (Exception e) {
                                e.getStackTrace();
                            }
                        }
                        String filePath = savePath + "\\" + filename;
                        file.transferTo(new File(filePath));

                        FileDTO fileDTO = new FileDTO();
                        fileDTO.setOrigFilename(origFilename);
                        fileDTO.setFilename(filename);
                        fileDTO.setFilePath(filePath);
                        fileDTO.setBno(bno);
                        model.addAttribute("filename", filename);
                        fileService.saveFile(fileDTO);
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
            // 원래 있던 부분 종점
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "/boards/upform";
        } boardService.update(bno, boardDTO);
        return "redirect:/boards";
    }

    @GetMapping("/{bno}/delform") //삭제폼
    public String getDelform(@ModelAttribute("bno") Long bno, Model model, HttpSession session, HttpServletResponse response)
            throws IOException {
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        BoardDTO boardDTO = boardService.getById(bno);
        model.addAttribute("boardDTO", boardDTO);
        if(!(session.getAttribute("loginid").equals(boardDTO.getWriterId()))) {
            if(session.getAttribute("isadmin") != null) {
                return "/boards/delform";
            } else {
                response.setContentType("text/html; charset=utf-8");
                PrintWriter out = response.getWriter();
                String element =
                        "<script> alert('자신의 작성한 글만 삭제할 수 있습니다.'); location.href='/'; </script>";
                out.println(element);
                out.flush();//브라우저 출력 비우기
                out.close();//아웃객체 닫기
            }
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('자신의 작성한 글만 삭제할 수 있습니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
            return "/";
        } else
            return "/boards/delform"; // view resolving : upform.html

    }
    @DeleteMapping("/{bno}") //삭제 구현
    public String deleteMember(@PathVariable Long bno){
        boardService.deleteWithReplies(bno);
        return "redirect:/boards"; //'/members' 요청을 함,
    }

    // 파일 다운로드
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDTO fileDTO = fileService.getFile(fileId);
        Path path = Paths.get(fileDTO.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getOrigFilename() + "\"")
                .body(resource);
    }

    // 파일 삭제
    @DeleteMapping("/{bno}/delete/{id}")
    public void deleteFile(@PathVariable("id") Long id, @PathVariable("bno") Long bno){
        fileService.deleteFile(id, bno);
    }
}