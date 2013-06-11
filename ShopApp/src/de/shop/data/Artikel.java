package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

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
	public BigInteger lagerbestand;
	public String name;
	public Date erzeugt;
	public Double preis;

	public Artikel() {
		super();
	}

	public Artikel(Long id, String art, String farbe, String groesse,
			String kategorie, BigInteger lagerbestand, String name,
			Date erzeugt, Double preis) {
		super();
		this.id = id;
		this.art = art;
		this.farbe = farbe;
		this.groesse = groesse;
		this.kategorie = kategorie;
		this.lagerbestand = lagerbestand;
		this.name = name;
		this.erzeugt = erzeugt;
		this.preis = preis;
	}



	@Override
	public JsonObject toJsonObject() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("id", id)
		                         .add("version", version)
		                         .add("art", art)
		                         .add("farbe", farbe)
		                         .add("groesse", groesse)
		                         .add("kategorie", kategorie)
		                         .add("lagerbestand", lagerbestand)
		                         .add("name", name)
		                         .add("erzeugt", new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(erzeugt))
		                         .add("preis", preis)
		                         .build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		Log.v("Artikel", "fromJsonObject !!! ID ");
		version = jsonObject.getInt("version");
		Log.v("Artikel", "fromJsonObject !!! Version ");
		try {
			erzeugt = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(jsonObject.getString("erzeugt"));
		}
		catch (ParseException e) {
			throw new InternalShopError(e.getMessage(), e);
		};
		Log.v("Artikel", "fromJsonObject !!! Erzeugt ");
		farbe = jsonObject.getString("farbe");
		Log.v("Artikel", "fromJsonObject !!! Farbe ");
		groesse = jsonObject.getString("groesse");
		Log.v("Artikel", "fromJsonObject !!! Groesse ");
		kategorie = jsonObject.getString("kategorie");
		Log.v("Artikel", "fromJsonObject !!! Kategorie ");
		lagerbestand = BigInteger.valueOf(jsonObject.getJsonNumber("lagerbestand").longValue());
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
				+ name + ", erzeugt=" + erzeugt + ", preis=" + preis + "]";
	}
}
