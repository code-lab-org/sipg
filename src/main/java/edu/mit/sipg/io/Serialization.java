/******************************************************************************
 * Copyright 2020 Paul T. Grogan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.mit.sipg.io;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.mit.sipg.core.City;
import edu.mit.sipg.core.Country;
import edu.mit.sipg.core.Society;
import edu.mit.sipg.core.agriculture.AgricultureElement;
import edu.mit.sipg.core.agriculture.AgricultureSoS;
import edu.mit.sipg.core.agriculture.AgricultureSystem;
import edu.mit.sipg.core.agriculture.DefaultAgricultureSystem;
import edu.mit.sipg.core.agriculture.EditableAgricultureElement;
import edu.mit.sipg.core.base.InfrastructureSystem;
import edu.mit.sipg.core.electricity.DefaultElectricitySystem;
import edu.mit.sipg.core.electricity.EditableElectricityElement;
import edu.mit.sipg.core.electricity.ElectricityElement;
import edu.mit.sipg.core.electricity.ElectricitySoS;
import edu.mit.sipg.core.electricity.ElectricitySystem;
import edu.mit.sipg.core.lifecycle.EditableSimpleLifecycleModel;
import edu.mit.sipg.core.lifecycle.LifecycleModel;
import edu.mit.sipg.core.petroleum.DefaultPetroleumSystem;
import edu.mit.sipg.core.petroleum.EditablePetroleumElement;
import edu.mit.sipg.core.petroleum.PetroleumElement;
import edu.mit.sipg.core.petroleum.PetroleumSoS;
import edu.mit.sipg.core.petroleum.PetroleumSystem;
import edu.mit.sipg.core.price.PriceModel;
import edu.mit.sipg.core.social.DefaultSocialSystem;
import edu.mit.sipg.core.social.SocialSoS;
import edu.mit.sipg.core.social.SocialSystem;
import edu.mit.sipg.core.social.demand.DemandModel;
import edu.mit.sipg.core.social.population.PopulationModel;
import edu.mit.sipg.core.water.DefaultWaterSystem;
import edu.mit.sipg.core.water.EditableWaterElement;
import edu.mit.sipg.core.water.WaterElement;
import edu.mit.sipg.core.water.WaterSoS;
import edu.mit.sipg.core.water.WaterSystem;
import edu.mit.sipg.scenario.ElementTemplate;
import edu.mit.sipg.scenario.Scenario;
import edu.mit.sipg.sim.hla.HlaAgricultureSystem;
import edu.mit.sipg.sim.hla.HlaElectricitySystem;
import edu.mit.sipg.sim.hla.HlaPetroleumSystem;
import edu.mit.sipg.sim.hla.HlaSocialSystem;
import edu.mit.sipg.sim.hla.HlaWaterSystem;

/**
 * The Class Serialization.
 */
public final class Serialization {
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

		@Override
		public T deserialize(JsonElement elem, Type interfaceType, JsonDeserializationContext context) throws JsonParseException {
			final JsonObject wrapper = (JsonObject) elem;
			final JsonElement typeName = get(wrapper, "type");
			final JsonElement data = get(wrapper, "data");
			final Type actualType = typeForName(typeName); 
			return context.deserialize(data, actualType);
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

		@Override
		public JsonElement serialize(T object, Type interfaceType, JsonSerializationContext context) {
			final JsonObject wrapper = new JsonObject();
			wrapper.addProperty("type", object.getClass().getName());
			wrapper.add("data", context.serialize(object));
			return wrapper;
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
	}

	private static final GsonBuilder gsonBuilder = new GsonBuilder();

	static {
		gsonBuilder.registerTypeAdapter(LifecycleModel.class, 
				new InterfaceAdapter<LifecycleModel>());
		gsonBuilder.registerTypeAdapter(PopulationModel.class, 
				new InterfaceAdapter<PopulationModel>());
		gsonBuilder.registerTypeAdapter(DemandModel.class, 
				new InterfaceAdapter<DemandModel>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.class, 
				new InterfaceAdapter<AgricultureSystem>());
		gsonBuilder.registerTypeAdapter(AgricultureSystem.Local.class, 
				new InterfaceAdapter<AgricultureSystem.Local>());
		gsonBuilder.registerTypeAdapter(AgricultureSoS.Local.class, 
				new InterfaceAdapter<AgricultureSoS.Local>());
		gsonBuilder.registerTypeAdapter(AgricultureSoS.class, 
				new InterfaceAdapter<AgricultureSoS>());
		gsonBuilder.registerTypeAdapter(WaterSystem.class, 
				new InterfaceAdapter<WaterSystem>());
		gsonBuilder.registerTypeAdapter(WaterSystem.Local.class, 
				new InterfaceAdapter<WaterSystem.Local>());
		gsonBuilder.registerTypeAdapter(WaterSoS.Local.class, 
				new InterfaceAdapter<WaterSoS.Local>());
		gsonBuilder.registerTypeAdapter(WaterSoS.class, 
				new InterfaceAdapter<WaterSoS>());
		gsonBuilder.registerTypeAdapter(PetroleumSystem.class, 
				new InterfaceAdapter<PetroleumSystem>());
		gsonBuilder.registerTypeAdapter(PetroleumSoS.class, 
				new InterfaceAdapter<PetroleumSoS>());
		gsonBuilder.registerTypeAdapter(ElectricitySystem.class, 
				new InterfaceAdapter<ElectricitySystem>());
		gsonBuilder.registerTypeAdapter(ElectricitySoS.class, 
				new InterfaceAdapter<ElectricitySoS>());
		gsonBuilder.registerTypeAdapter(SocialSystem.class, 
				new InterfaceAdapter<SocialSystem>());
		gsonBuilder.registerTypeAdapter(SocialSystem.Local.class, 
				new InterfaceAdapter<SocialSystem.Local>());
		gsonBuilder.registerTypeAdapter(SocialSoS.class, 
				new InterfaceAdapter<SocialSoS>());
		gsonBuilder.registerTypeAdapter(Society.class, 
				new InterfaceAdapter<Society>());
		gsonBuilder.registerTypeAdapter(AgricultureElement.class, 
				new InterfaceAdapter<AgricultureElement>());
		gsonBuilder.registerTypeAdapter(WaterElement.class, 
				new InterfaceAdapter<WaterElement>());
		gsonBuilder.registerTypeAdapter(PetroleumElement.class, 
				new InterfaceAdapter<PetroleumElement>());
		gsonBuilder.registerTypeAdapter(ElectricityElement.class, 
				new InterfaceAdapter<ElectricityElement>());
		gsonBuilder.registerTypeAdapter(PriceModel.class, 
				new InterfaceAdapter<PriceModel>());
		gsonBuilder.registerTypeAdapter(Scenario.class, 
				new InterfaceAdapter<Scenario>());
		gsonBuilder.registerTypeAdapter(ElementTemplate.class, 
				new InterfaceAdapter<ElementTemplate>());
	}

	private static Gson gson = gsonBuilder.create();

	/**
	 * Deserialize.
	 *
	 * @param json the json
	 * @return the country
	 */
	public static Scenario deserialize(String json) {
		Scenario scenario = getGson().fromJson(json, ScenarioWrapper.class).scenario;
		Country country = scenario.getCountry();
		recursiveReplaceCircularReferences(country);
		// replace HLA datatypes; they will need to be re-created
		// to get the proper data members
		for(City city : country.getCities()) {
			if(city.getAgricultureSystem() instanceof HlaAgricultureSystem) {
				city.setAgricultureSystem(new DefaultAgricultureSystem());
			}
			if(city.getWaterSystem() instanceof HlaWaterSystem) {
				city.setWaterSystem(new DefaultWaterSystem());
			}
			if(city.getPetroleumSystem() instanceof HlaPetroleumSystem) {
				city.setPetroleumSystem(new DefaultPetroleumSystem());
			}
			if(city.getElectricitySystem() instanceof HlaElectricitySystem) {
				city.setElectricitySystem(new DefaultElectricitySystem());
			}
			if(city.getSocialSystem() instanceof HlaSocialSystem) {
				city.setSocialSystem(new DefaultSocialSystem());
			}
		}
		for(City city : country.getCities()) {
			if(city.getAgricultureSystem() instanceof AgricultureSystem.Local) {
				List<AgricultureElement> elements = new ArrayList<AgricultureElement>(
						((AgricultureSystem.Local)city.getAgricultureSystem()).getInternalElements());
				for(AgricultureElement element : elements) {
					((AgricultureSystem.Local)city.getAgricultureSystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					EditableAgricultureElement mutable = (EditableAgricultureElement) element.getMutableElement();
					EditableAgricultureElement template = (EditableAgricultureElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
									((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeCommissionStart(), 
									element.getOrigin(), element.getDestination()).getMutableElement();
					((EditableSimpleLifecycleModel)template.getLifecycleModel()).setOperationDuration(
							((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationDuration());
					mutable.setDistributionEfficiency(template.getDistributionEfficiency());
					mutable.setLifecycleModel(template.getLifecycleModel());
					mutable.setMaxFoodInput(template.getMaxFoodInput());
					mutable.setMaxLandArea(template.getMaxLandArea());
					mutable.setCostIntensityOfLandUsed(template.getCostIntensityOfLandUsed());
					mutable.setLaborIntensityOfLandUsed(template.getLaborIntensityOfLandUsed());
					mutable.setFoodIntensityOfLandUsed(template.getFoodIntensityOfLandUsed());
					mutable.setWaterIntensityOfLandUsed(template.getWaterIntensityOfLandUsed());
					mutable.setVariableOperationsCostOfFoodDistribution(template.getVariableOperationsCostOfFoodDistribution());
					((AgricultureSystem.Local)city.getAgricultureSystem()).addElement(mutable.createElement());
				}

			}
			if(city.getWaterSystem() instanceof WaterSystem.Local){
				List<WaterElement> elements = new ArrayList<WaterElement>(
						((WaterSystem.Local)city.getWaterSystem()).getInternalElements());
				for(WaterElement element : elements) {
					((WaterSystem.Local)city.getWaterSystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					EditableWaterElement mutable = (EditableWaterElement) element.getMutableElement();
					EditableWaterElement template = (EditableWaterElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
									((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeCommissionStart(), 
									element.getOrigin(), element.getDestination()).getMutableElement();
					((EditableSimpleLifecycleModel)template.getLifecycleModel()).setOperationDuration(
							((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationDuration());
					mutable.setDistributionEfficiency(template.getDistributionEfficiency());
					mutable.setElectricalIntensityOfWaterDistribution(template.getElectricalIntensityOfWaterDistribution());
					mutable.setElectricalIntensityOfWaterProduction(template.getElectricalIntensityOfWaterProduction());
					mutable.setLifecycleModel(template.getLifecycleModel());
					mutable.setMaxWaterInput(template.getMaxWaterInput());
					mutable.setMaxWaterProduction(template.getMaxWaterProduction());
					mutable.setReservoirIntensityOfWaterProduction(template.getReservoirIntensityOfWaterProduction());
					mutable.setVariableOperationsCostOfWaterDistribution(template.getVariableOperationsCostOfWaterDistribution());
					mutable.setVariableOperationsCostOfWaterProduction(template.getVariableOperationsCostOfWaterProduction());
					((WaterSystem.Local)city.getWaterSystem()).addElement(mutable.createElement());
				}
			}
			if(city.getElectricitySystem() instanceof ElectricitySystem.Local) {
				List<ElectricityElement> elements = new ArrayList<ElectricityElement>(
						((ElectricitySystem.Local) city.getElectricitySystem()).getInternalElements());
				for(ElectricityElement element : elements) {
					((ElectricitySystem.Local)city.getElectricitySystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					EditableElectricityElement mutable = (EditableElectricityElement) element.getMutableElement();
					EditableElectricityElement template = (EditableElectricityElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
									((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeCommissionStart(), 
									element.getOrigin(), element.getDestination()).getMutableElement();
					((EditableSimpleLifecycleModel)template.getLifecycleModel()).setOperationDuration(
							((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationDuration());
					mutable.setDistributionEfficiency(template.getDistributionEfficiency());
					mutable.setLifecycleModel(template.getLifecycleModel());
					mutable.setMaxElectricityInput(template.getMaxElectricityInput());
					mutable.setMaxElectricityProduction(template.getMaxElectricityProduction());
					mutable.setPetroleumIntensityOfElectricityProduction(template.getPetroleumIntensityOfElectricityProduction());
					mutable.setVariableOperationsCostOfElectricityDistribution(template.getVariableOperationsCostOfElectricityDistribution());
					mutable.setVariableOperationsCostOfElectricityProduction(template.getVariableOperationsCostOfElectricityDistribution());
					mutable.setWaterIntensityOfElectricityProduction(template.getWaterIntensityOfElectricityProduction());
					((ElectricitySystem.Local)city.getElectricitySystem()).addElement(mutable.createElement());
				}
			}
			if(city.getPetroleumSystem() instanceof PetroleumSystem.Local) {
				List<PetroleumElement> elements = new ArrayList<PetroleumElement>(
						((PetroleumSystem.Local) city.getPetroleumSystem()).getInternalElements());
				for(PetroleumElement element : elements) {
					((PetroleumSystem.Local)city.getPetroleumSystem()).removeElement(element);
					if(!city.getName().equals(element.getOrigin())) {
						continue;
					}
					EditablePetroleumElement mutable = (EditablePetroleumElement) element.getMutableElement();
					EditablePetroleumElement template = (EditablePetroleumElement) 
							scenario.getTemplate(element.getTemplateName()).createElement(
									((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getTimeCommissionStart(), 
									element.getOrigin(), element.getDestination()).getMutableElement();
					((EditableSimpleLifecycleModel)template.getLifecycleModel()).setOperationDuration(
							((EditableSimpleLifecycleModel)mutable.getLifecycleModel()).getOperationDuration());
					mutable.setDistributionEfficiency(template.getDistributionEfficiency());
					mutable.setElectricalIntensityOfPetroleumDistribution(template.getElectricalIntensityOfPetroleumDistribution());
					mutable.setLifecycleModel(template.getLifecycleModel());
					mutable.setMaxPetroleumInput(template.getMaxPetroleumInput());
					mutable.setMaxPetroleumProduction(template.getMaxPetroleumProduction());
					mutable.setReservoirIntensityOfPetroleumProduction(template.getReservoirIntensityOfPetroleumProduction());
					mutable.setVariableOperationsCostOfPetroleumDistribution(template.getVariableOperationsCostOfPetroleumDistribution());
					mutable.setVariableOperationsCostOfPetroleumProduction(template.getVariableOperationsCostOfPetroleumProduction());
					((PetroleumSystem.Local)city.getPetroleumSystem()).addElement(mutable.createElement());
				}
			}
		}
		return scenario;
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
	 * Serialize.
	 *
	 * @param scenario the scenario
	 * @return the string
	 */
	public static String serialize(Scenario scenario) {
		return getGson().toJson(new ScenarioWrapper(scenario));
	}

	/**
	 * Instantiates a new serialization.
	 */
	private Serialization() { }
}
