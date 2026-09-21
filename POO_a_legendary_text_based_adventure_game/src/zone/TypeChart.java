package src.zone;

//  life -> curse ->mud -> fire -> life  B EST WEAK A A
public interface TypeChart {
	public static boolean isWeakTo(ElementType typeA, ElementType typeB) {
		if (typeA == ElementType.NEUTRAL || typeB == ElementType.NEUTRAL) {
			return false;
		} else {
			return (typeA == ElementType.MUD && typeB == ElementType.CURSED)
					|| (typeA == ElementType.FIRE && typeB == ElementType.MUD)
					|| (typeA == ElementType.LIFE && typeB == ElementType.FIRE)
					|| (typeA == ElementType.CURSED && typeB == ElementType.LIFE);
		}
	}

	public static boolean isStrongAgainst(ElementType typeA, ElementType typeB) {
		return isWeakTo(typeB, typeA);
	}
}
