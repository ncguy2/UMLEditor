package net.ncguy.uml.components.global;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.elements.EditorElement;

/**
 * Created by Nick on 06/10/2015 at 11:14.
 * Project: UMLEditor
 * Package: net.ncguy.uml.components.global
 */
public class TaskMenu extends MenuBar {

    public Menu screens;
    public MenuItem screens_UseCase;
    public MenuItem screens_ClassDiagram;

    public TaskMenu() {
        super();
        init();
        addMenu(screens);
    }

    private void init() {
        screens = new Menu("Screens");
        screens_UseCase = new MenuItem("Use Case");
        screens_UseCase.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                UMLLauncher.instance.setScreen(UMLLauncher.instance.useCaseDisplay);
                for(EditorElement e : UMLLauncher.instance.useCaseDisplay.elements)
                    e.redraw(UMLLauncher.instance.useCaseDisplay.uiStageOffset, UMLLauncher.instance.useCaseDisplay.zoom);
            }
        });
        screens_ClassDiagram = new MenuItem("Class Diagrams");
        screens_ClassDiagram.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                UMLLauncher.instance.setScreen(UMLLauncher.instance.classDiagramDisplay);
                for(EditorElement e : UMLLauncher.instance.classDiagramDisplay.elements)
                    e.redraw(UMLLauncher.instance.classDiagramDisplay.uiStageOffset, UMLLauncher.instance.classDiagramDisplay.zoom);
            }
        });
        screens.addItem(screens_UseCase);
        screens.addItem(screens_ClassDiagram);
    }

    public void addToStage(Stage stage) { stage.addActor(getTable()); }

}
