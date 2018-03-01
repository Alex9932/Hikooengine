package alex9932.vecmath;

public class Quaternion {
	public float x, y, z, w;
	
	public Quaternion() {
		this(0, 0, 0, 0);
	}
	
	public Quaternion(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float length() {
		return (float)Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
	}
	
	public void normalize() {
		float d = length();
		this.x /= d;
		this.y /= d;
		this.z /= d;
		this.w /= d;
	}
	
	public Quaternion conjugate(){
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion mul(Quaternion quat) {
		float x_ = x * quat.w + w * quat.x + y * quat.z - z * quat.y;
		float y_ = y * quat.w + w * quat.y + z * quat.x - x * quat.z;
		float z_ = z * quat.w + w * quat.z + x * quat.y - y * quat.x;
		float w_ = w * quat.w - x * quat.x - y * quat.y - z * quat.z;
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion mul(Vector3f vec) {
		float x_ =  w * vec.x + y * vec.z - z * vec.y;
		float y_ =  w * vec.y + z * vec.y - x * vec.z;
		float z_ =  w * vec.z + x * vec.y - y * vec.x;
		float w_ = -x * vec.x - y * vec.y - z * vec.z;
		return new Quaternion(x_, y_, z_, w_);
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
	
	public void setW(float w) {
		this.w = w;
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
	
	public float getW() {
		return w;
	}
	
	public static Quaternion parse(float[] array) {
		if(array.length < 4){
			return null;
		}
		return new Quaternion(array[0], array[1], array[2], array[3]);
	}
}