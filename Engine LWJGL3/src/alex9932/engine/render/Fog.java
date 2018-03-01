package alex9932.engine.render;

import alex9932.vecmath.Vector3f;

public class Fog {
	Vector3f color;
	float density, gradient;
	
	public Fog(Vector3f color, float density, float gradient) {
		this.color = color;
		this.density = density;
		this.gradient = gradient;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public float getDensity() {
		return density;
	}
	
	public float getGradient() {
		return gradient;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public void setDensity(float density) {
		this.density = density;
	}
	
	public void setGradient(float gradient) {
		this.gradient = gradient;
	}
}