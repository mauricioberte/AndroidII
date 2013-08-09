package com.unochapeco.androidii;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LoadImagem extends AsyncTask<String, Integer, Bitmap>{
	
	private String imageUri;
	private ImageView imageView;
    private int padraoWidth = 80;
    private int padraoHeight = 80;
    
    
    public LoadImagem(String uri, ImageView imageView, int escalaWidth, int escalaHeight ){
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
