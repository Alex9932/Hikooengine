package alex9932.engine.render;

import alex9932.vecmath.Vector3f;

public class DirLight {
	private Vector3f color;
	private Vector3f direction;
	private float intensity;
	
	public DirLight(Vector3f color, Vector3f dir, float intensity) {
		this.color = color;
		this.direction = dir;
		this.intensity = intensity;
	}

	public Vector3f getColor() {
		return color;
	}

	public float getIntensity() {
		return intensity;
	}

	public Vector3f getDirection() {
		return direction;
	}
	
	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
}