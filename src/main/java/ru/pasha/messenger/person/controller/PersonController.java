package ru.pasha.messenger.person.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pasha.messenger.person.domain.Person;
import ru.pasha.messenger.person.service.PersonService;

@RestController
@RequestMapping("/api/v1/people")
@AllArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<String> savePerson(@RequestBody Person person) {
        personService.savePerson(person);
        return new ResponseEntity<>("Person saved successfully", HttpStatus.OK);
    }

    @GetMapping("/{field}")
    public ResponseEntity<Person> getPersonByField(@PathVariable String field) {
        return new ResponseEntity<>(personService.getPersonByField(field), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updatePersonById(@PathVariable Long id, @RequestBody Person person) {
        personService.updatePerson(person, id);
        return new ResponseEntity<>("Person updated successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePersonById(@PathVariable Long id) {
        personService.deletePersonById(id);
        return new ResponseEntity<>("Person deleted successfully", HttpStatus.OK);
    }
}
