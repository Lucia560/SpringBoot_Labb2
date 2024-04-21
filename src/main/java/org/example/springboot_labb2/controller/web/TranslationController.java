package org.example.springboot_labb2.controller.web;

/**
 * @author Angela Gustafsson, anggus-1
 */
import org.example.springboot_labb2.entity.Message;
import org.example.springboot_labb2.service.MessageService;
import org.example.springboot_labb2.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
public class TranslationController {

    @Autowired
    private TranslationService translationService;
    @Autowired
    private MessageService messageService;
    @GetMapping("/web/translate/{lang}/{id}")
    public String translate(@PathVariable("lang") String lang, @PathVariable("id") Long id, Model model) throws IOException {
        Optional<Message> messageOptional = messageService.getMessageById(id);

        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            String translatedText = translationService.translate(message.getContent(),"auto", lang );
            message.setContent(translatedText);
            model.addAttribute("message", message);
            return "translatemessage";
        } else {
            return "error";
        }
    }
}
