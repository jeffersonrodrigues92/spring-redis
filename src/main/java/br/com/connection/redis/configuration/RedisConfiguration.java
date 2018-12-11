package br.com.connection.redis.configuration;

import br.com.connection.redis.entity.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

        @Bean
        JedisConnectionFactory jedisConnectionFactory() {
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
            return jedisConnectionFactory;
        }

        @Bean
        public RedisTemplate<String, Person> redisTemplate() {
            final RedisTemplate<String, Person> template = new RedisTemplate<String, Person>();
            template.setConnectionFactory(jedisConnectionFactory());
            template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
            return template;
    }
}
