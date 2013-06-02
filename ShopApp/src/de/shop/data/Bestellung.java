package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;
import java.util.Date;

import javax.json.JsonObject;


public class Bestellung implements JsonMappable, Serializable {
	private static final long serialVersionUID = -3227854872557641281L;
	
	public Long id;
	public int version;
	public Date erzeugt;

	public Bestellung() {
		super();
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
		                         .add("erzeugt", erzeugt.getTime())
		                         .build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("id").longValue());
		version = jsonObject.getInt("version");
		erzeugt = new Date(jsonObject.getJsonNumber("erzeugt").longValue());
	}
	
	@Override
	public void updateVersion() {
		version++;
	}

	@Override
	public String toString() {
		return "Bestellung [id=" + id + ", erzeugt=" + erzeugt + "]";
	}
}
