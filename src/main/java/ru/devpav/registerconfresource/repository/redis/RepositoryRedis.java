package ru.devpav.registerconfresource.repository.redis;

import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class RepositoryRedis<T> {

    protected HashOperations<String, String, T> hashOperations;

    protected final String key;

    protected RepositoryRedis(String key) {
        this.key = key;
    }

    public T save(T obj, String hash) {
        hashOperations.put(key, hash, obj);
        return obj;
    }

    public List<T> saveAll(Collection<T> objs, Function<T, String> transform) {
        Map<String, T> collect = objs.stream().collect(Collectors.toMap(transform, k -> k, (k, v) -> v));
        hashOperations.putAll(key, collect);
        return new ArrayList<>(objs);
    }

    public List<T> findAll() {
        return hashOperations.values(key);
    }

    public T findById(String hash) {
        return hashOperations.get(key, hash);
    }

    public long deleteById(String hash) {
        return hashOperations.delete(key, hash);
    }

    public void deleteAll() {
        Set<String> keys = hashOperations.keys(key);
        keys.forEach(this::deleteById);
    }

    public boolean existsById(String hash) {
        return hashOperations.hasKey(key, hash);
    }

    public long size() {
        return hashOperations.size(key);
    }

    @Resource(name="redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private void setHashOperations(HashOperations<String, String, T> hashOperations) {
        this.hashOperations = hashOperations;
    }
}
