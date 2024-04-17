package org.example.springboot_labb2.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Angela Gustafsson, anggus-1
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String rootRedirect(){
        return "redirect:/web/messages";
    }
}
