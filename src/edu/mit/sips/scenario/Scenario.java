package edu.mit.sips.scenario;

import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.Country;

/**
 * The Interface Scenario.
 */
public interface Scenario {
	
	/**
	 * Gets the country.
	 *
	 * @return the country
	 */
	public Country getCountry();
	
	/**
	 * Gets the templates.
	 *
	 * @param sectors the sectors
	 * @return the templates
	 */
	public List<? extends ElementTemplate> getTemplates(Collection<Sector> sectors);
	
	/**
	 * Gets the template.
	 *
	 * @param name the name
	 * @return the template
	 */
	public ElementTemplate getTemplate(String name);

	/**
	 * Gets the templates.
	 *
	 * @return the templates
	 */
	public List<? extends ElementTemplate> getTemplates();
	
	/**
	 * Checks if is team score displayed.
	 *
	 * @return true, if is team score displayed
	 */
	public boolean isTeamScoreDisplayed();
	
	/**
	 * Gets the max annual investment.
	 *
	 * @return the max annual investment
	 */
	public double getMaxAnnualInvestment();
}
