package org.example.springboot_labb2.repository;

import org.example.springboot_labb2.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListHolder {

    private MessageListHolder() {
    }

    static List<Message> listOfMessages = new ArrayList<>();
}
