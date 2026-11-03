package com.VladBobin.mediachecklist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button movieButton = findViewById(R.id.movieButton);
        Button booksButton = findViewById(R.id.booksButton);
        Button gamesButton = findViewById(R.id.gamesButton);

        SetOpenActivityToButton(movieButton, Item.TYPE_MOVIE,"Фильмы/Сериалы");
        SetOpenActivityToButton(booksButton, Item.TYPE_BOOK, "Книги");
        SetOpenActivityToButton(gamesButton, Item.TYPE_GAME, "Игры");
    }
    private void SetOpenActivityToButton(Button button, String type, String title){
        button.setOnClickListener(v ->
                OpenActivity(type, title));
    }
    private void OpenActivity(String type, String title)
    {
        Intent intent = new Intent(this, ItemsActivity.class);
        intent.putExtra(ItemsActivity.EXTRA_TYPE, type);
        intent.putExtra(ItemsActivity.EXTRA_TITLE, title);
        startActivity(intent);
    }
}