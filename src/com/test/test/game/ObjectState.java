package com.test.test.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import com.test.test.harness.Action;
import com.test.test.harness.HarnessCallback;
import com.test.test.harness.State;

public class ObjectState implements State
{
	//detect first render for doing graphics scale
	private boolean first = true;
	
	//are we currently aiming to throw the player?
	boolean throwing;
	
	//are we crrently aiming to throw a bomb?
	boolean attacking;
	
	//grab coordinates
	float gx = 0;
	float gy = 0;
	
	//hardcoded terrain boxes
	Hitbox[] terrain = new Hitbox[]
	{
		new Hitbox(-6f,-8f,12f,4f),
		new Hitbox(-2f,-4f,4f,3f),
		
		new Hitbox(-10f,0f,5f,2f),
		new Hitbox(-10f,2f,1f,7f),
		
		new Hitbox(5f,0f,5f,2f),
		new Hitbox(9f,2f,1f,7f)
	};
	
	//create players
	Mobile p1 = new Mobile(new Hitbox(-7f,2f,1f,1f));
	Mobile p2 = new Mobile(new Hitbox(6f,2f,1f,1f));
	
	Mobile[] players = new Mobile[] {p1,p2};
	
	//current player
	Mobile me = p1;
	
	//other misc objects
	Mobile attack = null;
	Explosion exp = null;
	
	//top level game update 
	@Override
	public void update(long dt) {
		
		//track whether we were stopped on the last update tick to determine when to switch players
		boolean was = me.stopped;
		
		//update the players
		p1.update(dt, terrain);
		p2.update(dt, terrain);
		
		//if we weren't stopped before and we are now, swap the active player
		if(!was && me.stopped)
		{
			if(me == p1)
				me = p2;
			else
				me = p1;
		}
		
		//if there currently is an attack object, update it
		if(attack != null)
		{
			attack.update(dt, terrain);
			if(attack.stopped || attack.h.y < -10) //attack hit something or fell out of the map
			{
				if(attack.stopped) //hit something
				{
					//create explosion
					float ax = attack.h.x + attack.h.w/2;
					float ay = attack.h.y + attack.h.h/2;
					exp = new Explosion();
					exp.x = ax;
					exp.y = ay;
					exp.size = 2f;

					//attempt to propel players near the explosion
					for(Mobile m : players)
					{
						float mx = m.h.x + m.h.w/2;
						float my = m.h.y + m.h.h/2;
											
						//if player center is <3 distance from explosion center
						if(Math.sqrt((mx-ax)*(mx-ax)+(my-ay)*(my-ay)) < 3)
						{
							//apply velocity to player: 3 if right on top of explosion, decreasing to 0 at distance of 3 from explosion
							//in direction
							m.vx = 4*(3f-mx+ax)*Math.signum(mx-ax);
							m.vy = 4*(3f-my+ay)*Math.signum(my-ay);
						}
					}
				}
				//remove attack and swap player
				attack = null;
				if(me == p1)
					me = p2;
				else
					me = p1;
			}
			
		}
		
		//if explosion exists, shrink it until it goes away
		if(exp != null)
		{
			exp.size -= 0.5f;
			if(exp.size < 0)
				exp = null;
		}
		
		//if players fall out of world reset the game
		if(p1.h.y < -10 || p2.h.y < -10)
		{
			p1 = new Mobile(new Hitbox(-7f,2f,1f,1f));
			p2 = new Mobile(new Hitbox(6f,2f,1f,1f));
			me = p1;
			players = new Mobile[] {p1,p2};
		}
	}

	//do actions
	@Override
	public void respond(Action a, HarnessCallback operations) {
		switch(a.type)
		{
			case KEY: //currently we have no key actions
				break;
			case MOUSE:
				if(a.mode == GLFW_PRESS)
				{
					if(a.value == GLFW_MOUSE_BUTTON_LEFT && me.stopped && attack == null) //left click with player stopped and not attacking, start throwing
					{
						throwing = true;
						//save the grab coordinates
						gx = (float)a.x*10;
						gy = (float)a.y*10;
					}
					else if(a.value == GLFW_MOUSE_BUTTON_RIGHT && me.stopped && attack == null) //instead, if right click, start attacking
					{
						attacking = true;
						//save the grab coordinates
						gx = (float)a.x*10;
						gy = (float)a.y*10;
						
					}
						
				}
				else if(a.mode == GLFW_RELEASE)
				{
					if(a.value == GLFW_MOUSE_BUTTON_LEFT && throwing) //release left mouse button while throwing (i.e. do the actual throw)
					{
						throwing = false;
						//set velocity vector proportional to vector from grab coordinates to player center
						me.vx = (me.h.x+me.h.w/2 - gx)*3; 
						me.vy = (me.h.y+me.h.h/2 - gy)*3;
						me.stopped = false;
						
						double mag = Math.sqrt(me.vx*me.vx+me.vy*me.vy);	
						//cap magnitude at 10
						if(mag > 10)
						{
							me.vx *= (10/mag);
							me.vy *= (10/mag);
						}
					}
					else if(a.value == GLFW_MOUSE_BUTTON_RIGHT && attacking) //release right mouse button while attacking (i.e. throw the bomb)
					{
						attacking = false;
						//create the bomb
						attack = new Mobile(new Hitbox(me.h.x + me.h.w/2-0.25f,me.h.y + me.h.h/2-0.25f,0.5f,0.5f),false);

						//give it speed proportional to vector from grab to player
						attack.vx = (me.h.x+me.h.w/2 - gx)*3;
						attack.vy = (me.h.y+me.h.h/2 - gy)*3;
						attack.stopped = false;		
						
						//cap at 20
						double mag = Math.sqrt(attack.vx*attack.vx+attack.vy*attack.vy);						
						if(mag > 20)
						{
							attack.vx *= (20/mag);
							attack.vy *= (20/mag);
						}			
					}
				}
				break;
			case MOTION: //mouse motion: update grab coordinates in real time

				gx = (float)a.x*10;
				gy = (float)a.y*10;
				break;
		}
		
	}

	@Override
	public void render() {
		
		if(first) //first render: scale the graphics
		{
			first = false;
			glScalef(1/10f,1/10f,0f);
		}
		
		//background
		
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//draw players
		
		glColor3f(1.0f, 1.0f, 0.0f);
		
		p1.h.draw();
		
		glColor3f(0f,1.0f,0f);
		
		p2.h.draw();
		
		//draw attack if it exists
		
		if(attack != null)
		{
			glColor3f(1f,0f,0f);
			attack.h.draw();
		}
		
		//draw explosion if it exists
		if(exp != null)
			exp.draw();
		
		glColor3f(0f,0f,1f);
		
		//draw terrain
		for(Hitbox t : terrain)
			t.draw();
		
		
		//draw aiming line
		if(throwing || attacking)
		{
			if(attacking)
				glColor3f(1f,0f,0f);
			else
				glColor3f(1f,0f,1f);
			
			float max = attacking?20:10;
			
			//player center coordinates
			float cx = me.h.x+me.h.w/2;
			float cy = me.h.y+me.h.h/2;
			glBegin(GL_LINES);
			glVertex3f(cx,cy,0f);
			//vector magnitude
			float mag = (float)(3*Math.sqrt((gx-cx)*(gx-cx)+ (gy-cy)*(gy-cy)));
			if(mag > max)
			{
				//draw in color up to the maximum magnitude
				glVertex3f(cx + (gx-cx)*max/mag, cy + (gy-cy)*max/mag,0f);
				//draw the rest of the line in white to show it's irrelevant
				glColor3f(1f,1f,1f);
				glVertex3f(cx + (gx-cx)*max/mag, cy + (gy-cy)*max/mag,0f);
			}
			glVertex3f(gx,gy,0f);
			glEnd();
		}
	}

}
