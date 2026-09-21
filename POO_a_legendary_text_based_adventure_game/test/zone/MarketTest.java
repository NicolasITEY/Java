package test.zone;

import static org.junit.Assert.*;

import org.junit.Test;

import src.game.World;
import src.hero.Hero;
import src.hero.item.Item;
import src.hero.item.Staff;
import src.zone.Market;

public class MarketTest {
	@Test
	public void testMarketSpendsMoney() {
		World.startGame();
		Hero hero = World.getPlayer();

		int moneyBefore = hero.getMoney();

		Market market = new Market(false);
		market.purchaseItem("FOOD");
		assertNotEquals(moneyBefore, hero.getMoney());
	}

	@Test
	public void testMarketGivesStaff() {
		World.startGame();
		Hero hero = World.getPlayer();

		Market market = new Market(true);

		market.purchaseItem("STAFF");

		boolean foundStaff = false;
		for (Item item : hero.getBag().getItems()) {
			if (item instanceof Staff && ((Staff) item).getPieceId() == 2) {
				foundStaff = true;
				break;
			}
		}

		assertTrue(foundStaff);
	}

}
