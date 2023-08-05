package com.souza.caio.inews.news.screen;

import static com.souza.caio.inews.Utils.loadCountry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.souza.caio.inews.R;
import com.souza.caio.inews.news.Article;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArcticleDetailsActivity extends AppCompatActivity {
    public static final String ARCTICLE = "artigo";
    public static final String SHARE_MESSAGE = "Selecione o aplicativo para compartilhar a matéria";
    private TextView tituloTextView, fonteTextView, dataTextView, descricaoTextView, conteudoTextView;
    private WebView webView;
    private ImageView folderArcticleImageView;
    private ProgressBar progressBar;
    private String title, font, date, description, linkImage, url, content;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes_noticia, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_arcticle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            getArcticle(intent);
            initComponents();
        }
    }

    private void getArcticle(Intent intent) {
        Article artigo = (Article) intent.getSerializableExtra(ARCTICLE);
        title = artigo.getTitle();
        font = artigo.getSource().getName();
        date = artigo.getPublishedAt();
        description = artigo.getDescription();
        linkImage = artigo.getUrlToImage();
        url = artigo.getUrl();

        if(artigo.getContent() != null) {
            if (artigo.getContent().contains("[+")) {
                content = artigo.getContent().substring(0, artigo.getContent().indexOf("[+"));
            } else {
                content = artigo.getContent();
            }
        }else {
            content = "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        } else if (item.getItemId() == R.id.compartilhar_noticia) {
            shareLink();
        }
        return true;
    }

    public void shareLink() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, SHARE_MESSAGE));
    }

    private void initComponents() {
        conteudoTextView = findViewById(R.id.conteudo_noticia_detalhes);
        tituloTextView = findViewById(R.id.manchete_noticia_detalhes);
        fonteTextView = findViewById(R.id.fonte_noticia_detalhes);
        dataTextView = findViewById(R.id.data_noticia_detalhes);
        descricaoTextView = findViewById(R.id.descricao_noticia_detalhes);
        webView = findViewById(R.id.webView_noticia);
        folderArcticleImageView = findViewById(R.id.poster_noticia_detalhes);
        progressBar = findViewById(R.id.load_webpage);
        progressBar.setVisibility(View.VISIBLE);


        conteudoTextView = findViewById(R.id.conteudo_noticia_detalhes);
        conteudoTextView.setVisibility(View.VISIBLE);
        showContent();

    }

    private void showContent() {
        loadDetails();

        loadBasicData();

        loadFolder();

        setWebView();

        verifyWebview();
    }

    private void verifyWebview() {
        if (webView.isShown()) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setWebView() {
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    private void loadFolder() {
        if (linkImage != null && !linkImage.isEmpty() && !linkImage.equalsIgnoreCase("null")) {
            Picasso.with(this).load(linkImage).into(folderArcticleImageView);
        }
    }

    private void loadBasicData() {
        tituloTextView.setText(title);
        descricaoTextView.setText(description);
        dataTextView.setText("\u2022" + formatDate(date));
        fonteTextView.setText(font);
    }

    public String formatDate(String data) {
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

    private void loadDetails() {
        if (content != null) {
            conteudoTextView.setText(content);
        } else {
            conteudoTextView.setVisibility(View.INVISIBLE);
        }
    }

}
