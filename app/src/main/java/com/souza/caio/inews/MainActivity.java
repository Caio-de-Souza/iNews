package com.souza.caio.inews;

import static com.souza.caio.inews.Utils.loadCountry;
import static com.souza.caio.inews.Utils.readApiKey;
import static com.souza.caio.inews.Utils.showToastMessage;
import static com.souza.caio.inews.connection.ApiClient.getInstance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.souza.caio.inews.adapter.AdapterJournal;
import com.souza.caio.inews.adapter.AdapterNews;
import com.souza.caio.inews.journal.Jornal;
import com.souza.caio.inews.journal.JournalsListAPI;
import com.souza.caio.inews.news.Article;
import com.souza.caio.inews.news.RecentArcticlesListAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String AVISO_FILTRO = "Filtro apenas para matérias!";
    private static final String TITULO_ALERTDIALOG = "Selecione a categoria de notícias:";
    private static final String DEFAULT_SORT_BY = "publishedAt";
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
                showToastMessage(this, AVISO_FILTRO);
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
        String pais = loadCountry();
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
                    String pais = loadCountry();
                    btnVerJornais.setVisibility(View.VISIBLE);
                    coletarJson(pais, API_KEY);
                }
            }
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            private Handler handler = new Handler();
            private boolean textChanged = false;

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                handler.removeCallbacksAndMessages(null); // Remove any pending callbacks

                if (newText != null && !newText.isEmpty()) {
                    textChanged = true; // Mark the text as changed

                    // Schedule a new Runnable to check if the text has changed after 1 second
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (textChanged) {
                                pesquisar(newText); // Call the method if text hasn't changed
                            }
                            textChanged = false; // Reset the flag after 1 seconds
                        }
                    }, 1000); // 1000 milliseconds = 1 second
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
                String pais = loadCountry();
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
                    String pais = loadCountry();
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
        Call<JournalsListAPI> call = getInstance().getApi().getJornalsNoCategory(apiKey, DEFAULT_SORT_BY);

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
            btnRadio.setChecked(indice == INDEX_SELECIONADO);


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
                    String pais = loadCountry();
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
            call = getInstance().getApi().getHeadlines(pais, apiKey, 100, categorias.get(INDEX_SELECIONADO), DEFAULT_SORT_BY);
        } else {
            call = getInstance().getApi().getHeadlinesNoCategory(pais, apiKey, 100, DEFAULT_SORT_BY);
        }

        call.enqueue(new Callback<RecentArcticlesListAPI>() {
            @Override
            public void onResponse(Call<RecentArcticlesListAPI> call, Response<RecentArcticlesListAPI> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carregarArtigos(response);
                    btnVerJornais.setClickable(true);
                    btnVerMaterias.setClickable(true);

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
        AdapterNews adaptador = new AdapterNews(MainActivity.this, artigos);
        recyclerView_sites.setAdapter(adaptador);
        carregando.setVisibility(ViewGroup.INVISIBLE);
        recyclerView_sites.setVisibility(View.VISIBLE);
    }

    public void pesquisar(String pesquisa) {
        tipoConteudo.setText(R.string.noticias);
        isMateriasExibindo = true;
        recyclerView_sites.setVisibility(View.INVISIBLE);
        carregando.setVisibility(ViewGroup.VISIBLE);

        if (pesquisa.trim().isEmpty()) {
            String pais = loadCountry();
            btnVerJornais.setVisibility(View.VISIBLE);
            btnVerMaterias.setVisibility(View.VISIBLE);
            coletarJson(pais, API_KEY);
        } else {
            btnVerJornais.setVisibility(View.GONE);
            btnVerMaterias.setVisibility(View.GONE);
            Call<RecentArcticlesListAPI> call;
            if (INDEX_SELECIONADO >= 0) {
                call = getInstance().getApi().getSearchResult(pesquisa.trim(), API_KEY, categorias.get(INDEX_SELECIONADO), DEFAULT_SORT_BY);
            } else {
                call = getInstance().getApi().getSearchResultNoCategory(pesquisa.trim(), API_KEY, DEFAULT_SORT_BY);
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
}
