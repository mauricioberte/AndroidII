package com.unochapeco.androidii.adapter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.unochapeco.androidii.Fotos;
import com.unochapeco.androidii.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
		
		new loadImagem( fotos.getImage(), image, 100, 100 ).execute();
		
		return item;
	}
	
	
	/*
	 * Classe respons‡vel por carregar a imagem, adaptar ao tamanho informado e setar a imageView no layout
	 * ƒ ass’ncrona por isso as imagens podem demorar mais tempo
	 * para carregar depois que Ž exibido o nome das mesmas
	 */
	public class loadImagem extends AsyncTask<String, Integer, Bitmap>{
		
		private String imageUri;
		private ImageView imageView;
	    private int padraoWidth = 80;
	    private int padraoHeight = 80;
	    
	    
	    public loadImagem(String uri, ImageView imageView, int escalaWidth, int escalaHeight ){
	    	this.imageUri = uri;
	    	this.imageView = imageView;
	    	this.padraoWidth = escalaWidth;
	    	this.padraoHeight = escalaHeight;
	    }
	    
		@Override
		public Bitmap doInBackground(String... params) {
			
			if( imageUri == null ) return null;
			
			String url = imageUri.toString();
			if( url.length() == 0 ) return null;
			
			HttpGet httpGet = new HttpGet(url);
			DefaultHttpClient client = new DefaultHttpClient();
			
			try{
				
				HttpResponse response = client.execute( httpGet );		
				InputStream is = new BufferedInputStream( response.getEntity().getContent() );
				
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				
		        if( padraoWidth > 0 && padraoHeight > 0 && bitmap.getWidth() > padraoWidth && bitmap.getHeight() > padraoHeight ) {
		            return Bitmap.createScaledBitmap(bitmap, padraoWidth, padraoHeight, false);
		        } else {
		            return bitmap;
		        }
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
	    public void onPostExecute( Bitmap drawable ) {
	        imageView.setImageBitmap( drawable );
	    }
	}
	


}
