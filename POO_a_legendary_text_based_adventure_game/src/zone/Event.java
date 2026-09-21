package src.zone;

public abstract class Event {
	private boolean eventEnded = false;

	public abstract void doEvent();

	public void endEvent() {
		eventEnded = true;
	}

	public boolean hasEventEnded() {
		return eventEnded;
	}
}
