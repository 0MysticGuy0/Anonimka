package com.vlat.entity;

import com.vlat.entity.enums.BotUserState;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "bot_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BotUser implements Serializable {

    @Column(name = "chat_id")
    @Id
    private String chatId;

    @Column
    @Enumerated(EnumType.STRING)
    private BotUserState state = BotUserState.IDLE;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "companion_id")
    private BotUser companion;

    public BotUser(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append("BotUser{")
                .append("chatId='").append(chatId)
                .append("', state=").append(state)
                .append(", companion-id=").append((companion!=null?companion.getChatId():null))
                .append('}');
        return  data.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BotUser botUser = (BotUser) o;
        return Objects.equals(chatId, botUser.chatId) && state == botUser.state && ( (companion==null && botUser.companion==null) || Objects.equals(companion.getChatId(), botUser.companion.getChatId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, state, companion.getChatId());
    }
}