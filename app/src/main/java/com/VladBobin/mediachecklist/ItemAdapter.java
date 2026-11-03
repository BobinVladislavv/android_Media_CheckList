package com.VladBobin.mediachecklist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class ItemAdapter extends ListAdapter<Item, ItemAdapter.ItemViewHolder> {

    public interface OnItemViewedChangedListener {
        void onItemViewedChanged(Item item, boolean isViewed);
        void onItemEdit(Item item);
        void onItemDelete(Item item);
        void onItemStart(Item item);
    }

    private static final DiffUtil.ItemCallback<Item> DIFF = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item a, @NonNull Item b) {
            return a.getId() == b.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item a, @NonNull Item b) {
            boolean sameTitle = Objects.equals(a.getTitle(), b.getTitle());
            boolean sameViewed = a.isViewed() == b.isViewed();
            boolean sameStart = Objects.equals(a.getStartDate(), b.getStartDate());
            boolean sameFinal = Objects.equals(a.getFinalDate(), b.getFinalDate());

            return sameTitle && sameViewed && sameStart && sameFinal;
        }
    };

    private final OnItemViewedChangedListener listener;

    public ItemAdapter(OnItemViewedChangedListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView startDateView;
        private final TextView finalDateView;
        private final CheckBox viewedCheckBox;
        private final ImageButton editButton;
        private final ImageButton startButton;
        private final ImageButton deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.itemTitle);
            startDateView = itemView.findViewById(R.id.itemStartDate);
            finalDateView = itemView.findViewById(R.id.itemFinalDate);
            viewedCheckBox = itemView.findViewById(R.id.itemViewed);
            editButton = itemView.findViewById(R.id.itemEdit);
            startButton = itemView.findViewById(R.id.itemStart);
            deleteButton = itemView.findViewById(R.id.itemDelete);
        }

        public void bind(Item item, OnItemViewedChangedListener listener) {
            titleView.setText(item.getTitle());
            startDateView.setText("Начало: " + item.getStartDateFormatted());
            finalDateView.setText("Конец: " + item.getFinalDateFormatted());

            // Чекбокс — только отображение, без собственного переключения
            viewedCheckBox.setOnCheckedChangeListener(null);
            viewedCheckBox.setChecked(item.isViewed());

            // Клик по чекбоксу обрабатываем сами
            viewedCheckBox.setOnClickListener(v -> {
                viewedCheckBox.setChecked(item.isViewed());
                if (listener != null) {
                    listener.onItemViewedChanged(item, !item.isViewed());
                }
            });

            editButton.setOnClickListener(v -> {
                if (listener != null) listener.onItemEdit(item);
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) listener.onItemDelete(item);
            });

            startButton.setOnClickListener(v -> {
                if (listener != null) listener.onItemStart(item);
            });
        }
    }
}