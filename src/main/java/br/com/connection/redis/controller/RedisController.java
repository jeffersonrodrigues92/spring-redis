package br.com.connection.redis.controller;

import br.com.connection.redis.response.ResponseTime;
import br.com.connection.redis.entity.Person;
import br.com.connection.redis.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequestMapping("/redis")
public class RedisController {

    private HashOperations hashOperations;

    private static final String KEY = "Person";

    @Autowired
    private RedisTemplate<String, Person> redisTemplate;

    @Autowired
    private PersonRepository personRepository;

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @GetMapping("/cache/{id}")
    public ResponseEntity<ResponseTime> cache(@PathVariable Long id){

        Person person = null;

        ResponseTime responseTime = new ResponseTime();
        responseTime.setInitTime(System.currentTimeMillis());

        person = (Person) hashOperations.get(KEY, id);

        if(person == null){
            Optional<Person> personOptional = personRepository.findById(id);
            if(personOptional.isPresent()){
                hashOperations.put(KEY, id, personOptional.get());
                person = personOptional.get();
            }
        }

        responseTime.setEndTime(System.currentTimeMillis());
        responseTime.setPerson(person);

        return ResponseEntity.ok(responseTime);
    }

    @GetMapping("/no-cache/{id}")
    public ResponseEntity<ResponseTime> noCache(@PathVariable Long id){

        ResponseTime responseTime = new ResponseTime();

        responseTime.setInitTime(System.currentTimeMillis());

        Optional<Person> person = personRepository.findById(id);

        responseTime.setEndTime(System.currentTimeMillis());
        responseTime.setPerson(person.get());

        return ResponseEntity.ok(responseTime);
    }

    @PostMapping("/save")
    public ResponseEntity<Person> save(@RequestBody Person person){
        person =  personRepository.save(person);
        return ResponseEntity.ok(person);
    }
}
