package edu.mit.sips.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import edu.mit.sips.core.City;
import edu.mit.sips.core.InfrastructureElement;
import edu.mit.sips.core.Society;

/**
 * The Class ResourceStatePanel.
 */
public class SpatialStatePanel extends JPanel {
	private static final long serialVersionUID = -8936760551671238274L;
	
	private final Society society;
	private final SpatialStateProvider stateProvider;
	private final Map<Society,Point> societyLocations = new HashMap<Society,Point>();
	private final Map<InfrastructureElement,Point> elementLocations = new HashMap<InfrastructureElement,Point>();
	
	private static final int MIN_ELEMENT_RADIUS = 30;
	private static final int MIN_SOCIETY_RADIUS = 40;

	/**
	 * Instantiates a new resource state panel.
	 *
	 * @param society the society
	 */
	public SpatialStatePanel(Society society, SpatialStateProvider stateProvider) {
		this.society = society;
		this.stateProvider = stateProvider;
		layoutSocieties();
		setBackground(new Color(0xdd,0xdd,0xdd));
	}
	
	/**
	 * Draw arrow line.
	 *
	 * @param g2d the g2d
	 * @param line the line
	 */
	private void drawArrowLine(Graphics2D g2d, Line2D.Float line) {
		Graphics2D g = (Graphics2D) g2d.create();
		
		g.drawLine(Math.round(line.x1), 
				Math.round(line.y1), 
				Math.round(line.x2), 
				Math.round(line.y2));

		// http://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
		// code based on Vicente Reig's example
		AffineTransform tx = new AffineTransform();
	    tx.setToIdentity();
	    double angle = Math.atan2(line.y2-line.y1, line.x2-line.x1);
		tx.translate(line.x2, line.y2);
	    tx.rotate((angle-Math.PI/2d));
	    Polygon arrowHead = new Polygon();  
	    arrowHead.addPoint( 0,0);
	    arrowHead.addPoint( -5, -10);
	    arrowHead.addPoint( 5,-10);
		g.transform(tx);
		g.fill(arrowHead);
	    g.dispose();
	}
	
	/**
	 * Draw arrow line with head label.
	 *
	 * @param g2d the g2d
	 * @param line the line
	 * @param label the label
	 */
	private void drawArrowLineWithHeadLabel(Graphics2D g2d, Line2D.Float line, String label) {
		Graphics2D g = (Graphics2D) g2d.create();
		drawArrowLine(g, line);

		double theta = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
		int margin = 5;
		g.setColor(Color.BLACK);
		g.drawString(label, 
				(int) Math.round(line.x2 + margin*Math.cos(theta)
						- (line.x2 < line.x1?g.getFontMetrics().stringWidth(label):0)), 
						(int) Math.round(line.y2 + margin*Math.sin(theta) 
								+ g.getFontMetrics().getHeight()/2));
		g.dispose();
	}
	
	/**
	 * Draw arrow line with tail label.
	 *
	 * @param g2d the g2d
	 * @param line the line
	 * @param label the label
	 */
	private void drawArrowLineWithTailLabel(Graphics2D g2d, Line2D.Float line, String label) {
		Graphics2D g = (Graphics2D) g2d.create();
		drawArrowLine(g, line);

		double theta = Math.atan2(line.y2 - line.y1, line.x2 - line.x1);
		int margin = 5;
		g.setColor(Color.BLACK);
		g.drawString(label, 
				(int) Math.round(line.x1 - margin*Math.cos(theta)
						- (line.x2 > line.x1?g.getFontMetrics().stringWidth(label):0)), 
						(int) Math.round(line.y1 - margin*Math.sin(theta) 
								+ g.getFontMetrics().getHeight()/2));
		g.dispose();
	}
	
	/**
	 * Draw country boundary.
	 *
	 * @param g2d the g2d
	 */
	private void drawBoundaries(Graphics2D g2d) {
		Graphics2D g = (Graphics2D)g2d.create();

		g.setStroke(new BasicStroke(
				1f, 
				BasicStroke.CAP_SQUARE, 
				BasicStroke.JOIN_BEVEL, 
				1f, 
				new float[]{10f,10f}, 
				0f));
		if(!society.equals(society.getCountry())) {
			Ellipse2D countryEllipse = new Ellipse2D.Float(
					getCenterX() - getCountryBoundaryRadius(), 
					getCenterY() - getCountryBoundaryRadius(), 
					2*getCountryBoundaryRadius(), 2*getCountryBoundaryRadius());
			g.setColor(new Color(0xff,0xff,0x99));
			g.fill(countryEllipse);
			g.setColor(Color.GRAY);
			g.draw(countryEllipse);
			g.drawString(society.getCountry().getName(), 
					getCenterX() + (int)Math.round((getSocietyBoundaryRadius() 
							+ 0.1*getMaxRadius() 
							- g.getFontMetrics().stringWidth(society.getCountry().getName()))
							*Math.cos(Math.PI/4)), 
							getCenterY() + (int)Math.round((getSocietyBoundaryRadius() 
									+ 0.1*getMaxRadius()
									+ g.getFontMetrics().getHeight())
									*Math.sin(Math.PI/4)));
			g.setColor(new Color(0xff,0xff,0xff));
		} else {
			g.setColor(new Color(0xff,0xff,0x99));
		}
		Ellipse2D societyEllipse = new Ellipse2D.Float(
				getCenterX() - getSocietyBoundaryRadius(), 
				getCenterY() - getSocietyBoundaryRadius(), 
				2*getSocietyBoundaryRadius(), 
				2*getSocietyBoundaryRadius());
		g.fill(societyEllipse);
		g.setColor(Color.GRAY);
		g.draw(societyEllipse);
		g.drawString(society.getName(), 
				getCenterX() - (int)Math.round(g.getFontMetrics().stringWidth(society.getName())/2), 
				getCenterY() + g.getFontMetrics().getHeight()/2);
		
		
		g.dispose();
	}
	
	/**
	 * Draw city.
	 *
	 * @param g2d the g2d
	 * @param city the city
	 */
	private void drawCity(Graphics2D g2d, City city) {
		Graphics2D g = (Graphics2D)g2d.create();
		
		Point p = societyLocations.get(city);
		
		double consumptionValue = stateProvider.getConsumption(city);
		if(consumptionValue == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.RED);
		}
		g.fillOval(p.x - getElementRadius(), p.y - getElementRadius(),
				2*getElementRadius(), 2*getElementRadius());
		g.setColor(Color.BLACK);
		g.drawString(city.getName(),
				p.x + (int)Math.round((getElementRadius() + 5)*Math.cos(Math.PI/4)), 
				p.y + (int)Math.round((getElementRadius() + 5 + g.getFontMetrics().getHeight())*Math.cos(Math.PI/4)));
		
		g.setColor(Color.BLACK);
		g.drawOval(p.x - getElementRadius(), p.y - getElementRadius(),
				2*getElementRadius(), 2*getElementRadius());
		g.drawString(formatValue(consumptionValue), 
				p.x - g.getFontMetrics().stringWidth(formatValue(consumptionValue))/2, 
				p.y + g.getFontMetrics().getHeight()/2);

		if(stateProvider.isImportAllowed()) {
			double importValue = stateProvider.getImport(city);
			if(importValue > 0) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.GRAY);
			}
			Line2D.Float importLine = new Line2D.Float(
					getCenterX() + getCountryBoundaryRadius() + getElementRadius(), 
					getCenterY() + 10, 
					getCenterX() + getElementRingRadius(),  
					getCenterY() + 10);
			drawArrowLineWithHeadLabel(g, importLine, formatValue(importValue));
			g.setColor(Color.BLACK);
		}
		
		if(stateProvider.isExportAllowed()) {
			double exportValue = stateProvider.getExport(city);
			if(exportValue > 0) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.GRAY);
			}
			Line2D.Float exportLine = new Line2D.Float(
					getCenterX() + getElementRingRadius(),  
					getCenterY() - 10,
					getCenterX() + getCountryBoundaryRadius() + getElementRadius(), 
					getCenterY() - 10);
			drawArrowLineWithTailLabel(g, exportLine, formatValue(exportValue));
			g.setColor(Color.BLACK);
		}
		
		if(stateProvider.isOtherProductionAllowed()) {
			double theta = 2*Math.PI*(getNumberPseudoElements()-1) 
					/ (stateProvider.getElements(city).size() 
							+ getNumberPseudoElements());
			Point loc = new Point(getCenterX() 
					+ (int)Math.round(getElementRingRadius()*Math.cos(theta)),
					getCenterY() 
					+ (int)Math.round(getElementRingRadius()*Math.sin(theta)));
			g.setColor(Color.WHITE);
			g.fillOval(loc.x - getElementRadius(), 
					loc.y - getElementRadius(), 
					2*getElementRadius(), 
					2*getElementRadius());
			double labelTheta = theta + ((theta > 0 && theta < Math.PI/2) || (theta < -Math.PI/2)?-1:1)*Math.PI/12;
			g.setColor(Color.BLACK);
			g.drawString(stateProvider.getOtherProductionLabel(), 
					loc.x + (int)Math.round(1.25*getElementRadius()*Math.cos(labelTheta) + 5*Math.cos(labelTheta))
					- (Math.cos(labelTheta) < 0?g.getFontMetrics().stringWidth(stateProvider.getOtherProductionLabel()):0), 
					loc.y + (int)Math.round(1.25*getElementRadius()*Math.sin(labelTheta) + 5*Math.sin(labelTheta))
					+ g.getFontMetrics().getHeight()/2);
			
			double productionValue = stateProvider.getOtherProduction(city);
			double maxProduction = stateProvider.getConsumption(city);
			int fillDiameter = (int) Math.min(2*getElementRadius(), productionValue/maxProduction*2*getElementRadius());
			g.setColor(Color.GREEN);
			g.fillOval(loc.x - fillDiameter/2, loc.y - fillDiameter/2, 
					fillDiameter, fillDiameter);
			g.setColor(Color.BLACK);
			g.drawString(formatValue(productionValue), 
					loc.x - g.getFontMetrics().stringWidth(formatValue(productionValue))/2, 
					loc.y + g.getFontMetrics().getHeight()/2);
			g.setColor(Color.BLACK);
			g.drawOval(loc.x - getElementRadius(), 
					loc.y - getElementRadius(), 
					2*getElementRadius(), 
					2*getElementRadius());
		}
		g.dispose();
	}
	
	/**
	 * Draw distribution.
	 *
	 * @param g2d the g2d
	 * @param society the society
	 * @param dest the dest
	 */
	private void drawDistribution(Graphics2D g2d, Society society, Society dest) {
		Graphics2D g = (Graphics2D)g2d.create();

		Point p1 = societyLocations.get(society);
		Point p2 = societyLocations.get(dest);
		double theta = Math.atan2(p2.y - p1.y, p2.x - p1.x);
		double dist = Math.sqrt(Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y,2));
		double distribOutValue = stateProvider.getDistributionOut(society, dest);
		if(distribOutValue > 0) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GRAY);
		}
		Line2D.Double distribOutLine = new Line2D.Double(
				p1.x + getSocietyRadius()*Math.cos(theta)+5*Math.sin(theta), 
				p1.y + getSocietyRadius()*Math.sin(theta)-5*Math.cos(theta), 
				p1.x + Math.min(dist/2, 2*getSocietyRadius())*Math.cos(theta)+5*Math.sin(theta), 
				p1.y + Math.min(dist/2, 2*getSocietyRadius())*Math.sin(theta)-5*Math.cos(theta));
		drawArrowLine(g, new Line2D.Float(distribOutLine.getP1(), distribOutLine.getP2()));
		g.setColor(Color.BLACK);
		g.drawString(formatValue(distribOutValue), 
				(int) Math.round(distribOutLine.x2 + 5*Math.cos(theta)
						- (Math.sin(theta)<0?g.getFontMetrics().stringWidth(formatValue(distribOutValue)):0)), 
						(int) Math.round(distribOutLine.y2 + 5*Math.sin(theta)
								+ g.getFontMetrics().getHeight()/2));

		
		double distribInValue = stateProvider.getDistributionIn(society, dest);
		if(distribInValue > 0) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.GRAY);
		}
		Line2D.Double distribInLine = new Line2D.Double(
				p1.x + Math.min(dist/2, 2*getSocietyRadius())*Math.cos(theta)-5*Math.sin(theta), 
				p1.y + Math.min(dist/2, 2*getSocietyRadius())*Math.sin(theta)+5*Math.cos(theta),
				p1.x + getSocietyRadius()*Math.cos(theta)-5*Math.sin(theta), 
				p1.y + getSocietyRadius()*Math.sin(theta)+5*Math.cos(theta));
		drawArrowLine(g, new Line2D.Float(distribInLine.getP1(), distribInLine.getP2()));
		g.setColor(Color.BLACK);
		g.drawString(formatValue(distribInValue), 
				(int) Math.round(distribInLine.x1 - 5*Math.cos(theta)
						- (Math.sin(theta)>0?g.getFontMetrics().stringWidth(formatValue(distribInValue)):0)), 
						(int) Math.round(distribInLine.y1 + 5*Math.sin(theta)
								+ g.getFontMetrics().getHeight()/2));
		
		g.setColor(Color.GRAY);
		g.drawLine(p1.x + (int) Math.round(getSocietyRadius() * Math.cos(theta)), 
				p1.y + (int) Math.round(getSocietyRadius() * Math.sin(theta)), 
				p2.x - (int) Math.round(getSocietyRadius() * Math.cos(theta)), 
				p2.y - (int) Math.round(getSocietyRadius() * Math.sin(theta)));
		g.dispose();
	}
	
	/**
	 * Draw element.
	 *
	 * @param g2d the g2d
	 * @param element the element
	 * @param city the city
	 */
	private void drawElement(Graphics2D g2d, InfrastructureElement element, City city) {
		Point p = elementLocations.get(element);

		Graphics2D g = (Graphics2D)g2d.create();
		double theta = Math.atan2(p.y - getCenterY(), p.x - getCenterX());
		double labelTheta = theta + ((theta > 0 && theta < Math.PI/2) || (theta < -Math.PI/2)?-1:1)*Math.PI/12;
		g.setColor(Color.BLACK);
		g.drawString(element.getName(), 
				p.x + (int)Math.round(1.25*getElementRadius()*Math.cos(labelTheta) + 5*Math.cos(labelTheta))
				- (Math.cos(labelTheta) < 0?g.getFontMetrics().stringWidth(element.getName()):0), 
				p.y + (int)Math.round(1.25*getElementRadius()*Math.sin(labelTheta) + 5*Math.sin(labelTheta))
				+ g.getFontMetrics().getHeight()/2);
		
		double productionValue = stateProvider.getProduction(element);
		if(productionValue == 0) {
			g.setColor(Color.WHITE);
		} else {
			g.setColor(Color.GREEN);
		}
		g.fillOval(p.x - getElementRadius(), 
				p.y - getElementRadius(), 
				2*getElementRadius(), 
				2*getElementRadius());
		
		g.setColor(Color.BLACK);
		g.drawString(formatValue(productionValue), 
				p.x - g.getFontMetrics().stringWidth(formatValue(productionValue))/2, 
				p.y + g.getFontMetrics().getHeight()/2);
		g.setColor(Color.BLACK);
		g.drawOval(p.x - getElementRadius(), 
				p.y - getElementRadius(), 
				2*getElementRadius(), 
				2*getElementRadius());
		
		double distribInValue = city.getName().equals(element.getDestination()) ? stateProvider.getOutput(element) : 0;
		if(distribInValue > 0) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.GRAY);
		}
		Line2D.Double distribInLine = new Line2D.Double(
				p.x - getElementRadius()*Math.cos(theta), 
				p.y - getElementRadius()*Math.sin(theta), 
				p.x - Math.min(1.5*getElementRadius(), getElementRingRadius()/2)*Math.cos(theta), 
				p.y - Math.min(1.5*getElementRadius(), getElementRingRadius()/2)*Math.sin(theta));
		drawArrowLineWithHeadLabel(g, 
				new Line2D.Float(distribInLine.getP1(), distribInLine.getP2()),
				formatValue(distribInValue));

		double distribOutValue = city.getName().equals(element.getOrigin()) ? stateProvider.getInput(element) : 0;
		if(distribOutValue > 0) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GRAY);
		}
		Line2D.Double distribOutLine = new Line2D.Double(
				p.x + getElementRadius()*Math.cos(theta), 
				p.y + getElementRadius()*Math.sin(theta), 
				p.x + Math.min(1.5*getElementRadius(), getElementRingRadius()/2)*Math.cos(theta), 
				p.y + Math.min(1.5*getElementRadius(), getElementRingRadius()/2)*Math.sin(theta));
		drawArrowLineWithHeadLabel(g, 
				new Line2D.Float(distribOutLine.getP1(), distribOutLine.getP2()), 
				formatValue(distribOutValue));
		g.dispose();
	}
	
	/**
	 * Draw society.
	 *
	 * @param g2d the g2d
	 * @param society the society
	 */
	private void drawSociety(Graphics2D g2d, Society society) {
		Graphics2D g = (Graphics2D)g2d.create();
		
		Point p = societyLocations.get(society);
		boolean isCenterSociety = p.equals(new Point(getCenterX(), getCenterY()));
		
		double theta = 0;
		g.setColor(Color.BLACK);
		if(isCenterSociety) {
			g.drawString(society.getName(), 
					getCenterX() + (int)Math.round((getSocietyRadius() + 5)
							*Math.cos(Math.PI/4)), 
					getCenterY() + (int)Math.round((getSocietyRadius() + 5
							+ g.getFontMetrics().getHeight())*Math.sin(Math.PI/4)));
		} else {
			theta = Math.atan2(p.y - getCenterY(), p.x - getCenterX());
			double labelTheta = theta + ((theta > 0 && theta < Math.PI/2) || (theta < -Math.PI/2)?-1:1)*Math.PI/4;
			g.drawString(society.getName(), 
					p.x + (int)Math.round(1.25*getSocietyRadius()*Math.cos(labelTheta) + 5*Math.cos(labelTheta))
					- (Math.cos(labelTheta) < 0?g.getFontMetrics().stringWidth(society.getName()):0), 
					p.y + (int)Math.round(1.25*getSocietyRadius()*Math.sin(labelTheta) + 5*Math.sin(labelTheta))
					+ g.getFontMetrics().getHeight()/2);
		}
		double netFlowValue = stateProvider.getNetFlow(society);
		if(netFlowValue == 0) {
			g.setColor(Color.WHITE);
		} else if(netFlowValue > 0) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.RED);
		}
		g.fillOval(p.x - getSocietyRadius(), p.y - getSocietyRadius(), 
				2*getSocietyRadius(), 2*getSocietyRadius());
		
		g.setColor(Color.BLACK);
		g.drawString(formatValue(Math.abs(netFlowValue)), 
				p.x - g.getFontMetrics().stringWidth(formatValue(Math.abs(netFlowValue)))/2, 
				p.y + g.getFontMetrics().getHeight()/2);
		g.setColor(Color.BLACK);
		g.drawOval(p.x - getSocietyRadius(), p.y - getSocietyRadius(), 
				2*getSocietyRadius(), 2*getSocietyRadius());
		
		if(!society.getCountry().equals(society.getSociety())) {
			double domesticExport = stateProvider.getOtherDistributionOut(society);
			if(domesticExport > 0) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.GRAY);
			}
			double length = 0;
			if(isCenterSociety) {
				length = Math.min(getSocietyBoundaryRadius() + MIN_SOCIETY_RADIUS/2, 
						getMaxRadius() + MIN_SOCIETY_RADIUS/2);
			} else {
				length = Math.min(getSocietyBoundaryRadius() - getSocietyRingRadius() + getSocietyRadius()/2, 
						getMaxRadius() - getSocietyRingRadius() + getSocietyRadius()/2);
			}
			Line2D.Double exportLine = new Line2D.Double(
					p.x + getSocietyRadius()*Math.cos(theta+Math.PI/12), 
					p.y + getSocietyRadius()*Math.sin(theta+Math.PI/12), 
					p.x + length*Math.cos(theta+Math.PI/12), 
					p.y + length*Math.sin(theta+Math.PI/12));
			drawArrowLineWithHeadLabel(g, 
					new Line2D.Float(exportLine.getP1(), exportLine.getP2()),
					formatValue(domesticExport));
		}
		
		if(stateProvider.isExportAllowed()) {
			double exportValue = stateProvider.getExport(society);
			if(exportValue > 0) {
				g.setColor(Color.RED);
			} else {
				g.setColor(Color.GRAY);
			}
			double length = 0;
			if(isCenterSociety) {
				length = Math.min(getCountryBoundaryRadius() + getSocietyRadius(), 
						getMaxRadius() + MIN_SOCIETY_RADIUS/2);
			} else {
				length = Math.min(getCountryBoundaryRadius() - getSocietyRingRadius() + getSocietyRadius(), 
						getMaxRadius() - getSocietyRingRadius() + getSocietyRadius()/2);
			}
			Line2D.Double exportLine = new Line2D.Double(
					p.x + getSocietyRadius()*Math.cos(theta+Math.PI/6), 
					p.y + getSocietyRadius()*Math.sin(theta+Math.PI/6), 
					p.x + length*Math.cos(theta+Math.PI/6), 
					p.y + length*Math.sin(theta+Math.PI/6));
			drawArrowLineWithHeadLabel(g, 
					new Line2D.Float(exportLine.getP1(), exportLine.getP2()),
					formatValue(exportValue));
		}

		if(stateProvider.isImportAllowed()) {
			double importValue = stateProvider.getImport(society);
			if(importValue > 0) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.GRAY);
			}
			double length = 0;
			if(isCenterSociety) {
				length = Math.min(getCountryBoundaryRadius() + getSocietyRadius(), 
						getMaxRadius() + MIN_SOCIETY_RADIUS/2);
			} else {
				length = Math.min(getCountryBoundaryRadius() - getSocietyRingRadius() + getSocietyRadius(), 
						getMaxRadius() - getSocietyRingRadius() + getSocietyRadius()/2);
			}
			Line2D.Double importLine = new Line2D.Double(
					p.x + length*Math.cos(theta-Math.PI/6), 
					p.y + length*Math.sin(theta-Math.PI/6),
					p.x + getSocietyRadius()*Math.cos(theta-Math.PI/6), 
					p.y + getSocietyRadius()*Math.sin(theta-Math.PI/6));
			drawArrowLineWithTailLabel(g, 
					new Line2D.Float(importLine.getP1(), importLine.getP2()),
					formatValue(importValue));
		}

		if(!society.getCountry().equals(society.getSociety())) {
			double domesticImport = stateProvider.getOtherDistributionIn(society);
			if(domesticImport > 0) {
				g.setColor(Color.GREEN);
			} else {
				g.setColor(Color.GRAY);
			}
			double length = 0;
			if(isCenterSociety) {
				length = Math.min(getSocietyBoundaryRadius() + MIN_SOCIETY_RADIUS/2, 
						getMaxRadius() + MIN_SOCIETY_RADIUS/2);
			} else {
				length = Math.min(getSocietyBoundaryRadius() - getSocietyRingRadius() + getSocietyRadius()/2, 
						getMaxRadius() - getSocietyRingRadius() + getSocietyRadius()/2);
			}
			Line2D.Double importLine = new Line2D.Double(
					p.x + length*Math.cos(theta-Math.PI/12), 
					p.y + length*Math.sin(theta-Math.PI/12),
					p.x + getSocietyRadius()*Math.cos(theta-Math.PI/12), 
					p.y + getSocietyRadius()*Math.sin(theta-Math.PI/12));
			drawArrowLineWithTailLabel(g, 
					new Line2D.Float(importLine.getP1(), importLine.getP2()),
					formatValue(domesticImport));
		}
		
		g.dispose();
	}
	
	/**
	 * Format value.
	 *
	 * @param value the value
	 * @return the string
	 */
	private String formatValue(double value) {
		if(value == 0) {
			return "";
		} else {
			DecimalFormat format = new DecimalFormat("0.0E0");
			return format.format(value)+ " " + stateProvider.getUnits();
		}
	}
	
	/**
	 * Gets the center x.
	 *
	 * @return the center x
	 */
	private int getCenterX() {
		return (int) (getWidth() - (getInsets().left + getInsets().right)) / 2;
	}
	
	/**
	 * Gets the center y.
	 *
	 * @return the center y
	 */
	private int getCenterY() {
		return (int) (getHeight() - (getInsets().top + getInsets().bottom)) / 2;
	}
	
	/**
	 * Gets the country radius.
	 *
	 * @return the country radius
	 */
	private int getCountryBoundaryRadius() {
		if(society.equals(society.getCountry())) {
			return (int)Math.round(0.9*getMaxRadius());
		} else {
			return getMaxRadius();
		}
	}
	
	/**
	 * Gets the element radius.
	 *
	 * @return the element radius
	 */
	private int getElementRadius() {
		return (int) Math.round(Math.min(getSocietyBoundaryRadius()/5, 
				Math.max(MIN_ELEMENT_RADIUS, 
				Math.PI / 6 * getSocietyBoundaryRadius()
				/ (stateProvider.getElements(society).size() + getNumberPseudoElements()))));
	}
	
	/**
	 * Gets the element ring radius.
	 *
	 * @return the element ring radius
	 */
	private int getElementRingRadius() {
		return getSocietyBoundaryRadius() - getElementRadius();
	}
	
	/**
	 * Gets the max radius.
	 *
	 * @return the max radius
	 */
	private int getMaxRadius() {
		return (int)Math.round(0.5*(
				Math.min(getWidth() - (getInsets().left + getInsets().right 
						+ 2*5 + 2*getFontMetrics(getFont()).stringWidth(formatValue(1e6))), 
						getHeight() - (getInsets().top + getInsets().bottom 
								+ 2*5 + 2*getFontMetrics(getFont()).getHeight()))));
	}
	
	/**
	 * Gets the number pseudo elements.
	 *
	 * @return the number pseudo elements
	 */
	private int getNumberPseudoElements() {
		int numberPseudoElements = 0;
		if(stateProvider.isExportAllowed() || stateProvider.isImportAllowed()) {
			numberPseudoElements++;
		}
		if(stateProvider.isOtherProductionAllowed()) {
			numberPseudoElements++;
		}
		return numberPseudoElements;
	}
	
	/**
	 * Gets the country radius.
	 *
	 * @return the country radius
	 */
	private int getSocietyBoundaryRadius() {
		if(society.equals(society.getCountry())) {
			return getCountryBoundaryRadius();
		} else if(society instanceof City) {
			return (int)Math.round(0.8*getCountryBoundaryRadius());
		} else {
			return (int)Math.round(0.8*getCountryBoundaryRadius());
		}
	}
	
	/**
	 * Gets the city radius.
	 *
	 * @return the city radius
	 */
	private int getSocietyRadius() {
		return (int) Math.round(Math.max(MIN_SOCIETY_RADIUS, 
				Math.PI / 6 * getSocietyBoundaryRadius() / society.getNestedSocieties().size()));
	}
	
	/**
	 * Gets the city ring radius.
	 *
	 * @return the city ring radius
	 */
	private int getSocietyRingRadius() {
		return getSocietyBoundaryRadius() - getSocietyRadius();
	}
	
	/**
	 * Layout elements.
	 *
	 * @param city the city
	 */
	private void layoutElements(City city) {
		elementLocations.clear();
		
		List<? extends InfrastructureElement> elements = stateProvider.getElements(city);
		for(InfrastructureElement element : elements) {
			double theta = 2*Math.PI*(elements.indexOf(element)+getNumberPseudoElements()) 
					/ (elements.size()+getNumberPseudoElements());
			elementLocations.put(element, new Point(
					societyLocations.get(city).x 
					+ (int) Math.round(getElementRingRadius() * Math.cos(theta)),
					societyLocations.get(city).y 
					+ (int) Math.round(getElementRingRadius() * Math.sin(theta))));
		}
	}
	
	/**
	 * Layout societies.
	 */
	private void layoutSocieties() {
		societyLocations.clear();

		if(society instanceof City) {
			// society is a city: lay out elements as well
			City city = (City) society;
			societyLocations.put(city, new Point(getCenterX(), getCenterY()));
			layoutElements(city);
		} else if(society.getNestedSocieties().size()==1) {
			// only one nested society, lay out in center
			societyLocations.put(society.getNestedSocieties().get(0), 
					new Point(getCenterX(), getCenterY()));
		} else {
			// lay out nested societies in a circle
			for(Society society : this.society.getNestedSocieties()) {
				double theta = 2*Math.PI*this.society.getNestedSocieties().indexOf(society) 
						/ this.society.getNestedSocieties().size();
				societyLocations.put(society, new Point(
						getCenterX() + (int) (getSocietyRingRadius() * Math.cos(theta)),
						getCenterY() + (int) (getSocietyRingRadius() * Math.sin(theta))));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(
		        RenderingHints.KEY_ANTIALIASING,
		        RenderingHints.VALUE_ANTIALIAS_ON);

		drawBoundaries(g2d);
		
		if(society instanceof City) {
			City city = (City) society;
			drawCity(g2d, city);
			layoutElements(city);
			for(InfrastructureElement element : stateProvider.getElements(city)) {
				drawElement(g2d, element, city);
			}
		} else {
			for(Society society : this.society.getNestedSocieties()) {
				drawSociety(g2d, society);
				
				for(Society dest : this.society.getNestedSocieties()) {
					if(society != dest) {
						drawDistribution(g2d, society, dest);
					}
				}
				
				// TODO draw distribution to non-nested societies
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		layoutSocieties();
	}
}
