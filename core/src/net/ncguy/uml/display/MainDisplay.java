package net.ncguy.uml.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImageButton;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.ElementController;

/**
 * Created by Nick on 30/09/2015 at 20:19,
 * Project: UMLEditor.
 */
public class MainDisplay implements Screen {

    Stage stage, uiStage;

    public Array<EditorElement> elements;
    public Actor currentElement;
    public ElementController controller;

    public Vector2 uiStageOffset;

    public Separator vertLeft;
    public VisImageButton delElementBtn;

    float leftPaneWidth = 150;

    @Override
    public void show() {
        VisUI.load();
        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStageOffset = new Vector2();
        elements = new Array<>();
        vertLeft = new Separator(true);
        delElementBtn = new VisImageButton(new TextureRegionDrawable());
        controller = new ElementController(this);
        vertLeft.setBounds(leftPaneWidth, 0, 5, Gdx.graphics.getHeight());
        stage.addActor(vertLeft);

        elements.add(new EditorElement());
        elements.add(new EditorElement());
        elements.add(new EditorElement());
        elements.add(new EditorElement());
        elements.add(new EditorElement());

        uiStage.addListener(new DragListener() {
            boolean valid = false;
            float scalar = 1f;
            float initX, initY;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                valid = button == Input.Buttons.RIGHT;
                initX = uiStageOffset.x;
                initY = uiStageOffset.y;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(!valid) return;
                System.out.println(Gdx.input.getDeltaX());
                float modX = initX+(Gdx.input.getDeltaX()*scalar);
                float modY = initY+((-Gdx.input.getDeltaY())*scalar);
                uiStageOffset.set(modX, modY);
                for(EditorElement editorElement : elements) {
                    editorElement.redraw(uiStageOffset);
                }
                controller.assertBody();

            }
        });

        for(EditorElement e : elements) {
            e.setBounds(200, 100, 100, 100);
            uiStage.addActor(e);
            e.addListener(new SelectionListener(e, this));
//            e.addListener(new ObjectDragListener(uiStage.getRoot(), e, this));
        }
        vertLeft.addListener(new SelectionListener(vertLeft, this));
        uiStage.addActor(controller.addedToStage(uiStage));
//        uiStage.setBounds(leftPaneWidth, 0, Gdx.graphics.getWidth()-leftPaneWidth, Gdx.graphics.getHeight());
//        stage.addActor(uiPane);



        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(uiStage);

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        uiStage.act(delta);
        uiStage.draw();
        uiStage.setDebugAll(true);
        stage.setDebugAll(false);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public void changeActiveActor(Actor a) {
        currentElement = a;
    }

    public static class SelectionListener extends ClickListener {
        Actor element;
        MainDisplay parent;
        public SelectionListener(Actor editorElement, MainDisplay parent) {
            super();
            this.element = editorElement;
            this.parent = parent;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            parent.currentElement = element;
            parent.controller.onAllocate(element);
        }

    }

}
