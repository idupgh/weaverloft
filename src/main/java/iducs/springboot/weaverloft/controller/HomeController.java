package iducs.springboot.weaverloft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 데이터(처리결과)를 view 에게 전달
//@RestController // 자원의 상태(응답)를 Client 에게 전달,
//Rest (Representational State Transfer), OpenAPIs
@RequestMapping("/")
public class HomeController {
    @GetMapping("")
    public String getHome() {
        return "index";
    }

    @GetMapping("index2")
    public String getIndex2() {
        return "index2"; //view name
    }

    @GetMapping("index3")
    public String getIndex3() {
        return "index3"; //view name
    }

    @GetMapping("pages/tables/simple")
    public String getSimpleTables() {
        return "/pages/tables/simple"; //view name
    }

    @GetMapping("pages/contacts")
    public String getContacts() {
        return "/pages/contacts"; //view name
    }

}
