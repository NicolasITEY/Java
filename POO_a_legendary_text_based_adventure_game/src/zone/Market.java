package src.zone;

import src.game.World;
import src.hero.item.Food;
import src.hero.item.Potion;
import src.hero.item.Staff;

public class Market extends Event {

	private final Food foodSold = new Food("Pizza","classic peperoni pizza",50);

	private final int foodPrice = 15;
	private final Potion potionSold = new Potion("Mana_Potion","normal potion", 25);
	private final int potionPrice = 15;
	private final Staff staffSold = new Staff(2,"the gem recepticle");
	private final int staffPrice = 100;
	private final boolean sellsStaff;

	public Market(boolean sellsStaff) {
		this.sellsStaff = sellsStaff;
	}

	public void purchaseItem(String type) {
		switch (type) {
		case "FOOD":
			boolean canPurchaseF = World.getPlayer().spendMoney(foodPrice);
			if (canPurchaseF) {
				World.getPlayer().gainItem(foodSold);
			}
			break;
		case "POTION":
			boolean canPurchaseP = World.getPlayer().spendMoney(potionPrice);
			if (canPurchaseP) {
				World.getPlayer().gainItem(potionSold);
			}
			break;
		case "STAFF":
			if (sellsStaff) {
				boolean canPurchaseS = World.getPlayer().spendMoney(staffPrice);
				if (canPurchaseS) {
					World.getPlayer().gainItem(staffSold);
				}
			} else {
				System.out.println("sorry link but i don't sell that here,maybe the graveyard has one");
			}
			break;
		default:
			System.out.println("sorry link but i don't sell that here");
		}
	}

	@Override
	public void doEvent() {
		endEvent();
	}

}
