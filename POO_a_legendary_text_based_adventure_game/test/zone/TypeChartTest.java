package test.zone;

import static org.junit.Assert.*;

import org.junit.Test;

import src.zone.ElementType;
import src.zone.TypeChart;

public class TypeChartTest {

	@Test
	public void testNeutralTypes() {

		assertFalse(TypeChart.isWeakTo(ElementType.NEUTRAL, ElementType.FIRE));
		assertFalse(TypeChart.isWeakTo(ElementType.LIFE, ElementType.NEUTRAL));
		assertFalse(TypeChart.isStrongAgainst(ElementType.NEUTRAL, ElementType.CURSED));
		assertFalse(TypeChart.isStrongAgainst(ElementType.FIRE, ElementType.NEUTRAL));
	}

	//
	@Test
	public void testWeaknesses() {

		assertTrue(TypeChart.isWeakTo(ElementType.CURSED, ElementType.LIFE));
		assertFalse(TypeChart.isWeakTo(ElementType.LIFE, ElementType.CURSED));

		// life -> curse ->mud -> fire -> life

		assertTrue(TypeChart.isWeakTo(ElementType.MUD, ElementType.CURSED));
		assertFalse(TypeChart.isWeakTo(ElementType.CURSED, ElementType.MUD));

		// FIRE est faible contre LIFE
		assertTrue(TypeChart.isWeakTo(ElementType.FIRE, ElementType.MUD));
		assertFalse(TypeChart.isWeakTo(ElementType.MUD, ElementType.FIRE));

		// LIFE est faible contre CURSED
		assertTrue(TypeChart.isWeakTo(ElementType.LIFE, ElementType.FIRE));
		assertFalse(TypeChart.isWeakTo(ElementType.FIRE, ElementType.LIFE));

	}

	@Test
	public void testStrengths() {
		// La force est l'inverse de la faiblesse // life -> curse ->mud -> fire -> life

		assertTrue(TypeChart.isStrongAgainst(ElementType.CURSED, ElementType.MUD));
		assertTrue(TypeChart.isStrongAgainst(ElementType.MUD, ElementType.FIRE));
		assertTrue(TypeChart.isStrongAgainst(ElementType.FIRE, ElementType.LIFE));
		assertTrue(TypeChart.isStrongAgainst(ElementType.LIFE, ElementType.CURSED));
	}

	@Test
	public void testOtherCombinations() {
		// Les autres combinaisons doivent être neutres
		assertFalse(TypeChart.isWeakTo(ElementType.FIRE, ElementType.MUD));
		assertFalse(TypeChart.isStrongAgainst(ElementType.LIFE, ElementType.MUD));
	}
}
