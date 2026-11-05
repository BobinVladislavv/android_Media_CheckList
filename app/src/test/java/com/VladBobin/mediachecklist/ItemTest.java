package com.VladBobin.mediachecklist;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;

public class ItemTest {
    @Test
    public void getStringType_returnsCorrectRussianName() {
        assertEquals("Фильм/Сериал",
                new Item("Matrix", Item.TYPE_MOVIE).getStringType());
        assertEquals("Книга",
                new Item("Dune", Item.TYPE_BOOK).getStringType());
        assertEquals("Игра",
                new Item("Witcher", Item.TYPE_GAME).getStringType());
        assertEquals("Медиа",
                new Item("Unknown", "OTHER").getStringType());
    }

    @Test
    public void getStartDateFormatted_returnsDashesWhenNull() {
        Item item = new Item("Test", Item.TYPE_MOVIE);
        assertEquals("---", item.getStartDateFormatted());
    }

    @Test
    public void getStartDateFormatted_formatsCorrectly() {
        Item item = new Item("Test", Item.TYPE_MOVIE,
                LocalDate.of(2024, 3, 15));
        assertEquals("15.03.2024", item.getStartDateFormatted());
    }

    @Test
    public void constructorWithFinalDate_setsViewedTrue() {
        Item item = new Item("Test", Item.TYPE_MOVIE,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1));
        assertTrue(item.isViewed());
    }

    @Test
    public void constructorWithStartDateOnly_setsViewedFalse() {
        Item item = new Item("Test", Item.TYPE_MOVIE,
                LocalDate.of(2024, 1, 1));
        assertFalse(item.isViewed());
        assertNull(item.getFinalDate());
    }

    @Test
    public void changeViewed_togglesState() {
        Item item = new Item("Test", Item.TYPE_MOVIE);
        assertFalse(item.isViewed());
        item.changeViewed();
        assertTrue(item.isViewed());
        item.changeViewed();
        assertFalse(item.isViewed());
    }
}
