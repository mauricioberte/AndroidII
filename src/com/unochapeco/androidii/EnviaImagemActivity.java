package com.unochapeco.androidii;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class EnviaImagemActivity extends Activity {
	
	private ImageView imageView;
	private Button btnEnvia, btnCancelar, btnCamera, btnGaleria;
	private Bitmap bitmap;
	private ProgressDialog dialog;
	Uri imagemUri;
	private static final int ORIG_IMAGEM = 1;
	private static final int ORIG_IMAGEM_CAMERA = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_envia_imagem);
		
		imageView = (ImageView) findViewById(R.id.imageViewEnvia);
		btnCamera = (Button) findViewById(R.id.btnCamera);
		btnGaleria = (Button) findViewById(R.id.btnGaleria);
		btnEnvia = (Button) findViewById(R.id.btnEnviar);
		btnCancelar = (Button) findViewById(R.id.btnCancelar);
		
		btnGaleria.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
		        try {
					Intent gintent = new Intent();
					gintent.setType("image/*");
					gintent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(Intent.createChooser(gintent, "Selecionar Imagem"), ORIG_IMAGEM);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
					e.getMessage(),
					Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
		});
		
		btnCamera.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
	        	//nome da foto para salvar
	        	String nomeFoto = System.currentTimeMillis()+".jpg";
	
	        	ContentValues values = new ContentValues();
	        	values.put(MediaStore.Images.Media.TITLE, nomeFoto);
	        	values.put(MediaStore.Images.Media.DESCRIPTION,"Imagem capturada da C‰mera");
	        	//salva para uso posterior
	        	imagemUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	        	//cria novo Intent
	        	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        	intent.putExtra(MediaStore.EXTRA_OUTPUT, imagemUri);
	        	intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
	        	startActivityForResult(intent, ORIG_IMAGEM_CAMERA);
			}
		});
		
		btnEnvia.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(),
							"Selecione uma imagem", Toast.LENGTH_SHORT).show();
				} else {
					dialog = ProgressDialog.show(EnviaImagemActivity.this, "Enviando",
							"Aguarde...", true);
					
					new EnviaImagemTask().execute();
				}
			}
		});
		
		btnCancelar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onFinish();
			}
		});
	}
	
	
	class EnviaImagemTask extends AsyncTask<Void, Void, String> {

		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(Void... unsued) {
				InputStream is;
			    BitmapFactory.Options bfo;
			    Bitmap bitmapOrg;
			    ByteArrayOutputStream bao ;
			   
			    bfo = new BitmapFactory.Options();
			    bfo.inSampleSize = 2;
			      
			    bao = new ByteArrayOutputStream();
			    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
				byte [] ba = bao.toByteArray();
				String ba1 = Base64.encodeBytes(ba);
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image",ba1));
				nameValuePairs.add(new BasicNameValuePair("cmd","image_android"));
	       
				try{
				        HttpClient httpclient = new DefaultHttpClient();
				        HttpPost httppost = new HttpPost("http://www.grupoinmove.com.br/webservice/upload/index.php");
				        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				        HttpResponse response = httpclient.execute(httppost);
				        HttpEntity entity = response.getEntity();
				        is = entity.getContent();

				   }catch(Exception e){
				        Log.v("log_tag", "Falha na conex‹o "+e.toString());
				   }
			return "Successo";
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing())dialog.dismiss();
									
					runOnPostExecute();
					
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}
	
	public void runOnPostExecute(){
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Sua imagem foi enviada!")
        .setMessage("Deseja enviar outra?")
        .setPositiveButton("Sim", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	imageView.setImageBitmap(null);
            	bitmap = null;   
            }
        })
        .setNegativeButton("N‹o", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	onFinish();  
            }
        })
        .show();
    }
	
	public void onFinish(){
		Intent intent = new Intent(this, ListaFotosActivity.class);
		
		startActivity(intent);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri imagemUriSelecionada = null;
		String caminhoArquivo = null;
		switch (requestCode) {
				case ORIG_IMAGEM:
					if (resultCode == Activity.RESULT_OK) {
						imagemUriSelecionada = data.getData();
					}
					break;
				case ORIG_IMAGEM_CAMERA:
					 if (resultCode == RESULT_OK) {
		 		        //usado para acessar a imagem
						 imagemUriSelecionada = imagemUri;
	
				    } else if (resultCode == RESULT_CANCELED) {
		 		        Toast.makeText(this, "Imagem n‹o obtida", Toast.LENGTH_SHORT).show();
		 		    } else {
		 		    	Toast.makeText(this, "Imagem n‹o obtida", Toast.LENGTH_SHORT).show();
		 		    }
					 break;
			}
		
			if(imagemUriSelecionada != null){
					try {
						// File manager
						String filemanagerstring = imagemUriSelecionada.getPath();
			
						// Galeria
						String selectedImagePath = getPath(imagemUriSelecionada);
			
						if (selectedImagePath != null) {
							caminhoArquivo = selectedImagePath;
						} else if (filemanagerstring != null) {
							caminhoArquivo = filemanagerstring;
						} else {
							Toast.makeText(getApplicationContext(), "Caminho desconhecido",
									Toast.LENGTH_LONG).show();
							Log.e("Bitmap", "Caminho desconhecido");
						}
			
						if (caminhoArquivo != null) {
							decodeFile(caminhoArquivo);
						} else {
							bitmap = null;
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Erro interno",
								Toast.LENGTH_LONG).show();
						Log.e(e.getClass().getName(), e.getMessage(), e);
					}
			}
	}
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	
	public void decodeFile(String filePath) {

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		final int REQUIRED_SIZE = 1024;

		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		imageView.setImageBitmap(bitmap);
	}

}
