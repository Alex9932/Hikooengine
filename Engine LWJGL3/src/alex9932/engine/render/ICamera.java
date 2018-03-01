package alex9932.engine.render;

import alex9932.vecmath.Matrix4f;
import alex9932.vecmath.Vector3f;

public interface ICamera {
	float getX();
	float getY();
	float getZ();
	float getAnglex();
	float getAngley();
	Matrix4f getProjectionMatrix();
	Matrix4f getViewMatrix();
	Vector3f getPosition();
}