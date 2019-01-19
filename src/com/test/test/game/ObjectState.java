package com.test.test.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import com.test.test.harness.Action;
import com.test.test.harness.HarnessCallback;
import com.test.test.harness.State;

public class ObjectState implements State
{
	private boolean first = true;
	
	boolean throwing;
	
	boolean attacking;
	
	float gx = 0;
	float gy = 0;
	
	Hitbox[] terrain = new Hitbox[]
	{
		new Hitbox(-6f,-8f,12f,4f),
		new Hitbox(-2f,-4f,4f,3f),
		
		new Hitbox(-10f,0f,5f,2f),
		new Hitbox(-10f,2f,1f,7f),
		
		new Hitbox(5f,0f,5f,2f),
		new Hitbox(9f,2f,1f,7f)
	};
	
	Mobile p1 = new Mobile(new Hitbox(-7f,2f,1f,1f));
	Mobile p2 = new Mobile(new Hitbox(6f,2f,1f,1f));
	
	Mobile[] players = new Mobile[] {p1,p2};
	
	Mobile me = p1;
	
	Mobile attack = null;
	Explosion exp = null;
	
	@Override
	public void update(long dt) {
		
		boolean was = me.stopped;
		
		p1.update(dt, terrain);
		p2.update(dt, terrain);
		
		if(!was && me.stopped)
		{
			if(me == p1)
				me = p2;
			else
				me = p1;
		}
		
		if(attack != null)
		{
			attack.update(dt, terrain);
			if(attack.stopped || attack.h.y < -10)
			{
				if(attack.stopped)
				{
					float ax = attack.h.x + attack.h.w/2;
					float ay = attack.h.y + attack.h.h/2;
					exp = new Explosion();
					exp.x = ax;
					exp.y = ay;
					exp.size = 2f;
					
					for(Mobile m : players)
					{
						float mx = m.h.x + m.h.w/2;
						float my = m.h.y + m.h.h/2;
												
						if(Math.sqrt((mx-ax)*(mx-ax)+(my-ay)*(my-ay)) < 3)
						{
							m.vx = 4*(3f-mx+ax)*Math.signum(mx-ax);
							m.vy = 4*(3f-my+ay)*Math.signum(my-ay);
						}
					}
				}
				attack = null;
				if(me == p1)
					me = p2;
				else
					me = p1;
			}
			
		}
		
		if(exp != null)
		{
			exp.size -= 0.5f;
			if(exp.size < 0)
				exp = null;
		}
		
		if(p1.h.y < -10 || p2.h.y < -10)
		{
			p1 = new Mobile(new Hitbox(-7f,2f,1f,1f));
			p2 = new Mobile(new Hitbox(6f,2f,1f,1f));
			me = p1;
			players = new Mobile[] {p1,p2};
		}
	}

	@Override
	public void respond(Action a, HarnessCallback operations) {
		switch(a.type)
		{
			case KEY:
				break;
			case MOUSE:
				if(a.mode == GLFW_PRESS)
				{
					if(a.value == GLFW_MOUSE_BUTTON_LEFT && me.stopped && attack == null)
					{
						throwing = true;
						gx = (float)a.x*10;
						gy = (float)a.y*10;
					}
					else if(a.value == GLFW_MOUSE_BUTTON_RIGHT && me.stopped && attack == null)
					{
						attacking = true;
						gx = (float)a.x*10;
						gy = (float)a.y*10;
						
					}
						
				}
				else if(a.mode == GLFW_RELEASE)
				{
					if(a.value == GLFW_MOUSE_BUTTON_LEFT && throwing)
					{
						throwing = false;
						me.vx = (me.h.x+me.h.w/2 - gx)*3;
						me.vy = (me.h.y+me.h.h/2 - gy)*3;
						me.stopped = false;
						
						double mag = Math.sqrt(me.vx*me.vx+me.vy*me.vy);						
						if(mag > 10)
						{
							me.vx *= (10/mag);
							me.vy *= (10/mag);
						}
					}
					else if(a.value == GLFW_MOUSE_BUTTON_RIGHT && attacking)
					{
						attacking = false;
						attack = new Mobile(new Hitbox(me.h.x + me.h.w/2-0.25f,me.h.y + me.h.h/2-0.25f,0.5f,0.5f),false);

						attack.vx = (me.h.x+me.h.w/2 - gx)*3;
						attack.vy = (me.h.y+me.h.h/2 - gy)*3;
						attack.stopped = false;		
						
						double mag = Math.sqrt(attack.vx*attack.vx+attack.vy*attack.vy);						
						if(mag > 20)
						{
							attack.vx *= (20/mag);
							attack.vy *= (20/mag);
						}			
					}
				}
				break;
			case MOTION:

				gx = (float)a.x*10;
				gy = (float)a.y*10;
				break;
		}
		
	}

	@Override
	public void render() {
		
		if(first)
		{
			first = false;
			glScalef(1/10f,1/10f,0f);
		}
		
		
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glColor3f(1.0f, 1.0f, 0.0f);
		
		p1.h.draw();
		
		glColor3f(0f,1.0f,0f);
		
		p2.h.draw();
		
		if(attack != null)
		{
			glColor3f(1f,0f,0f);
			attack.h.draw();
		}
		
		if(exp != null)
			exp.draw();
		
		glColor3f(0f,0f,1f);
		
		for(Hitbox t : terrain)
			t.draw();
		
		
		if(throwing || attacking)
		{
			if(attacking)
				glColor3f(1f,0f,0f);
			else
				glColor3f(1f,0f,1f);
			
			float max = attacking?20:10;
			
			float cx = me.h.x+me.h.w/2;
			float cy = me.h.y+me.h.h/2;
			glBegin(GL_LINES);
			glVertex3f(cx,cy,0f);
			float mag = (float)(3*Math.sqrt((gx-cx)*(gx-cx)+ (gy-cy)*(gy-cy)));
			if(mag > max)
			{
				glVertex3f(cx + (gx-cx)*max/mag, cy + (gy-cy)*max/mag,0f);
				glColor3f(1f,1f,1f);
				glVertex3f(cx + (gx-cx)*max/mag, cy + (gy-cy)*max/mag,0f);
			}
			glVertex3f(gx,gy,0f);
			glEnd();
		}
	}

}
