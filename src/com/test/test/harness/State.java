package com.test.test.harness;

public interface State 
{
	public void update(long dt);
	public void respond(Action a, HarnessCallback operations);
	public void render();
}
