package net.ncguy.uml;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;
import net.ncguy.uml.components.sub.ColourWindow;
import net.ncguy.uml.display.MainDisplay;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.global.Sprites;

public class UMLLauncher extends Game {

	public static UMLLauncher instance;
	public MainDisplay display;
	public static ColourWindow colourWindow;
	public static boolean prettyJson = false;

	public UMLLauncher(String[] args) {
		System.out.println("===============================================");
		System.out.println("Scanning launch arguments...");
		String argStr = "[";
		for(String s : args) {
			argStr += s+", ";
		}
		argStr = argStr.substring(0, argStr.length()-2)+"]";
		System.out.println("Arguments: "+argStr);
		for(int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase("-prettyjson")) {
				System.out.println("PrettyJson launch argument detected, prettifying JSON builder... ");
				prettyJson = true;
			}
		}
	}


	@Override
	public void create () {
		if(instance == null) instance = this;
		VisUI.load();
		Assets.load();
		colourWindow = new ColourWindow("Colour Editor");
		Sprites.initSprites();
		display = new MainDisplay();
		setScreen(display);
	}
}
