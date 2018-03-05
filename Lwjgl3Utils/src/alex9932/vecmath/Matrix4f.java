package alex9932.vecmath;


public class Matrix4f {
	private float[][] m;
	
	public Matrix4f() {
		this.m = new float[4][4];
	}
	
	public void setIdentity() {
		m[0][0] = 1; m[1][0] = 0; m[2][0] = 0; m[3][0] = 0;
		m[0][1] = 0; m[1][1] = 1; m[2][1] = 0; m[3][1] = 0;
		m[0][2] = 0; m[1][2] = 0; m[2][2] = 1; m[3][2] = 0;
		m[0][3] = 0; m[1][3] = 0; m[2][3] = 0; m[3][3] = 1;
	}
	
	public void setIdentity(float scale) {
		m[0][0] = scale; m[1][0] = 0;     m[2][0] = 0;     m[3][0] = 0;
		m[0][1] = 0;     m[1][1] = scale; m[2][1] = 0;     m[3][1] = 0;
		m[0][2] = 0;     m[1][2] = 0;     m[2][2] = scale; m[3][2] = 0;
		m[0][3] = 0;     m[1][3] = 0;     m[2][3] = 0;     m[3][3] = 1;
	}

	public static Matrix4f createProjMatrix(float fovy, float near, float far, float aspect) {
		Matrix4f projection = new Matrix4f();
		projection.setIdentity();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fovy / 2f))));
		float x_scale = y_scale / aspect;
		float frustum_length = far - near;
		projection.m[0][0] = x_scale;
		projection.m[1][1] = y_scale;
		projection.m[2][2] = -((far + near) / frustum_length);
		projection.m[2][3] = -1;
		projection.m[3][2] = -((2 * near * far) / frustum_length);
		projection.m[3][3] = 0;
		return projection;
	}

	public static Matrix4f createOrthoMatrix(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f projection = new Matrix4f();
		projection.m[0][0] = 2 / (right - left);
		projection.m[1][1] = 2 / (top - bottom);
		projection.m[2][2] = -2 / (far - near);
		projection.m[3][0] = -((right + left) / (right - left));
		projection.m[3][1] = -((top + bottom) / (top - bottom));
		projection.m[3][2] = -((far + near) / (far - near));
		projection.m[3][3] = 1;
		return projection;
	}

	public static Matrix4f createViewMatrix(float x, float y, float z, float anglex, float angley) {
		Matrix4f view = new Matrix4f();
		view.setIdentity();
		view.translate(-x, -y, -z);
		view.rotateY((float)Math.toRadians(anglex));
		view.rotateX((float)Math.toRadians(angley));
		return view;
	}

	public static Matrix4f createModelMatrix(float x, float y, float z, float anglex, float angley, float scale) {
		Matrix4f model = new Matrix4f();
		model.setIdentity();
		model.translate(x, y, z);
		model.rotateY((float)Math.toRadians(anglex));
		model.rotateX((float)Math.toRadians(angley));
		model.scale(scale);
		return model;
	}

	//===Translation===//
	public void translate(float x, float y, float z){
		set(3, 0, x);
		set(3, 1, y);
		set(3, 2, z);
	}

	public void translate(Vector3f position) {
		this.translate(position.x, position.y, position.z);
	}
	
	//===Scale===//
	public void scale(float scale) {
		Matrix4f smat = new Matrix4f();
		smat.setIdentity(scale);
		this.set(this.mul(smat).get());
	}
	
	//===Rotation===//
	public void rotateX(float angle){
		//Sin and cos
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);

		//RotX
		Matrix4f rotx = new Matrix4f();
		rotx.setIdentity();
		rotx.set(1, 1, cos);
		rotx.set(2, 1, -sin);
		rotx.set(1, 2, sin);
		rotx.set(2, 2, cos);
		this.set(this.mul(rotx).get());
	}

	public void rotateY(float angle){
		//Sin and cos
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);

		//RotY
		Matrix4f roty = new Matrix4f();
		roty.setIdentity();
		roty.set(0, 0, cos);
		roty.set(2, 0, sin);
		roty.set(0, 2, -sin);
		roty.set(2, 2, cos);
		this.set(this.mul(roty).get());
	}

	public void rotateZ(float angle){
		//Sin and cos
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);

		//RotZ
		Matrix4f rotz = new Matrix4f();
		rotz.setIdentity();
		rotz.set(0, 0, cos);
		rotz.set(1, 0, -sin);
		rotz.set(0, 1, sin);
		rotz.set(1, 1, cos);
		this.set(this.mul(rotz).get());
	}
	//==============//
	
	public Matrix4f mul(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result.set(i, j, m[i][0] * matrix.get(0, j) + m[i][1] * matrix.get(1, j) + m[i][2] * matrix.get(2, j) + m[i][3] * matrix.get(3, j));
			}
		}
		
		return result;
	}
	
	public float get(int x, int y){
		return m[x][y];
	}
	
	public void set(int x, int y, float value) {
		m[x][y] = value;
	}
	
	public float[][] get() {
		return m;
	}
	
	public void set(float[][] m) {
		this.m = m;
	}

	public float[] getGLMatrix() {
		float[] array = new float[16];
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				array[k] = m[i][j];
				k++;
			}
		}
		return array;
	}
	
	public static Matrix4f parse(float[] array) {
		if(array.length < 16){
			return null;
		}
		Matrix4f matrix = new Matrix4f();
		matrix.m[0][0] = array[0];
		matrix.m[0][1] = array[1];
		matrix.m[0][2] = array[2];
		matrix.m[0][3] = array[3];
		matrix.m[1][0] = array[4];
		matrix.m[1][1] = array[5];
		matrix.m[1][2] = array[6];
		matrix.m[1][3] = array[7];
		matrix.m[2][0] = array[8];
		matrix.m[2][1] = array[9];
		matrix.m[2][2] = array[10];
		matrix.m[2][3] = array[11];
		matrix.m[3][0] = array[12];
		matrix.m[3][1] = array[13];
		matrix.m[3][2] = array[14];
		matrix.m[3][3] = array[15];
		return matrix;
	}
}