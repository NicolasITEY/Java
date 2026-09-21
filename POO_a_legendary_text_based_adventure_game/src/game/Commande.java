package src.game;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;

import src.enemy.Enemy;
import src.zone.*;
import src.hero.*;
import src.hero.item.*;

public class Commande {
	private final String[] POSSIBLE_COMMANDS = 
		{ "HELP", // HELP [args]
															// for every commands put as an argument, display it's
															// description
															// or display currently usable commands

			"QUIT", // QUIT : Quits the game

			"GO", // GO [direction] : go NORTH SOUTH EAST or WEST

			"LOOK", // LOOK [arguments] : for every argument put after, display their descriptions
					// (displays the location's description by default)

			"REPLACE", // REPLACE [item name] : replaces an item put in the argument with the new item
						// if no space is advaliable in your bag (otherwise it's automatically added to
						// your bag)

			"USE", // USE [item name] : uses an item in your bag with name arg (grimoires and staff
					// items will need to be used to have their effects)

			"CAST", // CAST [spell name] [enemy name] : cast a spell to an enemy

			"DROP", // DROP [item name] : drops the item specified or refuse to replace an item in
					// your bag with the new item

			"ATTEMPT", // ATTEMPT [letter] : at a specific location you may attempt a letter

			"RESTART" // RESTART : on death or when your steps runs out, restart the game
	};

	
	
	private ArrayList<String> argV;
	private HashMap<String, String> commandDescription;
	private int c;
	private boolean bagNeedsSpace = false;
	
	public Commande() {

		commandDescription = new HashMap<>();
		String[] descriptions = {
				" HELP [args] \nfor every commands put as an argument, display it's description or display\n",

				"QUIT \nQuits the game /!\\ you will lose all your progress up to this point\n", 
				
				"GO [direction]\n go NORTH SOUTH EAST or WEST\n",

				"/LOOK [arguments] \n for every argument put after, display their descriptions \ndisplays the location's description by default\n",

				"/REPLACE [item name] \n replaces an item put in the argument with the new item if no space is advaliable \n otherwise it's automatically added to your bag\n",

				"USE [item name] \n uses an item in your bag with the name given\n",

				"CAST [spell name] [enemy name] \n cast a spell to an enemy",

				"DROP [item name] \n drops the item specified or refuse to replace an item in your bag with the new item\n",

				"ATTEMPT [letter] \n attempt a specific letter, good luck with the puzzle\n",

				"RESTART \n on death or when your steps runs out, restart the game" };
		for (int i = 0; i < POSSIBLE_COMMANDS.length; i++) {
			commandDescription.put(POSSIBLE_COMMANDS[i], descriptions[i]);
		}
	}
	public void toggleBagNeedsSpace() {
		bagNeedsSpace=!bagNeedsSpace;
	}
	public void handleLine() {
		try {
			c = System.in.read();
		} catch (IOException e) {
			System.err.println("Error reading input: " + e.getMessage());
		}

		argV = new ArrayList<>();
		// int curArgIndex=0;
		String arg = "";
		while ((char) c != '\n') {
			if ((char) c == ' ') {
				argV.add(arg);
				arg = "";
			} else {
				arg += (char) c;
			}
			try {
				c = System.in.read();
			} catch (IOException e) {
				System.err.println("Error reading input: " + e.getMessage());
			}
		}

		argV.add(arg);

		doCommand();
		argV = null;
	}

	private void helpCommand() {
		if (argV.size() == 1) {
			System.out.println("advaliable commands");
			for (String com : POSSIBLE_COMMANDS) {

				System.out.println(com);
			}
		} else {
			for (int i = 1; i < argV.size(); i++) {
				System.out.println(commandDescription.get(new String(argV.get(i))));
			}
		}
	}

	private void attackEnemy() {
		Event curEvent = World.getCurrentLocation().getEvent();
		if (curEvent instanceof EnemySpawn) {
			Enemy[] enemies = ((EnemySpawn) curEvent).getEnemies();
			Enemy targetedEnemy = null;
			for (Enemy enemy : enemies) {
				if (enemy.getName().equals(new String(argV.get(2)))) {
					targetedEnemy = enemy;
				}
			}
			if (targetedEnemy != null) {
				Spell useSpell = World.getPlayer().getSpell(new String(argV.get(1)));
				if (useSpell != null) {
					World.getPlayer().useSpell(targetedEnemy, useSpell);
				} else {
					System.out.println("spell doesn't exist");
					handleLine();
				}
			} else {
				System.out.println("enemy doesn't exist");
				handleLine();

			}
		} else {
			System.out.println("can't cast a spell, no enemies in sight");
			handleLine();
		}
	}
	
	private void dropItem() {
		Bag playerBag = World.getPlayer().getBag();
		boolean found = false;
		String dropItemName = new String(argV.get(1));
		for(Item items : playerBag.getItems() ) {
			if(items.getName().equals(dropItemName)) {
				playerBag.removeItem(items);
				found = true;
				break;
			}
		}
		if(found) {
			System.out.println("you dropped the "+dropItemName);
		}else {
			System.out.println("couldn't find the "+dropItemName);
			handleLine();
		}
	}

	private void useItem() {
		ArrayList<Item> items = World.getPlayer().getBag().getItems();
		String itemName = new String(argV.get(1));
		switch (itemName) {
			case "GRIMOIRE" : 
				boolean found = false;
				for(Item grimoire : items) {
					if(grimoire instanceof Grimoire) {
						found =true;
						System.out.println("you have studdied the "+((Grimoire) grimoire).getName());
						grimoire.consume();
					}
				}
				if(!found) {
					System.out.println("you couldn't find any grimoires in your bag");
					handleLine();
				}
				break;
			case "STAFF" :
				boolean foundS=false;
				for(Item staff : items) {
					if(staff  instanceof Staff) {
						foundS = true;
						System.out.println("you have assembled the piece into your staff");
						staff.consume();
					}
				}
				if(!foundS) {
					System.out.println("you couldn't find any grimoires in your bag");
					handleLine();
				}
				break;
			default :
				Item useItem = null;
				for(Item item : items) {
					if(item.getName().equals(itemName)) {
						useItem=item;
						break;
					}
				}
				if(useItem!=null) {
					useItem.consume();
				}else {
					System.out.println("item not found");
					handleLine();
				}
		}
	}
	
	public void doCommand() {
		String order = new String(argV.get(0));
		if (order.equals(POSSIBLE_COMMANDS[0]) && !World.gameIsOver() ) { 
			//HELP
			helpCommand();
		} else if (order.equals(POSSIBLE_COMMANDS[1])) { 
			//QUIT
			System.out.println("we're done here");
			World.quit();

		} else if(order.equals(POSSIBLE_COMMANDS[2]) && !World.gameIsOver() ) { 
			//GO dirrection
			World.getCurrentLocation().exitLocation(new String(argV.get(1)));
			
		}
		else if(order.equals(POSSIBLE_COMMANDS[3]) && !World.gameIsOver() ) { 
			//LOOK args
			if(argV.size()==1) {
				World.getCurrentLocation().look();
			}else {
				if(new String(argV.get(1)).equals("Bag")) {
					for(Item item : World.getPlayer().getBag().getItems()) {
						item.showDescription();
					}
				}
			}
			
		}
		else if(order.equals(POSSIBLE_COMMANDS[4]) && bagNeedsSpace && !World.gameIsOver() ) { 
			//REPLACE item name
			if(argV.size()==2) {
				dropItem();
			}else {
				System.out.println("amount of items trying to replace is incorrect");
				System.out.println("expected 1 found "+(argV.size()-1));
			}
		}
		else if(order.equals(POSSIBLE_COMMANDS[5]) && !World.gameIsOver() ) { 
			//USE item_name 
			if(argV.size()==2) {
				useItem();
			}else {
				System.out.println("amount of items trying to use at once is incorrect");
				System.out.println("expected 1 found "+(argV.size()-1));
			}
			
		}
		else if(order.equals(POSSIBLE_COMMANDS[6]) && !World.gameIsOver() ) { 
			//CAST spell enemy
			attackEnemy();
		}
		else if(order.equals(POSSIBLE_COMMANDS[7] ) && !World.gameIsOver() ) { 
			//DROP item_name
			if(argV.size()==2) {
				dropItem();
			}else {
				System.out.println("amount of items trying to replace is incorrect");
				System.out.println("expected 1 found "+(argV.size()-1));
			}
		}
		else if(order.equals(POSSIBLE_COMMANDS[8]) && !World.gameIsOver() && World.getCurrentLocation().getEvent() instanceof Puzzle ) {
			//ATTEMPT letter
			Puzzle puzzle = (Puzzle)World.getCurrentLocation().getEvent();
			puzzle.attemptLetter(new String(argV.get(1)).charAt(0));
		}
		else if(order.equals(POSSIBLE_COMMANDS[9]) && World.gameIsOver()) { 
			//RESTART
			World.restart();
		}
		else {
			System.out.println("command not found,write HELP to get all commands");
		}

	}

}
