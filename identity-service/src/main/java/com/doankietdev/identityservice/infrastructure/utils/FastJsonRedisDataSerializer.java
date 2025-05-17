package com.doankietdev.identityservice.infrastructure.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * Để thực hiện cache đối tượng, bạn cần định nghĩa bộ serializer và deserializer riêng của mình.
 * Thường thì sử dụng thư viện fastjson của Alibaba để thực hiện.
 */
public class FastJsonRedisDataSerializer<T> implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Class<T> clazz;

    public FastJsonRedisDataSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException {
        if (null == t) {
            return new byte[0];
        }
        // Sử dụng FastJson để serialize đối tượng thành chuỗi JSON.
        // WriteClassName đảm bảo rằng tên lớp được bao gồm trong JSON, điều này rất quan trọng cho việc deserialize.
        // DisableCircularReferenceDetect xử lý các tham chiếu vòng, ngăn ngừa lỗi tràn stack.
        return JSON.toJSONString(t,
                        SerializerFeature.WriteClassName,
                        SerializerFeature.DisableCircularReferenceDetect)
                .getBytes(DEFAULT_CHARSET);// Mã hóa chuỗi JSON thành byte sử dụng UTF-8
    }

    @Override
    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (null == bytes || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);// Giải mã mảng byte thành chuỗi sử dụng UTF-8.
        // Sử dụng FastJson để deserialize chuỗi JSON thành một đối tượng của lớp được chỉ định.
        return (T) JSON.parseObject(str, clazz);
    }
}