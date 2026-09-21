package up.csp;

import java.util.Arrays;

/**
 * Represents the set of possible values for a {@link Variable}.
 * <p>
 * This class implements a {@link Domain} using a boolean array to track the presence
 * of values within a specific range [min, max]
 * </p>
 * @author Evan RIBOULEAU
 */
public class Domain {

	private int min;
	private int max;
	private boolean[] presence;

	/**
	 * Constructor for {@link Domain} class
	 * @param min minimum value
	 * @param max maximum value
	 */
	public Domain(int min, int max) {
		this.min = min;
		this.max = max;
		if (this.min > this.max) {
			int temp = this.min;
			this.min = this.max;
			this.max = temp;
		}
		int length = this.max - this.min + 1;
		this.presence = new boolean[length];
        Arrays.fill(this.presence, true);
    }

	/**
	 * Remove an integer to the {@link Domain}
	 * @param value integer to remove
	 */
	public void removeValue(int value) {
		if (value >= this.min && value <= this.max) {
            this.presence[value - this.min] = false;
        }
	}

	/**
	 * Restore an integer to the {@link Domain}
	 * @param value integer to restore
	 * */
	public void restoreValue(int value) {
		if (value >= this.min && value <= this.max) {
			this.presence[value - this.min] = true;
		}
	}

	/**
	 * This method watch if an integer is in the {@link Domain}
	 * @param value integer tested
	 * @return {@code boolean}
	 */
	public boolean contains(int value) {
        if (value < min || value > max) {
            return false;
        }
        return this.presence[value - this.min];
	}

	/**
	 * Return the size of the domain
     * @return {@code int} size of the domain
	 * */

	public int size() {
        int count = 0;
        for (boolean b : this.presence) {
            if (b) {
                count++;
            }
        }
        return count;
	}

	/**
	 * Modifies the current {@link Domain} to keep only the values present in both domains
	 * @param d Second {@link Domain} to compare
	 * */
	public void intersection(Domain d) {
		for (int i = 0; i < this.presence.length; i++) {
			if (this.presence[i]) {
				int val = this.min + i;
				if (!d.contains(val)) {
					this.presence[i] = false;
					d.removeValue(i);
				}
			}
		}
	}

    /**
     * This method copy the current {@link Domain}
     * @return {@link Domain} copied
     * @author Chloé LEMAIRE
     */
    protected Domain copy() {
        Domain d =new Domain(min, max);
        for(int i=0; i < max - min + 1; i++) {
			if(!presence[i]){
				d.removeValue(i+min);
			}
		}
		return d;
    }



	public int getMin() {
		return this.min;
	}

	public int getMax() {
		return this.max;
	}

	@Override
	public String toString() {
		String res = "";
		for(int i=0;i<max-min+1;i++){
			res += "( "+(i+min)+" : "+Boolean.toString(presence[i])+" )";
		}
		return res;
	}

}