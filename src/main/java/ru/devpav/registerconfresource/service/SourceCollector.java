package ru.devpav.registerconfresource.service;

import org.jsoup.nodes.Document;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import ru.devpav.registerconfresource.core.constants.StatusSource;
import ru.devpav.registerconfresource.core.resource.Resource;
import ru.devpav.registerconfresource.domain.ConfResource;
import ru.devpav.registerconfresource.domain.Page;
import ru.devpav.registerconfresource.domain.WebSource;
import ru.devpav.registerconfresource.handler.PageLoader;
import ru.devpav.registerconfresource.handler.PageRipper;
import ru.devpav.registerconfresource.repository.redis.PageSourceRepository;
import ru.devpav.registerconfresource.repository.redis.SourceRepositoryRedis;
import ru.devpav.registerconfresource.repository.redis.SourceSitemapRedisRepository;
import ru.devpav.registerconfresource.sitemap.SourceInformant;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@EnableAsync
@Component
public class SourceCollector {

    private final PageSourceRepository pageResourceRepository;
    private final SourceInformant sourceInformant;
    private final PageLoader pageLoader;
    private final PageRipper pageRipper;
    private final SourceRepositoryRedis webSourceRepository;

    private final ConfResourceService confResourceService;

    private final StringRedisTemplate stringRedisTemplate;

    private final SourceSitemapRedisRepository sourceSitemapRedisRepository;

    public SourceCollector(PageSourceRepository pageResourceRepository,
                           SourceInformant sourceInformant,
                           PageLoader pageLoader,
                           PageRipper pageRipper,
                           SourceRepositoryRedis webSourceRepository,
                           ConfResourceService confResourceService,
                           StringRedisTemplate stringRedisTemplate,
                           SourceSitemapRedisRepository sourceSitemapRedisRepository) {
        this.pageResourceRepository = pageResourceRepository;
        this.sourceInformant = sourceInformant;
        this.pageLoader = pageLoader;
        this.pageRipper = pageRipper;
        this.webSourceRepository = webSourceRepository;
        this.confResourceService = confResourceService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.sourceSitemapRedisRepository = sourceSitemapRedisRepository;
    }

    @Async
    public void asyncSource(WebSource webSource, ConfResource confResource) {
        Collection<Resource> resources = sourceInformant.getResources(webSource, sitemaps -> {
            try {
                sourceSitemapRedisRepository.save(sitemaps, webSource.getURL().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        final Function<Resource, Pair<String, Document>> convertResource = (resource) -> {
            String path;
            Document load;

            try {

                if (resource.getPath() == null) {
                    return null;
                }

                path = webSource.getURL().toString() + resource.getPath();
                String query = resource.getQuery();

                if (query != null) {
                    path = path + query;
                }

                if (pageResourceRepository.existsById(path)) {
                    return null;
                }

                load = pageLoader.load(resource);

                if (load == null) {
                    return null;
                }


            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return Pair.of(path, load);
        };

        final Function<Pair<String, Document>, Pair<String, ru.devpav.registerconfresource.core.page.Page>> convertToPage = pair -> {
            ru.devpav.registerconfresource.core.page.Page ripper =
                    pageRipper.ripper(pair.getSecond(), confResource);

            if (ripper == null) return null;

            return Pair.of(pair.getFirst(), ripper);
        };

        final Function<Pair<String, ru.devpav.registerconfresource.core.page.Page>, Page> convertToObjectPage = pair -> {
            ru.devpav.registerconfresource.core.page.Page page = pair.getSecond();
            return new Page(pair.getFirst(), webSource.getHost(), page.getTextTitle(), page.getTextContent(), page.getTextAuthor(),
                    page.getDateUpload());
        };

        final List<Page> pages = resources.parallelStream()
                .map(convertResource)
                .filter(Objects::nonNull)
                .map(convertToPage)
                .filter(Objects::nonNull)
                .map(convertToObjectPage)
                .collect(Collectors.toList());

        transaction(() -> {
            pageResourceRepository.saveAll(pages);

            WebSource foundWebSource = webSourceRepository.findById(webSource.getHost());

            if (foundWebSource != null) {
                foundWebSource.setStatus(StatusSource.READY);
                webSourceRepository.save(foundWebSource);
            }

            return null;
        });

    }

    private <T> void transaction(Supplier<T> transactional) {
        stringRedisTemplate.execute((RedisCallback<T>) redisConnection -> {
            T object = null;
            redisConnection.multi();

            object = transactional.get();

            redisConnection.exec();

            return object;
        });
    }

    public void clean() {
        transaction(() -> {
            confResourceService.clean();
            webSourceRepository.deleteAll();
            pageResourceRepository.deleteAll();
            return null;
        });
    }

}
