package de.shop.service;

import static de.shop.ShopApp.jsonReaderFactory;
import static de.shop.ui.main.Prefs.username;
import static de.shop.util.Constants.ARTIKEL_PATH;
import static de.shop.util.Constants.KUNDEN_PATH;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;

import android.text.TextUtils;
import android.util.Log;
import de.shop.R;
import de.shop.ShopApp;
import de.shop.data.Artikel;
import de.shop.data.Kunde;
import de.shop.data.Bestellung;
import de.shop.util.InternalShopError;

final class Mock {
	private static final String LOG_TAG = Mock.class.getSimpleName();
	
	private static String read(int dateinameId) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(ShopApp.open(dateinameId)));
    	final StringBuilder sb = new StringBuilder();
    	try {
    		for (;;) {
				final String line = reader.readLine();
				if (line == null) {
					break;
				}
				sb.append(line);
			}
		}
    	catch (IOException e) {
    		throw new InternalShopError(e.getMessage(), e);
		}
    	finally {
    		if (reader != null) {
    			try {
					reader.close();
				}
    			catch (IOException e) {}
    		}
    	}
    	
    	final String jsonStr = sb.toString();
    	Log.v(LOG_TAG, "read: jsonStr = " + jsonStr);
		return jsonStr;
	}
	
	static HttpResponse<Kunde> sucheKundeById(Long id) {
    	if (id <= 0 || id >= 1000) {
    		return new HttpResponse<Kunde>(HTTP_NOT_FOUND, "Kein Kunde gefunden mit ID " + id);
    	}
    	
    	int dateinameId;
    	
    	dateinameId = R.raw.mock_kunde;
    	
    	
    	final String jsonStr = read(dateinameId);
    	JsonReader jsonReader = null;
    	JsonObject jsonObject;
    	try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonObject = jsonReader.readObject();
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    		}
    	}
    	
    	final Kunde kunde = new Kunde();

    	kunde.fromJsonObject(jsonObject);
    	kunde.id = id;
    	
    	Log.v(LOG_TAG, "Mock-SucheKunde-ByID-kunde = " + kunde.toString());
		
    	final HttpResponse<Kunde> result = new HttpResponse<Kunde>(HTTP_OK, jsonObject.toString(), kunde);
    	return result;
	}

	static HttpResponse<Kunde> sucheKundenByNachname(String nachname) {
		if (nachname.startsWith("X")) {
			return new HttpResponse<Kunde>(HTTP_NOT_FOUND, "Keine Kunde gefunden mit Nachname " + nachname);
		}
		
		final ArrayList<Kunde> kunden = new ArrayList<Kunde>();
		final String jsonStr = read(R.raw.mock_kunden);
		JsonReader jsonReader = null;
    	JsonArray jsonArray;
    	try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonArray = jsonReader.readArray();
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    		}
    	}
		
    	final List<JsonObject> jsonObjectList = jsonArray.getValuesAs(JsonObject.class);
   		for (JsonObject jsonObject : jsonObjectList) {
   			final Kunde kunde = new Kunde();
   			
			kunde.fromJsonObject(jsonObject);
			kunde.nachname = nachname;
   			kunden.add(kunde);
   		}
    	
    	final HttpResponse<Kunde> result = new HttpResponse<Kunde>(HTTP_OK, jsonArray.toString(), kunden);
		return result;
    }

	static List<Long> sucheBestellungenIdsByKundeId(Long id) {
//		------------ Original
//		if (id % 2 == 0) {
//			return Collections.emptyList();
//		}
//		
//		final int anzahl = (int) ((id % 3) + 3);  // 3 - 5 Bestellungen
//		final List<Long> ids = new ArrayList<Long>(anzahl);
//		
//		// Bestellung IDs sind letzte Dezimalstelle, da 3-5 Bestellungen (s.o.)
//		// Kunde-ID wird vorangestellt und deshalb mit 10 multipliziert
//		for (int i = 0; i < anzahl; i++) {
//			ids.add((long) (id * 10 + 2 * i + 1));
//		}
//		return ids;
		
		JsonObject jsonObject = mockBestellung(id);
		
    	final Bestellung bestellung = new Bestellung();

    	bestellung.fromJsonObject(jsonObject);
    	Log.v(LOG_TAG, "sucheBestellungenIdsByKundeId-Zuweisung von Json zu Bestellung");
    	bestellung.kunde = id;
    	
    	Log.v(LOG_TAG, "sucheBestellungenIdsByKundeId = " + bestellung.toString());
		
    	final List<Long> result = new ArrayList<Long>();
    	
    	result.add(bestellung.id);
//    	for (int i = 0; i < jsonObject.size(); i++) {
//			result.add(bestellung.id);
//		}
		Log.v(LOG_TAG, "result: " + result.toString());
    	return result;
    }

    static List<Long> sucheKundeIdsByPrefix(String kundeIdPrefix) {
		int dateinameId = -1;
    	if ("1".equals(kundeIdPrefix)) {
    		dateinameId = R.raw.mock_ids_1;
        	Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_1");
    	}
    	else if ("10".equals(kundeIdPrefix)) {
    		dateinameId = R.raw.mock_ids_10;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_10");
    	}
    	else if ("11".equals(kundeIdPrefix)) {
    		dateinameId = R.raw.mock_ids_11;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_11");
    	}
    	else if ("2".equals(kundeIdPrefix)) {
    		dateinameId = R.raw.mock_ids_2;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_2");
    	}
    	else if ("20".equals(kundeIdPrefix)) {
    		dateinameId = R.raw.mock_ids_20;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_20");
    	}
    	else {
    		Log.v(LOG_TAG, "dateinameId = emptyList");
    		return Collections.emptyList();
    	}
    	
    	final String jsonStr = read(dateinameId);
		JsonReader jsonReader = null;
    	JsonArray jsonArray;
    	try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonArray = jsonReader.readArray();
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    		}
    	}
    	
    	final List<Long> result = new ArrayList<Long>(jsonArray.size());
    	final List<JsonNumber> jsonNumberList = jsonArray.getValuesAs(JsonNumber.class);
	    for (JsonNumber jsonNumber : jsonNumberList) {
	    	final Long id = Long.valueOf(jsonNumber.longValue());
	    	result.add(id);
    	}
    	
    	Log.d(LOG_TAG, "ids= " + result.toString());
    	
    	return result;
    }

    static List<String> sucheNachnamenByPrefix(String nachnamePrefix) {
    	if (TextUtils.isEmpty(nachnamePrefix)) {
    		return Collections.emptyList();
    	}
    	
		int dateinameNachnamen = -1;
		if (nachnamePrefix.startsWith("T")) {
    		dateinameNachnamen = R.raw.mock_nachnamen_t;
    	}
    	else if (nachnamePrefix.startsWith("M")) {
    		dateinameNachnamen = R.raw.mock_nachnamen_m;
    	}
    	else {
    		return Collections.emptyList();
    	}
    	
    	final String jsonStr = read(dateinameNachnamen);
		JsonReader jsonReader = null;
    	JsonArray jsonArray;
    	try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonArray = jsonReader.readArray();
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    		}
    	}
    	
    	final List<JsonString> jsonStringList = jsonArray.getValuesAs(JsonString.class);
    	final List<String> result = new ArrayList<String>(jsonArray.size());
	    for (JsonString jsonString : jsonStringList) {
	    	final String nachname = jsonString.getString();
	    	result.add(nachname);
	    }
		
    	Log.d(LOG_TAG, "nachnamen= " + result.toString());
    	return result;
    }
    
    static HttpResponse<Kunde> createKunde(Kunde kunde) {
    	kunde.id = Long.valueOf(kunde.nachname.length());  // Anzahl der Buchstaben des Nachnamens als emulierte neue ID
    	Log.d(LOG_TAG, "createKunde: " + kunde);
    	Log.d(LOG_TAG, "createKunde: " + kunde.toJsonObject());
    	final HttpResponse<Kunde> result = new HttpResponse<Kunde>(HTTP_CREATED, KUNDEN_PATH + "/1", kunde);
    	return result;
    }

    static HttpResponse<Kunde> updateKunde(Kunde kunde) {
    	Log.d(LOG_TAG, "updateKunde: " + kunde);
    	
    	if (TextUtils.isEmpty(username)) {
    		return new HttpResponse<Kunde>(HTTP_UNAUTHORIZED, null);
    	}
    	
    	if ("x".equals(username)) {
    		return new HttpResponse<Kunde>(HTTP_FORBIDDEN, null);
    	}
    	
    	if ("y".equals(username)) {
    		return new HttpResponse<Kunde>(HTTP_CONFLICT, "Die Email-Adresse existiert bereits");
    	}
    	
    	Log.d(LOG_TAG, "updateKunde: " + kunde.toJsonObject());
    	return new HttpResponse<Kunde>(HTTP_NO_CONTENT, null, kunde);
    }

    static HttpResponse<Void> deleteKunde(Long kundeId) {
    	Log.d(LOG_TAG, "deleteKunde: " + kundeId);
    	return new HttpResponse<Void>(HTTP_NO_CONTENT, null);
    }

    static HttpResponse<Bestellung> sucheBestellungById(Long id) {
    	
    	JsonObject jsonObject = mockBestellung(id);
    	
    	final Bestellung bestellung = new Bestellung();

    	bestellung.fromJsonObject(jsonObject);
    	Log.v(LOG_TAG, "SucheBestellungById-Zuweisung von Json zu Bestellung");
    	
    	final HttpResponse<Bestellung> result = new HttpResponse<Bestellung>(HTTP_OK, jsonObject.toString(), bestellung);
    	Log.d(LOG_TAG, result.resultObject.toString());
		return result;
		
//    	---- Original
//		final Bestellung bestellung = new Bestellung(id, new Date());
//		
//		final JsonObject jsonObject = bestellung.toJsonObject();
//		final HttpResponse<Bestellung> result = new HttpResponse<Bestellung>(HTTP_OK, jsonObject.toString(), bestellung);
//		Log.d(LOG_TAG, result.resultObject.toString());
//		return result;
	}
    
    static JsonObject mockBestellung (Long id) {
    	
    	int dateinameId;
		
		dateinameId = R.raw.mock_bestellung;
		final String jsonStr = read(dateinameId);
		JsonReader jsonReader = null;
		JsonObject jsonObject;
		try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonObject = jsonReader.readObject();
    		Log.v(LOG_TAG, "mockBestellung-jsonObject: "+ jsonObject.toString());	
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    	    	Log.v(LOG_TAG, "mockBestellung-jsonReader geschlossen!!!");    			
    		}
    	}
    	
    	return jsonObject;
    }
    
    static HttpResponse<Artikel> sucheArtikelById(Long id) {
    	if (id <= 0 || id >= 10000) {
    		return new HttpResponse<Artikel>(HTTP_NOT_FOUND, "Kein Artikel gefunden mit ID " + id);
    	}
    	
    	int dateinameId;
    	
    	dateinameId = R.raw.mock_artikel;
    	
    	
    	final String jsonStr = read(dateinameId);
    	Log.v(LOG_TAG, "jsonStr = Dateiname = "+dateinameId);
    	JsonReader jsonReader = null;
    	JsonObject jsonObject;
    	try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonObject = jsonReader.readObject();
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    		}
    	}
    	
    	final Artikel artikel = new Artikel();

    	artikel.fromJsonObject(jsonObject);
    	artikel.id = id;
    	
    	Log.v(LOG_TAG, "Mock-SucheArtikel-ByID-artikel = " + artikel.toString());
		
    	final HttpResponse<Artikel> result = new HttpResponse<Artikel>(HTTP_OK, jsonObject.toString(), artikel);
    	return result;
	}
    
    static List<Long> sucheArtikelIdsByPrefix(String artikelIdPrefix) {
		int dateinameId = -1;
    	if ("1".equals(artikelIdPrefix)) {
    		dateinameId = R.raw.mock_ids_1;
        	Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_1");
    	}
    	else if ("10".equals(artikelIdPrefix)) {
    		dateinameId = R.raw.mock_ids_10;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_10");
    	}
    	else if ("11".equals(artikelIdPrefix)) {
    		dateinameId = R.raw.mock_ids_11;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_11");
    	}
    	else if ("2".equals(artikelIdPrefix)) {
    		dateinameId = R.raw.mock_ids_2;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_2");
    	}
    	else if ("20".equals(artikelIdPrefix)) {
    		dateinameId = R.raw.mock_ids_20;
    		Log.v(LOG_TAG, "dateinameId = R.raw.mock_ids_20");
    	}
    	else {
    		Log.v(LOG_TAG, "dateinameId = emptyList");
    		return Collections.emptyList();
    	}
    	
    	final String jsonStr = read(dateinameId);
		JsonReader jsonReader = null;
    	JsonArray jsonArray;
    	try {
    		jsonReader = jsonReaderFactory.createReader(new StringReader(jsonStr));
    		jsonArray = jsonReader.readArray();
    	}
    	finally {
    		if (jsonReader != null) {
    			jsonReader.close();
    		}
    	}
    	
    	final List<Long> result = new ArrayList<Long>(jsonArray.size());
    	final List<JsonNumber> jsonNumberList = jsonArray.getValuesAs(JsonNumber.class);
	    for (JsonNumber jsonNumber : jsonNumberList) {
	    	final Long id = Long.valueOf(jsonNumber.longValue());
	    	result.add(id);
    	}
    	
    	Log.d(LOG_TAG, "ids= " + result.toString());
    	
    	return result;
    }
    
    static HttpResponse<Artikel> createArtikel(Artikel artikel) {
    	artikel.id = Long.valueOf(artikel.name.length());  // Anzahl der Buchstaben der Bezeichnung als emulierte neue ID
    	Log.d(LOG_TAG, "createArtikel: " + artikel);
    	Log.d(LOG_TAG, "createArtikel: " + artikel.toJsonObject());
    	final HttpResponse<Artikel> result = new HttpResponse<Artikel>(HTTP_CREATED, ARTIKEL_PATH + "/1", artikel);
    	return result;
    }
    
    private Mock() {}
}
