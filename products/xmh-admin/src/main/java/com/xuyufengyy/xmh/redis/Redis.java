package com.xuyufengyy.xmh.redis;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class Redis {

    private static Logger logger = LoggerFactory.getLogger(Redis.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private Gson gson = new Gson();

//    @Autowired(required = false)
//    public void setRedisTemplate(RedisTemplate redisTemplate) {
//        RedisSerializer stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);
//        redisTemplate.setValueSerializer(stringSerializer);
//        redisTemplate.setHashKeySerializer(stringSerializer);
//        redisTemplate.setHashValueSerializer(stringSerializer);
//        redisTemplate.setDefaultSerializer(stringSerializer);
//        this.redisTemplate = redisTemplate;
//    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @param idx 库序号
     * @return
     */
    public boolean isExists(String key, int idx) {
        return (Boolean) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(idx);
                return redisConnection.exists(key.getBytes());
            }
        });
    }

    /**
     * 新增
     *
     * @param key   键
     * @param value 保存内容
     * @param time  有效时间
     * @param idx   库序号
     */
    public void save(String key, String value, int time, int idx) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(idx);
                redisConnection.set(key.getBytes(), value.getBytes());
                redisConnection.expire(key.getBytes(), time);
                return null;
            }
        });
    }

    /**
     * 新增
     *
     * @param key   键
     * @param value 保存内容
     * @param idx   库序号
     */
    public void save(String key, String value, int idx) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(idx);
                redisConnection.set(key.getBytes(), value.getBytes());
                return null;
            }
        });
    }

    /**
     * 查询
     *
     * @param key 键
     * @param idx 库序号
     * @return
     */
    public String getData(String key, int idx) {
        return (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(idx);
                if (redisConnection.exists(key.getBytes())) {
                    return new String(redisConnection.get(key.getBytes()));
                }
                return null;
            }
        });
    }

    /**
     * 查询
     *
     * @param key 键
     * @param idx 库序号
     * @return
     */
    public <T> T getData(String key, int idx, Class<T> clazz) {
        String data = (String) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(idx);
                if (redisConnection.exists(key.getBytes())) {
                    return new String(redisConnection.get(key.getBytes()));
                }
                return null;
            }
        });

        if (StringUtils.isNotBlank(data)) {
            return gson.fromJson(data, clazz);
        } else {
            return null;
        }
    }

    /**
     * 删除
     *
     * @param key 键
     * @param idx 库序号
     */
    public void remove(String key, int idx) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.select(idx);
                if (redisConnection.exists(key.getBytes())) {
                    redisConnection.del(key.getBytes());
                }
                return null;
            }
        });
    }
}
