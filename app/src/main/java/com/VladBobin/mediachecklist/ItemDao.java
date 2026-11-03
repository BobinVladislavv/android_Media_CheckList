package com.VladBobin.mediachecklist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items WHERE type = :type " +
            "ORDER BY " +
            "CASE " +
            "  WHEN viewed = 0 AND startDate IS NULL THEN 0 " +
            "  WHEN viewed = 0 AND startDate IS NOT NULL THEN 1 " +
            "  ELSE 2 " +
            "END ASC, " +
            "CASE " +
            "  WHEN viewed = 0 AND startDate IS NOT NULL THEN startDate " +
            "  WHEN viewed = 1 THEN finalDate " +
            "  ELSE NULL " +
            "END DESC")
    LiveData<List<Item>> getByType(String type);

    @Query("SELECT * FROM items WHERE type = :type AND title LIKE '%' || :q || '%' " +
            "ORDER BY " +
            "CASE " +
            "  WHEN viewed = 0 AND startDate IS NULL THEN 0 " +
            "  WHEN viewed = 0 AND startDate IS NOT NULL THEN 1 " +
            "  ELSE 2 " +
            "END ASC, " +
            "CASE " +
            "  WHEN viewed = 0 AND startDate IS NOT NULL THEN startDate " +
            "  WHEN viewed = 1 THEN finalDate " +
            "  ELSE NULL " +
            "END DESC")
    LiveData<List<Item>> search(String type, String q);

    @Query("SELECT * FROM items WHERE type = :type")
    List<Item> getByTypeSync(String type);

    // Для счётчиков на главном экране
    @Query("SELECT COUNT(*) FROM items WHERE type = :type AND viewed = 1")
    LiveData<Integer> countViewed(String type);

    @Query("SELECT COUNT(*) FROM items WHERE type = :type")
    LiveData<Integer> countAll(String type);

    @Insert
    long insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);
}