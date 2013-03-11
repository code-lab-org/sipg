package edu.mit.sips.io;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.mit.sips.core.City;
import edu.mit.sips.core.Country;
import edu.mit.sips.core.LifecycleModel;
import edu.mit.sips.core.Region;
import edu.mit.sips.core.Society;
import edu.mit.sips.core.agriculture.AgricultureElement;
import edu.mit.sips.core.agriculture.AgricultureSystem;
import edu.mit.sips.core.energy.ElectricityElement;
import edu.mit.sips.core.energy.ElectricitySystem;
import edu.mit.sips.core.energy.EnergyElement;
import edu.mit.sips.core.energy.EnergySystem;
import edu.mit.sips.core.energy.PetroleumElement;
import edu.mit.sips.core.energy.PetroleumSystem;
import edu.mit.sips.core.social.PopulationModel;
import edu.mit.sips.core.social.SocialSystem;
import edu.mit.sips.core.water.WaterElement;
import edu.mit.sips.core.water.WaterSystem;

/**
 * The Class Serialization.
 */
public final class Serialization {
	private static final GsonBuilder gsonBuilder = new GsonBuilder();
	
	static {
		gsonBuilder.registerTypeAdapter(LifecycleModel.class, 
				new InterfaceAdapter<LifecycleModel>());
		gsonBuilder.registerTypeAdapter(PopulationModel.class, 
				new InterfaceAdapter<PopulationModel>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.class, 
				new InterfaceAdapter<AgricultureSystem>());
		gsonBuilder.registerTypeAdapter(WaterSystem.class, 
				new InterfaceAdapter<WaterSystem>());
		gsonBuilder.registerTypeAdapter(EnergySystem.class, 
				new InterfaceAdapter<EnergySystem>());
		gsonBuilder.registerTypeAdapter(PetroleumSystem.class, 
				new InterfaceAdapter<PetroleumSystem>());
		gsonBuilder.registerTypeAdapter(ElectricitySystem.class, 
				new InterfaceAdapter<ElectricitySystem>());
		gsonBuilder.registerTypeAdapter(SocialSystem.class, 
				new InterfaceAdapter<SocialSystem>());
		gsonBuilder.registerTypeAdapter(Society.class, 
				new InterfaceAdapter<Society>());
		gsonBuilder.registerTypeAdapter(AgricultureElement.class, 
				new InterfaceAdapter<AgricultureElement>());
		gsonBuilder.registerTypeAdapter(WaterElement.class, 
				new InterfaceAdapter<WaterElement>());
		gsonBuilder.registerTypeAdapter(EnergyElement.class, 
				new InterfaceAdapter<EnergyElement>());
		gsonBuilder.registerTypeAdapter(PetroleumElement.class, 
				new InterfaceAdapter<PetroleumElement>());
		gsonBuilder.registerTypeAdapter(ElectricityElement.class, 
				new InterfaceAdapter<ElectricityElement>());
	}
	
	private static Gson gson = gsonBuilder.create();
	
	/**
	 * Serialize.
	 *
	 * @param country the country
	 * @return the string
	 */
	public static String serialize(Country country) {
		return getGson().toJson(country);
	}
	
	/**
	 * Deserialize.
	 *
	 * @param json the json
	 * @return the country
	 */
	public static Country deserialize(String json) {
		Country country = getGson().fromJson(json, Country.class);
		replaceCircularReferences(country);
		return country;
	}
	
	/**
	 * Gets the gson.
	 *
	 * @return the gson
	 */
	private static Gson getGson() {
		if(gson == null) {
			gson = gsonBuilder.create();
		}
		return gson;
	}
	
	/**
	 * Replace circular references.
	 *
	 * @param system the system
	 */
	public static void replaceCircularReferences(Society society) {
		if(society instanceof Country) {
			replaceCircularReferences((Country)society);
		} else if(society instanceof Region) {
			replaceCircularReferences((Region)society);
		} else if(society instanceof City) {
			replaceCircularReferences((City)society);
		}
	}
	
	/**
	 * Replace circular references.
	 *
	 * @param country the country
	 */
	private static void replaceCircularReferences(Country country) {
		country.getAgricultureSystem().setSociety(country);
		country.getWaterSystem().setSociety(country);
		country.getEnergySystem().setSociety(country);
		for(Society nestedSociety : country.getNestedSocieties()) {
			nestedSociety.setSociety(country);
			replaceCircularReferences(nestedSociety);
		}
	}
	
	/**
	 * Replace circular references.
	 *
	 * @param region the region
	 */
	private static void replaceCircularReferences(Region region) {
		region.getAgricultureSystem().setSociety(region);
		region.getWaterSystem().setSociety(region);
		region.getEnergySystem().setSociety(region);
		for(Society nestedSociety : region.getNestedSocieties()) {
			nestedSociety.setSociety(region);
			replaceCircularReferences(nestedSociety);
		}
	}
	
	/**
	 * Replace circular references.
	 *
	 * @param city the city
	 */
	private static void replaceCircularReferences(City city) {
		city.getAgricultureSystem().setSociety(city);
		city.getWaterSystem().setSociety(city);
		city.getEnergySystem().setSociety(city);
	}
	
	/**
	 * Instantiates a new serialization.
	 */
	private Serialization() {
		
	}
	
	/**
	 * The Class InterfaceAdapter.
	 * 
	 * Written by Maciek Makowski and published on StackOverflow.
	 * http://stackoverflow.com/questions/4795349/how-to-serialize-a-class-with-an-interface
	 * 
	 *
	 * @param <T> the generic type
	 */
	private static class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
	    
	    /* (non-Javadoc)
	     * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	     */
	    public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
	        final JsonObject wrapper = new JsonObject();
	        wrapper.addProperty("type", object.getClass().getName());
	        wrapper.add("data", context.serialize(object));
	        return wrapper;
	    }

	    /* (non-Javadoc)
	     * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	     */
	    public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
	        final JsonObject wrapper = (JsonObject) elem;
	        final JsonElement typeName = get(wrapper, "type");
	        final JsonElement data = get(wrapper, "data");
	        final Type actualType = typeForName(typeName); 
	        return context.deserialize(data, actualType);
	    }

	    /**
	     * Type for name.
	     *
	     * @param typeElem the type elem
	     * @return the type
	     */
	    private Type typeForName(final JsonElement typeElem) {
	        try {
	            return Class.forName(typeElem.getAsString());
	        } catch (ClassNotFoundException e) {
	            throw new JsonParseException(e);
	        }
	    }

	    /**
	     * Gets the.
	     *
	     * @param wrapper the wrapper
	     * @param memberName the member name
	     * @return the json element
	     */
	    private JsonElement get(final JsonObject wrapper, String memberName) {
	        final JsonElement elem = wrapper.get(memberName);
	        if (elem == null) throw new JsonParseException("no '" + memberName + "' member found in what was expected to be an interface wrapper");
	        return elem;
	    }
	}
}
