package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class Adresse implements JsonMappable, Serializable {
	private static final long serialVersionUID = -4218760204888865981L;
	
	public Long id;
	public int version;
	public String plz;
	public String stadt;
	public String strasse;
	public Integer hausnummer;
	public String land;

	public Adresse() {
		super();
	}
	
	public Adresse(Long id, String plz, String stadt, String strasse, Integer hausnummer, String land) {
		super();
		this.id = id;
		this.plz = plz;
		this.stadt = stadt;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.land = land;
	}
	
	// fuer AbstractKunde.toJsonObject()
	JsonObjectBuilder getJsonBuilderFactory() {
		return jsonBuilderFactory.createObjectBuilder()
								.add("id", id)
		                         .add("version", version)
		                         .add("plz", plz)
		                         .add("stadt", stadt)
		                         .add("strasse", strasse)
		                         .add("hausnummer", hausnummer)
		                         .add("land", land);
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonBuilderFactory().build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {	
		Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		version = jsonObject.getInt("version");
		plz = jsonObject.getString("plz");
		stadt = jsonObject.getString("stadt");
		strasse = jsonObject.getString("strasse");
		hausnummer = jsonObject.getInt("hausnummer");
	}
	
	@Override
	public void updateVersion() {
		version++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hausnummer == null) ? 0 : hausnummer.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((land == null) ? 0 : land.hashCode());
		result = prime * result + ((plz == null) ? 0 : plz.hashCode());
		result = prime * result + ((stadt == null) ? 0 : stadt.hashCode());
		result = prime * result + ((strasse == null) ? 0 : strasse.hashCode());
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
		final Adresse other = (Adresse) obj;
		if (hausnummer == null) {
			if (other.hausnummer != null)
				return false;
		} 
		else if (!hausnummer.equals(other.hausnummer))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} 
		else if (!id.equals(other.id))
			return false;
		if (land == null) {
			if (other.land != null)
				return false;
		} 
		else if (!land.equals(other.land))
			return false;
		if (plz == null) {
			if (other.plz != null)
				return false;
		} 
		else if (!plz.equals(other.plz))
			return false;
		if (stadt == null) {
			if (other.stadt != null)
				return false;
		} 
		else if (!stadt.equals(other.stadt))
			return false;
		if (strasse == null) {
			if (other.strasse != null)
				return false;
		} 
		else if (!strasse.equals(other.strasse))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Adresse [id=" + id + ", version=" + version
				+ ", hausnummer=" + hausnummer + ", land="
				+ land + ", plz=" + plz + ", stadt=" + stadt + ", strasse="
				+ strasse + "]";
	}
}
