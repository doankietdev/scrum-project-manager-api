package com.doankietdev.identityservice.infrastructure.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.doankietdev.identityservice.infrastructure.utils.FastJsonRedisDataSerializer;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class RedisConfig extends CachingConfigurerSupport {
  static final String REDIS_URI_PREFIX = "redis://";

  @Autowired
  AppProperties appProperties;

  /**
   * Khi có nhiều quản lý, bắt buộc phải sử dụng chú thích này trên một quản lý:
   * để biểu thị quản lý đó là quản lý mặc định.
   *
   * @param connectionFactory Nhà máy kết nối
   * @return Cache
   */
  @Bean
  @Primary
  public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
    FastJsonRedisDataSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisDataSerializer<>(Object.class);
    RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair
        .fromSerializer(fastJsonRedisSerializer);
    RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
    defaultCacheConfig = defaultCacheConfig.entryTtl(Duration.ofSeconds(appProperties.getCache().getExpirationTime()));
    RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);

    ParserConfig.getGlobalInstance().addAccept("com.doankietdev.");

    return cacheManager;
  }

  @Bean(name = "redisTemplate")
  @ConditionalOnMissingBean(name = "redisTemplate")
  RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
    RedisTemplate<Object, Object> template = new RedisTemplate<>();
    FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
    template.setValueSerializer(fastJsonRedisSerializer);
    template.setHashValueSerializer(fastJsonRedisSerializer);
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setConnectionFactory(lettuceConnectionFactory);
    return template;
  }

  @Bean(destroyMethod = "shutdown")
  public RedissonClient redisson(RedisProperties redisProperties) {
    Config config = new Config();
    if (redisProperties.getSentinel() != null && !redisProperties.getSentinel().getNodes().isEmpty()) {
      SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
      sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
      List<String> sentinelAddress = new ArrayList<>();
      for (String node : redisProperties.getSentinel().getNodes()) {
        sentinelAddress.add(REDIS_URI_PREFIX + node);
      }
      sentinelServersConfig.setSentinelAddresses(sentinelAddress);
      if (CharSequenceUtil.isNotEmpty(redisProperties.getSentinel().getPassword())) {
        sentinelServersConfig.setSentinelPassword(redisProperties.getSentinel().getPassword());
      }
      if (CharSequenceUtil.isNotEmpty(redisProperties.getPassword())) {
        sentinelServersConfig.setPassword(redisProperties.getPassword());
      }
    } else if (redisProperties.getCluster() != null && !redisProperties.getCluster().getNodes().isEmpty()) {
      ClusterServersConfig clusterServersConfig = config.useClusterServers();
      List<String> clusterNodes = new ArrayList<>();
      for (String node : redisProperties.getCluster().getNodes()) {
        clusterNodes.add(REDIS_URI_PREFIX + node);
      }
      clusterServersConfig.setNodeAddresses(clusterNodes);
      if (CharSequenceUtil.isNotEmpty(redisProperties.getPassword())) {
        clusterServersConfig.setPassword(redisProperties.getPassword());
      }
    } else {
      SingleServerConfig singleServerConfig = config.useSingleServer();
      singleServerConfig.setAddress(REDIS_URI_PREFIX + redisProperties.getHost() + ":" + redisProperties.getPort());
      if (CharSequenceUtil.isNotEmpty(redisProperties.getPassword())) {
        singleServerConfig.setPassword(redisProperties.getPassword());
      }
      singleServerConfig.setPingConnectionInterval(1000);
    }

    return Redisson.create(config);
  }

  /**
   * Chiến lược tạo khóa cache tùy chỉnh, mặc định sử dụng chiến lược này.
   */
  @Bean
  @Override
  public KeyGenerator keyGenerator() {
    return (target, method, params) -> {
      Map<String, Object> container = new HashMap<>(3);
      Class<?> targetClassClass = target.getClass();
      // class
      container.put("class", targetClassClass.toGenericString());
      // method name
      container.put("methodName", method.getName());
      // package
      container.put("package", targetClassClass.getPackage());
      // Danh sách tham số
      for (int i = 0; i < params.length; i++) {
        container.put(String.valueOf(i), params[i]);
      }
      // Chuyển đổi sang chuỗi JSON
      String jsonString = JSON.toJSONString(container);
      // Thực hiện phép tính băm SHA256 và lấy thông báo SHA256 làm Khóa
      return DigestUtils.sha256Hex(jsonString);
    };
  }

  @Bean
  @Override
  public CacheErrorHandler errorHandler() {
    // Xử lý ngoại lệ, khi xảy ra ngoại lệ trong Redis, nhật ký được in nhưng chương
    // trình vẫn chạy bình thường
    log.info("Initialize -> [{}]", "Redis CacheErrorHandler");
    return new CacheErrorHandler() {
      @Override
      public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
        log.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
      }

      @Override
      public void handleCachePutError(RuntimeException e, Cache cache, Object key, @Nullable Object value) {
        log.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
      }

      @Override
      public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
        log.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
      }

      @Override
      public void handleCacheClearError(RuntimeException e, Cache cache) {
        log.error("Redis occur handleCacheClearError：", e);
      }
    };
  }

}
