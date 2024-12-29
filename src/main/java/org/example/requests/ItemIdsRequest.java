package org.example.requests;

import java.util.List;

public class ItemIdsRequest {
    private List<Long> ids;

    // Getters and Setters
    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}