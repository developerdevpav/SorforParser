package ru.devpav.registerconfresource.repository.redis;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public class SourceSitemapRedisRepository extends RepositoryRedis<Set<String>> {

    protected SourceSitemapRedisRepository() {
        super("tbx_source_sitemap");
    }

}
