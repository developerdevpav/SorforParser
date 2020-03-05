package ru.devpav.registerconfresource.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSourceInformation implements Serializable {

    private String source;
    private ConfResource config;

}
