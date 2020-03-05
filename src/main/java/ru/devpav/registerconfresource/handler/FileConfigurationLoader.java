package ru.devpav.registerconfresource.handler;

import ru.devpav.registerconfresource.core.config.ConfigWebSource;
import ru.devpav.registerconfresource.core.loader.Loader;
import ru.devpav.registerconfresource.core.source.WebSource;

import java.io.IOException;

public interface FileConfigurationLoader extends Loader<ConfigWebSource, WebSource> {

    @Override
    ConfigWebSource load(WebSource obj) throws IOException;

}
