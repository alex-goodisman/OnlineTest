package com.test.test;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class State 
{
	protected float x = 0;
	protected float y = 0;
	protected float ax = 0;
	protected float ay = 0;
	protected float sp = 0.0f;
	protected boolean swapcolor = false;
	protected boolean running = true;
	protected boolean grabbed = false;
	
	public void update()
	{
		if(!running || grabbed)
			return;
		
		sp = sp + 0.005f;
		if (x+sp > 1 && x-sp < -1 && y+sp > 1 && y-sp < -1)
		{
			sp = 0.0f;
			swapcolor = !swapcolor;
		}
	}
	
	public void respond(Action action, HarnessCallback operations)
	{
		if(action instanceof Action.Exit)
		{
			operations.closeWindow();
		}
		else if(action instanceof Action.Toggle)
		{
			running = !running;
		}
		else if(action instanceof Action.Move)
		{
			Action.Move m = (Action.Move)action;
			x = (float) m.x-ax;
			y = (float) m.y-ay;
		}
		else if(action instanceof Action.Grab)
		{
			Action.Grab g = (Action.Grab)action;
			grabbed = g.grab;
			ax = (float)g.x-x;
			ay = (float)g.y-y;
		}
	}
	
	public void render()
	{
		if (!swapcolor)
			glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		else
			glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		if (!swapcolor)
			glColor3f(0.0f, 1.0f, 0.0f);
		else
			glColor3f(0.0f, 0.0f, 1.0f);
		
		glBegin(GL_QUADS);
		glVertex3f(x-sp, y-sp, 0.0f);
		glVertex3f(x+sp, y-sp, 0.0f);
		glVertex3f(x+sp, y+sp, 0.0f);
		glVertex3f(x-sp, y+sp, 0.0f);
		glEnd();
	}
}
