package de.shop.ui.artikel;

import static android.view.inputmethod.EditorInfo.IME_NULL;
import static de.shop.util.Constants.ARTIKEL_KEY;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

import java.util.Date;

import de.shop.R;
import de.shop.data.Artikel;
import de.shop.service.HttpResponse;
import de.shop.ui.main.Main;
import de.shop.ui.main.Prefs;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ArtikelNeu extends Fragment implements OnClickListener, OnEditorActionListener {
	private static final String LOG_TAG = ArtikelNeu.class.getSimpleName();
	
	private EditText editName;
	private EditText editArt;
	private EditText editGroesse;
	private EditText editKategorie;
	private EditText editPreis;
	private EditText editLagerbestand;
	private EditText editFarbe;
	private InputMethodManager imm; // = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	
	private Artikel neuerArtikel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		Log.v(LOG_TAG, "!!! onCreateView !!!");
		return inflater.inflate(R.layout.artikel_neu, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		editName = (EditText) view.findViewById(R.id.name_edt);
		editArt = (EditText) view.findViewById(R.id.art_edt);
		editFarbe = (EditText) view.findViewById(R.id.farbe_edt);
		editGroesse = (EditText) view.findViewById(R.id.groesse_edt);
		editKategorie = (EditText) view.findViewById(R.id.kategorie_edt);
		editLagerbestand = (EditText) view.findViewById(R.id.lagerbestand_edt);
		editPreis = (EditText) view.findViewById(R.id.preis_edt);
		
//		editName.requestFocus();
//		imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.showSoftInput(editName, InputMethodManager.SHOW_IMPLICIT);
//		imm.showSoftInput(editArt, InputMethodManager.SHOW_IMPLICIT);
//		imm.showSoftInput(editFarbe, InputMethodManager.SHOW_IMPLICIT);
//		imm.showSoftInput(editGroesse, InputMethodManager.SHOW_IMPLICIT);
//		imm.showSoftInput(editKategorie, InputMethodManager.SHOW_IMPLICIT);
//		imm.showSoftInput(editLagerbestand, InputMethodManager.SHOW_IMPLICIT);
//		imm.showSoftInput(editPreis, InputMethodManager.SHOW_IMPLICIT);
		
		final Main mainActivity = (Main) getActivity();
		
		mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
//		---------->Kein Erscheinen der Tastatur bei Tippen auf die Eingabefelder <----------
		
//    	final ArrayAdapter<Long> adapter = new ArrayAdapter<Long>(artikelIdTxt.getContext());
//    	artikelIdTxt.setAdapter(adapter);
//    	editName.setOnEditorActionListener(this);
		
//		---------->Kein Erscheinen der Tastatur bei Tippen auf die Eingabefelder <----------
		
    	// ArtikelNeu (this) ist gleichzeitig der Listener, wenn der Create-Button angeklickt wird
		// und implementiert deshalb die Methode onClick() unten
		view.findViewById(R.id.btn_artikel_anlegen).setOnClickListener(this);
		
		Log.v(LOG_TAG, "!!! onViewCreated !!!");
	    // Evtl. vorhandene Tabs der ACTIVITY loeschen
    	final ActionBar actionBar = getActivity().getActionBar();
    	actionBar.setDisplayShowTitleEnabled(true);
    	actionBar.removeAllTabs();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.einstellungen:
			getFragmentManager().beginTransaction()
                                .replace(R.id.details, new Prefs())
                                .addToBackStack(null)
                                .commit();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override  // OnEditorActionListener
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (actionId == R.id.ime_anlegen || actionId == IME_NULL) {
			anlegen(view);
			return true;
		}
		
		return false;
	}

	@Override // OnClickListener
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_artikel_anlegen:
			anlegen(view);
			break;
			
		default:
			break;
		}
	}
	
	public void anlegen(View view) {
		final Context ctx = view.getContext();
		
		final String artikelNameStr = editName.getText().toString();
		if (TextUtils.isEmpty(artikelNameStr)) {
			editName.setError(getString(R.string.a_name_fehlt));
			return;
		}
		final String artikelArtStr = editArt.getText().toString();
		if (TextUtils.isEmpty(artikelArtStr)) {
			editArt.setError(getString(R.string.a_art_fehlt));
			return;
		}
		final String artikelFarbeStr = editFarbe.getText().toString();
		if (TextUtils.isEmpty(artikelFarbeStr)) {
			editFarbe.setError(getString(R.string.a_farbe_fehlt));
			return;
		}
		final String artikelGroesseStr = editGroesse.getText().toString();
		if (TextUtils.isEmpty(artikelFarbeStr)) {
			editGroesse.setError(getString(R.string.a_groesse_fehlt));
			return;
		}
		final String artikelKategorieStr = editKategorie.getText().toString();
		if (TextUtils.isEmpty(artikelKategorieStr)) {
			editKategorie.setError(getString(R.string.a_kategorie_fehlt));
			return;
		}
		final String artikelLagerbestandStr = editLagerbestand.getText().toString();
		if (TextUtils.isEmpty(artikelLagerbestandStr)) {
			editLagerbestand.setError(getString(R.string.a_lagerbestand_fehlt));
			return;
		}
		final String artikelPreisStr = editPreis.getText().toString();
		if (TextUtils.isEmpty(artikelLagerbestandStr)) {
			editPreis.setError(getString(R.string.a_preis_fehlt));
			return;
		}
		final Double artikelPreis = Double.valueOf(artikelPreisStr);
		final Date erzeugt = new Date();
		final Long id = Long.valueOf(artikelNameStr.length());
		neuerArtikel = new Artikel(id, artikelArtStr, artikelFarbeStr, artikelGroesseStr, 
				artikelKategorieStr, artikelLagerbestandStr, artikelNameStr, erzeugt, artikelPreis);
		
		Log.v(LOG_TAG, "!!! anlegen() !!!");
		final Main mainActivity = (Main) getActivity();
		final HttpResponse<? extends Artikel> result = mainActivity.getArtikelServiceBinder().createArtikel(neuerArtikel, ctx);
		
		if (result.responseCode != HTTP_CREATED) {
			final String msg = getString(R.string.a_artikel_nicht_erstellt);
			editName.setError(msg);
			return;
		}
		
		final Artikel artikel = result.resultObject;
		final Bundle args = new Bundle(1);
		args.putSerializable(ARTIKEL_KEY, artikel);
		
		final Fragment neuesFragment = new ArtikelDetails();
		neuesFragment.setArguments(args);
		
//		// Kein Name (null) fuer die Transaktion, da die Klasse BackStageEntry nicht verwendet wird
		getFragmentManager().beginTransaction()
		                    .replace(R.id.details, neuesFragment)
		                    .addToBackStack(null)
		                    .commit();
	}
	
}
