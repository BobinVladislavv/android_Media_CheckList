package com.VladBobin.mediachecklist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;

public class ItemsActivity extends AppCompatActivity {

    public static final String EXTRA_TYPE = "extra_type";
    public static final String EXTRA_TITLE = "extra_title";

    private ItemsViewModel viewModel;
    private ItemAdapter adapter;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_activity);

        // Тип приходит из Intent, fallback — MOVIE
        type = getIntent().getStringExtra(EXTRA_TYPE);
        if (type == null) type = Item.TYPE_MOVIE;

        String screenTitle = getIntent().getStringExtra(EXTRA_TITLE);
        if (screenTitle == null) screenTitle = defaultTitleFor(type);

        TextView titleView = findViewById(R.id.itemsTitle);
        titleView.setText(screenTitle);

        // ViewModel с параметром type
        final String finalType = type;
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new ItemsViewModel(getApplication(), finalType);
            }
        }).get(ItemsViewModel.class);

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.itemsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemAdapter(new ItemAdapter.OnItemViewedChangedListener() {
            @Override
            public void onItemViewedChanged(Item item, boolean isViewed) {
                handleViewedChange(item, isViewed);
            }
            @Override
            public void onItemEdit(Item item) {
                EditDialog.show(ItemsActivity.this, item, edited -> viewModel.updateItem(edited));
            }
            @Override
            public void onItemStart(Item item) {
                handleStart(item);
            }
            @Override
            public void onItemDelete(Item item) {
                handleDelete(item);
            }
        });
        recyclerView.setAdapter(adapter);

        viewModel.getItems().observe(this, adapter::submitList);

        // Поиск
        SearchView searchView = findViewById(R.id.searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { viewModel.setQuery(q); return true; }
            @Override public boolean onQueryTextChange(String q) { viewModel.setQuery(q); return true; }
        });

        // Добавление
        MaterialButton addButton = findViewById(R.id.addItemButton);
        addButton.setOnClickListener(v -> addItem());
    }

    private String defaultTitleFor(String type) {
        switch (type) {
            case Item.TYPE_BOOK: return "Книги";
            case Item.TYPE_GAME: return "Игры";
            default:             return "Фильмы/Сериалы";
        }
    }

    private void handleViewedChange(Item item, boolean newState) {
        if (newState) {
            Item copy = copyOf(item);
            if (copy.getFinalDate() == null) {
                copy.setFinalDate(LocalDate.now());
            }
            copy.setViewed(true);
            viewModel.updateItem(copy);
            Toast.makeText(this, "Установлена сегодняшняя дата", Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Удалить?")
                    .setMessage("Очистить конечную дату?")
                    .setPositiveButton("Да", (d, w) -> {
                        Item copy = copyOf(item);
                        copy.setFinalDate(null);
                        copy.setViewed(false);
                        viewModel.updateItem(copy);
                    })
                    .setNegativeButton("Отмена", (d, w) -> {
                        d.dismiss();
                    })
                    .show();
        }
    }
    private void handleStart(Item item) {
        new AlertDialog.Builder(this)
                .setTitle("Начать?")
                .setMessage("Начать "+item.getStrintType()+" "+item.getTitle()+"?")
                .setPositiveButton("Да", (d, w) -> {
                    Item copy = copyOf(item);
                    copy.setStartDate(LocalDate.now());
                    viewModel.updateItem(copy);
                })
                .setNegativeButton("Нет", (d, w) -> {
                    d.dismiss();
                })
                .show();
    }
    private void handleDelete(Item item) {
        new AlertDialog.Builder(this)
                .setTitle("Удалить?")
                .setMessage("Удалить выбранный(-ую) "+item.getStrintType()+" ("+item.getTitle()+")?")
                .setPositiveButton("Да", (d, w) -> {
                    viewModel.deleteItem(item);
                })
                .setNegativeButton("Нет", (d, w) -> {
                    d.dismiss();
                })
                .show();
    }

    private Item copyOf(Item src) {
        return new Item(src.getId(), src.getTitle(), src.getType(),
                src.getStartDate(), src.getFinalDate(), src.isViewed());
    }

    private void addItem() {
        Item item = new Item("", type);
        EditDialog.show(ItemsActivity.this, item, edited -> viewModel.addItem(edited));
    }
}