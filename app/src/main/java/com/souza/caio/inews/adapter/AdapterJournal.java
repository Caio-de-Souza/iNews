package com.souza.caio.inews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.souza.caio.inews.R;
import com.souza.caio.inews.journal.Jornal;
import com.souza.caio.inews.journal.screen.NewsListActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterJournal extends RecyclerView.Adapter<AdapterJournal.MyViewHolder> {
    Context context;
    List<Jornal> journals;

    public AdapterJournal(Context context, List<Jornal> journals) {
        this.context = context;
        this.journals = journals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conteudo_main_jornal, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Jornal journal = journals.get(position);
        setJournalName(holder, journal);

        if (journal.getCountry() != null) {
            setJournalCountry(holder, journal);
        } else {
            holder.pais.setText("");
        }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJournalNews(journal);
            }
        });
    }

    private void showJournalNews(Jornal jornal) {
        Intent intent = new Intent(context, NewsListActivity.class);
        intent.putExtra("jornal", jornal);
        context.startActivity(intent);
    }

    private void setJournalCountry(@NonNull MyViewHolder holder, Jornal jornal) {
        if (jornal.getCountry().equalsIgnoreCase("ar")) {
            holder.pais.setText(R.string.argentina);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ar));

        } else if (jornal.getCountry().equalsIgnoreCase("au")) {
            holder.pais.setText(R.string.australia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_au));

        } else if (jornal.getCountry().equalsIgnoreCase("at")) {
            holder.pais.setText(R.string.austria);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_at));

        } else if (jornal.getCountry().equalsIgnoreCase("be")) {
            holder.pais.setText(R.string.belgica);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_be));

        } else if (jornal.getCountry().equalsIgnoreCase("br")) {
            holder.pais.setText(R.string.brasil);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_br));

        } else if (jornal.getCountry().equalsIgnoreCase("bg")) {
            holder.pais.setText(R.string.bulgaria);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_bg));

        } else if (jornal.getCountry().equalsIgnoreCase("ca")) {
            holder.pais.setText(R.string.canada);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ca));

        } else if (jornal.getCountry().equalsIgnoreCase("cn")) {
            holder.pais.setText(R.string.china);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_cn));

        } else if (jornal.getCountry().equalsIgnoreCase("co")) {
            holder.pais.setText(R.string.colombia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_co));

        } else if (jornal.getCountry().equalsIgnoreCase("cu")) {
            holder.pais.setText(R.string.cuba);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_cu));

        } else if (jornal.getCountry().equalsIgnoreCase("cz")) {
            holder.pais.setText(R.string.republica_checa);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_cz));

        } else if (jornal.getCountry().equalsIgnoreCase("eg")) {
            holder.pais.setText(R.string.egito);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_eg));

        } else if (jornal.getCountry().equalsIgnoreCase("fr")) {
            holder.pais.setText(R.string.franca);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_fr));

        } else if (jornal.getCountry().equalsIgnoreCase("de")) {
            holder.pais.setText(R.string.alemanha);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_de));

        } else if (jornal.getCountry().equalsIgnoreCase("gr")) {
            holder.pais.setText(R.string.grecia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_gr));

        } else if (jornal.getCountry().equalsIgnoreCase("hk")) {
            holder.pais.setText(R.string.hong_kong);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_hk));

        } else if (jornal.getCountry().equalsIgnoreCase("hu")) {
            holder.pais.setText(R.string.hungria);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_hu));

        } else if (jornal.getCountry().equalsIgnoreCase("in")) {
            holder.pais.setText(R.string.india);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_in));

        } else if (jornal.getCountry().equalsIgnoreCase("id")) {
            holder.pais.setText(R.string.indonesia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_id));

        } else if (jornal.getCountry().equalsIgnoreCase("ie")) {
            holder.pais.setText(R.string.irlanda);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ie));
        } else if (jornal.getCountry().equalsIgnoreCase("il")) {
            holder.pais.setText(R.string.israel);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_il));

        } else if (jornal.getCountry().equalsIgnoreCase("it")) {
            holder.pais.setText(R.string.italia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_it));

        } else if (jornal.getCountry().equalsIgnoreCase("jp")) {
            holder.pais.setText(R.string.japao);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_jp));

        } else if (jornal.getCountry().equalsIgnoreCase("lv")) {
            holder.pais.setText(R.string.letonia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_lv));

        } else if (jornal.getCountry().equalsIgnoreCase("lt")) {
            holder.pais.setText(R.string.lituania);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_lt));

        } else if (jornal.getCountry().equalsIgnoreCase("my")) {
            holder.pais.setText(R.string.malasia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_my));

        } else if (jornal.getCountry().equalsIgnoreCase("mx")) {
            holder.pais.setText(R.string.mexico);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_mx));

        } else if (jornal.getCountry().equalsIgnoreCase("ma")) {
            holder.pais.setText(R.string.marrocos);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ma));
        } else if (jornal.getCountry().equalsIgnoreCase("nl")) {
            holder.pais.setText(R.string.paises_baixos);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_nl));

        } else if (jornal.getCountry().equalsIgnoreCase("nz")) {
            holder.pais.setText(R.string.nova_zelandia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_nz));

        } else if (jornal.getCountry().equalsIgnoreCase("ng")) {
            holder.pais.setText(R.string.nigeria);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ng));

        } else if (jornal.getCountry().equalsIgnoreCase("no")) {
            holder.pais.setText(R.string.noruega);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_no));

        } else if (jornal.getCountry().equalsIgnoreCase("ph")) {
            holder.pais.setText(R.string.filipinas);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ph));

        } else if (jornal.getCountry().equalsIgnoreCase("pl")) {
            holder.pais.setText(R.string.polonia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_pl));

        } else if (jornal.getCountry().equalsIgnoreCase("pt")) {
            holder.pais.setText(R.string.portugal);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_pt));

        } else if (jornal.getCountry().equalsIgnoreCase("ro")) {
            holder.pais.setText(R.string.romenia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ro));

        } else if (jornal.getCountry().equalsIgnoreCase("ru")) {
            holder.pais.setText(R.string.russia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ru));

        } else if (jornal.getCountry().equalsIgnoreCase("sa")) {
            holder.pais.setText(R.string.arabia_saudita);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_sa));

        } else if (jornal.getCountry().equalsIgnoreCase("rs")) {
            holder.pais.setText(R.string.servia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_rs));

        } else if (jornal.getCountry().equalsIgnoreCase("sg")) {
            holder.pais.setText(R.string.cingapura);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_sg));

        } else if (jornal.getCountry().equalsIgnoreCase("sk")) {
            holder.pais.setText(R.string.eslovaquia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_sk));

        } else if (jornal.getCountry().equalsIgnoreCase("si")) {
            holder.pais.setText(R.string.eslovenia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_si));

        } else if (jornal.getCountry().equalsIgnoreCase("za")) {
            holder.pais.setText(R.string.afica_do_sul);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_za));

        } else if (jornal.getCountry().equalsIgnoreCase("kr")) {
            holder.pais.setText(R.string.coreia_do_sul);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_kr));

        } else if (jornal.getCountry().equalsIgnoreCase("se")) {
            holder.pais.setText(R.string.suecia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_se));

        } else if (jornal.getCountry().equalsIgnoreCase("ch")) {
            holder.pais.setText(R.string.suica);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ch));

        } else if (jornal.getCountry().equalsIgnoreCase("tw")) {
            holder.pais.setText(R.string.taiwan);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_tw));

        } else if (jornal.getCountry().equalsIgnoreCase("th")) {
            holder.pais.setText(R.string.tailandia);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_th));

        } else if (jornal.getCountry().equalsIgnoreCase("ae")) {
            holder.pais.setText(R.string.emirados_arabes_unidos);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ae));

        } else if (jornal.getCountry().equalsIgnoreCase("ua")) {
            holder.pais.setText(R.string.ucrania);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ua));

        } else if (jornal.getCountry().equalsIgnoreCase("gb")) {
            holder.pais.setText(R.string.reino_unido);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_gb));

        } else if (jornal.getCountry().equalsIgnoreCase("us")) {
            holder.pais.setText(R.string.eua);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_us));

        } else if (jornal.getCountry().equalsIgnoreCase("ve")) {
            holder.pais.setText(R.string.venezuela);
            holder.icone.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.ic_ve));

        } else {
            holder.pais.setText("");
        }
    }

    private void setJournalName(@NonNull MyViewHolder holder, Jornal jornal) {
        if (jornal.getName().length() >= 50) {
            String nome = jornal.getName().substring(0, 47);
            nome += "...";
            holder.nomeJornal.setText(nome);
        } else {
            holder.nomeJornal.setText(jornal.getName());
        }
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item;
        TextView nomeJornal, pais;
        CircleImageView icone;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            initComponents(itemView);
        }

        private void initComponents(@NonNull View itemView) {
            nomeJornal = itemView.findViewById(R.id.nome_jornal);
            item = itemView.findViewById(R.id.item_jornal_nome);
            pais = itemView.findViewById(R.id.pais_jornal_lista);
            icone = itemView.findViewById(R.id.iconeJornal);
        }
    }
}
