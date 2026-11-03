package com.VladBobin.mediachecklist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

public class ItemsViewModel extends AndroidViewModel {

    private final ItemRepository repo;
    private final MutableLiveData<String> query = new MutableLiveData<>("");
    private final String type;

    public ItemsViewModel(@NonNull Application app, String type) {
        super(app);
        this.type = type;
        this.repo = new ItemRepository(app);
        repo.seedIfEmpty(type, null);
    }

    public LiveData<List<Item>> getItems() {
        return Transformations.switchMap(query, q ->
                (q == null || q.isEmpty()) ? repo.getByType(type) : repo.search(type, q));
    }

    public void setQuery(String q) { query.setValue(q); }

    public void addItem(Item m) { repo.insert(m); }
    public void updateItem(Item m) { repo.update(m); }
    public void deleteItem(Item m) { repo.delete(m); }
}