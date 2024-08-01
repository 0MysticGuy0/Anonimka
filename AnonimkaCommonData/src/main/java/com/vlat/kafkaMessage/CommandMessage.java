package com.vlat.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CommandMessage implements Serializable {
    private String command;
    private String authorId;
}
