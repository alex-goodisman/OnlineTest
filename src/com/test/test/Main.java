package com.test.test;

import com.test.test.game.MineState;
import com.test.test.harness.Harness;

public class Main 
{		
	public static void main(String[] args)
	{
		Harness<MineState> test = new Harness<>(new MineState(), 300, 300, "Test Game");
		try
		{
			test.run();
		}
		finally
		{
			test.cleanup();
		}
	}
}
