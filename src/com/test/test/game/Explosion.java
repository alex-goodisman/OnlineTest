package com.test.test.game;

import static org.lwjgl.opengl.GL11.*;

public class Explosion 
{
	public float x;
	public float y;
	public float size;
	
	public void draw()
	{
		glColor3f(1f,0.5f,0f);
		glBegin(GL_QUADS);
		glVertex3f(x-size/2,y-size/2,0);
		glVertex3f(x-size/2,y+size/2,0);
		glVertex3f(x+size/2,y+size/2,0);
		glVertex3f(x+size/2,y-size/2,0);
		glEnd();
	}
}
