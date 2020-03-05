package ru.devpav.registerconfresource.handler.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import ru.devpav.registerconfresource.core.resource.Resource;
import ru.devpav.registerconfresource.core.source.WebSource;
import ru.devpav.registerconfresource.handler.PageLoader;

import java.io.IOException;

@Component
public class PageLoaderImpl implements PageLoader {

    @Override
    public Document load(Resource resource) throws IOException {
        if (resource == null || resource.getSource() == null) {
            throw new RuntimeException("Link or Resource [parent] mustn't be is null");
        }

        final WebSource webSource = resource.getSource();

        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(webSource.getProtocol())
                .append("://")
                .append(webSource.getHost());

        final String path = resource.getPath();
        if (path != null) {
            stringBuilder.append(path);
        }

        final String query = resource.getQuery();
        if (query != null) {
            stringBuilder.append(query);
        }

        final Connection connect = Jsoup.connect(stringBuilder.toString());

        try {
            return connect.get();
        } catch (Exception ex) {
            return null;
        }
    }

}
