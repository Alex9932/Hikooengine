package alex9932.engine.entity;

import alex9932.engine.Terrain;
import alex9932.engine.render.Material;
import alex9932.utils.Mesh;
import alex9932.utils.gl.texture.Texture;
import alex9932.vecmath.Matrix4f;
import alex9932.vecmath.Quaternion;
import alex9932.vecmath.Vector3f;

public class Entity {
	protected Vector3f pos = new Vector3f();
	protected Vector3f impulse = new Vector3f();
	protected Mesh mesh;
	private Texture texture;
	private Matrix4f model;
	private Material material;
	protected float rotation;
	protected boolean onGround = false;
	private Vector3f GRAVITY = new Vector3f(0, -0.0000118f, 0);
	private boolean isTransparent = false;
	
	public Entity(float x, float y, float z) {
		this(x, y, z, null);
	}
	
	public Entity(float x, float y, float z, Mesh mesh) {
		this.pos = new Vector3f(x, y, z);
		this.impulse = new Vector3f();
		this.mesh = mesh;
		this.material = new Material(new Quaternion(0.03f, 0.03f, 0.03f, 1), new Quaternion(0.0f, 0.0f, 0.0f, 0), new Quaternion(0, 0, 0, 0), 1, 0, 1);
	}
	
	public void update(Terrain terrain) {
		if(!onGround){
			this.impulse.add(GRAVITY);
		}
		this.pos.add(this.impulse);
		this.impulse.x *= 0;
		this.impulse.z *= 0;
		//this.impulse.setZero();
		this.onGround = false;
		if(terrain != null && terrain.getHeight(pos.x, pos.z) >= pos.y){
			this.pos.y = terrain.getHeight(pos.x, pos.z);
			this.onGround = true;
			this.impulse.setZero();
		}
		this.model = Matrix4f.createModelMatrix(this.pos.x, this.pos.y, this.pos.z, this.rotation, 0, 1);
	}
	
	public void applayImpulse(Vector3f impulse) {
		this.impulse.add(impulse);
	}
	
	public Vector3f getImpulse() {
		return this.impulse;
	}
	
	public Vector3f getPos() {
		return this.pos;
	}

	public float getRotation() {
		return this.rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public Mesh getMesh() {
		return this.mesh;
	}

	public Texture getTexture() {
		return this.texture;
	}
	
	public void setTexture(Texture texture){
		this.texture = texture;
	}

	public Matrix4f getModelMatrix() {
		return this.model;
	}

	public Material getMaterial() {
		return this.material;
	}
	
	public void	setMaterial(Material material){
		this.material = material;
	}

	public boolean onGround() {
		return onGround;
	}

	public void jump() {
		if(onGround){
			onGround = false;
			applayImpulse(new Vector3f(0, 0.005f, 0));
		}
	}
	
	public boolean isTransparent(){
		return isTransparent;
	}

	public void setTransparent(boolean transparent) {
		this.isTransparent = transparent;
	}
}