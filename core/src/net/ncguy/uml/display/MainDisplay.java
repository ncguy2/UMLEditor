package net.ncguy.uml.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import net.ncguy.uml.components.Panel;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.drawable.Icons;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.ElementController;

/**
 * Created by Nick on 30/09/2015 at 20:19,
 * Project: UMLEditor.
 */
public class MainDisplay implements Screen {

    Stage stage, uiStage;

    VisTable buttonTable;
    VisScrollPane buttonScroll;

    public Array<EditorElement> elements;
    public Actor currentElement;
    public ElementController controller;

    public Vector2 uiStageOffset;

    public Separator vertLeft, horzRight;
    public VisImageButton delElementBtn;

    float leftPaneWidth = 250;

    Panel leftPanelBg;

    VisList<EditorElement.Data> elementsTree;
    VisLabel camLocLbl, selObjLocLbl;

    @Override
    public void show() {
        VisUI.load();
        Assets.load();
        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStageOffset = new Vector2();
        elements = new Array<>();
        vertLeft = new Separator(true);
        horzRight = new Separator(false);
        delElementBtn = new VisImageButton(Assets.getIcon(Icons.WARNING));
        controller = new ElementController(this);
        vertLeft.setBounds(leftPaneWidth, 0, 5, Gdx.graphics.getHeight());
        leftPanelBg = new Panel(new Color(.4f, .4f, .4f, 1));
        leftPanelBg.setBounds(0, 0, leftPaneWidth, Gdx.graphics.getHeight());
        camLocLbl = new VisLabel();
        selObjLocLbl = new VisLabel();
        elementsTree = new VisList<>();
        elementsTree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(elementsTree.getSelected() == null) return;
                if(elementsTree.getSelected().element == null) return;
                currentElement = elementsTree.getSelected().element;
                controller.onAllocate(elementsTree.getSelected().element);
            }
        });
        buttonTable = new VisTable(true);
        buttonScroll = new VisScrollPane(buttonTable);

        buttonScroll.setBounds(5, elementsTree.getY()-155, leftPaneWidth-10, 150);

        stage.addActor(leftPanelBg);
        stage.addActor(buttonScroll);
        stage.addActor(elementsTree);
        stage.addActor(vertLeft);
        stage.addActor(horzRight);
        stage.addActor(camLocLbl);
        stage.addActor(selObjLocLbl);

        addButton(delElementBtn);

        elements.add(new EditorElement());
        elements.add(new EditorElement());
        elements.add(new EditorElement());
        elements.add(new EditorElement());
        elements.add(new EditorElement());

        regrowTree();

        uiStage.addListener(new DragListener() {
            boolean valid = false;
            float scalar = 1f;
            float initX, initY;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                valid = button == Input.Buttons.RIGHT;
                initX = x;
                initY = y;
                return true;
            }
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(!valid) return;
                scalar = 1f;
                float modX = uiStageOffset.x+( (Gdx.input.getDeltaX())*scalar);
                float modY = uiStageOffset.y+(-(Gdx.input.getDeltaY())*scalar);
                System.out.println("MainDisplay.touchDragged >>");
                System.out.println("\tModX: "+modX);
                System.out.println("\tModY: " + modY);
                uiStageOffset.set(modX, modY);
                for(EditorElement editorElement : elements) {
                    editorElement.redraw(uiStageOffset);
                }
                controller.assertBody(false);

            }
        });

        for(EditorElement e : elements) {
            e.setBounds(leftPaneWidth+50, 100, 100, 100);
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
        uiStage.setDebugAll(false);

        leftPanelBg.setBounds(0, 0, leftPaneWidth, Gdx.graphics.getHeight());

        camLocLbl.setText(String.format("X: %s\nY: %s", uiStageOffset.x, uiStageOffset.y));
        camLocLbl.pack();
        camLocLbl.setPosition(Gdx.graphics.getWidth() - camLocLbl.getWidth(), Gdx.graphics.getHeight() - camLocLbl.getHeight());
        horzRight.setBounds(camLocLbl.getX(), camLocLbl.getY(), camLocLbl.getWidth(), 5);
        if(currentElement instanceof EditorElement) {
            EditorElement e = (EditorElement)currentElement;
            selObjLocLbl.setText(String.format("X: %s\nY: %s", e.getBaseX()+(Gdx.graphics.getWidth()/2), e.getBaseY()+(Gdx.graphics.getHeight()/2)));
            selObjLocLbl.pack();
            selObjLocLbl.setPosition(Gdx.graphics.getWidth() - selObjLocLbl.getWidth(), Gdx.graphics.getHeight() - selObjLocLbl.getHeight()-(camLocLbl.getHeight()));
        }

        stage.setDebugAll(true);
        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        stage.getCamera().update(true);
        uiStage.getViewport().update(width, height, true);
        uiStage.getCamera().update(true);

        leftPanelBg.setBounds(0, 0, leftPaneWidth, Gdx.graphics.getHeight());
        vertLeft.setBounds(leftPaneWidth, 3, 5, Gdx.graphics.getHeight() - 6);
        regrowTree();

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

    public void regrowTree() {
        EditorElement.Data[] data = new EditorElement.Data[elements.size];
        int index = 0;
        for(EditorElement e : elements) {
            data[index] = e.data;
            index++;
        }
        elementsTree.setItems(data);
        elementsTree.setBounds(5, 250, leftPaneWidth-10, Gdx.graphics.getHeight()-250);
        buttonScroll.setBounds(5, elementsTree.getY()-155, leftPaneWidth-10, 150);
    }

    int index = 0;
    public void addButton(Button btn) {
        buttonTable.add(btn);
        index++;
        if(index % 4 == 0)
            buttonTable.row();
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
            if(parent.currentElement != element)
                parent.currentElement = element;
            parent.elementsTree.setSelectedIndex(-1);
            parent.controller.onAllocate(element);
        }

    }

}
