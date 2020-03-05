package ru.devpav.registerconfresource.sitemap.domain;

import lombok.Data;

@Data
public class Link {

    private Long time;

    private String link;

    private Boolean middling = false;

    private Boolean sitemap = false;

    private Integer hash;

    private Link parent;

}
