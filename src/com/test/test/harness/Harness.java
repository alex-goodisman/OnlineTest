package com.test.test.harness;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Harness<S extends State>
{
	private long window;
	private S state;
	private int width;
	private int height;
	private String title;
	private GameExternalCallback callback;
	
	public Harness(S s, int width, int height, String title)
	{
		window = -1;
		state = s;
		callback = new GameExternalCallback();
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void run()
	{
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		glfwSetKeyCallback(window, (window, key, scancode, mode, mods) ->
		{
			double[] x = new double[1];
			double[] y = new double[1];
			
			glfwGetCursorPos(window, x, y);
			
			x[0] = 2*x[0]/width-1;
			y[0] = -2*y[0]/height+1;
			
			Action a2 = new Action(Action.Type.KEY, key, mode, mods, x[0], y[0]);
			state.respond(a2,callback);
		});
		
		glfwSetMouseButtonCallback(window, (window,button,mode,mods) -> 
		{
			double[] x = new double[1];
			double[] y = new double[1];
			
			glfwGetCursorPos(window, x, y);
			
			x[0] = 2*x[0]/width-1;
			y[0] = -2*y[0]/height+1;
			
			Action a2 = new Action(Action.Type.MOUSE, button, mode, mods, x[0], y[0]);
			state.respond(a2, callback);
		});
		
		glfwSetCursorPosCallback(window, (window, x, y) ->
		{
			x = 2*x/width-1;
			y = -2*y/height+1;
			
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
	
	public void cleanup()
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
}
