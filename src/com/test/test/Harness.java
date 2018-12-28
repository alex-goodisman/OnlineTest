package com.test.test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Harness<S extends State>
{
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 300;
	public static final String DEFAULT_TITLE = "Test Game";
	
	private long window;
	private S state;
	private GameExternalCallback callback;
	
	public Harness(S s)
	{
		window = -1;
		state = s;
		callback = new GameExternalCallback();
	}
	
	public void run()
	{
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		window = glfwCreateWindow(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE, NULL, NULL);
		
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - DEFAULT_WIDTH) / 2, (vidmode.height() - DEFAULT_HEIGHT) / 2);
		
		glfwSetKeyCallback(window, (window, key, scancode, mode, mods) ->
		{
			double[] x = new double[1];
			double[] y = new double[1];
			
			glfwGetCursorPos(window, x, y);
			
			x[0] = 2*x[0]/DEFAULT_WIDTH-1;
			y[0] = -2*y[0]/DEFAULT_HEIGHT+1;
			
			Action a2 = new Action(Action.Type.KEY, key, mode, mods, x[0], y[0]);
			state.respond(a2,callback);
		});
		
		glfwSetMouseButtonCallback(window, (window,button,mode,mods) -> 
		{
			double[] x = new double[1];
			double[] y = new double[1];
			
			glfwGetCursorPos(window, x, y);
			
			x[0] = 2*x[0]/DEFAULT_WIDTH-1;
			y[0] = -2*y[0]/DEFAULT_HEIGHT+1;
			
			Action a2 = new Action(Action.Type.MOUSE, button, mode, mods, x[0], y[0]);
			state.respond(a2, callback);
		});
		
		glfwSetCursorPosCallback(window, (window, x, y) ->
		{
			x = 2*x/DEFAULT_WIDTH-1;
			y = -2*y/DEFAULT_HEIGHT+1;
			
			Action a2 = new Action(Action.Type.MOTION, -1, -1, 0, x, y);
			state.respond(a2, callback);
		});
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		while (!glfwWindowShouldClose(window))
		{
			state.update();
			state.render();
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
			
	}
	
	private void cleanup()
	{
		if(window > 0)
		{
			GLFWKeyCallback c = glfwSetKeyCallback(window,null);
			if(c != null)
				c.free();
			glfwDestroyWindow(window);
		}
		
		GLFWErrorCallback d = glfwSetErrorCallback(null);
		if(d != null)
			d.free();
		
		glfwTerminate();
	}
	
	private class GameExternalCallback implements HarnessCallback
	{
		public void closeWindow()
		{
			glfwSetWindowShouldClose(window, true);
		}
	}
	
	public static void main(String[] args)
	{
		Harness<TestGameState> test = new Harness<>(new TestGameState());
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
