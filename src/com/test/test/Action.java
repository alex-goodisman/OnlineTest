package com.test.test;

public class Action 
{
	public enum Type
	{
		KEY,
		MOUSE,
		MOTION;
	}
	
	//action type
	public Type type;
	
	//key/button stuff
	public int value;
	public int mode;
	public int mods;
	
	//cursor location
	public double x;
	public double y;
	
	public Action(Type type, int value, int mode, int mods, double x, double y)
	{
		this.type = type;
		this.value = value;
		this.mode = mode;
		this.mods = mods;
		this.x = x;
		this.y = y;
	}
}
