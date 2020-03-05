package ru.devpav.registerconfresource.core.page;

import java.util.Date;

public interface Page {

    String getSource();

    String getTextTitle();

    String getTextContent();

    default Date getDateUpload() {
        return null;
    }

    default String getTextAuthor() {
        return null;
    }

}
