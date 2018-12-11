package br.com.connection.redis.repository;

import br.com.connection.redis.entity.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

}
