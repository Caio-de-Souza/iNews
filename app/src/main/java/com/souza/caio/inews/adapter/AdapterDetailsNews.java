package com.souza.caio.inews.adapter;

import static com.souza.caio.inews.Utils.loadCountry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.souza.caio.inews.R;
import com.souza.caio.inews.news.Article;
import com.souza.caio.inews.news.screen.ArcticleDetailsActivity;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterDetailsNews extends RecyclerView.Adapter<AdapterDetailsNews.MyViewHolder> {
    Context context;
    List<Article> arcticles;

    public AdapterDetailsNews(Context context, List<Article> arcticles) {
        this.context = context;
        this.arcticles = arcticles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noticias_layout, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Article arcticle = arcticles.get(position);
        setArcticleTitle(holder, arcticle);
        setFontArcticle(holder, arcticle);
        holder.data.setText("\u2022" + dataTime(arcticle.getPublishedAt()));
        setFolderArcticle(holder, arcticle);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArcticle(arcticle);
            }
        });
    }

    private void showArcticle(Article artigo) {
        Intent intent = new Intent(context, ArcticleDetailsActivity.class);
        intent.putExtra("artigo", artigo);
        context.startActivity(intent);
    }

    private void setFolderArcticle(@NonNull MyViewHolder holder, Article artigo) {
        String imageUrl = artigo.getUrlToImage();
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equalsIgnoreCase("null")) {
            Picasso.with(context).load(imageUrl).into(holder.imageView);
        }
    }

    private void setFontArcticle(@NonNull MyViewHolder holder, Article artigo) {
        holder.fonte.setText(artigo.getSource().getName());
    }

    private void setArcticleTitle(@NonNull MyViewHolder holder, Article artigo) {
        holder.titulo.setText(artigo.getTitle());
    }

    @Override
    public int getItemCount() {
        return arcticles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fonte, data;
        ImageView imageView;
        LinearLayout item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initComponents(itemView);
        }

        private void initComponents(@NonNull View itemView) {
            titulo = itemView.findViewById(R.id.manchete_noticias_jornal);
            fonte = itemView.findViewById(R.id.fonte_noticias_jornal);
            data = itemView.findViewById(R.id.data_noticias_jornal);
            imageView = itemView.findViewById(R.id.poster_noticias_jornal);
            item = itemView.findViewById(R.id.item_arcticle);
        }
    }

    public String dataTime(String data) {
        PrettyTime prettyTime = new PrettyTime(new Locale(loadCountry()));
        String time = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:", Locale.getDefault());

            Date date = sdf.parse(data);
            time = prettyTime.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}