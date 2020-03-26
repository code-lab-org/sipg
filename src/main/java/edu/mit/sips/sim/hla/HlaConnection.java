package edu.mit.sips.sim.hla;

import edu.mit.sips.sim.DefaultConnection;

/**
 * The Class HLA connection.
 */
public class HlaConnection extends DefaultConnection {
	private static final long serialVersionUID = 8956180748476596664L;
	private String fomPath, federateType;
	
	/**
	 * Instantiates a new HLA connection.
	 */
	public HlaConnection() {
		super();
		fomPath = "";
		federateType = "";
	}
	
	/**
	 * Instantiates a new HLA connection.
	 *
	 * @param federationName the federation name
	 * @param fomPath the fom path
	 * @param federateName the federate name
	 * @param federateType the federate type
	 */
	public HlaConnection(String federationName, 
			String fomPath, String federateName, String federateType) {
		super(federationName, federateName);
		this.fomPath = fomPath;
		this.federateType = federateType;
	}
	
	/**
	 * Gets the federate type.
	 *
	 * @return the federate type
	 */
	public String getFederateType() {
		return federateType;
	}

	/**
	 * Gets the FOM path.
	 *
	 * @return the FOM path
	 */
	public String getFomPath() {
		return fomPath;
	}
			
	/**
	 * Sets the federate type.
	 *
	 * @param federateType the new federate type
	 */
	public void setFederateType(String federateType) {
		if(federateType == null) {
			throw new IllegalArgumentException("Federate type cannot be null");
		}
		this.federateType = federateType;
	}
		
	/**
	 * Sets the FOM path.
	 *
	 * @param fomPath the new FOM path
	 */
	public void setFomPath(String fomPath) {
		if(fomPath == null) {
			throw new IllegalArgumentException("FOM path cannot be null");
		}
		this.fomPath = fomPath;
	}
}
