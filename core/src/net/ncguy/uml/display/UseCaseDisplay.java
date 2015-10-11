package net.ncguy.uml.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.drawable.Icons;
import net.ncguy.uml.elements.ActorElement;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.data.LineData;
import net.ncguy.uml.event.EventHandler;

/**
 * Created by Nick on 30/09/2015 at 20:19,
 * Project: UMLEditor.
 */
public class UseCaseDisplay extends GenericDisplay {

    Separator horzRight;
    public VisImageButton delElementBtn;

    VisLabel camLocLbl, selObjLocLbl;

    public VisImageButton addGenericBtn;
    public VisImageButton addActorBtn;

    @Override
    public void show() {
        super.show();

        addGenericBtn = new VisImageButton(Assets.getIcon(Icons.LAYER_ADD));
        new Tooltip(addGenericBtn, "Adds a new Use case element");
        addActorBtn = new VisImageButton(Assets.getIcon(Icons.LAYER_ADD));
        new Tooltip(addActorBtn, "Adds a new Actor element");
        addGenericBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EditorElement e = new EditorElement();
                e.addListener(new SelectionListener(e, UMLLauncher.instance.useCaseDisplay));
                addElementToStage(e);
            }
        });
        addActorBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ActorElement a = new ActorElement();
                a.addListener(new SelectionListener(a, UMLLauncher.instance.useCaseDisplay));
                addElementToStage(a);
            }
        });

        EventHandler.addEventToHandler("updateColour.usecaseDisplay", (args) -> {
            if(currentElement == null) return;
            if(args[0] instanceof Color) {
                currentElement.setColor((Color) args[0]);
            }
        });

        horzRight = new Separator(false);
        delElementBtn = new VisImageButton(Assets.getIcon(Icons.WARNING));
        delElementBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentElement == null) return;
                if(!(currentElement instanceof EditorElement)) return;
                EditorElement e = (EditorElement) currentElement;
                e.remove();
                elements.remove(e);
                regrowTree();
            }
        });


        camLocLbl = new VisLabel();
        selObjLocLbl = new VisLabel();

        stage.addActor(horzRight);
        stage.addActor(camLocLbl);
        stage.addActor(selObjLocLbl);

        addButton(delElementBtn);
        addButton(addGenericBtn);
        addButton(addActorBtn);

        regrowTree();

        for(EditorElement e : elements) {
            e.setBounds(leftPaneWidth+50, 100, 100, 100);
            uiStage.addActor(e);
            e.addListener(new SelectionListener(e, this));
//            e.addListener(new ObjectDragListener(uiStage.getRoot(), e, this));
        }
//        uiStage.setBounds(leftPaneWidth, 0, Gdx.graphics.getWidth()-leftPaneWidth, Gdx.graphics.getHeight());
//        stage.addActor(uiPane);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
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

        indexTimer.setPosition(0, 0);
        indexTimer.setWidth(leftPaneWidth);
//        indexTimer.increment();

        stage.setDebugAll(false);
        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            indexer.index();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            System.out.println("Indexed Lines: ");
            for(LineData line : lineIndex) System.out.println("\t"+line.name);
        }
    }

    @Override
    public void configureContent() {
        dataDialog_contents = new VisTextArea();
        dataDialog_contents.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char c) {
                if(currentElement instanceof EditorElement) {
                    EditorElement e = (EditorElement) currentElement;
                    e.data.contents = ((TextArea)dataDialog_contents).getText();
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
    public void dispose() {}

    @Override public void changeActiveActor(Actor a) {
        currentElement = a;
    }


}
