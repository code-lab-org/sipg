package edu.mit.isos3;


public interface SimEntity {
	public String getName();
	
	public void initialize(long initialTime);
	public void iterateTick(long duration);
	public void iterateTock();
	public void tick(long duration);
	public void tock();
}
