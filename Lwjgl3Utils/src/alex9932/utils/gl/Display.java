package alex9932.utils.gl;

import java.nio.IntBuffer;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import alex9932.utils.EventSystem;

public class Display {
	private long window;
	private long startTime;
	private int frames, fps;
	private EventSystem eventSystem;
	private long lastFrameTime;
	private float delta;
	
	public Display(int width, int height, String title) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!GLFW.glfwInit()){
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if (window == MemoryUtil.NULL){
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		GLFW.glfwSetKeyCallback(window, new GLFWKeyCallbackI() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
					GLFW.glfwSetWindowShouldClose(window, true);
				}
			}
		});
		
		GLFW.glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallbackI() {
			@Override
			public void invoke(long window, int key, int action, int hz) {
				double[] xpos = new double[1];
				double[] ypos = new double[1];
				
				GLFW.glfwGetCursorPos(window, xpos, ypos);
				System.out.println("Mouse: " + key + " " + action);
				for (int i = 0; i < xpos.length; i++) {
					System.out.println(xpos[i] + " " + ypos[i]);
				}
			}
		});

		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}
		
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		
		eventSystem = new EventSystem(window);
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(0);
		GLFW.glfwShowWindow(window);
		GL.createCapabilities();
	}
	
	public void destroy() {
		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public void update() {
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
		eventSystem.update();
		frames++;
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
		if(System.nanoTime() - startTime > 1000000000){
			fps = frames;
			frames = 0;
			System.out.println("Fps: " + fps);
			startTime = System.nanoTime();
		}
	}
	
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public EventSystem getEventSystem() {
		return eventSystem;
	}
	
	public int getFps() {
		return fps;
	}

	public double getWidth() {
		int[] width = new int[1];
		GLFW.glfwGetWindowSize(window, width, null);
		return width[0];
	}

	public double getHeight() {
		int[] height = new int[1];
		GLFW.glfwGetWindowSize(window, null, height);
		return height[0];
	}

	public float getAspect() {
		return (float)(getWidth() / getHeight());
	}
	
	public float getFrameTime(){
		return delta;
	}
}