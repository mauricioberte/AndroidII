package com.unochapeco.androidii;

import java.util.ArrayList;
import java.util.List;

import com.unochapeco.androidii.adapter.ListaFotosAdapter;
import com.unochapeco.androidii.task.ListarFotosTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.Menu;

public class ListaFotosActivity extends ListActivity {
	
	private List<Fotos> listaFotos;
	private ListaFotosAdapter adapter;
	private ListarFotosTask listarTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_fotos);
		
		this.listaFotos = new ArrayList<Fotos>();
        this.adapter = new ListaFotosAdapter(this, 
        		R.layout.layout_item_lista, listaFotos);
        
        this.setListAdapter(adapter);
        
        this.listarTask = new ListarFotosTask(this);
        this.listarTask.execute();
	}
	
	public void receberRetornoLista(List<Fotos> fotos){
		this.listaFotos.clear();
		this.listaFotos.addAll(fotos);
		this.adapter.notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_fotos, menu);
		return true;
	}

}
