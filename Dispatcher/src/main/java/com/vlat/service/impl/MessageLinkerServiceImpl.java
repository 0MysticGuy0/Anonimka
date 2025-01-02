package com.vlat.service.impl;

import com.vlat.kafkaMessage.AnswerMessage;
import com.vlat.service.MessageLinkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
@RequiredArgsConstructor
public class MessageLinkerServiceImpl implements MessageLinkerService {

    @Autowired
    private final StringRedisTemplate redisTemplate;

    @Override
    public void createLink(AnswerMessage answerMessage, Integer sentMessageId) {
        createSenderLink(answerMessage, sentMessageId);
        createReceiverLink(answerMessage, sentMessageId);
    }

    @Override
    public Integer getLinkedMessageId(String senderChatId, String receiverChatId, Integer messageId) {
        if(senderChatId == null || messageId == null) return  null;

        Integer linkedMessageId = null;

        if(redisTemplate.hasKey(senderChatId)){
            String data = (String) redisTemplate.opsForHash()
                    .get(senderChatId, messageId.toString());

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

    @Override
    public String[] getLinkedData(String senderChatId, Integer messageId) {
        if(senderChatId == null || messageId == null) return  null;

        String[] linkedData = null;

        if(redisTemplate.hasKey(senderChatId)){
            String data = (String) redisTemplate.opsForHash()
                    .get(senderChatId, messageId.toString());

            if(data != null){
                String[] dataParts = data.split(":");
                linkedData = dataParts;
            }
        }

        return linkedData;
    }

    @Override
    public void clearUserLinks(String userChatId) {
        boolean deleteRes = redisTemplate.delete(userChatId);
        log.info("-=-=-| Deleted messages links for user " + userChatId + " successfully: " + deleteRes);
    }


    private void createSenderLink(AnswerMessage answerMessage, Integer sentMessageId){
        String receiverChatId = answerMessage.getReceiverChatId();
        String senderChatId = answerMessage.getSenderChatId();
        Integer senderMessageId = answerMessage.getMessageId();

        if(senderChatId == null || senderMessageId == null || sentMessageId == null) return;

        String data;
        data = receiverChatId + ":" + sentMessageId;
        redisTemplate.opsForHash().put(senderChatId, senderMessageId.toString(), data);
    }
    private void createReceiverLink(AnswerMessage answerMessage, Integer sentMessageId){
        String receiverChatId = answerMessage.getReceiverChatId();
        String senderChatId = answerMessage.getSenderChatId();
        Integer senderMessageId = answerMessage.getMessageId();

        if(senderChatId == null || senderMessageId == null || sentMessageId == null) return;


        String data;
        data = senderChatId + ":" + senderMessageId;
        redisTemplate.opsForHash().put(receiverChatId, sentMessageId.toString(), data);
    }
}
