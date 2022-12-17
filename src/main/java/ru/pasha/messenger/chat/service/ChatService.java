package ru.pasha.messenger.chat.service;

import ru.pasha.messenger.chat.domain.Chat;

import java.util.List;

public interface ChatService {

    Chat saveChat(Chat chat);
    Chat getChatById(Long id);
    List<Chat> getAllChats();
    Chat deleteChatById(Long id);
}
