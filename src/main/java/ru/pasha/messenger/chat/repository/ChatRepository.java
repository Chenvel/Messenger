package ru.pasha.messenger.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pasha.messenger.chat.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
