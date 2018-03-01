package alex9932.utils;

public class Frame {
	private float x, y;
	private float width, height;
	private boolean dragable = false;
	private float offsetx, offsety;
	private int level = 0;
	
	public Frame(float x, float y) {
		this(x, y, 450, 250);
	}
	
	public Frame(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public void update() {
		
	}
	
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void moveToWithOffset(float x, float y) {
		moveTo(x - offsetx, y - offsety);
	}
	
	public void move(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}

	public void setDragable(boolean b) {
		this.dragable  = b;
	}
	
	public boolean isDragable(){
		return this.dragable;
	}

	public void loadOffset(float x, float y) {
		this.offsetx = x;
		this.offsety = y;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
}