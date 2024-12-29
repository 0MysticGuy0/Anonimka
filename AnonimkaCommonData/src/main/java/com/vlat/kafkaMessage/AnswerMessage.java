package com.vlat.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerMessage implements Serializable {
    protected String receiverChatId;
    protected String senderChatId;
    protected Integer replyToMessageId;
    protected Integer messageId;
}
