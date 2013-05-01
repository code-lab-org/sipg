package edu.mit.sips;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.Country;

/**
 * A factory for creating Country objects.
 */
public abstract class CountryFactory {
	
	/**
	 * Creates a new Country object.
	 *
	 * @param cities the cities
	 * @param sectors the sectors
	 * @return the country
	 */
	public static Country createSaudiCountry(Collection<CityTemplate> cities,
			List<Sector> sectors) {
		return  Country.buildCountry("KSA", Arrays.asList(
				CityTemplate.INDUSTRIAL.createCity(
						cities.contains(CityTemplate.INDUSTRIAL), sectors),
				CityTemplate.URBAN.createCity(
						cities.contains(CityTemplate.URBAN), sectors),
				CityTemplate.RURAL.createCity(
						cities.contains(CityTemplate.RURAL), sectors)
		));
	}
}
