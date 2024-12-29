package org.example.requests;

import java.util.List;

public class BorrowItemsRequest {
    private List<Long> itemIds;
    private Integer borrowDuration;

    // Getters and Setters
    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Integer getBorrowDuration() {
        return borrowDuration;
    }

    public void setBorrowDuration(Integer borrowDuration) {
        this.borrowDuration = borrowDuration;
    }
}