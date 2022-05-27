package dhbw.sose2022.softwareengineering.airportagentsim.simulation.simulation;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.AirportAgentSim;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.World;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message.*;
import dhbw.sose2022.softwareengineering.airportagentsim.simulation.plugin.AirportAgentSimulationAPI;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public final class SimulationWorld implements World {

    @SuppressWarnings("unused")
    private final AirportAgentSim aas;
    private final Logger logger;
    private final int width;
    private final int height;
    private long lifetime = 0;
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<StoredMessage> messages = new ArrayList<>();

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
        return this.entities;
    }

    @Override
    public void add(Entity e) {
        this.entities.add(e);
    }

    @Override
    public void addAll(Collection<? extends Entity> c) {
        this.entities.addAll(c);
    }

    @Override
    public void remove(Entity e) {
        this.entities.remove(e);
    }

    public void update() {
        for (Entity entity : this.entities) {
            try {
                receiveMessages(entity);
                entity.update();
            } catch (Exception e) {
                this.logger.warn("Failed to update entity of type " + entity.getClass().getSimpleName()
                        + " from plugin \"" + AirportAgentSimulationAPI.getLoadedPlugin(entity.getPlugin()).getName()
                        + "\"", e);
            }
        }
        lifetime++;
        updateMessenger();
    }

    /**
     * Entities can call this method to send messages to (other) entities. <p>
     *
     * @param m The message to send.
     */
    public void sendMessage(Message m) {
        if (messages instanceof DirectedMessage) {
            if (((DirectedMessage) m).getTarget() == null)
                throw new NullPointerException("target must not be null");
            messages.add(new StoredMessage(m, this.lifetime, ((DirectedMessage) m).getTarget()));
        } else {
            messages.add(new StoredMessage(m, this.lifetime, this.entities));
        }
    }

    /**
     * This method sends all messages intended for an {@link Entity}  that it can receive at the current time.<p>
     *
     * @param entity the entity for which the messages are intended.
     */
    public void receiveMessages(Entity entity) {
        for (StoredMessage storedMessage : this.messages) {
            if (storedMessage.getTargets().contains(entity)) {
                if (storedMessage.getMessage() instanceof GlobalMessage) {
                    // GlobalMessages
                    entity.receiveMessage(storedMessage.getMessage());
                    storedMessage.removeTarget(entity);
                } else {
                    // LocalMessages
                    LocalMessage localMessage = (LocalMessage) storedMessage.getMessage();
                    if (!entity.getPosition().isInRadius(
                            localMessage.getOriginPosition(),
                            localMessage.getMaxRange())) {
                        entity.receiveMessage(storedMessage.getMessage());
                    }
                }
            }
        }
    }

    /**
     * In this method, all {@link StoredMessage messages} that are older than their lifetime or have already been
     * delivered are deleted from {@code StoredMessages}.
     */
    private void updateMessenger() {
        int messageLifetime = 2; // Number of iterations of
        // the simulation after which a message is no longer delivered.

        this.messages.removeIf((StoredMessage message) ->
                ((this.lifetime - message.getCreationTime()) > messageLifetime
                        || message.getTargets().isEmpty()));
    }
}
