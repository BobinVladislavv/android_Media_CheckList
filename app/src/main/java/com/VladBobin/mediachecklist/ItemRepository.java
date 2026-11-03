package com.VladBobin.mediachecklist;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemRepository {

    private final ItemDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public ItemRepository(Application app) {
        dao = AppDatabase.get(app).itemDao();
    }

    public LiveData<List<Item>> getByType(String type) {
        return dao.getByType(type);
    }

    public LiveData<List<Item>> search(String type, String q) {
        return dao.search(type, q);
    }

    // Все пишущие операции — в фоне
    public void insert(Item item) {
        io.execute(() -> dao.insert(item));
    }

    public void update(Item item) {
        io.execute(() -> dao.update(item));
    }

    public void delete(Item item) {
        io.execute(() -> dao.delete(item));
    }

    // Проверка «пустая ли БД для этого типа» — чтобы залить стартовые данные
    public void seedIfEmpty(String type, Runnable onSeeded) {
        io.execute(() -> {
            if (dao.getByTypeSync(type).isEmpty()) {
                insertSeedData(type);
                // вернуть в UI-поток, если нужно что-то сделать после
                if (onSeeded != null) onSeeded.run();
            }
        });
    }

    private void insertSeedData(String type) {
        // примеры для фильмов
        if (Item.TYPE_MOVIE.equals(type)) {
            dao.insert(new Item("Интерстеллар", type));
            dao.insert(new Item("Начало", type,
                    java.time.LocalDate.of(2013, 5, 12)));
            dao.insert(new Item("Матрица", type,
                    java.time.LocalDate.of(2000, 1, 1),
                    java.time.LocalDate.of(2000, 1, 2)));
            dao.insert(new Item("Форрест Гамп", type,
                    java.time.LocalDate.of(2018, 6, 5),
                    java.time.LocalDate.of(2018, 6, 6)));
        }
        // для BOOK, GAME — аналогично, если нужно
    }
}