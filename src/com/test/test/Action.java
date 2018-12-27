package com.test.test;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public abstract class Action 
{
	public static class Exit extends Action
	{
		private Exit()
		{
		}
	}
	
	
	
	public static Action decodeKeyPress(int key, int scancode, int action, int mods)
	{
		if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
			return new Exit();
		
		return null;
			
	}
	
	
}
