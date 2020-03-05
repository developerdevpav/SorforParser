package ru.devpav.registerconfresource.core.resource;

import ru.devpav.registerconfresource.core.source.WebSource;

public interface Resource {

    WebSource getSource();

    String getPath();

    String getQuery();

}
