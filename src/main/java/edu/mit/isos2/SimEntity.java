package edu.mit.isos2;

import java.util.Collection;

import edu.mit.isos2.element.Element;

public interface SimEntity {
	public String getName();
	public Collection<? extends Element> getElements();
	
	public void initialize(long initialTime);
	public void iterateTick(long duration);
	public void iterateTock();
	public void tick(long duration);
	public void tock();
}
