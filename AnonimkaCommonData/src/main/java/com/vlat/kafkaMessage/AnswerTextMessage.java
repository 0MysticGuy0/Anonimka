package com.vlat.kafkaMessage;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerTextMessage extends AnswerMessage{
    private String text;

    public AnswerTextMessage(String receiverChatId, Integer replyToMessageId, String text) {
        super(receiverChatId, replyToMessageId);
        this.text = text;
    }
}
