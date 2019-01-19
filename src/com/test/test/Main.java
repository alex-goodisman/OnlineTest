package com.test.test;

//import com.test.test.game.MineState;
import com.test.test.game.ObjectState;
//import com.test.test.game.TestGameState;
import com.test.test.harness.Harness;

public class Main 
{		
	public static void main(String[] args)
	{
		Harness<ObjectState> test = new Harness<>(new ObjectState(), 600, 600, "Test Game");
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
