package com.vlat.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class TextMessage implements Serializable {
    private String text;
    private String authorId;
    private Integer replyToMessageId;
}
