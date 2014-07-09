package edu.mit.isos2.element;

public interface State {	
	public String getName();
	
	public void iterateTick(Element element, long duration);
	public void iterateTock();
}
