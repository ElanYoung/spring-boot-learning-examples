package com.starimmortal.security.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis通用工具类
 *
 * @author william@StarImmortal
 * @date 2022/04/13
 */
@Component
public class RedisUtil {

	@Autowired
	private StringRedisTemplate redisTemplate;

	// Key（键），简单 Key-Value 操作

	/**
	 * 判断 Key 是否存在
	 * @param key 键
	 * @return true：存在；false：不存在
	 */
	public boolean hasKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	/**
	 * 查询 Key 存活时间（单位秒）
	 * <p>
	 * 实现命令：TTL key（Get the time to live for a key in seconds）
	 * @param key 键
	 * @return 剩余生存时间（0代表永久有效）
	 */
	public long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 设置过期时间（单位秒）
	 * <p>
	 * 实现命令：expire（Set a key's time to live in seconds）
	 * @param key 键
	 * @param timeout 过期时间（秒）
	 */
	public void expire(String key, long timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 增加 Key 某次 实现命令：increment key
	 * @param key 键
	 * @param delta 要增加几（大于0）
	 * @return 新增后的值
	 */
	public long increment(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 减少 Key 某次
	 * <p>
	 * 实现命令：decrement key
	 * @param key 键
	 * @param delta 要减少几（小于0）
	 * @return 减少后的值
	 */
	public long decrement(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return redisTemplate.opsForValue().decrement(key, delta);
	}

	/**
	 * Hash递增：如果不存在，就会创建一个，并把新增后的值返回
	 * @param name Redis键
	 * @param key Hash键
	 * @param delta 要增加几（大于0）
	 * @return 新增后的值
	 */
	public long incrementHash(String name, String key, long delta) {
		return redisTemplate.opsForHash().increment(name, key, delta);
	}

	/**
	 * Hash递减
	 * @param name Redis键
	 * @param key Hash键
	 * @param delta 要减少几（大于0）
	 * @return 减少后的值
	 */
	public long decrementHash(String name, String key, long delta) {
		delta = delta * (-1);
		return redisTemplate.opsForHash().increment(name, key, delta);
	}

	/**
	 * 往Hash中存入数据
	 * @param name Redis键
	 * @param key Hash键
	 * @param value 值
	 */
	public void setHashValue(String name, String key, String value) {
		redisTemplate.opsForHash().put(name, key, value);
	}

	/**
	 * 获取Hash中的数据
	 * @param name Redis键
	 * @param key Hash键
	 * @return Hash中的对象
	 */
	public String getHashValue(String name, String key) {
		return (String) redisTemplate.opsForHash().get(name, key);
	}

	/**
	 * 查找所有符合给定模式的Key
	 * <p>
	 * 实现命令：KEYS pattern
	 * @param pattern 字符串前缀
	 * @return
	 */
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 删除指定 Key 实现命令：DEL key
	 * @param key 键
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	// String（字符串）

	/**
	 * 设置键值对字符串
	 * <p>
	 * 实现命令：SET key value（Set the string value of a key）
	 * @param key 缓存键值
	 * @param value 缓存值
	 */
	public void set(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 设置键值对与过期时间（秒）
	 * <p>
	 * 实现命令：SET key value EX seconds（Set the value and expiration of a key）
	 * @param key 缓存键值
	 * @param value 缓存值
	 * @param timeout （以秒为单位）
	 */
	public void set(String key, String value, long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 如果 Key 不存在，则设置，如果存在，则报错
	 * @param key 缓存键值
	 * @param value 缓存值
	 */
	public void setIfAbsentBy60s(String key, String value) {
		redisTemplate.opsForValue().setIfAbsent(key, value, 60, TimeUnit.SECONDS);
	}

	/**
	 * 如果 Key 不存在，则设置，如果存在，则报错
	 * @param key 缓存的键值
	 * @param value 缓存的值
	 */
	public void setIfAbsent(String key, String value) {
		redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	/**
	 * 返回 Key 所关联的字符串值
	 * <p>
	 * 实现命令：GET key（Get the value of a key）
	 * @param key 缓存的键值
	 * @return 缓存的值
	 */
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 批量查询
	 * @param keys Hash键集合
	 * @return 字符串集合
	 */
	public List<String> multiGet(List<String> keys) {
		return redisTemplate.opsForValue().multiGet(keys);
	}

	/**
	 * 管道批量查询
	 * @param keys Hash键集合
	 * @return 对象集合
	 */
	public List<Object> batchGet(List<String> keys) {
		return redisTemplate.executePipelined((RedisCallback<String>) connection -> {
			StringRedisConnection src = (StringRedisConnection) connection;
			for (String k : keys) {
				src.get(k);
			}
			return null;
		});
	}

	// Hash（哈希表）

	/**
	 * 实现命令：删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * @param key 键不能为null
	 * @param fields 项可以使多个，不能为null
	 */
	public void deleteHashValue(String key, Object... fields) {
		redisTemplate.opsForHash().delete(key, fields);
	}

	/**
	 * 获取Hash中Key对应的所有键值
	 * <p>
	 * 实现命令：返回哈希表key中，所有的域和值。
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<Object, Object> getHashMap(String key) {
		return redisTemplate.opsForHash().entries(key);
	}

	// List（列表）

	/**
	 * 将一个值Value插入到列表Key的表头
	 * <p>
	 * 实现命令：LPUSH key value
	 * @param key 键
	 * @param value 值
	 * @return 列表的长度
	 */
	public long listPush(String key, String value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 移除并返回列表Key的头元素
	 * <p>
	 * 实现命令：LPOP key
	 * @param key 键
	 * @return 列表key的头元素
	 */
	public String listPop(String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 将一个值Value插入到列表Key的表尾（最右边)）。
	 * <p>
	 * 实现命令：RPUSH key value
	 * @param key 键
	 * @param value 值
	 * @return 列表的长度
	 */
	public long rightPush(String key, String value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

}
