package dhbw.sose2022.softwareengineering.airportagentsim.simulation.ui.update;

import javafx.application.Platform;

public final class GUIUpdater implements Runnable {
	
	private final UpdatableGUI target;
	private boolean requested;
	private boolean done;
	
	public GUIUpdater(UpdatableGUI target) {
		this.target = target;
	}
	
	@Override
	public void run() {
		synchronized(this) {
			if(!this.requested)
				throw new IllegalStateException();
			this.requested = false;
			try {
				this.target.updateGUI();
			} finally {
				this.done = true;
				notify();
			}
		}
	}
	
	public void runInJFXThread() {
		synchronized(this) {
			if(this.requested)
				throw new IllegalStateException();
			this.requested = true;
			Platform.runLater(this);
			while(!this.done) {
				try {
					wait();
				} catch(InterruptedException e) {}
			}
			this.done = false;
		}
	}
	
}
