package com.vlat.kafkaMessage;

import com.vlat.kafkaMessage.enums.FileMessageTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMessage implements Serializable {
    private String authorId;
    private Integer replyToMessageId;
    private String fileId;
    private FileMessageTypes fileType;

}
