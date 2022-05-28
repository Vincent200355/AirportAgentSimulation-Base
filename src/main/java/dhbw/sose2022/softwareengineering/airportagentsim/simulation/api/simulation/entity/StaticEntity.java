package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity;

public abstract non-sealed class StaticEntity extends Entity {
	
	public StaticEntity() {}
	
	@Override
	public final void update() {
		pluginUpdate();
	}
	
}
