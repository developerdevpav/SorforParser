package ru.devpav.registerconfresource.handler.impl;

import org.springframework.stereotype.Component;
import ru.devpav.registerconfresource.core.config.ConfigWebSource;
import ru.devpav.registerconfresource.core.loader.Loader;
import ru.devpav.registerconfresource.core.source.WebSource;

import java.io.IOException;

@Component
public class ResourceCacheLoaderImpl implements Loader<ConfigWebSource, WebSource> {

    @Override
    public ConfigWebSource load(WebSource obj) throws IOException {
        return null;
    }

}
