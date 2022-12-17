package ru.pasha.messenger.message.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pasha.messenger.message.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
