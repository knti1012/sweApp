package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import static de.shop.ui.main.Prefs.mock;
import java.io.Serializable;

import javax.json.JsonObject;

import de.shop.util.InternalShopError;
import android.util.Log;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Artikel implements JsonMappable, Serializable  {
	private static final long serialVersionUID = -3227854872557641281L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public Long id;
	public int version;
	public String art;
	public String farbe;
	public String groesse;
	public String kategorie;
	public Long lagerbestand;
	public String name;
	public Double preis;

	public Artikel() {
		super();
	}

	public Artikel(Long id, String art, String farbe, String groesse,
			String kategorie, Long lagerbestand, String name,
			Double preis) {
		super();
		this.id = id;
		this.art = art;
		this.farbe = farbe;
		this.groesse = groesse;
		this.kategorie = kategorie;
		this.lagerbestand = lagerbestand;
		this.name = name;
		this.preis = preis;
	}



	@Override
	public JsonObject toJsonObject() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("version", version)
		                         .add("art", art)
		                         .add("farbe", farbe)
		                         .add("groesse", groesse)
		                         .add("kategorie", kategorie)
		                         .add("lagerbestand", lagerbestand)
		                         .add("name", name)
		                         .add("preis", preis)
		                         .build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		Log.v("Artikel", "fromJsonObject !!! ID ");
		version = jsonObject.getInt("version");
		Log.v("Artikel", "fromJsonObject !!! Version ");
		art = jsonObject.getString("art");
		Log.v("Artikel", "fromJsonObject !!! Art ");
		farbe = jsonObject.getString("farbe");
		Log.v("Artikel", "fromJsonObject !!! Farbe ");
		groesse = jsonObject.getString("groesse");
		Log.v("Artikel", "fromJsonObject !!! Groesse ");
		kategorie = jsonObject.getString("kategorie");
		Log.v("Artikel", "fromJsonObject !!! Kategorie ");
		try {
			lagerbestand = Long.valueOf(jsonObject.getJsonNumber("lagerbestand").longValue());
		}
		catch (Exception e) {
			throw new InternalShopError(e.getMessage(), e);
		};
//		lagerbestand = jsonObject.getString("lagerbestand");
		Log.v("Artikel", "fromJsonObject !!! Lagerbestand ");
		name = jsonObject.getString("name");
		Log.v("Artikel", "fromJsonObject !!! Name ");
		preis = Double.valueOf(jsonObject.getJsonNumber("preis").doubleValue());
		Log.v("Artikel", "fromJsonObject !!! Preis ");
	}
	
	@Override
	public void updateVersion() {
		version++;
	}

	@Override
	public String toString() {
		return "Artikel [id=" + id + ", version=" + version + ", art=" + art
				+ ", farbe=" + farbe + ", groesse=" + groesse + ", kategorie="
				+ kategorie + ", lagerbestand=" + lagerbestand + ", name="
				+ name + ", preis=" + preis + "]";
	}
}
