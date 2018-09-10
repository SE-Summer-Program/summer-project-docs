package com.sjtubus.model.response;

import com.sjtubus.entity.Collection;
import java.util.List;

public class CollectionResponse extends HttpResponse {
    private List<Collection> collections;

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }
}

