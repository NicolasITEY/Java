package src.zone;

import src.game.World;

public class UpgradeBag extends Event {

	private final int upgradeAmount;

	public UpgradeBag(int amount) {
		upgradeAmount = amount;
	}

	@Override
	public void doEvent() {
		World.getPlayer().getBag().upgradeCapacity(upgradeAmount);
		endEvent();
	}

}
