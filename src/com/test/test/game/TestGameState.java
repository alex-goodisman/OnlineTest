package com.test.test.game;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;

import com.test.test.harness.Action;
import com.test.test.harness.HarnessCallback;
import com.test.test.harness.State;

import static org.lwjgl.glfw.GLFW.*;


public class TestGameState implements State
{
	//location
	protected float x = 0;
	protected float y = 0;
	
	//velocity
	protected float vx = 0;
	protected float vy = 0;
	
	//anchor
	protected float ax = 0;
	protected float ay = 0;
	
	//drawing instructions
	protected float sp = 0.0f;
	protected boolean swapcolor = false;
	
	//grow?
	protected boolean running = true;
	protected boolean grabbed = false;
	
	public void update()
	{
		x += vx;
		y += vy;
		
		if(!running || grabbed) //don't grow
			return;
		
		sp = sp + 0.005f; //grow
		if (x+sp > 1 && x-sp < -1 && y+sp > 1 && y-sp < -1) //if full screen, swap color
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
						case GLFW_KEY_ESCAPE: //exit
							operations.closeWindow();
							break;
						case GLFW_KEY_S: //pause
							running = !running;
							break;
						case GLFW_KEY_RIGHT: //adjust speed for held down key
							vx += 0.03f;
							break;
						case GLFW_KEY_LEFT:
							vx -= 0.03f;
							break;
						case GLFW_KEY_UP:
							vy += 0.03f;
							break;
						case GLFW_KEY_DOWN:
							vy -= 0.03f;
							break;
					}
				}
				else if(a.mode == GLFW_RELEASE)
				{
					switch(a.value)
					{
						case GLFW_KEY_RIGHT: //adjust speed for released key
							vx -= 0.03f;
							break;
						case GLFW_KEY_LEFT:
							vx += 0.03f;
							break;
						case GLFW_KEY_UP:
							vy -= 0.03f;
							break;
						case GLFW_KEY_DOWN:
							vy += 0.03f;
							break;
					}
				}
				break;
			case MOUSE:
				if(a.value == GLFW_MOUSE_BUTTON_LEFT)
				{
					if(a.mode == GLFW_PRESS) //grab
					{
						if(a.x > x - sp && a.x < x + sp && a.y > y - sp && a.y < y + sp) //hitbox
						{
							grabbed = true;
							ax = (float)a.x - x;
							ay = (float)a.y - y;
						}
					}
					else if(a.mode == GLFW_RELEASE) //ungrab
					{
						grabbed = false;
						ax = 0;
						ay = 0;
					}
				}
				break;
			case MOTION:
				if(grabbed) //drag
				{
					x = (float) a.x - ax;
					y = (float) a.y - ay;
				}
				break;
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
