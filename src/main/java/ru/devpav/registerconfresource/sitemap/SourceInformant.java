package ru.devpav.registerconfresource.sitemap;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.devpav.registerconfresource.core.resource.Resource;
import ru.devpav.registerconfresource.core.source.WebSource;
import ru.devpav.registerconfresource.core.util.URLUtil;
import ru.devpav.registerconfresource.sitemap.domain.Link;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SourceInformant {

    private static final Integer SOCKET_TIME_WAIT = 10000;

    private final SitemapParser sitemapParser;

    public SourceInformant(SitemapParser sitemapParser) {
        this.sitemapParser = sitemapParser;
    }

    public Collection<Resource> getResources(WebSource webSource, Consumer<Set<String>> handleSitemap) {
        Set<String> sitemapXmlLinks = new HashSet<>();
        try {
            sitemapXmlLinks = getSitemapXmlLinks(webSource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        handleSitemap.accept(sitemapXmlLinks);

        Function<String, Link> converter = (linkString) -> {
            final Link link = new Link();
            link.setLink(linkString);
            link.setSitemap(true);
            link.setMiddling(true);
            link.setParent(null);
            link.setTime(new Date().getTime());
            return link;
        };

        Function<URL, Resource> converterToResource = (url) ->
                new Resource() {
                    @Override
                    public WebSource getSource() {
                        return webSource;
                    }

                    @Override
                    public String getPath() {
                        return url.getPath();
                    }

                    @Override
                    public String getQuery() {
                        return url.getQuery();
                    }
                };

        return sitemapXmlLinks.parallelStream()
                .map(converter)
                .flatMap(link -> sitemapParser.parse(link).stream())
                .map(link -> {
                    try {
                        return new URL(link.getLink());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(converterToResource)
                .collect(Collectors.toList());
    }

    private Set<String> getSitemapXmlLinks(WebSource webSource) throws MalformedURLException {
        final String robotUrl = webSource.getURL().toString() + "/robots.txt";

        final Set<String> links = new HashSet<>();

        GetRequest getRequest;
        HttpResponse<String> stringHttpResponse;
        HttpStatus responseStatus;

        try {
            getRequest = Unirest.get(robotUrl).socketTimeout(SOCKET_TIME_WAIT);
            stringHttpResponse = getRequest.asString();
            responseStatus = HttpStatus.valueOf(stringHttpResponse.getStatus());
        } catch (Exception ex) {
            return links;
        }

        final String body = stringHttpResponse.getBody();

        boolean isOk = responseStatus.equals(HttpStatus.OK);

        if (!isOk || Objects.isNull(body)) {
            return links;
        }

        final String[] lines = body.toLowerCase().split("\n");

        return Stream.of(lines)
                .filter(line -> line.contains("sitemap"))
                .map(URLUtil::substringRegexp)
                .collect(Collectors.toSet());

    }

}
