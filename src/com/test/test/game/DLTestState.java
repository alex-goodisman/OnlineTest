package com.test.test.game;

import static org.lwjgl.opengl.GL11.*;

import com.test.test.glAbstraction.Image;
import com.test.test.glAbstraction.Sprite;
import com.test.test.harness.Action;
import com.test.test.harness.HarnessCallback;
import com.test.test.harness.State;

public class DLTestState implements State {

	public Image testImage;
	
	public DLTestState()
	{
		
	}
	
	public void update(long dt) {
		// TODO Auto-generated method stub
		
	}

	
	public void respond(Action a, HarnessCallback operations) {
		// TODO Auto-generated method stub
		
	}

	
	public void render() {
		if (testImage == null)
		{
			glEnable(GL_TEXTURE_2D);
			testImage = new Image("Resources/Dirt_Block.png");
		}
		
		
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glBindTexture(GL_TEXTURE_2D, testImage.texID);
		
		glBegin(GL_QUADS);
        	glTexCoord2f(0, 0);
        	glVertex2f(0, 0);
        	glTexCoord2f(testImage.width, 0);
        	glVertex2f(testImage.width, 0);
        	glTexCoord2f(testImage.width, testImage.height);
        	glVertex2f(testImage.width, testImage.height);
        	glTexCoord2f(0, testImage.height);
        	glVertex2f(0, testImage.height);
        glEnd();
        
		
	}

}
