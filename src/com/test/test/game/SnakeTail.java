package com.test.test.game;

import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class SnakeTail {
	
	int x, y, gridSize;
	
	public SnakeTail(int x, int y, int gridSize)
	{
		this.x = x;
		this.y = y;
		this.gridSize = gridSize;
		
	}
	
	public void render()
	{
		glPushMatrix();
		glTranslatef(-0.5f, -0.5f, 0f);
		glScalef(gridSize, gridSize, 1f);
		
		glColor3f(0.0f, 1.0f, 0.0f);
		
		glBegin(GL_QUADS);
	    glVertex3f(0.5f + x, 0.5f + y, 0.0f);
	    glVertex3f(0.5f + x, -0.5f + y, 0.0f);
	    glVertex3f(-0.5f + x, -0.5f + y, 0.0f);
	    glVertex3f(-0.5f + x, 0.5f + y, 0.0f);
	    glEnd();
		
		glPopMatrix();
		
	}
}
