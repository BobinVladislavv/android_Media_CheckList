package com.VladBobin.mediachecklist;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(tableName = "items")
public class Item {
    public static final String TYPE_MOVIE="MOVIE";
    public static final String TYPE_BOOK="BOOK";
    public static final String TYPE_GAME="GAME";
    private static final long NEW_ID = 0L;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String type;
    private LocalDate startDate;
    private LocalDate finalDate;
    private Boolean viewed;

    public Item(long id, String title, String type, LocalDate startDate, LocalDate finalDate, boolean viewed){
        this.id=id;
        this.title=title;
        this.type=type;
        this.startDate=startDate;
        this.finalDate=finalDate;
        this.viewed=viewed;
    }
    @Ignore
    public Item(String title, String type){
        this(NEW_ID,title, type, null, null, false);
    }
    @Ignore
    public Item(String title, String type, LocalDate startDate, LocalDate finalDate){
        this(NEW_ID,title, type, startDate, finalDate, finalDate != null);
    }
    @Ignore
    public Item(String title, String type, LocalDate startDate){
        this(NEW_ID,title, type, startDate, null, false);
    }
    // Геттеры
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getStrintType() {
        switch (type){
            case Item.TYPE_MOVIE:
                return "Фильм/Сериал";
            case Item.TYPE_BOOK:
                return "Книга";
            case Item.TYPE_GAME:
                return "Игра";
            default:
                return "Медиа";
        }
    }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getFinalDate() { return finalDate; }
    public Boolean isViewed() { return viewed; }
    public String getStartDateFormatted() {
        return startDate == null ? "---" : startDate.format(FMT);
    }
    public String getFinalDateFormatted() {
        return finalDate == null ? "---" : finalDate.format(FMT);
    }


    // Сеттеры
    public void setId(long id) { this.id=id; }
    public void setTitle(String newTitle) { title=newTitle; }
    public void setType(String type) { this.type = type; }
    public void setStartDate(LocalDate newDate) { this.startDate=newDate; }
    public void setFinalDate(LocalDate newDate) { finalDate=newDate; }
    public void changeViewed() { viewed=!viewed; }
    public void setViewed(boolean v) { this.viewed=v; }
}
