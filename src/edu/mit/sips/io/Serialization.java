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

import edu.mit.sips.core.Country;
import edu.mit.sips.core.InfrastructureSystem;
import edu.mit.sips.core.LifecycleModel;
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
		gsonBuilder.registerTypeAdapter(AgricultureSystem.Local.class, 
				new InterfaceAdapter<AgricultureSystem.Local>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.Remote.class, 
				new InterfaceAdapter<AgricultureSystem.Remote>());
		gsonBuilder.registerTypeAdapter(WaterSystem.Local.class, 
				new InterfaceAdapter<WaterSystem.Local>());
		gsonBuilder.registerTypeAdapter(WaterSystem.Remote.class, 
				new InterfaceAdapter<WaterSystem.Remote>());
		gsonBuilder.registerTypeAdapter(EnergySystem.Local.class, 
				new InterfaceAdapter<EnergySystem.Local>());
		gsonBuilder.registerTypeAdapter(EnergySystem.Remote.class, 
				new InterfaceAdapter<EnergySystem.Remote>());
		gsonBuilder.registerTypeAdapter(PetroleumSystem.class, 
				new InterfaceAdapter<PetroleumSystem>());
		gsonBuilder.registerTypeAdapter(ElectricitySystem.class, 
				new InterfaceAdapter<ElectricitySystem>());
		gsonBuilder.registerTypeAdapter(SocialSystem.Local.class, 
				new InterfaceAdapter<SocialSystem.Local>());
		gsonBuilder.registerTypeAdapter(SocialSystem.Remote.class, 
				new InterfaceAdapter<SocialSystem.Remote>());
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
		recursiveReplaceCircularReferences(country);
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
	 * Recursive replace circular references.
	 *
	 * @param society the society
	 */
	private static void recursiveReplaceCircularReferences(Society society) {
		for(InfrastructureSystem system : society.getInfrastructureSystems()) {
			system.setSociety(society);
		}
		for(Society nestedSociety : society.getNestedSocieties()) {
			nestedSociety.setSociety(society);
			recursiveReplaceCircularReferences(nestedSociety);
		}
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
