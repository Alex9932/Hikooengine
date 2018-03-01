package alex9932.engine.entity;

public class EntityPlayer extends Entity{
	protected float rotationy;
	
	public EntityPlayer(float x, float y, float z) {
		super(x, y, z);
	}

	public float getRotationY() {
		return rotationy;
	}
	
	public void setRotationY(float rotationy) {
		this.rotationy = rotationy;
	}
}