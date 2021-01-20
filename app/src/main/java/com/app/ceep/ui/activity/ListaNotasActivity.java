package com.app.ceep.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.ceep.R;
import com.app.ceep.dao.NotaDAO;
import com.app.ceep.model.Nota;
import com.app.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import java.util.List;

import static com.app.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static com.app.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.app.ceep.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;
import static com.app.ceep.ui.activity.NotaActivityConstantes.TITULO_APPBAR;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botãoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botãoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaAcivity();
            }
        });
    }

    private void vaiParaFormularioNotaAcivity() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(ehResultadoComNota(requestCode, resultCode, data)) {
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adiciona(Nota nota) {
        new NotaDAO().insere(nota);
        adapter.adiciona(nota);
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && ehCodigoResultadoNotaCriada(resultCode) && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean ehCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}