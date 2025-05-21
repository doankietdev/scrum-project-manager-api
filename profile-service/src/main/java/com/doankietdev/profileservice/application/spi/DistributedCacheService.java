package com.doankietdev.profileservice.application.spi;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface DistributedCacheService<T> {

  /**
   * Retrieve an item from the cache, non-transactional.
   *
   * @param key Cache key
   * @return The cached object or <tt>null</tt>
   */
  T get(Object key);

  /**
   * Add an item to the cache, non-transactional,
   * with fail-fast semantics.
   *
   * @param key   Cache key
   * @param value Cache value
   */
  Boolean put(Object key, T value);

  /**
   * Store an item in the cache with an expiration time in seconds.
   *
   * @param key   Cache key
   * @param value Cache value
   * @param exp   Expiration time in seconds
   */
  Boolean put(Object key, T value, Long exp);

  /**
   * Store an item in the cache with an expiration time and time unit.
   *
   * @param key      Cache key
   * @param value    Cache value
   * @param exp      Expiration time
   * @param timeUnit Time unit for the expiration time
   */
  Boolean put(Object key, T value, Long exp, TimeUnit timeUnit);

  /**
   * Check if the cache contains the given key.
   *
   * @param key Cache key
   * @return True if the key exists, False otherwise
   */
  Boolean hasKey(Object key);

  /**
   * Remove an item from the cache.
   *
   * @param key Cache key
   */
  Boolean remove(Object key);

  /**
   * Redis counter increment.
   * Note: After the specified lifetime expires, the increment will be reverted
   * (i.e., auto-decremented by 1),
   * instead of leaving an empty Redis value.
   *
   * @param key      Counter key. Each call with the same key increases the value
   *                 by 1.
   * @param liveTime Lifetime in seconds
   * @return The result of the counter
   */
  Long increment(String key, Long liveTime);

  Boolean setInt(String key, Integer value);

  Integer getInt(String key);

  /**
   * Batch delete cache entries.
   *
   * @param keys Collection of cache keys to delete
   */
  void batchDelete(Collection keys);
}
