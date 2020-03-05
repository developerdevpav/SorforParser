package ru.devpav.registerconfresource.handler.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.devpav.registerconfresource.core.config.ConfigWebSource;
import ru.devpav.registerconfresource.core.page.Page;
import ru.devpav.registerconfresource.handler.PageRipper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

@Component
public class PageRipperImpl implements PageRipper {

    @Override
    public Page ripper(Document document, ConfigWebSource confResource) {
        final Elements titleElements = document.select(confResource.getQueryTitle());
        final Elements contentElements = document.select(confResource.getQueryContent());

        if (titleElements.isEmpty() || contentElements.isEmpty()) {
            return null;
        }

        final String textTitle = getByQuery(confResource.getQueryTitle(), document, null);
        final String textContent = getByQuery(confResource.getQueryContent(), document, null);

        final Date dateUpload = getByQuery(confResource.getQueryDateUpload(), document, (dateString) -> {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        });

        final String textAuthor = getByQuery(confResource.getQueryAuthor(), document, null);

        return new PageImpl(null, textTitle, textContent, dateUpload, textAuthor);
    }

    private <T> T getByQuery(String query, Document document, Function<String, T> transform) {
        if (query == null || query.isEmpty()) {
            return null;
        }

        final Elements elements = document.select(query);
        final String text = textCollect(elements);

        if (text != null && transform != null) {
            return transform.apply(text);
        }

        if (text == null) {
            return null;
        }

        return (T) text;
    }


    @AllArgsConstructor
    @Data
    private static class PageImpl implements Page {

        private String source;
        private String textTitle;
        private String textContent;
        private Date date;
        private String textAuthor;


        @Override
        public String getTextTitle() {
            return textTitle;
        }

        @Override
        public String getTextContent() {
            return textContent;
        }

        @Override
        public Date getDateUpload() {
            return date;
        }

        @Override
        public String getTextAuthor() {
            return textAuthor;
        }

    }

    private String textCollect(Elements elements) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }

        final StringBuilder stringBuilderTitle = new StringBuilder();

        for (Element e: elements) {
            stringBuilderTitle.append(e.text());
        }

        return stringBuilderTitle.toString();
    }

}
