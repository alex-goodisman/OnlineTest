package com.test.test.game;

import static org.lwjgl.opengl.GL11.*;

public class Hitbox //drawing rectangle from xywh
{
	public float x;
	public float y;
	public float w;
	public float h;
	
	public Hitbox(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void draw()
	{
		glBegin(GL_QUADS);
		glVertex3f(x,y,0f);
		glVertex3f(x+w,y,0f);
		glVertex3f(x+w,y+h,0f);
		glVertex3f(x,y+h,0f);
		glEnd();
	}
}
