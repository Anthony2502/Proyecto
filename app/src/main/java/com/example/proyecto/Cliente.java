package com.example.proyecto;

import java.io.Serializable;

public class Cliente implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
