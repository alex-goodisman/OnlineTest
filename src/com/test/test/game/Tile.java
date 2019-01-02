package com.test.test.game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glColor3f;

public class Tile 
{
	boolean mine;
	boolean flag;
	boolean clicked;
	int adjacent;
	
	public Tile(boolean mine)
	{
		this.mine = mine;
		flag = false;
		clicked = false;
		adjacent = 0;
	}
	
	public void render()
	{

		
		glColor3f(0.5f,0.5f,0.5f);
		
		glBegin(GL_QUADS);
		glVertex3f(-1f,-1f,0.0f);
		glVertex3f(-1f,1f,0.0f);
		glVertex3f(1f,1f,0.0f);
		glVertex3f(1f,-1f,0.0f);
		glEnd();
		
		if(flag)
		{
			glColor3f(0f, 0f, 1f);
			glBegin(GL_LINES);
			glVertex3f(0.0f, -1f, 0.0f);
			glVertex3f(0.0f, 1f, 0.0f);
			glEnd();
		}
		
		if(mine)
		{
			/*glColor3f(0f,0f,0f);
			glBegin(GL_LINES);
			glVertex3f(-0.9f, -0.9f, 0.0f);
			glVertex3f(0.9f, -0.9f, 0.0f);
			glEnd();*/
		}
		
		if(clicked)
		{
			if(mine)
			{
				glColor3f(1f,0f,0f);
				glBegin(GL_LINES);
				glVertex3f(-0.9f, -0.9f, 0f);
				glVertex3f(0.9f, 0.9f, 0f);
				glVertex3f(0.9f,-0.9f,0f);
				glVertex3f(-0.9f,0.9f,0f);
				glEnd();
			}
			else
				drawNumber(adjacent);
		}
	}
	
	private static void drawNumber(int number)
	{
		glColor3f(0f,1f,0f);
		glBegin(GL_LINES);
		
		switch(number)
		{
			case 0:
				glVertex3f(-0.8f,-0.8f,0.0f);
				glVertex3f(-0.8f,0.8f,0.0f);
				glVertex3f(-0.8f,0.8f,0.0f);
				glVertex3f(0.8f,0.8f,0.0f);
				glVertex3f(0.8f,0.8f,0.0f);
				glVertex3f(0.8f,-0.8f,0.0f);
				glVertex3f(0.8f,-0.8f,0.0f);
				glVertex3f(-0.8f,-0.8f,0.0f);
				break;
			case 1:
				glVertex3f(0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				break;
			case 2:
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				
				glVertex3f(0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0f, 0.0f);
				
				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				

				glVertex3f(-0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, -0.8f, 0.0f);
				
				glVertex3f(-0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);

				break;
				
			case 3:
				glVertex3f(-0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				break;
			case 4:
				glVertex3f(0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0.8f, 0.0f);
				break;
			case 5:
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				
				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				

				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				
				glVertex3f(-0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				break;
			case 6:
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(-0.8f, -0.8f, 0.0f);
				
				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				

				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				
				glVertex3f(-0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				break;
			case 7:
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				break;
			case 8:
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, 0.8f, 0.0f);
				
				glVertex3f(-0.8f, 0.8f, 0.0f);
				glVertex3f(-0.8f, -0.8f, 0.0f);
				
				glVertex3f(0.8f, 0f, 0.0f);
				glVertex3f(-0.8f, 0f, 0.0f);
				

				glVertex3f(0.8f, 0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				
				glVertex3f(-0.8f, -0.8f, 0.0f);
				glVertex3f(0.8f, -0.8f, 0.0f);
				
				
		}
		
		glEnd();
	}
}
