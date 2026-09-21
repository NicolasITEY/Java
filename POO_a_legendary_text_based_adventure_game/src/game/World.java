package src.game;

import java.util.Arrays;

import src.enemy.Direwolf;
import src.enemy.Dragon;
import src.enemy.Enemy;
import src.enemy.FinalBoss;
import src.enemy.Litch;
import src.enemy.SwampMonster;
import src.enemy.Witch;
import src.hero.Hero;
import src.zone.Biome;
import src.zone.ElementType;
import src.zone.EnemySpawn;
import src.zone.FinalBossSpawn;
import src.zone.Location;
import src.zone.Market;
import src.zone.NaturalDisaster;
import src.zone.Puzzle;
import src.zone.SpawnBoss;
import src.zone.UpgradeBag;

public class World {

	private static Location[] MAP;
	private static Location[] workMap;
	private static int locationIndex = 3;
	private static Location curLocation;
	private static Commande commandHandler;
	private static Hero hero;
	private static boolean gameRunning = false;
	private static boolean isGameOver = false;

	public static boolean gameIsOver() {
		return isGameOver;
	}
	
	public static Location getLocationAt(int i) {
		if(workMap!=null) {
			return workMap[i];
		}
		else {
			return MAP[i];
		}
	}
	
	public static Location getCurrentLocation() {
		return workMap[locationIndex];
	}
	
	public static void startGame() {
		if (MAP != null) {
			workMap = Arrays.copyOf(MAP, 49);
			((NaturalDisaster)workMap[35].getEvent()).setStaffSpawn(workMap[28]);;
		}

		isGameOver=false;
		hero = new Hero();
		commandHandler = new Commande();
		gameRunning = true;
	}

	public static Hero getPlayer() {
		return hero;
	}

	public static void restart() {
		if (MAP != null) {
			workMap = Arrays.copyOf(MAP, 49);
		}
		locationIndex = 3;
		isGameOver=false;
	}

	public static void gameOver() {
		System.out.println("restart? or give up?");
		isGameOver=true;
		commandHandler.handleLine();
	}

	public static void quit() {
		System.exit(0);
	}

	public static void goToLocation(int locIndex) {
		try {
			curLocation = workMap[locIndex];
			locationIndex = locIndex;
			curLocation.enterLocation();
		} catch (Exception e) {
			System.err.println("tried accessing a location out of bounds");
		}
	}

	/**
	 * the loop where the game happens. while the game is running handle the line
	 * comands
	 */
	public static void gamePlayLoop() {
		while (gameRunning) {
			System.out.println("you're somewhere do something");
			commandHandler.handleLine();
			System.out.println("here we go again");
		}
	}

	public static void initWorld() {
		hero = new Hero();

		Enemy[] swampEnemies = new Enemy[] { new Enemy(10, 10, ElementType.MUD, null, 0, 0, "mud monster"),
				new Enemy(10, 10, ElementType.MUD, null, 0, 0, "other mud monster") };
		Enemy[] forestEnemies = new Enemy[] { new Enemy(10, 10, ElementType.LIFE, null, 0, 0, "bear"),
				new Enemy(10, 10, ElementType.LIFE, null, 0, 0, "wolf") };
		Enemy[] volcanoEnemies = new Enemy[] { new Enemy(10, 10, ElementType.FIRE, null, 0, 0, "lava slime"),
				new Enemy(10, 10, ElementType.FIRE, null, 0, 0, "another hot enemy") };
		Enemy[] graveyardEnemies = new Enemy[] { new Enemy(10, 10, ElementType.CURSED, null, 0, 0, "skeleton"),
				new Witch(0, 0, ElementType.CURSED, null, 0, 0, "witch") };

		Direwolf direwolf = new Direwolf(0, 0, ElementType.LIFE, null, 0, 0, null);
		Dragon dragon = new Dragon(0, 0, ElementType.FIRE, null, 0, 0, null);
		Litch litch = new Litch(0, 0, ElementType.CURSED, null, 0, 0, null);
		SwampMonster swampMonster = new SwampMonster(0, 0, ElementType.MUD, null, 0, 0, null);
		FinalBoss finalBoss = new FinalBoss(0, 0, null);
		Biome swamp = new Biome(swampEnemies, swampMonster, "Swamp", ElementType.MUD);
		Biome forest = new Biome(forestEnemies, direwolf, "Forest", ElementType.LIFE);
		Biome volcano = new Biome(volcanoEnemies, dragon, "Volcano", ElementType.FIRE);
		Biome graveyard = new Biome(graveyardEnemies, litch, "Graveyard", ElementType.CURSED);
		Biome neutral = new Biome(null, finalBoss, "Neutral Zone", ElementType.NEUTRAL);
		String baseTexts = "location";

		Location[] normalLocs = new Location[] {
				new Location(null, false, neutral, new int[] { 10, 4, 2 }, new String[] { "NORTH", "EAST", "WEST" },
						baseTexts), // 3
				new Location(new UpgradeBag(2), false, neutral, new int[] { 17, 11, 3, 9 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 10
				new Location(null, false, neutral, new int[] { 24, 18, 10, 16 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 17

				new Location(null, false, neutral, new int[] { 28, 22, 14 }, new String[] { "NORTH", "EAST", "SOUTH" },
						baseTexts), // 21
				new Location(new UpgradeBag(1), false, neutral, new int[] { 29, 23, 15, 21 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 22
				new Location(null, false, neutral, new int[] { 29, 24, 16, 22 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 23
				new Location(new FinalBossSpawn(0), false, neutral, new int[] { 29, 25, 17, 23 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 24 boss
				new Location(null, false, neutral, new int[] { 29, 26, 18, 24 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 25
				new Location(new UpgradeBag(1), false, neutral, new int[] { 29, 27, 19, 25 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 26
				new Location(null, false, neutral, new int[] { 34, 20, 26 }, new String[] { "NORTH", "SOUTH", "WEST" },
						baseTexts), // 27

				new Location(null, false, neutral, new int[] { 38, 32, 24, 30 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 31
				new Location(new UpgradeBag(2), false, neutral, new int[] { 45, 39, 31, 37 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 38
				new Location(null, false, neutral, new int[] { 46, 38, 47 }, new String[] { "EAST", "SOUTH", "WEST" },
						baseTexts), // 45
		};

		Location[] swampLocs = new Location[] {
				new Location(new SpawnBoss(1), false, swamp, new int[] { 7, 1 }, new String[] { "NORTH", "EAST" },
						baseTexts), // 0
				new Location(new EnemySpawn(3), false, swamp, new int[] { 8, 2, 0 },
						new String[] { "NORTH", "EAST", "WEST" }, baseTexts), // 1
				new Location(null, false, swamp, new int[] { 9, 3, 1 }, new String[] { "NORTH", "EAST", "WEST" },
						baseTexts), // 2

				new Location(null, false, swamp, new int[] { 14, 8, 0 }, new String[] { "NORTH", "EAST", "SOUTH" },
						baseTexts), // 7
				new Location(new Market(false), false, swamp, new int[] { 15, 9, 1, 7 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 8
				new Location(new EnemySpawn(2), false, swamp, new int[] { 16, 10, 2, 8 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 9

				new Location(null, false, swamp, new int[] { 21, 15, 7 }, new String[] { "NORTH", "EAST", "SOUTH" },
						baseTexts), // 14
				new Location(null, false, swamp, new int[] { 22, 16, 8, 14 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 15
				new Location(new Puzzle(), false, swamp, new int[] { 23, 17, 9, 15 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts),// 16
		};

		Location[] forestLocs = new Location[] {
				new Location(null, false, forest, new int[] { 11, 5, 3 }, new String[] { "NORTH", "EAST", "WEST" },
						baseTexts), // 4
				new Location(null, false, forest, new int[] { 12, 6, 4 }, new String[] { "NORTH", "EAST", "WEST" },
						baseTexts), // 5
				new Location(new SpawnBoss(1), false, forest, new int[] { 13, 5 }, new String[] { "NORTH", "WEST" },
						baseTexts), // 6

				new Location(new EnemySpawn(2), false, forest, new int[] { 18, 12, 4, 10 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 11
				new Location(new Market(false), false, forest, new int[] { 19, 13, 5, 11 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 12
				new Location(new EnemySpawn(2), false, forest, new int[] { 20, 6, 12 },
						new String[] { "NORTH", "SOUTH", "WEST" }, baseTexts), // 13

				new Location(new EnemySpawn(1), false, forest, new int[] { 25, 19, 11, 17 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 18
				new Location(new EnemySpawn(1), false, forest, new int[] { 26, 20, 12, 18 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 19
				new Location(null, false, forest, new int[] { 27, 13, 19 }, new String[] { "NORTH", "SOUTH", "WEST" },
						baseTexts),// 20
		};

		Location[] volcanoLocs = new Location[] {
				new Location(null, false, volcano, new int[] { 35, 29, 21 }, new String[] { "NORTH", "EAST", "SOUTH" },
						baseTexts), // 28
				new Location(new EnemySpawn(2), false, volcano, new int[] { 36, 30, 22, 28 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 29
				new Location(null, false, volcano, new int[] { 37, 31, 23, 29 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 30

				new Location(new NaturalDisaster(25,null), false, volcano, new int[] { 42, 36, 28 },
						new String[] { "NORTH", "EAST", "SOUTH" }, baseTexts), // 35
				new Location(new Market(false), false, volcano, new int[] { 43, 37, 29, 35 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 36
				new Location(null, false, volcano, new int[] { 44, 38, 30, 36 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 37

				new Location(new SpawnBoss(1), false, volcano, new int[] { 43, 35 }, new String[] { "EAST", "SOUTH" },
						baseTexts), // 42
				new Location(new EnemySpawn(2), false, volcano, new int[] { 42, 36, 42 },
						new String[] { "EAST", "SOUTH", "WEST" }, baseTexts), // 43
				new Location(null, false, volcano, new int[] { 43, 37, 43 }, new String[] { "EAST", "SOUTH", "WEST" },
						baseTexts),// 44
		};

		Location[] graveYardLocs = new Location[] {
				new Location(null, false, graveyard, new int[] { 39, 33, 25, 31 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 32
				new Location(new EnemySpawn(1), false, graveyard, new int[] { 40, 34, 26, 32 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 33
				new Location(new EnemySpawn(1), false, graveyard, new int[] { 41, 27, 33 },
						new String[] { "NORTH", "SOUTH", "WEST" }, baseTexts), // 34

				new Location(new EnemySpawn(1), false, graveyard, new int[] { 46, 40, 32, 38 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 39
				new Location(new Market(true), false, graveyard, new int[] { 47, 41, 33, 39 },
						new String[] { "NORTH", "EAST", "SOUTH", "WEST" }, baseTexts), // 40
				new Location(null, false, graveyard, new int[] { 48, 34, 40 },
						new String[] { "NORTH", "SOUTH", "WEST" }, baseTexts), // 41

				new Location(null, false, graveyard, new int[] { 47, 39, 45 }, new String[] { "EAST", "SOUTH", "WEST" },
						baseTexts), // 46
				new Location(new EnemySpawn(2), false, graveyard, new int[] { 48, 40, 46 },
						new String[] { "EAST", "SOUTH", "WEST" }, baseTexts), // 47
				new Location(new SpawnBoss(1), false, graveyard, new int[] { 41, 47 }, new String[] { "SOUTH", "WEST" },
						baseTexts),// 48
		};

		MAP = new Location[] { 
				swampLocs[0], swampLocs[1], swampLocs[2], normalLocs[0], forestLocs[0], forestLocs[1],forestLocs[2], 
				swampLocs[3], swampLocs[4], swampLocs[5], normalLocs[1], forestLocs[3], forestLocs[4],forestLocs[5], 
				swampLocs[6], swampLocs[7], swampLocs[8], normalLocs[2], forestLocs[6], forestLocs[7],forestLocs[8], 
				normalLocs[3], normalLocs[4], normalLocs[5], normalLocs[6], normalLocs[7], normalLocs[8],normalLocs[9],
				volcanoLocs[0], volcanoLocs[1], volcanoLocs[2], normalLocs[10], graveYardLocs[0],graveYardLocs[1], graveYardLocs[2], 
				volcanoLocs[3], volcanoLocs[4], volcanoLocs[5], normalLocs[11],graveYardLocs[3], graveYardLocs[4], graveYardLocs[5], 
				volcanoLocs[6], volcanoLocs[7], volcanoLocs[8], normalLocs[12], graveYardLocs[6], graveYardLocs[7], graveYardLocs[8], 
				};
	}

	public static void main(String[] args) {
		World.initWorld();
		World.startGame();
		World.gamePlayLoop();

	}

}
