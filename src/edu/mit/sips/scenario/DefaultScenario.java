package edu.mit.sips.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.mit.sips.core.Country;

/**
 * The Class DefaultScenario.
 */
public class DefaultScenario implements Scenario {
	private final Country country;
	private final List<? extends ElementTemplate> templates;
	private final boolean isTeamScoreDisplayed;
	private final double maxAnnualInvestment = 4e9;
	
	/**
	 * Instantiates a new default scenario.
	 */
	public DefaultScenario() {
		this.isTeamScoreDisplayed = true;
		country = new Country();
		templates = new ArrayList<ElementTemplate>();
	}
	
	/**
	 * Instantiates a new default scenario.
	 *
	 * @param country the country
	 * @param templates the templates
	 */
	public DefaultScenario(Country country, 
			List<? extends ElementTemplate> templates, 
			boolean isTeamScoreDisplayed) {
		this.isTeamScoreDisplayed = isTeamScoreDisplayed;
		this.country = country;
		this.templates = templates;
	}
	
	/* (non-Javadoc)
	 * @see edu.mit.sips.scenario.Scenario#getCountry()
	 */
	@Override
	public Country getCountry() {
		return country;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.scenario.Scenario#getTemplates(java.util.Collection)
	 */
	@Override
	public List<? extends ElementTemplate> getTemplates(Collection<Sector> sectors) {
		List<ElementTemplate> filteredTemplates = new ArrayList<ElementTemplate>();
		for(ElementTemplate template : templates) {
			if(sectors.contains(template.getSector())) {
				filteredTemplates.add(template);
			}
		}
		return filteredTemplates;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.scenario.Scenario#getTemplate(java.lang.String)
	 */
	@Override
	public ElementTemplate getTemplate(String name) {
		for(ElementTemplate template : templates) {
			if(template.getName().equals(name)) {
				return template;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.mit.sips.scenario.Scenario#getTemplates()
	 */
	@Override
	public List<? extends ElementTemplate> getTemplates() {
		return new ArrayList<ElementTemplate>(templates);
	}

	@Override
	public boolean isTeamScoreDisplayed() {
		return isTeamScoreDisplayed;
	}

	@Override
	public double getMaxAnnualInvestment() {
		return maxAnnualInvestment;
	}
}
