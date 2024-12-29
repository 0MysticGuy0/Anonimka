package com.vlat.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TextMessage extends ReplyableMessage implements Serializable {
    private String text;

    public TextMessage(String authorId, Integer replyToMessageId, Integer MessageId, String text) {
        super(authorId, replyToMessageId, MessageId);
        this.text = text;
    }
}
