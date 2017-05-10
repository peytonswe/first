package com.example.ksusha.bitsandpizzas;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {

    //Переменные для хранения информации о пицце.
    private String[] captions;
    private int[] imageIds;
    private Listener listener;

    // создается интерфейс Listener:
    public static interface Listener {
        public void onClick(int position);
    }

    //Данные передаются адаптеру через конструктор.
    public CaptionedImagesAdapter(String[] captions, int[] imageIds){
        this.captions = captions;
        this.imageIds = imageIds;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    //Определение класса ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //В нашем компоненте RecyclerView
        //должны отображаться карточки, поэ-
        //тому мы указываем, что ViewHolder
        //содержит представления CardView.
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_captioned_image, parent, false); //layout
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(CaptionedImagesAdapter.ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.info_image);
        Drawable drawable = cardView.getResources().getDrawable(imageIds[position]);
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(captions[position]);

        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(captions[position]);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        //Возвращает количество вариантов в наборе данных
        return captions.length;
    }

}
