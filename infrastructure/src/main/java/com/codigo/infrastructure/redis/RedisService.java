package com.codigo.infrastructure.redis;

import com.codigo.domain.agregates.constants.Constants;
import com.codigo.domain.agregates.response.EmpresaResponse;
import com.codigo.infrastructure.utiliies.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final Utilities utilities;

    public void saveRedisIn(String key, String value, int expiration){
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key,expiration, TimeUnit.MINUTES);
    }

    public String getRedisValue(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <T> void saveObjectInRedis(T object, String keyName){
        String redisData = utilities.convertObjectToString(object);
        saveRedisIn(keyName, redisData, 10);
    }

    public String validateObjectInRedis(String keyName){
        return getRedisValue(keyName);
    }

}
