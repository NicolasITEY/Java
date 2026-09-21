package src.hero.item;

public class Staff extends Item {

	private int pieceId; // part of the staff

	public Staff(int id, String description) {
		pieceId = id;
		this.desc = description;
	}

	public int getPieceId() {
		return this.pieceId;
	}

	/**
	 * method that consume staff piece
	 *
	 * @author Kamardine
	 */
	@Override
	public void consume() {
		if (this.theBag == null) {
			System.out.println("this staff piece are not in a bag");
		} else {
			if (!(this.theBag.isOntheBag(this))) {
				System.out.println("this part " + this.pieceId + " hasn't been found yet");
			} else {
				(this.theBag).addGrimoire(pieceId);
				(this.theBag).removeItem(this);
			}
		}
	}

}