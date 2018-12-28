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

import static org.lwjgl.glfw.GLFW.*;


public class TestGameState implements State
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
	
	public void respond(Action a, HarnessCallback operations)
	{		
		switch(a.type)
		{
			case KEY:
				if(a.mode == GLFW_PRESS)
				{
					switch(a.value)
					{
						case GLFW_KEY_ESCAPE:
							operations.closeWindow();
							break;
						case GLFW_KEY_S:
							running = !running;
							break;
						case GLFW_KEY_UP:
							y += 0.1;
							break;
						case GLFW_KEY_DOWN:
							y -= 0.1;
							break;
						case GLFW_KEY_RIGHT:
							x += 0.1;
							break;
						case GLFW_KEY_LEFT:
							x -= 0.1;
					}
				}
				break;
			case MOUSE:
				if(a.value == GLFW_MOUSE_BUTTON_LEFT)
				{
					if(a.mode == GLFW_PRESS)
					{
						if(a.x > x - sp && a.x < x + sp && a.y > y - sp && a.y < y + sp)
						{
							System.out.println("grab");
							grabbed = true;
							ax = (float)a.x - x;
							ay = (float)a.y - y;
						}
					}
					else if(a.mode == GLFW_RELEASE)
					{
						System.out.println("ungrab");
						grabbed = false;
						ax = 0;
						ay = 0;
					}
				}
				break;
			case MOTION:
				if(grabbed)
				{
					x = (float) a.x - ax;
					y = (float) a.y - ay;
				}
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
