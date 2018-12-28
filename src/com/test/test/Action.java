package com.test.test;

import static org.lwjgl.glfw.GLFW.*;

public abstract class Action 
{
	//quit
	public static class Exit extends Action
	{
	}
	
	//stop/start square growing
	public static class Toggle extends Action
	{
	}
	
	//move square center to x,y
	public static class Move extends Action
	{
		public double x;
		public double y;
		private Move(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	//grab/ungrab square with anchor position x,y
	public static class Grab extends Action
	{
		public boolean grab;
		public double x;
		public double y;
		private Grab(boolean grab, double x, double y)
		{
			this.grab = grab;
			this.x = x;
			this.y = y;
		}
	}
	
	public static Action decodeKeyPress(State state, int key, int scancode, int mode, int mods)
	{
		if(mode == GLFW_PRESS || mode == GLFW_REPEAT)
		{
			if(key == GLFW_KEY_ESCAPE)
				return new Exit();
			else if(key == GLFW_KEY_S)
				return new Toggle();
			else if(key == GLFW_KEY_RIGHT)
				return new Move(state.x+0.1,state.y);
			else if(key == GLFW_KEY_LEFT)
				return new Move(state.x-0.1,state.y);
			else if(key == GLFW_KEY_UP)
				return new Move(state.x,state.y+0.1);
			else if(key == GLFW_KEY_DOWN)
				return new Move(state.x,state.y-0.1);
		}
		
		return null;		
	}
	
	public static Action decodeMousePress(State state, int button, double x, double y, int mode, int mods)
	{
		if(x > state.x - state.sp && x < state.x + state.sp && y > state.y - state.sp && y < state.y + state.sp)
		{
			if(mode == GLFW_PRESS)
			{
				return new Grab(true,x,y);
			}
			
		}
		
		if(mode == GLFW_RELEASE && state.grabbed)
		{
			return new Grab(false,state.x,state.y);
		}
		
		return null;
	}
	
	public static Action decodeMouseMotion(State state, double x, double y)
	{
		if(state.grabbed)
			return new Move(x,y);
		
		return null;
		
	}
	
	
}
