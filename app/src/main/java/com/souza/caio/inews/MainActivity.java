package com.souza.caio.inews;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.system.Os;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.GsonBuilder;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.souza.caio.inews.adapter.AdapterJournal;
import com.souza.caio.inews.adapter.AdapterNews;
import com.souza.caio.inews.connection.ApiClient;
import com.souza.caio.inews.journal.JournalsListAPI;
import com.souza.caio.inews.journal.Jornal;
import com.souza.caio.inews.news.Article;
import com.souza.caio.inews.news.RecentArcticlesListAPI;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String AVISO_FILTRO = "Filtro apenas para matérias!";
    public static final String TITULO_ALERTDIALOG = "Selecione a categoria de notícias: ( Deslize para baixo para mais opções)";
    private RecyclerView recyclerView_sites;
    private List<Article> artigos;
    private MaterialSearchView searchView;
    private String API_KEY;
    private TextView tipoConteudo;

    private List<Jornal> jornais;

    private SwipeRefreshLayout swipe;

    private List<String> categoriasBr;
    private List<String> categorias;
    private int INDEX_SELECIONADO = -1;
    private List<RadioButton> opcoesFiltros;
    private RadioButton btnRadioOne;
    private ProgressBar carregando;

    private boolean isMateriasExibindo;
    private ImageButton btnVerJornais;
    private ImageButton btnVerMaterias;

    public MainActivity() {
        opcoesFiltros = new ArrayList<>();
        artigos = new ArrayList<>();
        jornais = new ArrayList<>();
        categoriasBr = new ArrayList<>();
        categoriasBr.add("Geral");
        categoriasBr.add("Negócios");
        categoriasBr.add("Entretenimento");
        categoriasBr.add("Saúde");
        categoriasBr.add("Ciência");
        categoriasBr.add("Esporte");
        categoriasBr.add("Tecnologia");

        categorias = new ArrayList<>();
        categorias.add("general");
        categorias.add("business");
        categorias.add("entertainment");
        categorias.add("health");
        categorias.add("science");
        categorias.add("sports");
        categorias.add("technology");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_artigos, menu);
        MenuItem item = menu.findItem(R.id.pesquisar);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filtrar_busca_artigos) {
            if (isMateriasExibindo) {
                filtrar();
            } else {
                exibirMensagem(AVISO_FILTRO);
            }
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = readApiKey(getApplicationContext());

        configurarToolBar();
        configurarBarraDePesquisa();
        iniciarComponentes();
        isMateriasExibindo = true;
        String pais = coletarPais();
        coletarJson(pais, API_KEY);
    }

    private void configurarBarraDePesquisa() {
        searchView = findViewById(R.id.searchview_pesquisar_artigo);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                if (isMateriasExibindo) {
                    String pais = coletarPais();
                    coletarJson(pais, API_KEY);
                }
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    pesquisar(newText);
                } else {
                }

                return true;
            }
        });
    }

    private void configurarToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_artigos);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.branco));
    }

    private void exibirMensagem(String mensagem) {
        Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_LONG).show();
    }


    private void iniciarComponentes() {
        carregando = findViewById(R.id.progressbar_carregando_noticias_main);
        carregando.setVisibility(ViewGroup.VISIBLE);
        swipe = findViewById(R.id.swipe_main);
        tipoConteudo = findViewById(R.id.titulo_menu);
        tipoConteudo.setText(R.string.noticias);
        recyclerView_sites = findViewById(R.id.lista_conteudos);
        btnVerJornais = findViewById(R.id.btnVerJornais);
        btnVerMaterias = findViewById(R.id.btnVerMaterias);

        btnVerJornais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVerJornais.setVisibility(View.GONE);
                btnVerMaterias.setVisibility(View.VISIBLE);

                tipoConteudo.setText(R.string.jornais);
                coletarJornais(API_KEY);
                isMateriasExibindo = false;
            }
        });

        btnVerMaterias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnVerMaterias.setVisibility(View.GONE);
                btnVerJornais.setVisibility(View.VISIBLE);

                tipoConteudo.setText(R.string.noticias);
                String pais = coletarPais();
                coletarJson(pais, API_KEY);
                isMateriasExibindo = true;
            }
        });

        configurarRecarregamentoDePagina();
        configurarRecyclerView();

    }

    private void configurarRecyclerView() {
        recyclerView_sites.setLayoutManager(new LinearLayoutManager(this));
    }


    private void configurarRecarregamentoDePagina() {
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isMateriasExibindo) {
                    carregando.setVisibility(View.VISIBLE);
                    tipoConteudo.setText(R.string.noticias);
                    String pais = coletarPais();
                    coletarJson(pais, API_KEY);
                } else {
                    tipoConteudo.setText(R.string.jornais);
                    coletarJornais(API_KEY);
                }
            }
        });
    }

    public void coletarJornais(String apiKey) {
        btnVerJornais.setClickable(false);
        btnVerMaterias.setClickable(false);
        Call<JournalsListAPI> call = ApiClient.getInstance().getApi().getJornalsNoCategory(apiKey);

        call.enqueue(new Callback<JournalsListAPI>() {
            @Override
            public void onResponse(Call<JournalsListAPI> call, Response<JournalsListAPI> response) {
                if (response.isSuccessful() && response.body() != null) {
                    exibirListaJornais(response);
                    btnVerMaterias.setClickable(true);
                }

                if (swipe.isRefreshing()) {
                    swipe.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<JournalsListAPI> call, Throwable t) {

            }
        });
    }

    private void exibirListaJornais(Response<JournalsListAPI> response) {
        jornais.clear();
        jornais = response.body().getSources();
        AdapterJournal adaptadorJornais = new AdapterJournal(MainActivity.this, jornais);
        recyclerView_sites.setAdapter(adaptadorJornais);
    }

    public void filtrar() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(TITULO_ALERTDIALOG);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_filtro, null);
        builder.setView(view);

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup_filtro);
        radioGroup.removeAllViews();

        btnRadioOne = new RadioButton(MainActivity.this);
        btnRadioOne.setText(R.string.todas_as_categorias);
        btnRadioOne.setTextSize(15);
        btnRadioOne.setTextColor(getResources().getColor(R.color.preto));

        if (INDEX_SELECIONADO == -1) {
            btnRadioOne.setChecked(true);
        }

        btnRadioOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    INDEX_SELECIONADO = -1;
                    verificarFiltros();
                }
            }
        });

        radioGroup.addView(btnRadioOne);
        opcoesFiltros.clear();
        for (int indice = 0; indice < categorias.size(); indice++) {
            RadioButton btnRadio = new RadioButton(MainActivity.this);
            btnRadio.setText(categoriasBr.get(indice));
            btnRadio.setTextSize(15);
            btnRadio.setTextColor(getResources().getColor(R.color.preto));

            if (indice == INDEX_SELECIONADO) {
                btnRadio.setChecked(true);
            } else {
                btnRadio.setChecked(false);
            }

            final int finalIndice = indice;

            btnRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        INDEX_SELECIONADO = finalIndice;
                        verificarFiltros();
                    }
                }
            });

            opcoesFiltros.add(btnRadio);
            radioGroup.addView(opcoesFiltros.get(indice));
        }

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isMateriasExibindo) {
                    tipoConteudo.setText(R.string.jornais);
                    coletarJornais(API_KEY);
                } else {
                    tipoConteudo.setText(R.string.noticias);
                    String pais = coletarPais();
                    coletarJson(pais, API_KEY);
                }
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void verificarFiltros() {
        if (INDEX_SELECIONADO != -1) {
            btnRadioOne.setChecked(false);
        }
        for (int indice = 0; indice < opcoesFiltros.size(); indice++) {
            if (indice != INDEX_SELECIONADO) {
                opcoesFiltros.get(indice).setChecked(false);
            }
        }
    }


    public void coletarJson(String pais, String apiKey) {
        Call<RecentArcticlesListAPI> call;
        btnVerMaterias.setClickable(false);
        btnVerJornais.setClickable(false);
        if (INDEX_SELECIONADO >= 0) {
            call = ApiClient.getInstance().getApi().getHeadlines(pais, apiKey, 100, categorias.get(INDEX_SELECIONADO));
        } else {
            call = ApiClient.getInstance().getApi().getHeadlinesNoCategory(pais, apiKey, 100);
        }

        call.enqueue(new Callback<RecentArcticlesListAPI>() {
            @Override
            public void onResponse(Call<RecentArcticlesListAPI> call, Response<RecentArcticlesListAPI> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carregarArtigos(response);
                    btnVerJornais.setClickable(true);

                    if (swipe.isRefreshing()) {
                        swipe.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<RecentArcticlesListAPI> call, Throwable t) {

            }
        });
    }

    private void carregarArtigos(Response<RecentArcticlesListAPI> response) {
        artigos.clear();
        artigos = response.body().getArticles();
        carregando.setVisibility(ViewGroup.INVISIBLE);
        AdapterNews adaptador = new AdapterNews(MainActivity.this, artigos);
        recyclerView_sites.setAdapter(adaptador);
    }

    public void pesquisar(String pesquisa) {
        tipoConteudo.setText(R.string.noticias);
        isMateriasExibindo = true;

        if (pesquisa.trim().isEmpty()) {

            String pais = coletarPais();
            coletarJson(pais, API_KEY);
        } else {
            Call<RecentArcticlesListAPI> call;

            if (INDEX_SELECIONADO >= 0) {
                call = ApiClient.getInstance().getApi().getSearchResult(pesquisa.trim(), API_KEY, categorias.get(INDEX_SELECIONADO));
            } else {
                call = ApiClient.getInstance().getApi().getSearchResultNoCategory(pesquisa.trim(), API_KEY);
            }
            call.enqueue(new Callback<RecentArcticlesListAPI>() {
                @Override
                public void onResponse(Call<RecentArcticlesListAPI> call, Response<RecentArcticlesListAPI> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        carregarArtigos(response);
                    }
                }

                @Override
                public void onFailure(Call<RecentArcticlesListAPI> call, Throwable t) {

                }
            });
        }
    }

    public String coletarPais() {
        Locale locale = Locale.getDefault();
        String pais = locale.getCountry();
        return pais.toLowerCase();
    }

    public static String readApiKey(Context context) {
        AssetManager assetManager = context.getAssets();
        String apiKey = null;

        try {
            InputStream inputStream = assetManager.open("api_key.txt");
            apiKey = new Scanner(inputStream).useDelimiter("\\A").next();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiKey;
    }
}
