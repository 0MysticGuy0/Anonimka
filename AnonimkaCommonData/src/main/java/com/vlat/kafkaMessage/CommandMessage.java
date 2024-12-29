package com.vlat.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommandMessage extends ReceivedMessage implements Serializable {
    private String command;

    public CommandMessage(String authorId, String command) {
        super(authorId);
        this.command = command;
    }
}
