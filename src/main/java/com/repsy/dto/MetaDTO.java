package com.repsy.dto;

import lombok.Data;

import java.util.List;

@Data
public class MetaDTO {
    private String name;
    private String version;
    private String author;
    private List<DependencyDto> dependencies;

}
