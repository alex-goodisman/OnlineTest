package com.test.test.glAbstraction;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Sprite {

	private Texture texture;
	private Point worldCoords, worldDimentions;
	
	public Sprite(String imagePath)
	{
		try {
	        // load texture from PNG file
	        texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(imagePath));
	        
	        setWorldCoords(Point.ZERO);
	        setWorldDimentions(new Point(texture.getTextureWidth(), texture.getTextureHeight()));
	        
	        /*
	        System.out.println("Texture loaded: "+texture);
	        System.out.println(">> Image width: "+texture.getImageWidth());
	        System.out.println(">> Image height: "+texture.getImageHeight());
	        System.out.println(">> Texture width: "+texture.getTextureWidth());
	        System.out.println(">> Texture height: "+texture.getTextureHeight());
	        System.out.println(">> Texture ID: "+texture.getTextureID());
	        */
		} catch (IOException e) {
			e.printStackTrace();
		}
	
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
		Color.white.bind();
        texture.bind(); // or GL11.glBind(texture.getTextureID());
         
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
