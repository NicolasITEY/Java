package test.zone;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import src.game.World;
import src.zone.Puzzle;

public class PuzzleTest {

	public Puzzle puzzleEvent;

	@Before
	public void init() {
		World.startGame();
		puzzleEvent = new Puzzle();
	}

	@Test
	public void testAttemptCorrectLetter() {
		puzzleEvent.attemptLetter('e');
		assertNotEquals(puzzleEvent.getLevel(), 1);
	}

	@Test
	public void testAttemptIncorrectLetter() {
		puzzleEvent.attemptLetter('b');
		assertEquals(puzzleEvent.getLevel(), 1);
	}

	@Test
	public void testSucceedPuzzle() {

		puzzleEvent.attemptLetter('e');
		puzzleEvent.attemptLetter('m');
		puzzleEvent.attemptLetter('a');
		puzzleEvent.attemptLetter('t');
		puzzleEvent.attemptLetter('i');
		assertTrue(World.getPlayer().getBag().staffInBag(1));
	}
}
