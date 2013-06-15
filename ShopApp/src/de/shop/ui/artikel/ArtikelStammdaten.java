package de.shop.ui.artikel;

import static de.shop.util.Constants.ARTIKEL_KEY;
import static de.shop.util.Constants.KUNDE_KEY;
import de.shop.R;
import de.shop.data.Artikel;
import de.shop.ui.main.Prefs;
import de.shop.util.WischenListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

public class ArtikelStammdaten extends Fragment implements OnTouchListener {
private static final String LOG_TAG = ArtikelStammdaten.class.getSimpleName();
	
	private Artikel artikel;
	private GestureDetector gestureDetector;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        artikel = (Artikel) getArguments().get(ARTIKEL_KEY);
        Log.d(LOG_TAG, "ArtikelStammdaten, OnCreateView "+ artikel.toString());

        // Voraussetzung fuer onOptionsItemSelected()
        setHasOptionsMenu(true);
        
		// attachToRoot = false, weil die Verwaltung des Fragments durch die Activity erfolgt
		return inflater.inflate(R.layout.artikel_stammdaten, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.v(LOG_TAG, "onViewCreated = " + artikel.toString());
		fillValues(view);
    	
    	final Activity activity = getActivity();
	    final OnGestureListener onGestureListener = new WischenListener(activity);
	    gestureDetector = new GestureDetector(activity, onGestureListener);  // Context und OnGestureListener als Argumente
	    view.setOnTouchListener(this);
    }
	
	private void fillValues(View view) {
		Log.v(LOG_TAG, "fillValues- ArtikelID = " + artikel.id.toString());
		final TextView txtId = (TextView) view.findViewById(R.id.artikel_id);
    	txtId.setText(artikel.id.toString());
    	
    	Log.v(LOG_TAG, "fillValues- Name = " + artikel.name);
    	final TextView txtName = (TextView) view.findViewById(R.id.name_txt);
    	txtName.setText(artikel.name);
    	
    	Log.v(LOG_TAG, "fillValues- Art = " + artikel.art);
    	final TextView txtArt = (TextView) view.findViewById(R.id.art_txt);
    	txtArt.setText(artikel.art);
    	
    	Log.v(LOG_TAG, "fillValues- Farbe = " + artikel.farbe);
    	final TextView txtFarbe = (TextView) view.findViewById(R.id.farbe_txt);
    	txtFarbe.setText(artikel.farbe);
    	
    	Log.v(LOG_TAG, "fillValues- Groesse = " + artikel.groesse);
    	final TextView txtGroesse = (TextView) view.findViewById(R.id.groesse_txt);
    	txtGroesse.setText(artikel.groesse);	
    	
    	Log.v(LOG_TAG, "fillValues- Kategorie = " + artikel.kategorie);
    	final TextView txtKategorie = (TextView) view.findViewById(R.id.kategorie_txt);
    	txtKategorie.setText(artikel.kategorie);
    	
    	Log.v(LOG_TAG, "fillValues- Lagerbestand = " + artikel.lagerbestand);
    	final TextView txtLagerbestand = (TextView) view.findViewById(R.id.lagerbestand_txt);
    	txtLagerbestand.setText(artikel.lagerbestand.toString());
    	
    	Log.v(LOG_TAG, "fillValues- Preis = " + artikel.preis);
    	final TextView txtPreis = (TextView) view.findViewById(R.id.preis_txt);
    	txtPreis.setText(artikel.preis.toString());
    	
//    	Log.v(LOG_TAG, "fillValues- Erzeugt = " + artikel.erzeugt);
//    	final TextView txtErzeugt = (TextView) view.findViewById(R.id.erzeugt_txt);
//    	final String erzeugtStr = DateFormat.getDateFormat(view.getContext()).format(artikel.erzeugt);
//    	txtErzeugt.setText(erzeugtStr);    	
	}

	@Override
	// http://developer.android.com/guide/topics/ui/actionbar.html#ChoosingActionItems :
	// "As a general rule, all items in the options menu (let alone action items) should have a global impact on the app,
	//  rather than affect only a small portion of the interface."
	// Nur aufgerufen, falls setHasOptionsMenu(true) in onCreateView() aufgerufen wird
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.artikel_stammdaten_options, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.einstellungen) {
			getFragmentManager().beginTransaction()
                                    .replace(R.id.details, new Prefs())
                                    .addToBackStack(null)
                                    .commit();
				return true;
		}
		else {
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	// OnTouchListener
	public boolean onTouch(View view, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
}
