package com.test.test.game;

public class Mobile //represents moving stuff
{
	//my own hitbox
	public Hitbox h;
	
	//velocity
	public float vx = 0;
	public float vy = 0;
	
	//has this 
	public boolean stopped = true;
	
	//does this object bounce of walls or stop when it hits them
	public boolean bouncy = true;
	
	//constructor (with bounce setting)
	public Mobile(Hitbox h, boolean bouncy)
	{
		this.h = h;
		this.bouncy = bouncy;
	}
	
	//constructor (default to bouncy setting)
	public Mobile(Hitbox h)
	{
		this(h,true);
	}
	
	//do one tick of motion.
	//inputs time step, list of collideable terrain
	public void update(long dt, Hitbox[] terrain)
	{
		//gravity accel
		vy -= 0.1f;
	
		//apply velocity vector in 10 steps to avoid "bullet through paper"
		for(int i = 0; i < 10; i++)
		{
			//compute distance offset
			float dx = vx*dt/1000000000;
			float dy = vy*dt/1000000000;
			
			dx /= 10;
			dy /= 10;
			
			//apply offset
			h.x += dx;
			h.y += dy;
			
			//check fr collisions
			for(Hitbox it : terrain)
			{
				
				//AABB collision check
				float b = it.y + it.h - h.y;
				float t = h.y + h.h - it.y;
				float l = h.x + h.w - it.x;
				float r = it.x + it.w - h.x;
				
				if(b > 0 && t > 0 && l > 0 && r > 0)
				{
					//undo motion
					h.x -= dx;
					h.y -= dy;
					
					if(!bouncy) //stop on collision if not bouncy
						stopped = true;
					else
					{
						//reflect off the wall
						//reduce speed by half
						if((l < b && l < t) || (r < b && r < t)) //reflect left/right
						{
							vx *= -0.5f;
							vy *= 0.5f;
						}
						else //reflect up/down
						{
							vx *= 0.5f;
							vy *= -0.5f;
						}
						
						//when slow down enough, stop moving
						if(Math.sqrt(vx*vx + vy*vy) < 0.4f)
						{
							vx = 0f;
							vy = 0f;
							stopped = true;
						}
					}

				}
			}
			
		}
				
	}
}
