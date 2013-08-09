package com.unochapeco.androidii.adapter;


import java.util.List;
import com.unochapeco.androidii.Fotos;
import com.unochapeco.androidii.LoadImagem;
import com.unochapeco.androidii.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListaFotosAdapter extends ArrayAdapter<Fotos>{

	private Context context;
	private int layout;
	private List<Fotos> listaFotos;
	private LoadImagem loadImagem;
	
	public ListaFotosAdapter(Context context, int textViewResourceId,
			List<Fotos> objects) {
		super(context, textViewResourceId, objects);

		this.context = context;
		this.layout = textViewResourceId;
		this.listaFotos = objects;
	}
	
	
	/*
	 * Popula cada item da listView
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inflater.inflate(layout, null);
		
		Fotos fotos = listaFotos.get(position);
		
		TextView name = (TextView) item.findViewById(R.id.textNameImage);
		name.setText(fotos.getName());
		
		ImageView image = (ImageView) item.findViewById(R.id.imageThumb);
		
		//new loadImagem( fotos.getImage(), image, 100, 100 ).execute();
		
		this.loadImagem = new LoadImagem(fotos.getImage(), image, 100, 100 );
		this.loadImagem.execute();
		
		return item;
	}
	
}
