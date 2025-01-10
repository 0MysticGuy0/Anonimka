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
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Log4j
@RequiredArgsConstructor
public class MessageLinkerServiceImpl implements MessageLinkerService {

    @Autowired
    private final StringRedisTemplate redisTemplate;

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public void createLink(AnswerMessage answerMessage, Integer sentMessageId) {
        readWriteLock.writeLock().lock();

        createSenderLink(answerMessage, sentMessageId);
        createReceiverLink(answerMessage, sentMessageId);

        readWriteLock.writeLock().unlock();
    }

    @Override
    public Integer getLinkedMessageId(String senderChatId, String receiverChatId, Integer messageId) {
        if(senderChatId == null || messageId == null) return  null;

        readWriteLock.readLock().lock();

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
                        log.error(String.format(
                                "Cant parse linked message ID: %s -> %s - %s | (error parsing %s )", senderChatId, messageId, data, receiverMessageId  ));
                    }
                }
            }
        }

        readWriteLock.readLock().unlock();
        return linkedMessageId;
    }

    @Override
    public String[] getLinkedData(String senderChatId, Integer messageId) {
        if(senderChatId == null || messageId == null) return  null;

        readWriteLock.readLock().lock();
        String[] linkedData = null;

        if(redisTemplate.hasKey(senderChatId)){
            String data = (String) redisTemplate.opsForHash()
                    .get(senderChatId, messageId.toString());

            if(data != null){
                String[] dataParts = data.split(":");
                linkedData = dataParts;
            }
        }

        readWriteLock.readLock().unlock();
        return linkedData;
    }

    @Override
    public void clearUserLinks(String userChatId) {
        readWriteLock.writeLock().lock();

        boolean deleteRes = redisTemplate.delete(userChatId);
        log.info(String.format("-=-=-| Deleted messages links for user %s successfully: %s", userChatId, deleteRes));

        readWriteLock.writeLock().unlock();
    }


    private void createSenderLink(AnswerMessage answerMessage, Integer sentMessageId){
        String receiverChatId = answerMessage.getReceiverChatId();
        String senderChatId = answerMessage.getSenderChatId();
        Integer senderMessageId = answerMessage.getMessageId();

        if(senderChatId == null || senderMessageId == null || sentMessageId == null) return;

        String data;
        data = String.format("%s:%s", receiverChatId, sentMessageId);
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
