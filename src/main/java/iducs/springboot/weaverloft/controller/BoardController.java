package iducs.springboot.weaverloft.controller;



import iducs.springboot.weaverloft.domain.BoardDTO;
import iducs.springboot.weaverloft.domain.FileDTO;
import iducs.springboot.weaverloft.domain.PageRequestDTO;
import iducs.springboot.weaverloft.entity.FileEntity;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/boards")
public class BoardController {
    // 생성자 주입 : Spring Framework <- Autowired (필드 주입)
    private final BoardService boardService;
    private final FileService fileService;
    public BoardController(BoardService boardService, ReplyService replyService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    @GetMapping("/regform")
    public String getRegform(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("boardDTO", BoardDTO.builder().build()); // 빈 Board 객체 생성
        return "/boards/regform"; // boards/regform.html 전달
    }

    @PostMapping("")
    public String post(@RequestParam("file")MultipartFile files, BoardDTO boardDTO, Model model) {

        try {
            String origFilename = files.getOriginalFilename();
            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "\\files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            FileDTO fileDTO = new FileDTO();
            fileDTO.setOrigFilename(origFilename);
            fileDTO.setFilename(filename);
            fileDTO.setFilePath(filePath);

            model.addAttribute("filename", filename);

            Long fileId = fileService.saveFile(fileDTO);
            boardDTO.setFileId(fileId);
            boardService.register(boardDTO);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "redirect:/boards/"; // 등록 후 상세보기
        // return "redirect:/boards/" + bno; >> 등록 후 바로 상세보기 띄우기였음
    }

    @GetMapping("")
    public String getList(PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("list", boardService.getList(pageRequestDTO));
        return "/boards/list"; // boards/list.html 전달
    }

    // 조회수
    @GetMapping("/{bno}")
    public String getBoard(@PathVariable("bno") Long bno, Model model) {

        BoardDTO boardDTO = boardService.getById(bno);
        boardService.updateView(bno);

        if(boardDTO.getFileId() != null) {
            Long id = boardDTO.getFileId();
            FileDTO fileDTO = fileService.getFile(id);
            model.addAttribute("fileDTO", fileDTO);
        }

        model.addAttribute("boardDTO", boardDTO);


        return "/boards/read";
    }

    @GetMapping("/{bno}/upform") //업데이트폼
    public String getUpform(@PathVariable("bno") Long bno, Model model){
        BoardDTO boardDTO = boardService.getById(bno);

        Long id = boardDTO.getFileId();
        FileDTO fileDTO = fileService.getFile(id);

        model.addAttribute("fileDTO", fileDTO);

        model.addAttribute("boardDTO", boardDTO); //입력한 객체를 전달, DB로부터 가져온 것 아님
        return "/boards/upform"; //view resolving : upform.html
    }

    @PutMapping("/{bno}") //업데이트 구현
    public String putBoard(@RequestParam("file")MultipartFile files, Long bno, BoardDTO boardDTO, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        try {
            boardDTO = boardService.getById(bno);

            Long id = boardDTO.getFileId();
            FileDTO fileDTO = fileService.getFile(id);

            String origFilename = files.getOriginalFilename();
            if(origFilename.isEmpty()) {
                origFilename = fileDTO.getOrigFilename();
            }

            String filename = new MD5Generator(origFilename).toString();
            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
            String savePath = System.getProperty("user.dir") + "\\files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if (!new File(savePath).exists()) {
                try{
                    new File(savePath).mkdir();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + filename;
            files.transferTo(new File(filePath));

            fileDTO.setOrigFilename(origFilename);
            fileDTO.setFilename(filename);
            fileDTO.setFilePath(filePath);

            model.addAttribute("filename", filename);

            Long fileId = fileService.saveFile(fileDTO);
            boardDTO.setFileId(fileId);
            boardService.update(bno,boardDTO);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "redirect:/boards";
    }

    @GetMapping("/{bno}/delform") //삭제폼
    public String getDelform(@ModelAttribute("bno") Long bno, Model model){
        // html에서 model 객체를 전달 받음 : memberDTO (애드트리뷰트 명으로 접근, th:object 애트리뷰트 값)
        BoardDTO boardDTO = boardService.getById(bno);
        model.addAttribute("boardDTO", boardDTO);
        //return "members/delform";
        return "/boards/delform";

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
}