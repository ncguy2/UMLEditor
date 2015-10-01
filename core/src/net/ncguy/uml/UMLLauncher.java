package net.ncguy.uml;

import com.badlogic.gdx.Game;
import net.ncguy.uml.display.MainDisplay;
import net.ncguy.uml.global.Sprites;

public class UMLLauncher extends Game {

	public static UMLLauncher instance;
	public MainDisplay display;

	@Override
	public void create () {
		if(instance == null) instance = this;
		Sprites.initSprites();
		display = new MainDisplay();
		setScreen(display);
	}
}
