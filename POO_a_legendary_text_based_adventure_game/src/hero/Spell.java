package src.hero;

import src.zone.ElementType;

public class Spell {

	protected ElementType type;
	protected int damage;
	protected String name;
	protected String description;
	protected int cost;

	/**
	 *
	 * Constructor of Spell
	 *
	 * @author Kamardine
	 * @param type        element of the spell
	 * @param damage      damage of the spell
	 * @param name        name of the spell
	 * @param description description of the spell
	 * @param cost        cost of the spell
	 *
	 */
	public Spell(ElementType type, int damage, int cost, String name, String description) {
		this.type = type;
		this.damage = damage;
		this.name = name;
		this.description = description;
		this.cost = cost;
	}

	/**
	 *
	 * Getter of the element type
	 *
	 * @author Kamardine
	 * @return type element of the spell
	 */
	public ElementType getType() {
		return this.type;
	}

	/**
	 *
	 * Getter of the damage
	 *
	 * @author Kamardine
	 * @return damage of the spell
	 */
	public int getDamage() {
		return this.damage;
	}

	/**
	 *
	 * Getter of the name
	 *
	 * @author Kamardine
	 * @return name of the spell
	 */
	public String getName() {
		return this.name;
	}

	/**
	 *
	 * Getter of the cost
	 *
	 * @author Kamardine
	 * @return cost of the spell
	 */
	public int getCost() {
		return this.cost;
	}

	/**
	 *
	 * Getter of the description
	 *
	 * @author Kamardine
	 * @return description of the spell
	 */
	public String getDescription() {
		return this.description;
	}

}