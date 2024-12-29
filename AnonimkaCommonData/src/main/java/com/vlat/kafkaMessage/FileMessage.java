package com.vlat.kafkaMessage;

import com.vlat.kafkaMessage.enums.FileMessageTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class FileMessage extends ReplyableMessage implements Serializable {
    private String fileId;
    private FileMessageTypes fileType;

    public FileMessage(String authorId, Integer replyToMessageId, Integer MessageId, String fileId, FileMessageTypes fileType) {
        super(authorId, replyToMessageId, MessageId);
        this.fileId = fileId;
        this.fileType = fileType;
    }
}
