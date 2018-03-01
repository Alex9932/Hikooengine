package alex9932.engine.render;

import alex9932.vecmath.Vector3f;

public class PointLight {
	private Vector3f color;
	private Vector3f position;
    private float intensity;
    private float constant;
    private float linear;
    private float exponent;
    
    public PointLight(Vector3f pos, Vector3f color, float intensity, float constant, float linear, float exponent) {
    	this.position = pos;
    	this.color = color;
    	this.intensity = intensity;
    	this.linear = linear;
    	this.exponent = exponent;
    	this.constant = constant;
	}
    
    public Vector3f getColor() {
		return color;
	}
    
    public Vector3f getPosition() {
		return position;
	}
    
    public float getConstant() {
		return constant;
	}
    
    public float getExponent() {
		return exponent;
	}
    
    public float getLinear() {
		return linear;
	}
    
    public float getIntensity() {
		return intensity;
	}
    
    public void setPosition(Vector3f position) {
		this.position = position;
	}
    
    public void setColor(Vector3f color) {
		this.color = color;
	}
    
    public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
    
    public void setLinear(float linear) {
		this.linear = linear;
	}
    
    public void setExponent(float exponent) {
		this.exponent = exponent;
	}
    
    public void setConstant(float constant) {
		this.constant = constant;
	}
}