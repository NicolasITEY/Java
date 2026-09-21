package src.enemy;

import java.util.Arrays;

import src.game.World;
import src.hero.Hero;
import src.hero.item.Item;
import src.zone.ElementType;

public class Enemy {

	protected int health;
	protected int attackPower;
	protected ElementType element;
	protected Item[] itemDroped;
	protected int coinsDropMin;
	protected int coinsDropMax;
	protected String name;

	public Enemy(int health, int attackPower, ElementType element, Item[] itemDroped, int coinsDropMin,
			int coinsDropMax, String name) {
		this.health = health;
		this.attackPower = attackPower;
		this.element = element;
		this.itemDroped = itemDroped;
		this.coinsDropMin = coinsDropMin;
		this.coinsDropMax = coinsDropMax;
		this.name = name;
	}

	public Enemy(Enemy enemy) {
		this.health = enemy.health;
		this.attackPower = enemy.attackPower;
		this.element = enemy.element;
		this.itemDroped = Arrays.copyOf(enemy.itemDroped, enemy.itemDroped.length);
		this.coinsDropMin = enemy.coinsDropMin;
		this.coinsDropMax = enemy.coinsDropMax;
		this.name = enemy.name;
	}

	public int getHealth() {
		return this.health;
	}

	public int getAttackPower() {
		return this.attackPower;
	}

	public Item[] getItemDroped() {
		return this.itemDroped;
	}

	public int getCoinsDropMin() {
		return this.coinsDropMin;
	}

	public int getCoinsDropMax() {
		return this.coinsDropMax;
	}

	public String getName() {
		return this.name;
	}

	public void setHealth(int newHealth) {
		this.health = newHealth;
	}

	public int coinDrop() {
		int coinDrop = this.coinsDropMin + (int) (Math.random() * ((this.coinsDropMax - this.coinsDropMin) + 1));
		return coinDrop;
	}

	public void attack() {
		World.getPlayer().takeDamage(attackPower);
	}

	/**
	 * Calcule les hp perdu de l'ennemie
	 *
	 * @author Nicolas ITEY
	 * @param amount
	 */
	public void takeDamage(int amount) {
		if (this.health - amount < 0) {
			this.health = 0;
		} else {
			this.health -= amount;

		}
	}
// A FAIE UN INTERFACE JE PESNE POUR HERO ET ENNEMY

	public Item giveitem() {
		if (itemDroped != null) {
			Item itemdrop = this.itemDroped[(int) (Math.random() * (itemDroped.length))];
			return itemdrop;
		} else {
			return null;
		}
	}

	public ElementType getElement() {
		return element;
	}

	public void attack(Hero hero) {
		hero.takeDamage(attackPower);
	}

	/**
	 * Vérifie si l'ennemie est mort
	 *
	 * @author Nicolas ITEY
	 * @return bool
	 *
	 */
	public boolean isDead() {

		return health <= 0;
	}

}

/*
 * pour le gain de monaie if (isdead == true ) this.gainMoney = this.coinDrop
 * Ennemy.die(); pour quand lennemeie meurt this.coinDrop = this.coinsDropMin +
 * (int)(Math.random() * ((this.coinsDropMax - this.coinsDropMin) + 1));
 */
