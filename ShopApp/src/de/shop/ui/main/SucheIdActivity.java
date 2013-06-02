package de.shop.ui.main;

import static android.app.SearchManager.QUERY;
import static de.shop.util.Constants.KUNDE_KEY;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import de.shop.data.Kunde;
import de.shop.service.KundeService;
import de.shop.service.KundeService.KundeServiceBinder;
import de.shop.util.InternalShopError;

public class SucheIdActivity extends Activity {
	private static final String LOG_TAG = SucheIdActivity.class.getSimpleName();
	
	private String query;
	private KundeServiceBinder kundeServiceBinder;
	
	private ServiceConnection serviceConnection; 

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.search);

		final Intent intent = getIntent();
		if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
			return;
		}
	
		query = intent.getStringExtra(QUERY);
		Log.d(LOG_TAG, query);
		
		createServiceConnection();
	}
	
	private void createServiceConnection() {
		Log.v(LOG_TAG, "createServiceConnection()");
		serviceConnection = new ServiceConnection() {
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
				Log.v(LOG_TAG, "onServiceConnected()");
				kundeServiceBinder = (KundeServiceBinder) serviceBinder;
			}
	
			@Override
			public void onServiceDisconnected(ComponentName name) {
				kundeServiceBinder = null;
			}
		};
	}
	
	// TODO Methodenname onResume() Folie 199
	@Override
	public void onStart() {
		Log.v(LOG_TAG, "onStart()");
		final Intent intent = new Intent(this, KundeService.class);
		final boolean succes = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
		if (!succes) {
			throw new InternalShopError("bindService() von SucheIdActivity funktioniert nicht für KundeService");
		}
		// TODO kundeServiceBinder wird nicht gesetzt und bleibt null: WARUM ???
		
		suchen();

		super.onStart();
	}
	// TODO Methodenname onPause() Folie 200
	@Override
	public void onStop() {
		unbindService(serviceConnection);
		super.onStop();
	}
	
	private void suchen() {
		Log.v(LOG_TAG, "suchen()");
		final Long kundeId = Long.valueOf(query);
		final Kunde kunde = kundeServiceBinder.sucheKundeById(kundeId, this).resultObject;
		Log.d(LOG_TAG, kunde.toString());		
		
		final Intent intent = new Intent(this, Main.class);
		if (kunde != null) {
			intent.putExtra(KUNDE_KEY, kunde);
		}
		startActivity(intent);
	}
}
