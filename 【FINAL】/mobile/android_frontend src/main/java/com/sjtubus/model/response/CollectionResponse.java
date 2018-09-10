package com.sjtubus.model.response;

import com.google.gson.annotations.SerializedName;

import com.sjtubus.model.Collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionResponse extends HttpResponse {
    @SerializedName("collections")
    private List<Collection> collections;

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<String> getShifts() {
        List<String> shifts = new ArrayList<>();
        if (collections != null && collections.size()!=0) {
            for (int i = 0; i < collections.size(); i++) {
                shifts.add(collections.get(i).getShiftid());
            }
        }
        return shifts;
    }
}
