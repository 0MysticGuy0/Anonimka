package com.vlat.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class ReplyableMessage extends ReceivedMessage {
    protected Integer replyToMessageId;
    protected Integer messageId;

    public ReplyableMessage(String authorId, Integer replyToMessageId, Integer messageId) {
        super(authorId);
        this.replyToMessageId = replyToMessageId;
        this.messageId = messageId;
    }
}
