package com.doankietdev.identityservice.infrastructure.cache.distributed;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import com.doankietdev.identityservice.application.spi.DistributedCacheService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RedisDistributedCacheService implements DistributedCacheService {
  @Autowired
  private RedisTemplate<Object, Object> redisTemplate;

  public RedisDistributedCacheService() {
  }

  @Override
  public Object get(Object key) {
    return redisTemplate.opsForValue().get(key);
  }

  @Override
  public void put(Object key, Object value) {
    redisTemplate.opsForValue().set(key, value);
  }

  @Override
  public void put(Object key, Object value, Long exp) {
    put(key, value, exp, TimeUnit.SECONDS);
  }

  @Override
  public void put(Object key, Object value, Long exp, TimeUnit timeUnit) {
    redisTemplate.opsForValue().set(key, value, exp, timeUnit);
  }

  @Override
  public boolean hasKey(Object key) {
    return this.redisTemplate.opsForValue().get(key) != null;
  }

  @Override
  public Boolean remove(Object key) {
    return redisTemplate.delete(key);
  }

  @Override
  public Long increment(String key, long liveTime) {
    RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
    if (connectionFactory == null) {
      log.error("RedisConnectionFactory is null when trying to increment key: {}", key);
      throw new IllegalStateException("RedisConnectionFactory is not initialized.");
    }

    RedisAtomicLong atomicCounter = new RedisAtomicLong(key, connectionFactory);
    Long counterValue = atomicCounter.getAndIncrement();
    if (counterValue == 0 && liveTime > 0) {
      atomicCounter.expire(liveTime, TimeUnit.SECONDS);
    }
    return counterValue;
  }

  @Override
  public void batchDelete(Collection keys) {
    redisTemplate.delete(keys);
  }
}
