package src.hero.item;

import java.util.ArrayList;

public class Bag extends Item {

	/**
	 * items held in a bag
	 */
	private ArrayList<Item> items; // items in the bag
	private int capacity = 2; // max bag's capacity (can be increased)
	private int occupiedSpace = 0; // capacity occupied by all items, must be <= capacity
	private boolean[] staffPieceFound; // array that represent a staff , and each square represent a part of the staff
	private boolean[] grimoireFound; // array that represent a grimoire , and each square represent a part of the
										// staff

	/**
	 * Default Constructor of a Bag
	 *
	 * @author Kamardine
	 * @return Bag return a bag
	 */
	public Bag() {
		this.capacity = 2;
		items = new ArrayList<>();
		staffPieceFound = new boolean[4];
		staffPieceFound[0] = true;
		grimoireFound = new boolean[4];
	}

	public int getBagCapacity() {
		return this.capacity;
	}

	public int getinitoccupiedspace() {
		return this.occupiedSpace;
	}

	public int getOccupiedSpace() {
		int res = this.capacity - occupiedSpace;
		return res;
	}

	/**
	 * upgrade capacity of a bag given in parameters, with amount
	 *
	 * @author Kamardine
	 * @param amount
	 */
	public void upgradeCapacity(int amount) {
		this.capacity += amount;
	}

	/**
	 * method that calculate the first occurence of an element in the bag
	 *
	 * @author Kamardine
	 * @param item the item
	 * @return the position on the array(bag)
	 */
	public int positionOntheBag(Item item) {
		int theIndex = -1;
		boolean found = false;
		for (int i = 0; (i < occupiedSpace && !found); i++) {
			if (this.items.get(i) == item) {
				theIndex = i;
				found = true;
			}
		}

		return theIndex;
	}

	public Item getItem(int position) {
		return items.get(position);
	}

	public ArrayList<Item> getItems() {
		return this.items;
	}

	/**
	 * method that verify if an element is in the bag
	 *
	 * @author Kamardine
	 * @param item the item
	 * @return boolean true if yes , false if not
	 */
	public boolean isOntheBag(Item item) {
		int pos = this.positionOntheBag(item);
		if (pos == -1) {
			return false;
		} else {
			return true;
		}
	}

	public boolean grimoireInBag(int id) {
		return grimoireFound[id];
	}

	public boolean staffInBag(int id) {
		return staffPieceFound[id];
	}

	/**
	 * method that add item to a bag
	 *
	 * @author Kamardine
	 * @param item item that will be added
	 */
	public void addItem(Item item) {
		if ((item.spaceOccupied + this.occupiedSpace) > capacity) {
			System.out.println("Not enough space in the bag");
		} else {
			this.items.add(item);
			item.theBag = this;
			this.occupiedSpace += item.spaceOccupied;
		}

	}

	/**
	 * method that remove item to a bag
	 *
	 * @author Kamardine
	 * @param item item that will be removed
	 */
	public void removeItem(Item item) {
		if (!this.isOntheBag(item)) {
			System.out.println("item not in the bag");
		} else {
			int theIndex = this.positionOntheBag(item);
			this.items.remove(theIndex);
			item.theBag = null;
		}
	}

	/**
	 * method that add a piece of staff on the bag
	 *
	 * @author Kamardine
	 * @param id piece of the staff found
	 */
	public void addStaff(int id) {
		this.staffPieceFound[id] = true;
		occupiedSpace -= 1;
	}

	/**
	 * method that add a piece of grimoire on the bag
	 *
	 * @author Kamardine
	 * @param grimoireId piece of the grimoire found
	 */
	public void addGrimoire(int grimoireId) {
		this.grimoireFound[grimoireId] = true;
		occupiedSpace -= 1;
	}

}