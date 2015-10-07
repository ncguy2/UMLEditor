package net.ncguy.uml.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.ncguy.uml.elements.EditorElement;

import java.util.ArrayList;

/**
 * Created by Nick on 06/10/2015 at 11:22.
 * Project: UMLEditor
 * Package: net.ncguy.uml.display
 */
public class ClassDiagramDisplay extends GenericDisplay {

    Stage stage, uiStage;
    public ArrayList<EditorElement> elements;
    Vector2 uiStageOffset;

    @Override
    public void show() {
        super.show();
        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));

        if(elements == null) elements = new ArrayList<>();
        else for(EditorElement e : elements) uiStage.addActor(e);

        if(uiStageOffset == null) uiStageOffset = new Vector2();

        taskMenu.addToStage(stage);

        Gdx.input.setInputProcessor(stage);

        for(EditorElement e : elements) e.redraw(uiStageOffset);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.act(delta);
        stage.draw();
    }

}
