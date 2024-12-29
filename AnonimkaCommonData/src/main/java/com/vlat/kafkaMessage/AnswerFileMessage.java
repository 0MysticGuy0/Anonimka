package com.vlat.kafkaMessage;


import com.vlat.kafkaMessage.enums.FileMessageTypes;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AnswerFileMessage extends AnswerMessage{
    private String fileId;
    private FileMessageTypes fileType;

    public AnswerFileMessage(String receiverChatId, String senderChatId, Integer replyToMessageId, Integer messageId, String fileId, FileMessageTypes fileType) {
        super(receiverChatId, senderChatId, replyToMessageId, messageId);
        this.fileId = fileId;
        this.fileType = fileType;
    }
}
