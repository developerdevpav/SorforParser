package ru.devpav.registerconfresource.core.source;

import java.net.MalformedURLException;
import java.net.URL;

public interface WebSource {

    String getHost();

    String getProtocol();

    URL getURL() throws MalformedURLException;

}
