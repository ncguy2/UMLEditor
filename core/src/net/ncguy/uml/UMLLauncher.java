package net.ncguy.uml;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;
import net.ncguy.uml.components.sub.ColourWindow;
import net.ncguy.uml.display.ClassDiagramDisplay;
import net.ncguy.uml.display.GenericDisplay;
import net.ncguy.uml.display.UseCaseDisplay;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.event.IEvent;
import net.ncguy.uml.global.Sprites;

import java.util.HashMap;

public class UMLLauncher extends Game {

	public static UMLLauncher instance;
	public UseCaseDisplay useCaseDisplay;
	public ClassDiagramDisplay classDiagramDisplay;
	public static ColourWindow colourWindow;
	public static boolean prettyJson = false;

	private static HashMap<String, IEvent> launchArgs = new HashMap<>();

	public UMLLauncher(String[] args) {
		initLaunchArgs();
        handleLaunchArgs(args);
	}

	private void initLaunchArgs() {
        addLaunchArg("-prettyjson", (args) -> prettyJson = true);
	}

    private void addLaunchArg(String key, IEvent val) {
        key = key.toLowerCase();
        launchArgs.put(key, val);
    }

	private void handleLaunchArgs(String[] args) {
		System.out.println("===============================================");
		System.out.println("Scanning launch arguments...");
		String argStr = "[";
		if(args.length < 1) {
			argStr += "..";
		}else{
			for(String s : args) {
				argStr += s + ", ";
			}
		}
		argStr = argStr.substring(0, argStr.length()-2)+"]";
		System.out.println("Arguments: "+argStr);

		for(int i = 0; i < args.length; i++) {
			if(launchArgs.containsKey(args[i]))
                launchArgs.get(args[i]).run(args, i);
		}
	}


	@Override
	public void create () {
		if(instance == null) instance = this;
		VisUI.load();
		Assets.load();
		colourWindow = new ColourWindow("Colour Editor");
		Sprites.initSprites();
		useCaseDisplay = new UseCaseDisplay();
		classDiagramDisplay = new ClassDiagramDisplay();
		setScreen(useCaseDisplay);
	}

	public GenericDisplay getDisplay() {
		if(instance.getScreen() instanceof GenericDisplay)
			return (GenericDisplay)instance.getScreen();
		return useCaseDisplay;
	}
}
