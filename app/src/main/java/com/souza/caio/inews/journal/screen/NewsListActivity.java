package com.souza.caio.inews.journal.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.souza.caio.inews.R;
import com.souza.caio.inews.adapter.AdapterDetailsNews;
import com.souza.caio.inews.connection.ApiClient;
import com.souza.caio.inews.journal.Jornal;
import com.souza.caio.inews.news.Article;
import com.souza.caio.inews.news.RecentArcticlesListAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsListActivity extends AppCompatActivity
{
    private RecyclerView newsList;
    private List<Article> arcticles;
    private final String API_KEY = "2881e06e23d144858d8fad22530a54fe";
    private String id, name;
    private SwipeRefreshLayout swipe;
    private ProgressBar loadingProgressBar;
    private TextView warningTextView;

    public NewsListActivity()
    {
        arcticles = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        if (intent != null)
        {
            getJournals(intent);
            initComponents();
            getData(id, API_KEY);
        }
    }

    private void getJournals(Intent intent)
    {
        Jornal jornal = (Jornal) intent.getSerializableExtra("jornal");
        id = jornal.getId();
        name = jornal.getName();
    }


    private void initComponents()
    {
        warningTextView = findViewById(R.id.aviso_nenhuma_noticia_jornal);
        warningTextView.setVisibility(View.INVISIBLE);

        loadingProgressBar = findViewById(R.id.progress_noticias_jornal);
        loadingProgressBar.setVisibility(View.VISIBLE);

        swipe = findViewById(R.id.swipe_lista_noticias);

        TextView nomeJornalTextView = findViewById(R.id.nome_jornal_tela);
        nomeJornalTextView.setText(name);

        newsList = findViewById(R.id.lista_noticias);
        setRecyclerView();
        setPageReload();
    }

    private void setRecyclerView()
    {
        newsList.setLayoutManager(new LinearLayoutManager(this));
        newsList.setVisibility(View.INVISIBLE);
    }

    private void setPageReload()
    {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                reloadPage();
            }
        });
    }

    private void reloadPage()
    {
        newsList.setVisibility(View.INVISIBLE);
        warningTextView.setVisibility(View.INVISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);

        getData(id, API_KEY);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            this.finish();
        }
        return true;
    }

    public void getData(String id, String apiKey)
    {
        Call<RecentArcticlesListAPI> call;


        call = ApiClient.getInstance().getApi().getArticlesJornalNoCategory(id, apiKey, 100);

        call.enqueue(new Callback<RecentArcticlesListAPI>()
        {
            @Override
            public void onResponse(Call<RecentArcticlesListAPI> call, Response<RecentArcticlesListAPI> response)
            {
                if (response.isSuccessful() && response.body() != null)
                {
                    arcticles.clear();
                    arcticles = response.body().getArticles();

                    loadArcticles();
                }
            }

            @Override
            public void onFailure(Call<RecentArcticlesListAPI> call, Throwable t)
            {

            }
        });
    }

    private void loadArcticles()
    {
        if (arcticles != null && !arcticles.isEmpty())
        {
            showArcticles();
        }
        else
        {
            showArcticleError();
        }

        if (swipe.isRefreshing())
        {
            swipe.setRefreshing(false);
        }
    }

    private void showArcticleError()
    {
        loadingProgressBar.setVisibility(View.INVISIBLE);
        warningTextView.setVisibility(View.VISIBLE);
    }

    private void showArcticles()
    {
        AdapterDetailsNews adapter = new AdapterDetailsNews(NewsListActivity.this, arcticles);
        newsList.setAdapter(adapter);
        newsList.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.INVISIBLE);
        warningTextView.setVisibility(View.INVISIBLE);
    }
}
