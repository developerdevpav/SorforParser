package ru.devpav.registerconfresource.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devpav.registerconfresource.core.config.ConfigWebSource;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfResource implements ConfigWebSource, Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String source;

    @JsonProperty("queryTitle")
    private String queryTitle;

    @JsonProperty("queryContent")
    private String queryContent;

    @JsonProperty("queryDateUpload")
    private String queryDateUpload;

    @JsonProperty("queryAuthor")
    private String queryAuthor;


}
