package ru.devpav.registerconfresource.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.devpav.registerconfresource.core.constants.StatusSource;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSource implements ru.devpav.registerconfresource.core.source.WebSource, Serializable {

    private String host;

    private String protocol;

    private StatusSource status;

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(protocol + "://" + host);
    }
}
