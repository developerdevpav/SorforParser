package ru.devpav.registerconfresource.handler.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.devpav.registerconfresource.core.config.ConfigWebSource;
import ru.devpav.registerconfresource.core.source.WebSource;
import ru.devpav.registerconfresource.handler.FileConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileConfigurationLoaderImpl implements FileConfigurationLoader {

    private final ObjectMapper objectMapper;

    public FileConfigurationLoaderImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public ConfigWebSource load(WebSource resource) throws IOException {
        final String domain = resource.getHost();

        final String confFileName = "confs/" + domain + ".conf.json";

        final InputStream resourceAsStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(confFileName);

        if (resourceAsStream == null) {
            throw new RuntimeException("ResourceConf file was not found");
        }

        return objectMapper.readValue(resourceAsStream, new TypeReference<WebWebSource>() {});
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class WebWebSource implements ConfigWebSource {

        @JsonProperty(value = "queryTitle")
        private String queryTitle;

        @JsonProperty(value = "queryContent")
        private String queryContent;

        @JsonProperty(value = "queryDate")
        private String queryDate;

        @JsonProperty(value = "queryAuthor")
        private String queryAuthor;

        @Override
        public String getQueryTitle() {
            return queryTitle;
        }

        @Override
        public String getQueryContent() {
            return queryContent;
        }

        @Override
        public String getQueryDateUpload() {
            return queryDate;
        }

        @Override
        public String getQueryAuthor() {
            return queryAuthor;
        }
    }

}
