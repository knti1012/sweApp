package de.shop.service;

import static android.app.ProgressDialog.STYLE_SPINNER;
import static de.shop.ui.main.Prefs.mock;
import static de.shop.util.Constants.KUNDEN_PATH;
import static de.shop.ui.main.Prefs.timeout;
import static de.shop.util.Constants.KUNDEN_ID_PREFIX_PATH;
import static de.shop.util.Constants.LOCALHOST;
import static de.shop.util.Constants.LOCALHOST_EMULATOR;
import static de.shop.util.Constants.NACHNAME_PATH;
import static de.shop.util.Constants.NACHNAME_PREFIX_PATH;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import de.shop.R;
import de.shop.data.Kunde;
import de.shop.util.InternalShopError;

public class KundeService extends Service {
	private static final String LOG_TAG = KundeService.class.getSimpleName();
	
	private final KundeServiceBinder binder = new KundeServiceBinder();
	

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class KundeServiceBinder extends Binder {
		
		public KundeService getService() {
			return KundeService.this;
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
		
		public HttpResponse<Kunde> sucheKundeById(Long id, final Context ctx) {
			
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "Kunde"
			final AsyncTask<Long, Void, HttpResponse<Kunde>> sucheKundeByIdTask = new AsyncTask<Long, Void, HttpResponse<Kunde>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kunde> doInBackground(Long... ids) {
					final Long id = ids[0];
		    		final String path = KUNDEN_PATH + "/" + id;
		    		Log.v(LOG_TAG, "path = " + path);
		    		
		    		final HttpResponse<Kunde> result;// = mock
//		    				                                   ? Mock.sucheKundeById(id)
//		    				                                	: WebServiceClient.getJsonSingle(path, Kunde.class);
		    		if (mock) {
		    			result = Mock.sucheKundeById(id);
		    		}
		    		else {
		    			result = WebServiceClient.getJsonSingle(path, Kunde.class);
		    		}

					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kunde> unused) {
					progressDialog.dismiss();
	    		}
			};

    		sucheKundeByIdTask.execute(id);
    		HttpResponse<Kunde> result = null;
	    	try {
	    		result = sucheKundeByIdTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	    	
    		if (result.responseCode != HTTP_OK) {
	    		return result;
		    }
    		
    		setBestellungenUri(result.resultObject);
		    return result;
		}
		
		private void setBestellungenUri(Kunde kunde) {
			// URLs der Bestellungen fuer Emulator anpassen
			final String bestellungenUri = kunde.bestellungenUri;
			if (!TextUtils.isEmpty(bestellungenUri)) {
			    kunde.bestellungenUri = bestellungenUri.replace(LOCALHOST, LOCALHOST_EMULATOR);
			}
		}
		
		public HttpResponse<Kunde> sucheKundenByNachname(String nachname, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "String", Resultat vom Typ "List<Kunde>"
			final AsyncTask<String, Void, HttpResponse<Kunde>> sucheKundenByNameTask = new AsyncTask<String, Void, HttpResponse<Kunde>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected HttpResponse<Kunde> doInBackground(String... nachnamen) {
					final String nachname = nachnamen[0];
					final String path = KUNDEN_PATH + "/" + nachname;
					Log.v(LOG_TAG, "path = " + path);
		    		final HttpResponse<Kunde> result = mock
		    				                                   ? Mock.sucheKundenByNachname(nachname)
		    				                                   : WebServiceClient.getJsonList(path, Kunde.class);
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(HttpResponse<Kunde> unused) {
					progressDialog.dismiss();
	    		}
			};
			
			sucheKundenByNameTask.execute(nachname);
			HttpResponse<Kunde> result = null;
			try {
				result = sucheKundenByNameTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}

	    	if (result.responseCode != HTTP_OK) {
	    		return result;
	    	}
	    	
	    	final ArrayList<Kunde> kunden = result.resultList;
	    	// URLs fuer Emulator anpassen
	    	for (Kunde k : kunden) {
	    		setBestellungenUri(k);
	    	}
			return result;
	    }	
	

		public List<Long> sucheBestellungenIdsByKundeId(Long id, final Context ctx) {
			// (evtl. mehrere) Parameter vom Typ "Long", Resultat vom Typ "List<Long>"
			final AsyncTask<Long, Void, List<Long>> sucheBestellungenIdsByKundeIdTask = new AsyncTask<Long, Void, List<Long>>() {
				@Override
	    		protected void onPreExecute() {
					progressDialog = showProgressDialog(ctx);
				}
				
				@Override
				// Neuer Thread, damit der UI-Thread nicht blockiert wird
				protected List<Long> doInBackground(Long... ids) {
					final Long kundeId = ids[0];
			    	final String path = KUNDEN_PATH + "/" + kundeId + "/bestellungenIds";
					Log.v(LOG_TAG, "path = " + path);
					final List<Long> result;
//													? Mock.sucheBestellungenIdsByKundeId(kundeId)
//													: WebServiceClient.getJsonLongList(path);
					if (mock) {
		    			result = Mock.sucheBestellungenIdsByKundeId(kundeId);
		    		}
		    		else {
		    			result = WebServiceClient.getJsonLongList(path);
		    		}
					
					Log.d(LOG_TAG + ".AsyncTask", "doInBackground: " + result);
					return result;
				}
				
				@Override
	    		protected void onPostExecute(List<Long> ids) {
					progressDialog.dismiss();
	    		}
			};
			

			Log.v(LOG_TAG, "kundeID= "+id);
			sucheBestellungenIdsByKundeIdTask.execute(id);
			List<Long> bestellungIds = null;
			try {
				bestellungIds = sucheBestellungenIdsByKundeIdTask.get(timeout, SECONDS);
			}
	    	catch (Exception e) {
	    		throw new InternalShopError(e.getMessage(), e);
			}
	
			return bestellungIds;
	    }
		
		public List<Long> sucheIds(String prefix) {
			final String path = KUNDEN_ID_PREFIX_PATH + "/" + prefix;
		    Log.v(LOG_TAG, "sucheIds: path = " + path);

    		final List<Long> ids = mock
   				                   ? Mock.sucheKundeIdsByPrefix(prefix)
   				                   : WebServiceClient.getJsonLongList(path);

			Log.d(LOG_TAG, "sucheIds: " + ids.toString());
			return ids;
		}
		
		/**
		 * Annahme: wird ueber AutoCompleteTextView aufgerufen, wobei die dortige Methode
		 * performFiltering() schon einen neuen Worker-Thread startet, so dass AsyncTask hier
		 * ueberfluessig ist.
		 */
		public List<String> sucheNachnamen(String prefix) {
			final String path = NACHNAME_PREFIX_PATH +  "/" + prefix;
		    Log.v(LOG_TAG, "sucheNachnamen: path = " + path);

    		final List<String> nachnamen = mock
    				                       ? Mock.sucheNachnamenByPrefix(prefix)
    				                       : WebServiceClient.getJsonStringList(path);
			Log.d(LOG_TAG, "sucheNachnamen: " + nachnamen);

			return nachnamen;
		}
		
	}
}
