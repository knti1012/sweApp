package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import static de.shop.ui.main.Prefs.mock;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.json.JsonObject;

import de.shop.util.InternalShopError;
import android.util.Log;


public class Bestellung implements JsonMappable, Serializable {
	private static final long serialVersionUID = -3227854872557641281L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public Long id;
	public int version;
	public Date erzeugt;
	public Long kunde;

	public Bestellung() {
		super();
	}

	public Bestellung(long id, Date erzeugt, Long kunde) {
		super();
		this.id = id;
		this.erzeugt = erzeugt;
		this.kunde = kunde;
	}
	
	public Bestellung(long id, Date erzeugt) {
		super();
		this.id = id;
		this.erzeugt = erzeugt;
	}

	@Override
	public JsonObject toJsonObject() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("id", id)
		                         .add("version", version)
		                         .add("datum", erzeugt.getTime())
		                         .add("kunde", kunde)
		                         .build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		Log.v("Bestellung", "fromJsonObject !!! ID ");
		version = jsonObject.getInt("version");
		Log.v("Bestellung", "fromJsonObject !!! Version ");
		final Date datum = new Date(Long.valueOf(jsonObject.getJsonNumber("datum").longValue()));
		Log.v("Datum", datum.toString());
		try {
			erzeugt =  datum;//new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(jsonObject.getString("datum"));
		}
		catch (Exception e) {
			throw new InternalShopError(e.getMessage(), e);
		};
		Log.v("Bestellung", "fromJsonObject !!! Erzeugt ");
		if (mock) {
			kunde = Long.valueOf(jsonObject.getString("kunde"));
			Log.v("Bestellung", "fromJsonObject !!! Kunde ");
		}
	}
	
	@Override
	public void updateVersion() {
		version++;
	}

	@Override
	public String toString() {
		return "Bestellung [id=" + id + ", erzeugt=" + erzeugt + ", version=" + version +", kunde=" + kunde +"]";
	}
}
