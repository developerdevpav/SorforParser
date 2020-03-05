package ru.devpav.registerconfresource.handler;

import org.jsoup.nodes.Document;
import ru.devpav.registerconfresource.core.config.ConfigWebSource;
import ru.devpav.registerconfresource.core.page.Page;

public interface PageRipper {

    Page ripper(Document document, ConfigWebSource confResource);

}
