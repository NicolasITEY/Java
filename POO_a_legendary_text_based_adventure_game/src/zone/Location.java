package src.zone;

import java.util.HashMap;

import src.game.World;

public class Location {

	private Event event;
	private final Biome biome;
	private HashMap<String, Integer> exits;
	private final String textShown;
	private boolean eventStartsOnEnter;

	/**
	 * @Param associatedEvent event associated to the location
	 * @Param biome biome given to a class
	 * @Param ExitLocations all possible exits of the tile
	 * @Param ExitNames names given to the exits
	 * @Param texts array of texts shown when entering(0) looking around(1) and
	 *        exiting(2)
	 */
	public Location(Event associatedEvent, boolean eventStartsOnEnter, Biome biome, int[] exitLocationIndecies,
			String[] exitNames, String text) {
		event = associatedEvent;
		this.biome = biome;
		exits = new HashMap<>();
		int index = 0;
		textShown = text;
		for (int exit : exitLocationIndecies) {
			exits.put(exitNames[index], exit);
			index++;
		}
		this.eventStartsOnEnter = eventStartsOnEnter;

	}

	public Event getEvent() {
		return this.event;
	}

	public HashMap<String, Integer> getExits() {
		return this.exits;
	}

	public Biome getBiome() {
		return this.biome;
	}

	public String getText() {
		return this.textShown;
	}

	public void addEvent(Event e) {
		event = e;
	}

	public void removeEvent() {
		event = null;
	}

	public void enterLocation() {
		if (event != null && eventStartsOnEnter) {
			event.doEvent();
		}
	}

	public void doEvent() {
		event.doEvent();
	}

	public void exitLocation(String direction) {
		if (event != null) {
			if (event.hasEventEnded()) {
				World.goToLocation(exits.get(direction));
			}
		} else {
			System.out.println("there is more to do, you can't leave just yet");
		}
	}

	public void look() {
		System.out.println(textShown);
	}

}
