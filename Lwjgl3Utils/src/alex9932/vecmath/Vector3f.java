package alex9932.vecmath;

public class Vector3f {
	public float x, y, z;
	
	public Vector3f() {
		this(0, 0, 0);
	}
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length() {
		return (float)Math.sqrt((x * x) + (y * y) + (z * z));
	}
	
	public void normalize() {
		float d = length();
		if (d != 0) {
			this.x /= d;
			this.y /= d;
			this.z /= d;
		}
	}
	
	public void rotate(float angle){
		//TODO: Rotate!
	}
	
	public float dot(Vector3f vec) {
		return x * vec.x + y * vec.y + z * vec.z;
	}
	
	public Vector3f cross(Vector3f vec) {
		float x_ = y * vec.z - z * vec.y;
		float y_ = z * vec.x - x * vec.z;
		float z_ = x * vec.y - y * vec.x;
		return new Vector3f(x_, y_, z_);
	}
	
	public void add(float a) {
		this.x += a;
		this.y += a;
		this.z += a;
	}
	
	public void sub(float a) {
		this.x -= a;
		this.y -= a;
		this.z -= a;
	}
	
	public void mul(float a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;
	}
	
	public void div(float a) {
		this.x /= a;
		this.y /= a;
		this.z /= a;
	}
	
	public void add(Vector3f vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}

	public void addAndMul(Vector3f vec, float a) {
		this.x += vec.x * a;
		this.y += vec.y * a;
		this.z += vec.z * a;
	}
	
	public void sub(Vector3f vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		this.z -= vec.z;
	}
	
	public void mul(Vector3f vec) {
		this.x *= vec.x;
		this.y *= vec.y;
		this.z *= vec.z;
	}
	
	public void div(Vector3f vec) {
		this.x /= vec.x;
		this.y /= vec.y;
		this.z /= vec.z;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public static Vector3f cross(Vector3f vec0, Vector3f vec1) {
		float x_ = vec1.y * vec0.z - vec1.z * vec0.y;
		float y_ = vec1.z * vec0.x - vec1.x * vec0.z;
		float z_ = vec1.x * vec0.y - vec1.y * vec0.x;
		return new Vector3f(x_, y_, z_);
	}

	public void setZero() {
		this.x = this.y = this.z = 0;
	}

	public void set(Vector3f pos) {
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
	}
	
	@Override
	public String toString() {
		return "{Vector3f: " + hashCode() + " X: " + x + " Y: " + y + " Z: " + z + "}";
	}
	
	public static Vector3f parse(float[] array) {
		if(array.length < 3){
			return null;
		}
		return new Vector3f(array[0], array[1], array[2]);
	}
}