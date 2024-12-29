package com.vlat.service.impl;

import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.service.MessageLinkerService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class MessageLinkerServiceImpl implements MessageLinkerService {

    public static final Map<String, Map<Integer, String>> links = new HashMap();

    @Override
    public void createLink(AnswerMessage answerMessage, Integer sentMessageId) {
        createSenderLink(answerMessage, sentMessageId);
        createReceiverLink(answerMessage, sentMessageId);
    }

    @Override
    public Integer getLinkedMessageId(String senderChatId, String receiverChatId, Integer messageId) {
        if(senderChatId == null || messageId == null) return  null;

        Integer linkedMessageId = null;

        if(links.containsKey(senderChatId)){
            Map<Integer, String> userLinks = links.get(senderChatId);

            String data = userLinks.get(messageId);
            if(data != null){
                String[] dataParts = data.split(":");
                String receiverId = dataParts[0];
                String receiverMessageId = dataParts[1];
                if(receiverChatId != null && receiverChatId.equals(receiverId)){
                    try {
                        linkedMessageId = Integer.parseInt(receiverMessageId);
                    }catch (NumberFormatException ex){
                        log.error("Cant parse linked message ID: " + senderChatId + " -> " + messageId + " - " + data + " | (error parsing " + receiverMessageId +")");
                    }
                }
            }
        }

        return linkedMessageId;
    }


    private void createSenderLink(AnswerMessage answerMessage, Integer sentMessageId){
        String receiverChatId = answerMessage.getReceiverChatId();
        String senderChatId = answerMessage.getSenderChatId();
        Integer senderMessageId = answerMessage.getMessageId();
        if(!links.containsKey(senderChatId)){
            links.put(senderChatId, new HashMap<>());
        }

        String data;
        Map senderLinks = links.get(senderChatId);
        data = receiverChatId + ":" + sentMessageId;
        senderLinks.put(senderMessageId, data);
    }
    private void createReceiverLink(AnswerMessage answerMessage, Integer sentMessageId){
        String receiverChatId = answerMessage.getReceiverChatId();
        String senderChatId = answerMessage.getSenderChatId();
        Integer senderMessageId = answerMessage.getMessageId();
        if(!links.containsKey(receiverChatId)){
            links.put(receiverChatId, new HashMap<>());
        }

        String data;
        Map receiverLinks = links.get(receiverChatId);
        data = senderChatId + ":" + senderMessageId;
        receiverLinks.put(sentMessageId, data);
    }
}
