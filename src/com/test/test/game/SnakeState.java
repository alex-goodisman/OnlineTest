package com.test.test.game;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.test.test.harness.Action;
import com.test.test.harness.HarnessCallback;
import com.test.test.harness.State;

import static org.lwjgl.glfw.GLFW.*;

public class SnakeState implements State{
	
	static enum Direction{
		UP, DOWN, LEFT, RIGHT;	
	}
	
	private static final int GRID_SIZE = 100; //must be at least 6
	ArrayList<SnakeTail> tailList = new ArrayList<SnakeTail>();
	
	Direction headDir;
	
	public SnakeState()
	{
		headDir = Direction.UP;
		tailList.add(new SnakeTail(GRID_SIZE / 2, GRID_SIZE / 2, GRID_SIZE));
		tailList.add(new SnakeTail(GRID_SIZE / 2, (GRID_SIZE / 2) - 1, GRID_SIZE));
		tailList.add(new SnakeTail(GRID_SIZE / 2, (GRID_SIZE / 2) - 2, GRID_SIZE));
	}

	public void update(long timeSinceLastUpdate) {
		// TODO Auto-generated method stub
		
	}
	
	public void respond(Action a, HarnessCallback operations) {
		// TODO Auto-generated method stub
		
	}

	public void render() {
		glClearColor(0.9f, 0.9f, 0.9f, 0.0f);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glColor3f(0.5f, 0.5f, 0.5f);
		
		glBegin(GL_QUADS);
	    glVertex3f(0.5f, 0.5f, 0.0f);
	    glVertex3f(0.5f, -0.5f, 0.0f);
	    glVertex3f(-0.5f, -0.5f, 0.0f);
	    glVertex3f(-0.5f, 0.5f, 0.0f);
	    glEnd();
	    
	    for (int i = 0; i < tailList.size(); i++)
	    {
	    	tailList.get(i).render();
	    }
		
	}

}
