package com.ev.userservice.Redis;


import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;


@Configuration
@EnableCaching

public class RedisConfig {


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory){
        GenericJacksonJsonRedisSerializer serializer = GenericJacksonJsonRedisSerializer.builder()
                .enableUnsafeDefaultTyping() .build();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                                    .serializeValuesWith( RedisSerializationContext.SerializationPair
                                                                        .fromSerializer(serializer) );

        return RedisCacheManager.builder(connectionFactory) .cacheDefaults(configuration) .build();
    }
}
