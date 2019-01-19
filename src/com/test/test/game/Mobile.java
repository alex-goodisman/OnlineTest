package com.test.test.game;

public class Mobile 
{
	public Hitbox h;
	
	public float vx = 0;
	public float vy = 0;
	
	public boolean stopped = true;
	
	public boolean bouncy = true;
	
	public Mobile(Hitbox h, boolean bouncy)
	{
		this.h = h;
		this.bouncy = bouncy;
	}
	
	public Mobile(Hitbox h)
	{
		this(h,true);
	}
	
	public void update(long dt, Hitbox[] terrain)
	{
		vy -= 0.1f;
		for(int i = 0; i < 10; i++)
		{
			float dx = vx*dt/1000000000;
			float dy = vy*dt/1000000000;
			
			dx /= 10;
			dy /= 10;
			
			h.x += dx;
			h.y += dy;
			
			for(Hitbox it : terrain)
			{
				float b = it.y + it.h - h.y;
				float t = h.y + h.h - it.y;
				float l = h.x + h.w - it.x;
				float r = it.x + it.w - h.x;
				
				if(b > 0 && t > 0 && l > 0 && r > 0)
				{
					h.x -= dx;
					h.y -= dy;
					
					if(!bouncy)
						stopped = true;
					else
					{
						if((l < b && l < t) || (r < b && r < t))
						{
							vx *= -0.5f;
							vy *= 0.5f;
						}
						else
						{
							vx *= 0.5f;
							vy *= -0.5f;
						}
						
						
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
