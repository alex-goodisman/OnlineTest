package com.test.test.glAbstraction;

import java.io.IOException;
import org.lwjgl.opengl.*;

public class Sprite {

	
	
	private Point worldCoords, worldDimentions;
	
	public Sprite(String imagePath)
	{
		
	
	}
	
	public Point getWorldCoords() {
		return worldCoords;
	}

	public void setWorldCoords(Point worldCoords) {
		this.worldCoords = worldCoords;
	}

	public Point getWorldDimentions() {
		return worldDimentions;
	}

	public void setWorldDimentions(Point worldDimentions) {
		this.worldDimentions = worldDimentions;
	}

	public void draw()
	{
		//GL11.glBind(texture.getTextureID());
         
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(worldCoords.x, worldCoords.y);
            GL11.glTexCoord2f(1,0);
            GL11.glVertex2f(worldCoords.x + worldDimentions.x, worldCoords.y);
            GL11.glTexCoord2f(1,1);
            GL11.glVertex2f(worldCoords.x + worldDimentions.x, worldCoords.y + worldDimentions.y);
            GL11.glTexCoord2f(0,1);
            GL11.glVertex2f(worldCoords.x,worldCoords.y + worldDimentions.y);
        GL11.glEnd();
	}
	
	
}
