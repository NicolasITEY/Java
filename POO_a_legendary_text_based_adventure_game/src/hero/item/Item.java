package src.hero.item;

public abstract class Item {

	protected String name; 
	protected String desc;
	protected int spaceOccupied = 1; 
	protected Bag theBag = null; // bag which is contained

	public void consume() {

	}
	public String getName() {
		return name;
	}
	public void showDescription() {
		System.out.println(name+" : "+desc);
	}
	public void addBag(Bag bag) {
		this.theBag = bag;
	}

}