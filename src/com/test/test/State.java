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
	private float sp = 0.0f;
	private boolean swapcolor = false;
	
	public void update()
	{
		sp = sp + 0.001f;
		if (sp > 1.0f)
		{
			sp = 0.0f;
			swapcolor = !swapcolor;
		}
	}
	

	public void render()
	{
		if (!swapcolor)
		{
			glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
		} 
		else
		{
			glClearColor(0.0f, 1.0f, 0.0f, 0.0f);
		}
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		drawQuad();
	}
	
	private void drawQuad()
	{
		if (!swapcolor)
		{
			glColor3f(0.0f, 1.0f, 0.0f);
		}
		else
		{
			glColor3f(0.0f, 0.0f, 1.0f);
		}
		
		glBegin(GL_QUADS);
		{
			glVertex3f(-sp, -sp, 0.0f);
			glVertex3f(sp, -sp, 0.0f);
			glVertex3f(sp, sp, 0.0f);
			glVertex3f(-sp, sp, 0.0f);
		}
		glEnd();
	}
	
	public void respond(Action action, HarnessCallback operations)
	{
		if(action instanceof Action.Exit)
		{
			operations.closeWindow();
		}
	}
}
