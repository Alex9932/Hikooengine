package alex9932.engine.render;

import alex9932.vecmath.Quaternion;

public class Material {
	private Quaternion ambient;
	private Quaternion diffuse;
	private Quaternion specular;
	private int hasTexture;
	private float reflectance;
	private float specularPower;
	
	public Material(Quaternion ambient, Quaternion diffuse, Quaternion specular, int hasTexture, float reflectance, float specularPower) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.hasTexture = hasTexture;
		this.reflectance = reflectance;
		this.specularPower = specularPower;
	}

	public Quaternion getAmbient() {
		return ambient;
	}

	public Quaternion getDiffuse() {
		return diffuse;
	}

	public int getHasTexture() {
		return hasTexture;
	}

	public float getReflectance() {
		return reflectance;
	}

	public Quaternion getSpecular() {
		return specular;
	}

	public float getSpecularPower() {
		return specularPower;
	}

	public void setAmbient(Quaternion ambient) {
		this.ambient = ambient;
	}

	public void setDiffuse(Quaternion diffuse) {
		this.diffuse = diffuse;
	}

	public void setHasTexture(int hasTexture) {
		this.hasTexture = hasTexture;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public void setSpecular(Quaternion specular) {
		this.specular = specular;
	}

	public void setSpecularPower(float specularPower) {
		this.specularPower = specularPower;
	}
}