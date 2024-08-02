package com.vlat.kafkaMessage;


import com.vlat.kafkaMessage.enums.FileMessageTypes;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnswerFileMessage extends AnswerMessage{
    private String fileId;
    private FileMessageTypes fileType;

    public AnswerFileMessage(String receiverChatId, Integer replyToMessageId, String fileId, FileMessageTypes fileType) {
        super(receiverChatId, replyToMessageId);
        this.fileId = fileId;
        this.fileType = fileType;
    }
}
