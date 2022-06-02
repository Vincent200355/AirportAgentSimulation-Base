package dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.geometry.Point;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.GlobalMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.LocalMessage;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.Message;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation.message.StoredMessage;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class SimulationWorld implements World {

	@SuppressWarnings("unused")
	private final AirportAgentSim aas;
	private final Logger logger;

	private final int width;
	private final int height;
	private long lifetime = 0;
	private final ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<StoredMessage> messages = new ArrayList<StoredMessage>();

	/**
	 * Creates a new simulation world. Both the width and the height must be
	 * positive.<br>
	 * <br>
	 * 
	 * @param aas    the simulation
	 * @param logger the logger to use
	 * @param width  the width of the world
	 * @param height the height of the world
	 */
	public SimulationWorld(AirportAgentSim aas, Logger logger, int width, int height) {
		this.aas = aas;
		this.logger = logger;
		this.width = width;
		this.height = height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public Collection<Entity> getEntities() {
		ArrayList<Entity> list = new ArrayList<Entity>();
		list.addAll(this.entities);
		return list;
	}

	@Override
	public Collection<Entity> getEntities(int x, int y, int width, int height) {
		return getEntities(x, y, width, height, false);
	}

	@Override
	public Collection<Entity> getEntities(int x, int y, int width, int height, boolean excludeTouching) {
		if (width < 0 || height < 0)
			return new ArrayList<Entity>();
		ArrayList<Entity> list = new ArrayList<Entity>();
		findEntities(list, x, y, width, height, excludeTouching);
		return list;
	}

	@Override
	public Collection<Entity> getEntities(int centerX, int centerY, double maxDistance) {
		Validate.isTrue(!Double.isNaN(maxDistance), "Cannot search a circle of radius NaN");
		if (maxDistance < 0)
			return new ArrayList<Entity>();
		if (Double.isInfinite(maxDistance))
			return getEntities();
		ArrayList<Entity> list = new ArrayList<Entity>();
		findEntities(list, centerX, centerY, maxDistance);
		return list;
	}

	public ArrayList<Message> getMessages() {
		ArrayList<Message> list = new ArrayList<Message>();
		this.messages.forEach(m -> list.add(m.getMessage()));
		return list;
	}

	@Override
	public void sendMessage(Message m) {
		this.messages.add(new StoredMessage(m, this.lifetime, this.entities));
	}

	@Override
	@Deprecated
	public void add(Entity e) {
		Validate.notNull(e);
		e.spawn(this, 0, 0, 1, 1);
	}

	@Override
	@Deprecated
	public void addAll(Collection<? extends Entity> c) {
		Validate.notNull(c);
		Validate.isTrue(c.size() <= this.width * this.height, "Cannot fit entities in world");
		Iterator<? extends Entity> iterator = c.iterator();
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				if (!iterator.hasNext())
					return;
				Entity e = iterator.next();
				Validate.notNull(e);
				e.spawn(this, x, y, 1, 1);
			}
		}
	}

	@Override
	@Deprecated
	public void remove(Entity e) {
		Validate.notNull(e);
		e.kill();
	}

	public int getNextEntityUID() {
		return (this.entities.size() == 0) ? 1 : this.entities.get(this.entities.size() - 1).getUID() + 1;
	}

	public void addEntity(Entity e) {
		this.entities.add(e);
	}

	public void removeEntity(Entity e) {
		this.entities.remove(e);
	}

	public void update() {

		// Process messages
		for (Entity entity : this.entities) {
			receiveMessages(entity);
		}

		// Process entity updates
		for (Entity entity : this.entities) {
			try {
				entity.update();
			} catch (Exception e) {
				this.logger
						.warn("Failed to update entity of type " + entity.getClass().getSimpleName() + " from plugin \""
								+ AirportAgentSimulationAPI.getLoadedPlugin(entity.getPlugin()).getName() + "\"", e);
			}
		}

		this.lifetime++;

		updateMessenger();

	}

	/**
	 * This method sends all messages intended for an {@link Entity} that it
	 * can receive at the current time.
	 * <p>
	 * 
	 * @param entity the entity for which the messages are intended.
	 */
	public void receiveMessages(Entity entity) {
		for (StoredMessage storedMessage : this.messages) {
			if (storedMessage.getTargets().contains(entity)) {
				if (storedMessage.getMessage() instanceof GlobalMessage) {
					// GlobalMessages
					try {
						entity.receiveMessage(storedMessage.getMessage());
					} catch (Exception e) {
						this.logger.warn("Entity of type " + entity.getClass().getSimpleName() + " from plugin \""
								+ AirportAgentSimulationAPI.getLoadedPlugin(entity.getPlugin()).getName()
								+ "\" failed to process a message of type "
								+ storedMessage.getMessage().getClass().getSimpleName(), e);
					}
					storedMessage.removeTarget(entity);
				} else {
					// LocalMessages
					LocalMessage localMessage = (LocalMessage) storedMessage.getMessage();
					if (!entity.getPosition().isInRadius(
							localMessage.getOriginPosition(),
							localMessage.getMaxRange())) {
						try {
							entity.receiveMessage(storedMessage.getMessage());
						} catch (Exception e) {
							this.logger.warn("Entity of type " + entity.getClass().getSimpleName() + " from plugin \""
									+ AirportAgentSimulationAPI.getLoadedPlugin(entity.getPlugin()).getName()
									+ "\" failed to process a message of type "
									+ storedMessage.getMessage().getClass().getSimpleName(), e);
						}
					}
				}
			}
		}
	}

	public void findEntities(Collection<Entity> output, int x, int y, int width, int height, boolean excludeTouching) {

		int minX = x;
		int minY = y;
		int maxX = minX + width;
		int maxY = minY + height;

		for (Entity e : this.entities) {

			Point pos = e.getPosition();

			int entityMinX = pos.getX();
			int entityMaxX = entityMinX + e.getWidth();
			if (entityMaxX < minX || entityMinX > maxX)
				continue;
			int entityMinY = pos.getY();
			int entityMaxY = entityMinY + e.getHeight();
			if (entityMaxY < minY || entityMinY > maxY)
				continue;
			if (excludeTouching
					&& (entityMaxX == minX || entityMinX == maxX)
					&& (entityMaxY == minY || entityMinY == maxY))
				continue;
			if (excludeTouching
					&& (!(entityMaxX > minX && entityMinX < maxX) || !(entityMaxY > minY && entityMinY < maxY)))
				continue;

			output.add(e);

		}

	}

	public void findEntities(Collection<Entity> output, int centerX, int centerY, double maxDistance) {

		for (Entity e : this.entities) {
			Point pos = e.getPosition();
			if (calculateDistance(centerX, centerY, 0, 0, pos.getX(), pos.getY(), e.getWidth(),
					e.getHeight()) <= maxDistance)
				output.add(e);
		}

	}

	public boolean collides(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2,
			boolean excludeTouching) {

		int minX1 = x1;
		int minX2 = x2;
		int maxX1 = minX1 + width1;
		int maxX2 = minX2 + width2;
		if (maxX2 < minX1 || minX2 > maxX1)
			return false;

		int minY1 = y1;
		int minY2 = y2;
		int maxY1 = minY1 + height1;
		int maxY2 = minY2 + height2;
		if (maxY2 < minY1 || minY2 > maxY1)
			return false;

		if (excludeTouching
				&& (!(maxX2 > minX1 && minX2 < maxX1) || !(maxY2 > minY1 && minY2 < maxY1)))
			return false;

		return true;

	}

	public double calculateDistance(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {

		int minX1 = x1;
		int minX2 = x2;
		int minY1 = y1;
		int minY2 = y2;
		int maxX1 = minX1 + width1;
		int maxX2 = minX2 + width2;
		int maxY1 = minY1 + height1;
		int maxY2 = minY2 + height2;

		double dx;
		if (maxX1 < minX2)
			dx = minX2 - maxX1;
		else if (minX1 > maxX2)
			dx = minX1 - maxX2;
		else
			dx = 0;

		double dy;
		if (maxY1 < minY2)
			dy = minY2 - maxY1;
		else if (minY1 > maxY2)
			dy = minY1 - maxY2;
		else
			dy = 0;

		return Math.sqrt(dx * dx + dy * dy);

	}

	/**
	 * In this method, all {@link StoredMessage messages} that are older than
	 * their lifetime or have already been delivered are deleted from
	 * {@code StoredMessages}.
	 */
	private void updateMessenger() {
		int messageLifetime = 2; // Number of iterations of
		// the simulation after which a message is no longer delivered.

		this.messages.removeIf((StoredMessage message) -> ((this.lifetime - message.getCreationTime()) > messageLifetime
				|| message.getTargets().isEmpty()));
	}

}
