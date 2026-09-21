package src.zone;

import src.game.World;

public class NaturalDisaster extends Event {
	private final int stepLimit;
	private Location staffSpawn;

	public NaturalDisaster(int stepLimit, Location staffSpawn) {
		if (staffSpawn!=null) {
			this.staffSpawn=staffSpawn;
		} else {
			staffSpawn = null;
		}
		this.stepLimit = stepLimit;
	}

	@Override
	public void doEvent() {
		int NbrOfStep = World.getPlayer().getStepCount();
		if (NbrOfStep == stepLimit) {
			World.getPlayer().takeDamage(100000);
			System.out.println("BOOOOOOOOOOOOM vla la lave");
		} else if (World.getPlayer().getStepCount() < stepLimit) {
			staffSpawn.addEvent(new FindStaffPiece());
		}
	}

	public void setStaffSpawn(Location loc) {
		this.staffSpawn = loc;
	}

}
