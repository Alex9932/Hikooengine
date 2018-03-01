package alex9932.engine.entity;

import alex9932.engine.render.ICamera;
import alex9932.vecmath.Matrix4f;
import alex9932.vecmath.Vector2f;
import alex9932.vecmath.Vector3f;

public class Camera implements ICamera{
	private Vector3f position = new Vector3f();
	private Vector2f angle = new Vector2f();
	private float offset = 0;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	public Camera(float x, float y, float z, float fov, float aspect) {
		position.x = x;
		position.y = y;
		position.z = z;
		projectionMatrix = Matrix4f.createProjMatrix(fov, 0.01f, 1000f, aspect);
		viewMatrix = Matrix4f.createViewMatrix(position.x, position.y + offset, position.z, angle.x, angle.y);
	}
	
	public void update() {
		viewMatrix = Matrix4f.createViewMatrix(position.x, position.y + offset, position.z, angle.x, angle.y);
	}

	@Override
	public float getX() {
		return position.x;
	}

	@Override
	public float getY() {
		return position.y;
	}

	@Override
	public float getZ() {
		return position.z;
	}

	@Override
	public float getAnglex() {
		return angle.x;
	}

	@Override
	public float getAngley() {
		return angle.y;
	}

	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	@Override
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void moveTo(Entity player) {
		this.position.set(player.getPos());
		this.angle.x = player.getRotation();
		if(player instanceof EntityPlayer){
			this.angle.y = ((EntityPlayer)player).getRotationY();
		}
	}
	
	public void setOffset(float offset) {
		this.offset = offset;
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}
}