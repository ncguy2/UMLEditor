package net.ncguy.uml.display;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTextButton;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.elements.ClassElement;
import net.ncguy.uml.elements.EditorElement;

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
                    c.addListener(new GenericDisplay.SelectionListener(c, (GenericDisplay)UMLLauncher.instance.getScreen()));
                addElementToStage(c);
            }
        });
        stage.addActor(addClassBtn);

        for(EditorElement e : elements) e.redraw(uiStageOffset);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        addClassBtn.setBounds(0, 0, 150, 30);
    }

}
