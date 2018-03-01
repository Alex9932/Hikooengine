package alex9932.vecmath;

public class Vector2f {
	public float x, y;
	
	public Vector2f() {
		this(0, 0);
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float length() {
		return (float)Math.sqrt((x * x) + (y * y));
	}
	
	public void normalize() {
		float d = length();
		this.x /= d;
		this.y /= d;
	}
	
	public void rotate(float angle){
		double rad = Math.toRadians(angle);
		double sin = Math.sin(rad);
		double cos = Math.sin(rad);
		float a = (float)(x * cos - y * sin);
		float b = (float)(x * sin - y * cos);
		this.x = a;
		this.y = b;
	}
	
	public float dot(Vector2f vec) {
		return x * vec.x + y * vec.y;
	}
	
	public void add(float a) {
		this.x += a;
		this.y += a;
	}
	
	public void sub(float a) {
		this.x -= a;
		this.y -= a;
	}
	
	public void mul(float a) {
		this.x *= a;
		this.y *= a;
	}
	
	public void div(float a) {
		this.x /= a;
		this.y /= a;
	}
	
	public void add(Vector2f vec) {
		this.x += vec.x;
		this.y += vec.y;
	}
	
	public void sub(Vector2f vec) {
		this.x -= vec.x;
		this.y -= vec.y;
	}
	
	public void mul(Vector2f vec) {
		this.x *= vec.x;
		this.y *= vec.y;
	}
	
	public void div(Vector2f vec) {
		this.x /= vec.x;
		this.y /= vec.y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public static Vector2f parse(float[] array) {
		if(array.length < 2){
			return null;
		}
		return new Vector2f(array[0], array[1]);
	}
}