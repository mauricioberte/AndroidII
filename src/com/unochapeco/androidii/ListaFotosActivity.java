package com.unochapeco.androidii;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.unochapeco.androidii.adapter.ListaFotosAdapter;
import com.unochapeco.androidii.task.ListarFotosTask;

public class ListaFotosActivity extends ListActivity {

	private List<Fotos> listaFotos;
	private ListaFotosAdapter adapter;
	private ListarFotosTask listarTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_fotos);

		if (verificaConexao()) {

			this.listaFotos = new ArrayList<Fotos>();
			this.adapter = new ListaFotosAdapter(this,
					R.layout.layout_item_lista, listaFotos);

			this.setListAdapter(adapter);

			this.listarTask = new ListarFotosTask(this);
			this.listarTask.execute();
		} else {
			Toast.makeText(getBaseContext(), "Não foi detectada uma conexão com a Internet.", Toast.LENGTH_LONG).show();
			Toast.makeText(getBaseContext(), "Por favor ative uma conexão de Internet",  Toast.LENGTH_LONG).show();
		}
	}

	/*
	 * Função para verificar existência de conexão com a internet
	 */
	public boolean verificaConexao() {
		boolean conectado;
		ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conectivtyManager.getActiveNetworkInfo() != null
				&& conectivtyManager.getActiveNetworkInfo().isAvailable()
				&& conectivtyManager.getActiveNetworkInfo().isConnected()) {
			conectado = true;
		} else {
			conectado = false;
		}
		return conectado;
	}

	public void receberRetornoLista(List<Fotos> fotos) {
		this.listaFotos.clear();
		this.listaFotos.addAll(fotos);
		this.adapter.notifyDataSetChanged();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Fotos fotoSelecionada = this.listaFotos.get(position);

		Uri uriFoto = Uri.parse(fotoSelecionada.getImage());

		Intent intent = new Intent(this, VisualizarFotoActivity.class);
		intent.setData(uriFoto);

		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_fotos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent(this, EnviaImagemActivity.class);
		this.startActivityForResult(i, 0);

		return true;
	}

}
