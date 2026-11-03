package com.VladBobin.mediachecklist;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditDialog {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public interface OnItemEditedListener  {
        void onItemEdited(Item edited);
    }

    public static void show(Context context, Item item, OnItemEditedListener onSaved) {
        View layout = LayoutInflater.from(context).inflate(R.layout.edit_toast_activity, null);
        EditText itemTitle = layout.findViewById(R.id.editMovieTitle);
        EditText startDate  = layout.findViewById(R.id.editStartDate);
        EditText finalDate  = layout.findViewById(R.id.editFinalDate);
        Button saveButton   = layout.findViewById(R.id.saveEditMovie);
        Button cancelButton = layout.findViewById(R.id.cancelEditMovie);

        itemTitle.setText(item.getTitle());
        startDate.setText(item.getStartDate() == null ? "" :
                item.getStartDate().format(FMT));
        finalDate.setText(item.getFinalDate() == null ? "" :
                item.getFinalDate().format(FMT));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(layout)
                .create();

        saveButton.setOnClickListener(v -> {
            String t = itemTitle.getText().toString().trim();
            String s = startDate.getText().toString().trim();
            String f = finalDate.getText().toString().trim();

            if (t.isEmpty()) {
                Toast.makeText(context, "Название пустое", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalDate start, fin;
            try {
                start = s.isEmpty() ? null : LocalDate.parse(s, FMT);
                fin   = f.isEmpty() ? null : LocalDate.parse(f, FMT);
            } catch (DateTimeParseException e) {
                Toast.makeText(context, "Формат даты: dd.MM.yyyy", Toast.LENGTH_SHORT).show();
                return;
            }
            Item edited = new Item(
                    item.getId(),
                    t,
                    item.getType(),
                    start,
                    fin,
                    fin != null
            );
            android.util.Log.d("EDIT", "updating id=" + edited.getId()
                    + " title=" + edited.getTitle()
                    + " final=" + edited.getFinalDate());

            if (onSaved != null) onSaved.onItemEdited(edited);
            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}