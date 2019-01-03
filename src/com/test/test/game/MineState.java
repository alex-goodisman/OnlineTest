package com.test.test.game;

import static org.lwjgl.opengl.GL11.*;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;

import com.test.test.harness.Action;
import com.test.test.harness.HarnessCallback;
import com.test.test.harness.State;

import static org.lwjgl.glfw.GLFW.*;

public class MineState implements State
{
	private static final float SIZE = 8f;
	private static final int MINES = 10;
	
	Tile[][] board;
	
	int open;
	
	int done;
	
	long time;
	
	int mines;
	
	private int isMine(int i, int j)
	{
		if(i < 0 || i >= SIZE || j < 0 || j >= SIZE)
			return 0;
		
		else return board[i][j].mine?1:0;
	}
	
	private void reset()
	{
		board = new Tile[(int)SIZE][(int)SIZE];
		Set<Integer> set = new HashSet<>();
		int k = 0;
		while(k < MINES)
		{
			int i = (int)(Math.random()*SIZE*SIZE);
			if(set.contains(i))
				continue;
			else
			{
				set.add(i);
				k++;
			}
			
			
		}
		
		for(int i = 0; i < SIZE; i++)
		{
			for(int j = 0; j < SIZE; j++)
			{
				board[i][j] = new Tile(set.contains((int)SIZE*i+j));
			}
		}
		
		for(int i = 0; i < SIZE; i++)
		{
			for(int j = 0; j < SIZE; j++)
			{
				int c = 0;
				c += isMine(i-1,j-1);
				c += isMine(i-1,j);
				c += isMine(i-1,j+1);
				c += isMine(i,j+1);
				c += isMine(i+1,j+1);
				c += isMine(i+1,j);
				c += isMine(i+1,j-1);
				c += isMine(i,j-1);
				board[i][j].adjacent = c;
				
			}
		}
		open = 0;
		done = 0;
		time = 0;
		mines = MINES;
	}
	
	public MineState()
	{
		reset();
	}
	
	public void update(long dt)
	{
		if(done != 0)
			return;
		time += dt;
		if(time > 999 * 1000000000L)
			time = 999 * 1000000000L;
	}
	
	public void respond(Action a, HarnessCallback operations)
	{		
		switch(a.type)
		{
			case KEY:
				break;
			case MOUSE:
				
				if(a.y >= 0.6)
				{
					float d = (float)Math.sqrt(Math.pow(0-a.x, 2) + Math.pow(0.8-a.y, 2));
					if(d <= 0.15)
					{
						reset();
					}
				}
				else
				{
					if(done != 0)
						return;
					a.y = (a.y+1)*2/1.6-1;
					if(a.value == GLFW_MOUSE_BUTTON_LEFT && a.mode == GLFW_RELEASE)
					{
							int i = (int)((a.x+1)*SIZE/2);
							int j = (int)((a.y+1)*SIZE/2);
							
							propagate(i,j);
							
							if(open == SIZE*SIZE-MINES)
							{
								done = 1;
								for(int k = 0; k < SIZE; k++)
								{
									for(int l = 0; l < SIZE; l++)
									{
										if(board[k][l].mine)
											board[k][l].flag = true;
									}
								}
								mines = 0;
							}					
					}
					else if(a.value == GLFW_MOUSE_BUTTON_RIGHT && a.mode == GLFW_RELEASE)
					{
						int i = (int)((a.x+1)*SIZE/2);
						int j = (int)((a.y+1)*SIZE/2);
								
						if(!board[i][j].clicked)
						{
							if(board[i][j].flag)
								mines++;
							else
								mines--;
							board[i][j].flag = !board[i][j].flag;
						}
					}
				}
				break;
			case MOTION:
				break;
		}
	}
	
	private void propagate(int i, int j)
	{
		if(i < 0 || i >= SIZE || j < 0 || j >= SIZE)
			return;
		
		if(board[i][j].clicked || board[i][j].flag)
			return;
		
		board[i][j].clicked = true;
		open++;
		
		if(board[i][j].mine)
		{
			done = -1;
			for(int k = 0; k < SIZE; k++)
			{
				for(int l = 0; l < SIZE; l++)
				{
					if(board[k][l].mine)
						board[k][l].clicked = true;
				}
			}
		}
		else if(board[i][j].adjacent == 0)
		{
			propagate(i-1,j);
			propagate(i,j+1);
			propagate(i+1,j);
			propagate(i,j-1);
		}
			
	}
	
	public void render()
	{
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glColor3f(1.0f, 1.0f, 0.0f);
		
		glBegin(GL_TRIANGLE_FAN);
		glVertex3f(0f, 0.8f, 0f);
		for(int i = 0; i < 360; i++)
		{
			glVertex3f((float)(0.15f*Math.cos(i*Math.PI/180)), 0.8f+(float)(0.15f*Math.sin(i*Math.PI/180)), 0f);
			glVertex3f((float)(0.15f*Math.cos((i+1)*Math.PI/180)), 0.8f+(float)(0.15f*Math.sin((i+1)*Math.PI/180)), 0f);
		}
		glEnd();
		glColor3f(0f,0f,0f);
		switch(done)
		{
			case 0:
				glBegin(GL_QUADS);
				glVertex3f(-0.075f,0.85f,0f);
				glVertex3f(-0.05f,0.875f,0f);
				glVertex3f(-0.025f,0.85f,0f);
				glVertex3f(-0.05f,0.825f,0f);
				glVertex3f(0.075f,0.85f,0f);
				glVertex3f(0.05f,0.875f,0f);
				glVertex3f(0.025f,0.85f,0f);
				glVertex3f(0.05f,0.825f,0f);
				glEnd();
				glBegin(GL_LINE_STRIP);
				for(int i = 180; i < 360; i++)
				{
					glVertex3f((float)(0.08f*Math.cos(i*Math.PI/180)), (float)(0.75f + 0.05f*Math.sin(i*Math.PI/180)), 0f);
				}
				glEnd();
				break;
			case -1:
				glColor3f(0f,0f,0f);
				glBegin(GL_LINES);
				glVertex3f(-0.075f,0.875f, 0f);
				glVertex3f(-0.025f,0.825f, 0f);
				glVertex3f(-0.025f,0.875f, 0f);
				glVertex3f(-0.075f,0.825f, 0f);
				glVertex3f(0.075f,0.875f, 0f);
				glVertex3f(0.025f,0.825f, 0f);
				glVertex3f(0.025f,0.875f, 0f);
				glVertex3f(0.075f,0.825f, 0f);
				glEnd();
				glBegin(GL_LINE_STRIP);
				for(int i = 180; i < 360; i++)
				{
					glVertex3f((float)(0.08f*Math.cos(i*Math.PI/180)), (float)(0.7f - 0.05f*Math.sin(i*Math.PI/180)), 0f);
				}
				glEnd();
				break;
			case 1:
				glBegin(GL_QUADS);
				glVertex3f(-0.075f,0.875f, 0f);
				glVertex3f(-0.025f,0.875f, 0f);
				glVertex3f(-0.025f,0.825f, 0f);
				glVertex3f(-0.075f,0.825f, 0f);
				glVertex3f(0.075f,0.875f, 0f);
				glVertex3f(0.025f,0.875f, 0f);
				glVertex3f(0.025f,0.825f, 0f);
				glVertex3f(0.075f,0.825f, 0f);
				glEnd();
				glBegin(GL_TRIANGLES);
				glVertex3f(-0.025f,0.825f, 0f);
				glVertex3f(-0.075f,0.825f, 0f);
				glVertex3f(-0.05f,0.805f,0f);
				glVertex3f(0.025f,0.825f, 0f);
				glVertex3f(0.075f,0.825f, 0f);
				glVertex3f(0.05f,0.805f,0f);
				glEnd();
				glBegin(GL_LINES);
				glVertex3f(-0.03f,0.85f,0f);
				glVertex3f(0.03f,0.85f,0f);
				glEnd();
				glBegin(GL_LINE_STRIP);
				for(int i = 180; i < 360; i++)
				{
					glVertex3f((float)(0.08f*Math.cos(i*Math.PI/180)), (float)(0.75f + 0.05f*Math.sin(i*Math.PI/180)), 0f);
				}
				glEnd();
				break;
				
		}
		
		glPushMatrix();
		
		glTranslatef(0f,-0.2f,0f);
		glScalef(1f,0.8f,1f);
		
		
		for(int i = 0; i < SIZE; i++)
		{
			for(int j = 0; j < SIZE; j++)
			{
				float x = (2*i+1)/SIZE - 1f;
				float y = (2*j+1)/SIZE - 1f;
				
				glPushMatrix();
				
				glTranslatef(x,y,0.0f);
				glScalef(0.9f/SIZE,0.9f/SIZE,1);
				
				board[i][j].render();
				
				glPopMatrix();
			}
		}
		
		glPopMatrix();
		
		glPushMatrix();
		
		glTranslatef(0.8f,0.8f,0f);
		glScalef(0.13f,0.13f,1f);
		
		glColor3f(1f,0f,0f);
		
		Tile.drawNumber((int)(time / 1000000000L));
		
		glPopMatrix();
		
		glPushMatrix();
		
		glTranslatef(-0.4f,0.8f,0f);
		glScalef(0.13f,0.13f,1f);
		
		glColor3f(1f,0f,0f);
		
		Tile.drawNumber(mines);
		
		glPopMatrix();
	}
}
