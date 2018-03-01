package alex9932.utils;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import alex9932.utils.gl.Display;

public class WindowManager {
	private static int list;

	static{
		list = GL11.glGenLists(1);
		GL11.glNewList(list, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(1, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(1, 1);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(0, 1);
		GL11.glEnd();
		GL11.glEndList();
	}
	
	
	private Display display;
	private ArrayList<Frame> frames = new ArrayList<Frame>();
	
	public WindowManager(Display display) {
		this.display = display;
		
		display.getEventSystem().addMouseListener(new IMouseListener() {
			
			@Override
			public void move(double x, double y) {
			}
			
			@Override
			public void drag(int button, double x, double y) {
				for (int i = 0; i < frames.size(); i++) {
					Frame frame = frames.get(i);
					if(frame.isDragable()){
						frame.moveToWithOffset((float)x, (float)y);
					}
				}
			}
			
			@Override
			public void buttonReleased(int button, double x, double y) {
				for (int i = 0; i < frames.size(); i++) {
					Frame frame = frames.get(i);
					frame.setDragable(false);
					frame.loadOffset(0, 0);
				}
			}
			
			@Override
			public void buttonPressed(int button, double x, double y) {
				for (int i = 0; i < frames.size(); i++) {
					Frame frame = frames.get(i);
					if(x > frame.getX() && x < frame.getX() + frame.getWidth() && y > frame.getY() && y < frame.getY() + frame.getHeight()){
						frame.setDragable(true);
						frame.loadOffset((float)x - frame.getX(), (float)y - frame.getY());
					}
				}
			}

			@Override
			public void scroll(int scroll) {
				
			}
		});
	}
	
	public void update() {
		for (int i = 0; i < frames.size(); i++) {
			Frame frame = frames.get(i);
			frame.update();
		}
	}
	
	public void render() {
		GL11.glLoadIdentity();
		GL11.glOrtho(0, display.getWidth(), display.getHeight(), 0, -1, 1);
		
		for (int i = 0; i < frames.size(); i++) {
			Frame frame = frames.get(i);
			drawFrame(frame);
		}
	}
	
	public void addWindow(Frame frame) {
		for (int i = 0; i < frames.size(); i++) {
			frames.get(i).setLevel(frames.get(i).getLevel() + 1);
		}
		frames.add(frame);
	}
	
	public Display getDisplay() {
		return display;
	}

	private void drawFrame(Frame frame) {
		GL11.glPushMatrix();
		GL11.glTranslatef(frame.getX(), frame.getY(), 0);
		GL11.glScalef(frame.getWidth(), frame.getHeight(), 1);
		GL11.glCallList(list);
		GL11.glPopMatrix();
	}
}