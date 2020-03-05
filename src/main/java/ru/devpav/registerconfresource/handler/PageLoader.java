package ru.devpav.registerconfresource.handler;

import org.jsoup.nodes.Document;
import ru.devpav.registerconfresource.core.loader.Loader;
import ru.devpav.registerconfresource.core.resource.Resource;

import java.io.IOException;

public interface PageLoader extends Loader<Document, Resource> {

    Document load(Resource resource) throws IOException;

}
