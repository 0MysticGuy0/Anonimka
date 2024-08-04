package com.vlat.entity;

import com.vlat.botUser.enums.BotUserState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "bot_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BotUser {

    @Column(name = "chat_id")
    @Id
    private String chatId;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private BotUserState state = BotUserState.IDLE;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "companion_id")
    private BotUser companion;

    public BotUser(String chatId) {
        this.chatId = chatId;
    }
}