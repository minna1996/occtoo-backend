package com.valoriz.occtoo.model;

import lombok.Data;

import java.util.List;
@Data
public class Entity {

    private String key;

    private boolean delete;

    private List<Property> properties;

}
