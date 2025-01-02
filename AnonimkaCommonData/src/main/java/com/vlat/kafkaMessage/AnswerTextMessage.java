package com.vlat.kafkaMessage;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AnswerTextMessage extends AnswerMessage{
    private String text;
    private boolean needsToClearLinks = false;

    public AnswerTextMessage(String receiverChatId, String senderChatId, Integer replyToMessageId, Integer messageId, String text) {
        super(receiverChatId, senderChatId, replyToMessageId, messageId);
        this.text = text;
    }
}
