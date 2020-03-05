package ru.devpav.registerconfresource.service;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import ru.devpav.registerconfresource.core.constants.StatusSource;
import ru.devpav.registerconfresource.domain.ConfResource;
import ru.devpav.registerconfresource.domain.WebSource;
import ru.devpav.registerconfresource.domain.WebSourceInformation;
import ru.devpav.registerconfresource.repository.redis.SourceRepositoryRedis;
import ru.devpav.registerconfresource.repository.redis.SourceSitemapRedisRepository;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;

@Service
@EnableAsync
public class WebSourceService {

    private final SourceRepositoryRedis webSourceRepository;
    private final ConfResourceService confResourceService;
    private final SourceCollector sourceCollector;
    private final StringRedisTemplate stringRedisTemplate;
    private final SourceSitemapRedisRepository sourceSitemapRedisRepository;

    public WebSourceService(SourceRepositoryRedis webSourceRepository,
                            ConfResourceService confResourceService,
                            SourceCollector sourceCollector,
                            StringRedisTemplate stringRedisTemplate,
                            SourceSitemapRedisRepository sourceSitemapRedisRepository) {
        this.webSourceRepository = webSourceRepository;
        this.confResourceService = confResourceService;
        this.sourceCollector = sourceCollector;
        this.stringRedisTemplate = stringRedisTemplate;
        this.sourceSitemapRedisRepository = sourceSitemapRedisRepository;
    }

    public Collection<WebSource> findAll() {
        return webSourceRepository.findAll();
    }

    public void delete(String host) {
        stringRedisTemplate.execute((RedisCallback<WebSource>) redisConnection -> {
            redisConnection.multi();

            confResourceService.delete(host);
            webSourceRepository.deleteById(host);
            sourceSitemapRedisRepository.deleteById(host);

            redisConnection.exec();
            return null;
        });
    }

    public WebSource findByHost(String host) {
        return webSourceRepository.findById(host);
    }

    public Set<String> getSitemap(String host) {
        return sourceSitemapRedisRepository.findById(host);
    }

    public ConfResource getConfig(String url) {
        return confResourceService.findById(url);
    }

    public WebSource save(WebSourceInformation webSourceInformation) {
        final String source = webSourceInformation.getSource();
        final ConfResource config = webSourceInformation.getConfig();

        if (source == null) {
            throw new RuntimeException("Source mustn't be is null");
        }

        if (config == null) {
            throw new RuntimeException("Config mustn't be is null");
        }


        final URL uri;
        try {
            uri = new URL(source);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Source not valid [ " + source + "]");
        }

        config.setSource(uri.getHost());

        WebSource webSource = findByHost(uri.getHost());
        ConfResource confResource = confResourceService.findById(uri.getHost());

        if (webSource == null) {
            webSource = new WebSource(uri.getHost(), uri.getProtocol(), StatusSource.PROCESSING);
            webSource = webSourceRepository.save(webSource);

            if (confResource == null) {
                confResource = confResourceService.save(config);
            }

            sourceCollector.asyncSource(webSource, confResource);
        } else if (webSource.getStatus().equals(StatusSource.READY)) {
            sourceCollector.asyncSource(webSource, confResource);
        }

        return webSource;
    }


}
