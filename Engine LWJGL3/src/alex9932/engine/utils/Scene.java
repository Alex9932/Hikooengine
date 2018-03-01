package alex9932.engine.utils;

import java.util.ArrayList;

import alex9932.engine.Terrain;
import alex9932.engine.entity.Entity;
import alex9932.engine.render.DirLight;
import alex9932.engine.render.PointLight;
import alex9932.engine.sound.Source;
import alex9932.vecmath.Vector3f;

public class Scene {
	private ArrayList<Entity> entitys = new ArrayList<Entity>();
	private ArrayList<PointLight> lights = new ArrayList<PointLight>();
	private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
	private ArrayList<Source> sources = new ArrayList<Source>();
	private DirLight dirLight;
	
	public Scene() {
		dirLight = new DirLight(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 0);
	}

	public void setDirLight(DirLight dirLight) {
		this.dirLight = dirLight;
	}
	
	public DirLight getDirLight() {
		return dirLight;
	}
	
	public void addLight(PointLight light) {
		this.lights.add(light);
	}
	
	public void removeLight(PointLight light) {
		this.lights.remove(light);
	}
	
	public void addEntity(Entity entity) {
		this.entitys.add(entity);
	}
	
	public void removeEntity(Entity entity) {
		this.entitys.remove(entity);
	}

	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}

	public void removeTerrain(Terrain terrain) {
		terrains.remove(terrain);
	}

	public void addSoundSource(Source src) {
		sources.add(src);
	}

	public void removeSoundSource(Source src) {
		sources.remove(src);
	}
	
	public ArrayList<Entity> getEntitys() {
		return entitys;
	}
	
	public ArrayList<PointLight> getLights() {
		return lights;
	}
	
	public ArrayList<Terrain> getTerrains() {
		return terrains;
	}
	
	public ArrayList<Source> getSources() {
		return sources;
	}

	public void update() {
		for (int i = 0; i < entitys.size(); i++) {
			entitys.get(i).update(getTerrain(entitys.get(i)));
		}
	}

	private Terrain getTerrain(Entity entity) {
		return getTrrain(entity.getPos().x / Terrain.SIZE, entity.getPos().z / Terrain.SIZE);
	}

	private Terrain getTrrain(float x, float z) {
		for (int i = 0; i < terrains.size(); i++) {
			if(terrains.get(i).getX() == x && terrains.get(i).getZ() == z){
				return terrains.get(i);
			}
		}
		return terrains.get(0);
	}
}