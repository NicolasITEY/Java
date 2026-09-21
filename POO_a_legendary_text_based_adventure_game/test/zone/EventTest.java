package test.zone;

import static org.junit.Assert.*;

import org.junit.Test;

import src.zone.Event;

public class EventTest {

	private static class TestEvent extends Event {

		@Override
		public void doEvent() {
		}
	}

	@Test
	public void testEventInitiallyNotEnded() {
		Event event = new TestEvent();
		assertFalse(event.hasEventEnded());
	}

	@Test
	public void testEndEventSetsFlagToTrue() {
		Event event = new TestEvent();
		event.endEvent();
		assertTrue(event.hasEventEnded());
	}
}
