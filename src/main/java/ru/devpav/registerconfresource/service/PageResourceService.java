package ru.devpav.registerconfresource.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import ru.devpav.registerconfresource.domain.Page;
import ru.devpav.registerconfresource.repository.redis.PageSourceRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
public class PageResourceService {

    private final PageSourceRepository pageResourceRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    public PageResourceService(PageSourceRepository pageResourceRepository,
                               @Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        this.pageResourceRepository = pageResourceRepository;
        this.redisTemplate = redisTemplate;
    }


    public Collection<Page> findByHost(String host) {
        RedisCallback<Collection<Page>> redisCallback = connection -> {
            ScanOptions scanOptions = ScanOptions.scanOptions().match("*").build();
            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan("pages", scanOptions);

            Collection<Page> pages = new ArrayList<>();
            if (cursor != null) {
                while (cursor.hasNext()) {
                    Page page = (Page) cursor.next().getValue();
                    pages.add(page);
                }
                return pages;
            }
            return Collections.emptyList();
        };

        return redisTemplate.execute(redisCallback);
    }
}
