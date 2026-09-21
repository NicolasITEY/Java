package src.zone;

import java.util.ArrayList;

import src.game.World;
import src.hero.item.Staff;

public class Puzzle extends Event {
	private final String correctWord = "hematite";
	private char[] guesses;
	private int level = 0;
	private final int staffID = 1;
	private ArrayList<Character> guessedLetters;

	public Puzzle() {
		guesses = new char[correctWord.length()];
		guessedLetters = new ArrayList<>();
		guesses[0] = 'h';
		for (int i = 1; i < correctWord.length(); i++) {
			guesses[i] = ' ';
		}
	}

	@Override
	public void doEvent() {
		System.out.println(
				"you find yourself in an old and small room,you barely have the space to stand up. Moss grows on the walls");
		System.out.println("In front of you resides a meter going from the floor to the top, with 8 different levels.");
		System.out.println("As you look upwards, you notice giant pipes leak water on the ceiling");
		System.out.println("on the western wall is all the letters of the alphabet except for the letter B");
		System.out.println("above it reads \"h_______\"");
		System.out.println("finally, to your right is a locked trap door with a metalic looking crystal");
	}

	public void attemptLetter(char letter) {
		boolean alreadyGuessed = false;
		for (Character c : guessedLetters) {
			alreadyGuessed = alreadyGuessed && c.charValue() == letter;
		}
		if (!alreadyGuessed) {
			System.out.println("you feel the mechanism wir and rumble as the letter sinks into the wall");
			guessedLetters.add(letter);
			boolean found = false;
			for (int i = 0; i < correctWord.length(); i++) {
				if (correctWord.charAt(i) == letter) {
					found = true;
					guesses[i] = letter;
				}
			}
			if (!found) {
				level++;
				switch (level) {
				case 1:
					System.out.println("water slowly moves through the pipes");
					System.out.println("puddles form under the pipes");
					break;
				case 2:
					System.out.println("water starts flowing harder");
					System.out.println("the water reaches the bottom of your ankles");
					break;
				case 3:
					System.out.println("water flows freely into the room");
					System.out.println("the level has risen above your knees");
					break;
				case 4:
					System.out.println("the air gets denser as water floods the room");
					System.out.println("the room is half full");
					break;
				case 5:
					System.out.println("it's getting harder to move around the room");
					break;
				case 6:
					System.out.println("the water reaches your chest, you don't have that many tries left");
					break;
				case 7:
					System.out.println("only your head is sticking out of the water");
					break;
				case 8:
					System.out.println(
							"Water rises above your head, you look at the result of your work during your final moments");
					System.out.print("The guessed word reads ");
					for (char c : guesses) {
						if (c != (char) (0)) {
							System.out.print(c);
						} else {
							System.out.print("_");
						}
					}
					System.out.println("");
					World.gameOver();
					break;

				}
			} else {
				System.out.println("you breathe a sigh of relief as the letters show up in front of you");
				if (!success()) {
					System.out.print("the wall now reads : ");
					for (char c : guesses) {
						if (c != (char) (0)) {
							System.out.print(c);
						} else {
							System.out.print("_");
						}
					}
					System.out.println("");
				} else {
					System.out.println("you spelled the word \"hematite\"");
					System.out.println("the trapdoor opens and a glowing hematite crystal sits inside");
					System.out.println("you take it and add it to your collected pieces");
					World.getPlayer().gainItem(new Staff(staffID,"a beautifully made hematite crystal"));
				}
			}
		} else {
			System.out.println("you can't feel anything where the letter used to be");
		}
	}

	private boolean success() {
		int i = 0;
		boolean res = true;
		for (char c : guesses) {
			if (res) {
				res = res && (c == correctWord.charAt(i));
				i++;
			}
		}
		return res;
	}

	public int getLevel() {
		return level;
	}

	public ArrayList<Character> getGuessedLetters() {
		return guessedLetters;
	}
}
