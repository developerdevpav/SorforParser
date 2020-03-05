package ru.devpav.registerconfresource.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Page implements ru.devpav.registerconfresource.core.page.Page, Serializable {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String source;

    @Indexed
    private String resource;

    private String title;

    private String content;

    private String author;

    private Date date;

    @Override
    public String getTextTitle() {
        return title;
    }

    @Override
    public String getTextContent() {
        return content;
    }

    @Override
    public Date getDateUpload() {
        return date;
    }

    @Override
    public String getTextAuthor() {
        return author;
    }

}
