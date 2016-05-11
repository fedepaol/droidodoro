package com.whiterabbit.droidodoro.trelloclient.model;

/**
 * Created by fedepaol on 07/05/16.
 */
public class TrelloList {
    private String id;
    private String name;

    public TrelloList(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
