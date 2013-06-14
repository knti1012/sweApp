package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import android.util.Log;
import de.shop.util.InternalShopError;


public class Kunde implements JsonMappable, Serializable {
	private static final long serialVersionUID = 1293068472891525321L;
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public Long id;
	public int version;
	public String nachname;
	public String vorname;
	public String email;
	public String geschlecht;
	public boolean agbAkzeptiert = true;
//	public Date erzeugt;
	public String bestellungenUri;
	public Adresse adresse;
	
	protected JsonObjectBuilder getJsonObjectBuilder() {
		return jsonBuilderFactory.createObjectBuilder()
				.add("id", id)
				.add("version", version)
				.add("nachname", nachname)
				.add("vorname", vorname)
				.add("email", email)
				.add("geschlecht", geschlecht)
				.add("agbAkzeptiert", agbAkzeptiert)
//				.add("erzeugt", new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(erzeugt))
				.add("bestellungenUri", bestellungenUri)
				.add("adresse", adresse.getJsonBuilderFactory());
											
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonObjectBuilder().build();
	}

	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		version = jsonObject.getInt("version");
		nachname = jsonObject.getString("nachname");
		vorname = jsonObject.getString("vorname");
		email = jsonObject.getString("email");
		adresse = new Adresse();
		adresse.fromJsonObject(jsonObject.getJsonObject("adresse"));
		Log.v("Adresse", "adresse = " + this.adresse.toString());
		agbAkzeptiert = jsonObject.getBoolean("agbAkzeptiert");
		geschlecht = jsonObject.getString("geschlecht");
		Log.v("Kunde", "kunde ohne bestellungen = " + this.toString());
//		try {
//			Log.v("Kunde", "Vor erzeugt: "+erzeugt);
//			erzeugt = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).parse(jsonObject.getString("erzeugt"));
//			Log.v("Kunde", "Nach erzeugt: "+erzeugt);
//		}
//		catch (ParseException e) {
//			throw new InternalShopError(e.getMessage(), e);
//		};
		String x = jsonObject.getString("bestellungen");
		Log.v("Kunde json objekt", x);
		bestellungenUri = x;
		Log.v("Kunde", "kunde mit bestellungen= " + this.toString());
	}
	
	@Override
	public void updateVersion() {
		version++;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kunde other = (Kunde) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Kunde [id=" + id + ", version=" + version
				+ ", email=" + email + ", geschlecht="
				+ geschlecht + ", nachname=" + nachname + ", password="
				+ ", vorname=" + vorname + "]";
	}
}
