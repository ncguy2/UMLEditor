package net.ncguy.uml.display;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.components.TwinTextArea;
import net.ncguy.uml.elements.ClassElement;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.event.EventHandler;

/**
 * Created by Nick on 06/10/2015 at 11:22.
 * Project: UMLEditor
 * Package: net.ncguy.uml.display
 */
public class ClassDiagramDisplay extends GenericDisplay {

    VisTextButton addClassBtn;

    @Override
    public void show() {
        super.show();

        addClassBtn = new VisTextButton("Add class");
        addClassBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                ClassElement c = new ClassElement();
                if(UMLLauncher.instance.getScreen() instanceof GenericDisplay)
                    c.addListener(new GenericDisplay.SelectionListener(c, (GenericDisplay) UMLLauncher.instance.getScreen()));
                addElementToStage(c);
            }
        });

        EventHandler.addEventToHandler("updateColour.ClassDiagramDisplay", (args) -> {
            if(currentElement == null) return;
            if(args[0] instanceof Color) {
                currentElement.setColor((Color) args[0]);
            }
        });

        addButton(addClassBtn);

        for(EditorElement e : elements) e.redraw(uiStageOffset);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        addClassBtn.setBounds(0, 0, 150, 30);
    }

    @Override
    public void configureContent() {
        TwinTextArea txtArea = new TwinTextArea();
        txtArea.setColLbls("Attributes", "Methods");
        txtArea.setLeftChangeEvent((args) -> {
            if(args == null) return;
            if(args[0] == null) return;
            if(args[0] instanceof TextField || args[0] instanceof VisTextField) {
                if(currentElement instanceof ClassElement) {
                    ClassElement ce = (ClassElement)currentElement;
                    if(ce.data.contents instanceof ClassElement.ClassData) {
                        ClassElement.ClassData cd = (ClassElement.ClassData)ce.data.contents;
                        cd.attributes = txtArea.getLeftLines();
                        ce.updateFromData();
                    }
                }
            }
        });
        txtArea.setRightChangeEvent((args) -> {
            if(args == null) return;
            if(args[0] == null) return;
            if(args[0] instanceof TextField || args[0] instanceof VisTextField) {
                if(currentElement instanceof ClassElement) {
                    ClassElement ce = (ClassElement) currentElement;
                    if(ce.data.contents instanceof ClassElement.ClassData) {
                        ClassElement.ClassData cd = (ClassElement.ClassData) ce.data.contents;
                        cd.methods = txtArea.getRightLines();
                        ce.updateFromData();
                    }
                }
            }
        });

        dataDialog_contents = txtArea;
//        dataDialog_contents.addListener(new InputListener() {
//            @Override
//            public boolean keyTyped(InputEvent event, char c) {
//                if(currentElement instanceof EditorElement) {
//                    EditorElement e = (EditorElement) currentElement;
//                    e.data.contents = ((TextArea) dataDialog_contents).getText();
//                }
//                return false;
//            }
//        });
    }


}
