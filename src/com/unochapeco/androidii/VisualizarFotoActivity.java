package com.unochapeco.androidii;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.ImageView;

public class VisualizarFotoActivity extends Activity {
	
	private ImageView imgFoto;
	private LoadImagem loadImagem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualizar_foto);
		
		Intent intent = getIntent();
		Uri imagemFoto = intent.getData();
		
		imgFoto = (ImageView) findViewById(R.id.imgFoto);
		
		this.loadImagem = new LoadImagem( imagemFoto.toString(), imgFoto, 400, 500 );
		this.loadImagem.execute();
		
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.visualizar_foto, menu);
		return true;
	}

}
