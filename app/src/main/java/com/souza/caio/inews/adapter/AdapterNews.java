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

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.NewsViewHolder> {
    Context context;
    List<Article> arcticles;

    public AdapterNews(Context context, List<Article> arcticles) {
        this.context = context;
        this.arcticles = arcticles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conteudo_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, final int position) {
        final Article arcticle = arcticles.get(position);
        setArcticleTitle(holder, arcticle);
        setArcticleFont(holder, arcticle);
        setArcticleDate(holder, arcticle);
        setArcticleFolder(holder, arcticle);
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

    private void setArcticleFolder(@NonNull NewsViewHolder holder, Article artigo) {
        String imageUrl = artigo.getUrlToImage();
        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equalsIgnoreCase("null")) {
            Picasso.with(context).load(imageUrl).into(holder.imageView);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setArcticleDate(@NonNull NewsViewHolder holder, Article artigo) {
        holder.date.setText("\u2022" + dataTime(artigo.getPublishedAt()));
    }

    private void setArcticleFont(@NonNull NewsViewHolder holder, Article artigo) {
        holder.font.setText(artigo.getSource().getName());
    }

    private void setArcticleTitle(@NonNull NewsViewHolder holder, Article artigo) {
        holder.title.setText(artigo.getTitle());
    }

    @Override
    public int getItemCount() {
        return arcticles.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, font, date;
        ImageView imageView;
        LinearLayout item;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            initComponents(itemView);
        }

        private void initComponents(@NonNull View itemView) {
            title = itemView.findViewById(R.id.manchete_noticia_main);
            font = itemView.findViewById(R.id.fonte_noticia_main);
            date = itemView.findViewById(R.id.data_noticia_main);
            imageView = itemView.findViewById(R.id.poster_noticia_main);
            item = itemView.findViewById(R.id.item_arcticle_content);
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
