package com.test.test;

import com.test.test.game.TestGameState;
import com.test.test.harness.Harness;

public class Main 
{		
	public static void main(String[] args)
	{
		Harness<TestGameState> test = new Harness<>(new TestGameState(), 300, 300, "Test Game");
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
