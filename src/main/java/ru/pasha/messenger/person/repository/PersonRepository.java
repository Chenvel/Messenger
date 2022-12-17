package ru.pasha.messenger.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.pasha.messenger.person.domain.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findPersonByUsername(String username);

    Optional<Person> findPersonByPhoneNumber(String phoneNumber);
}
