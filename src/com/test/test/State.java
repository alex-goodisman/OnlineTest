package com.test.test;

public interface State 
{
	public void update();
	public void respond(Action a, HarnessCallback operations);
	public void render();
}
