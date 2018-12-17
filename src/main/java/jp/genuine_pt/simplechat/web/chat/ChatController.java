package jp.genuine_pt.simplechat.web.chat;

import jp.genuine_pt.simplechat.model.message.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {

    @MessageMapping(value = "/message")
    @SendTo(value = "/topic/messages")
    public Message index( String message )
    {
        return new Message(message);
    }
}
