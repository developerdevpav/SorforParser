package ru.devpav.registerconfresource.core.config;

public interface ConfigWebSource {

    String getQueryTitle();

    String getQueryContent();

    default String getQueryDateUpload() {
        return null;
    }

    default String getQueryAuthor() {
        return null;
    }

}
