package ru.pasha.messenger.person.service;

import ru.pasha.messenger.person.domain.Person;

public interface PersonService {

    Person savePerson(Person person);
    Person getPersonByField(String field);
    Person getPersonByUsername(String username);
    Person getPersonById(Long id);
    Person deletePersonById(Long id);
    Person updatePerson(Person person, Long id);
}
