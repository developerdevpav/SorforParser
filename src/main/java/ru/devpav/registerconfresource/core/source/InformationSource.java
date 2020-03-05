package ru.devpav.registerconfresource.core.source;

import ru.devpav.registerconfresource.core.config.ConfigWebSource;

public interface InformationSource {

    WebSource getWebSource();

    ConfigWebSource getConfig();

}
