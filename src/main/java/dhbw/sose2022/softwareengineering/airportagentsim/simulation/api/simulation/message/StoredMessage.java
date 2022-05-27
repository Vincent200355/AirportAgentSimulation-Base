package dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.message;

import dhbw.sose2022.softwareengineering.airportagentsim.simulation.api.simulation.entity.Entity;

import java.util.ArrayList;

public class StoredMessage {
    /**
     * The actual message being saved.
     */
    private final Message message;
    /**
     * The time during the simulation when this message was saved.
     */
    private final long creationTime;
    /**
     * An array of entities to transmit this message to.
     */
    private final ArrayList<Entity> targets;

    public StoredMessage(Message message, long creationTime, ArrayList<Entity> targets) {
        this.message = message;
        this.targets = targets;
        this.creationTime = creationTime;
    }

    public StoredMessage(Message message, long creationTime, Entity target) {
        this.message = message;

        ArrayList<Entity> t = new ArrayList<>();
        t.add(target);

        this.targets = t;
        this.creationTime = creationTime;
    }

    public Message getMessage() {
        return message;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public ArrayList<Entity> getTargets() {
        return targets;
    }

    public void removeTarget(Entity entity) {
        this.targets.remove(entity);
    }
}
