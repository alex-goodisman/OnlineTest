package com.test.test.harness;

public interface State 
{
	public void update();
	public void respond(Action a, HarnessCallback operations);
	public void render();
}
