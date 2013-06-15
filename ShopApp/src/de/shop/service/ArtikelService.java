package de.shop.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.shop.ui.main.Prefs.mock;
import static de.shop.ui.main.Prefs.timeout;
import static de.shop.util.Constants.ARTIKEL_ID_PREFIX_PATH;
import static de.shop.util.Constants.ARTIKEL_PATH;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;

import de.shop.R;
import de.shop.data.Artikel;
import de.shop.util.InternalShopError;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ArtikelService extends Service {
	private static final String LOG_TAG = ArtikelService.class.getSimpleName();
	
	private final ArtikelServiceBinder binder = new ArtikelServiceBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class ArtikelServiceBinder extends Binder {
		
		public ArtikelService getService() {
			return ArtikelService.this;
		}
		
		private ProgressDialog progressDialog;
		private ProgressDialog showProgressDialog(Context ctx) {
			progressDialog = new ProgressDialog(ctx);
			progressDialog.setProgressStyle(STYLE_SPINNER);
			progressDialog.setMessage(getString(R.string.s_bitte_warten));
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(true);
			progressDialog.show();
			return progressDialog;
		}
		
		public HttpResponse<Artikel> sucheArtikelById(Long id, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Kunde"
			final AsyncTask<Long, Void, HttpResponse<Artikel>> sucheArtikelByIdTask = new AsyncTask<Long, Void, HttpResponse<Artikel>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Artikel> doInBackground(Long... ids) {
					final Long id = ids[0];
		    		final String path = ARTIKEL_PATH + "/" + id;
		    		Log.v(LOG_TAG, "path = " + path);
		    		
		    		final HttpResponse<Artikel> result;
		    		
		    		if (mock) {
		    			result = Mock.sucheArtikelById(id);
		    		}
		    		else {
		    			result = WebServiceClient.getJsonSingle(path, Artikel.class);
		    		}

					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
					progressDialog.dismiss();
	    		}
			};

    		sucheArtikelByIdTask.execute(id);
    		HttpResponse<Artikel> result = null;
	    	try {
	    		result = sucheArtikelByIdTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	    	
    		if (result.responseCode != HTTP_OK) {
	    		return result;
		    }
    		
		    return result;
		}
		
		public List<Long> sucheIds(String prefix) {
			final String path = ARTIKEL_ID_PREFIX_PATH + "/" + prefix;
		    Log.v(LOG_TAG, "sucheIds: path = " + path);

    		final List<Long> ids = mock
   				                   ? Mock.sucheArtikelIdsByPrefix(prefix)
   				                   : WebServiceClient.getJsonLongList(path);

			Log.d(LOG_TAG, "sucheIds: " + ids.toString());
			return ids;
		}
		
		
		public HttpResponse<Artikel> createArtikel(Artikel artikel, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "Artikel", Resultat vom Typ "void"
			final AsyncTask<Artikel, Void, HttpResponse<Artikel>> createArtikelTask = new AsyncTask<Artikel, Void, HttpResponse<Artikel>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Artikel> doInBackground(Artikel... artikels) {
					final Artikel artikel = artikels[0];
		    		final String path = ARTIKEL_PATH;
		    		Log.v(LOG_TAG, "path = " + path);

		    		final HttpResponse<Artikel> result = mock
                                                               ? Mock.createArtikel(artikel)
                                                               : WebServiceClient.postJson(artikel, path);
		    		
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Artikel> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			createArtikelTask.execute(artikel);
			HttpResponse<Artikel> response = null; 
			try {
				response = createArtikelTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
			
			//eigentlich: 
			//artikel.id = Long.valueOf(response.content);
			artikel.id = Long.valueOf(response.content.length());
			final HttpResponse<Artikel> result = new HttpResponse<Artikel>(response.responseCode, response.content, artikel);
			return result;
	    }
	}
}
