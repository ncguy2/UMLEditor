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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.*;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.components.LineDialog;
import net.ncguy.uml.components.Panel;
import net.ncguy.uml.components.VisProgressBarEvent;
import net.ncguy.uml.components.global.TaskMenu;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.drawable.Icons;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.ElementController;
import net.ncguy.uml.elements.data.LineData;
import net.ncguy.uml.io.JSONHandler;
import net.ncguy.uml.util.ListIndexer;

import java.util.ArrayList;

/**
 * Created by Nick on 06/10/2015 at 11:22.
 * Project: UMLEditor
 * Package: net.ncguy.uml.useCaseDisplay
 */
public class GenericDisplay implements Screen {

    JSONHandler jsonHandler;
    TaskMenu taskMenu;
    public Separator vertLeft;

    Stage stage, uiStage;
    public InputMultiplexer multiplexer;

    public float leftPaneWidth = 250;
    public LineDialog lineDialog;
    public Vector2 uiStageOffset;
    public float zoom = 1;
    public ArrayList<EditorElement> elements;
    public ArrayList<LineData> lineIndex;
    public Actor currentElement;
    public ElementController controller;

    public ListIndexer indexer;
    public VisProgressBarEvent indexTimer;

    public VisWindow dataDialog;
    public VisTable dataDialog_table, dataDialog_optionTable;
    public VisScrollPane dataDialog_optionPane;
    public VisTextField dataDialog_name;
    public VisImageButton dataDialog_exit;
    public Actor dataDialog_contents;
    public VisTextButton colEditBtn;
    public VisTextButton openLineDialogBtn;

    VisTable buttonTable;
    VisScrollPane buttonScroll;
    VisList<EditorElement.Data> elementsTree;

    Panel leftPanelBg;

    @Override
    public void show() {
        jsonHandler = new JSONHandler();
        taskMenu = new TaskMenu();
        lineDialog = new LineDialog("Line editor");

        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));

        lineIndex = new ArrayList<>();

        indexer = new ListIndexer(this, "elements", "lineIndex");

        indexTimer = new VisProgressBarEvent(0, 10, 1, false, "indexTimer");
        indexTimer.addEvent((args) -> indexer.index());

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(uiStage);
        Gdx.input.setInputProcessor(multiplexer);

        if(uiStageOffset == null) uiStageOffset = new Vector2();
        if(elements == null) elements = new ArrayList<>();
        else for(EditorElement e : elements) uiStage.addActor(e);
        taskMenu.addToStage(stage);

        elementsTree = new VisList<>();
        elementsTree.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(elementsTree.getSelected() == null) return;
                if(elementsTree.getSelected().element == null) return;
                currentElement = elementsTree.getSelected().element;
                controller.onAllocate(elementsTree.getSelected().element);

                if(getTapCount() < 2) return;
                if(currentElement == null) return;
                if(currentElement instanceof EditorElement) {
                    EditorElement e = (EditorElement) currentElement;
                    dataDialog.getTitleLabel().setText(e.data.name);
                    dataDialog_name.setText(e.data.name);
                    e.contentHandle(dataDialog_contents);
//                    dataDialog_contents.setText(e.data.contents.toString());
                    dataDialog.setBounds((Gdx.graphics.getWidth() / 2) - 500, (Gdx.graphics.getHeight() / 2) - 275, 1000, 550);
                    dataDialog.setVisible(true);
                }
            }
        });
        buttonTable = new VisTable(true);
        buttonScroll = new VisScrollPane(buttonTable);
        vertLeft = new Separator(true);
        controller = new ElementController(this);
        vertLeft.setBounds(leftPaneWidth, 0, 5, Gdx.graphics.getHeight());
        leftPanelBg = new Panel(new Color(.4f, .4f, .4f, 1));
        leftPanelBg.setBounds(0, 0, leftPaneWidth, Gdx.graphics.getHeight());

        stage.addActor(leftPanelBg);
        stage.addActor(buttonScroll);
        stage.addActor(elementsTree);
        stage.addActor(vertLeft);
        stage.addActor(indexTimer);

        uiStage.addActor(controller.addedToStage(uiStage));

        buttonScroll.setBounds(5, elementsTree.getY() - 155, leftPaneWidth - 10, 150);

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
                scalar = zoom;
                float modX = uiStageOffset.x + ((Gdx.input.getDeltaX()) / scalar);
                float modY = uiStageOffset.y + (-(Gdx.input.getDeltaY()) / scalar);
                System.out.println("MainDisplay.touchDragged >>");
                System.out.println("\tModX: " + modX);
                System.out.println("\tModY: " + modY);
                uiStageOffset.set(modX, modY);
                for(EditorElement editorElement : elements) {
                    editorElement.redraw(uiStageOffset);
                }
                controller.assertBody(false);

            }
        });

        dataDialog = new VisDialog("Data");
        dataDialog.setModal(false);
        dataDialog_table = new VisTable(true);
        dataDialog_name = new VisTextField("");
        dataDialog_optionTable = new VisTable(true);
        dataDialog_optionPane = new VisScrollPane(dataDialog_optionTable);
        dataDialog_exit = new VisImageButton(Assets.getIcon(Icons.EXIT));
        dataDialog_exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dataDialog.setVisible(false);
            }
        });
        dataDialog_name.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char c) {
                dataDialog.getTitleLabel().setText(dataDialog_name.getText());
                if(currentElement instanceof EditorElement) {
                    EditorElement e = (EditorElement) currentElement;
                    e.data.name = dataDialog_name.getText();
                    lineDialog.setTitle(e.data.name);
                }
                return false;
            }
        });
        configureContent();
        dataDialog.getTitleTable().add(dataDialog_exit);
        dataDialog_table.defaults().padLeft(5).padRight(5);
        dataDialog_table.add(new VisLabel("Element Name: "));
        dataDialog_table.add(dataDialog_name).width(400).colspan(4);
        dataDialog_table.row();
        dataDialog_table.addSeparator().colspan(5);
        dataDialog_table.row();
        dataDialog_table.add(dataDialog_contents).colspan(4).fillX().height(400);
        dataDialog_table.add(dataDialog_optionPane);
        dataDialog_table.setFillParent(true);
//        dataDialog_table.setBounds(0, 0, dataDialog.getH);
        dataDialog.addActor(dataDialog_table);

        openLineDialogBtn = new VisTextButton("Line editor");
        colEditBtn = new VisTextButton("Edit Colour");
        openLineDialogBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentElement == null) return;
                if(!(currentElement instanceof EditorElement)) return;
                EditorElement e = (EditorElement) currentElement;
                lineDialog.setTitle(e.data.name);
                lineDialog.setVisible(true);
            }
        });
        colEditBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                stage.addActor(UMLLauncher.colourWindow.fadeIn("updateColour.Maindisplay"));
            }
        });
        dataDialog_optionTable.add(openLineDialogBtn);
        dataDialog_optionTable.row();
        dataDialog_optionTable.add(colEditBtn);

        dataDialog.setVisible(false);

        stage.addActor(dataDialog);
        stage.addActor(lineDialog);

        stage.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, int amount) {
                zoom -= Float.parseFloat(amount + "f") / 100;
                if(zoom < .01f) zoom = .01f;
                if(zoom > 100) zoom = 100;
                for(EditorElement e : elements)
                    e.redraw(uiStageOffset, zoom);
                controller.assertBody(false);
                return super.scrolled(event, x, y, amount);
            }
        });

        for(EditorElement e : elements) e.redraw(uiStageOffset, zoom);
    }

    public void configureContent() {
        dataDialog_contents = new Actor();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        taskMenu.getTable().setPosition(leftPaneWidth + 5, Gdx.graphics.getHeight() - 30);
        taskMenu.getTable().pack();

        uiStage.act(delta);
        uiStage.draw();
        stage.act(delta);
        stage.draw();


        uiStage.setDebugAll(false);
    }

    public void changeActiveActor(Actor a) {}

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

    int index = 0;
    public void addButton(Button btn) {
        buttonTable.add(btn).size(45);
        index++;
        if(index % 4 == 0)
            buttonTable.row();
    }

    public void regrowTree() {
        EditorElement.Data[] data = new EditorElement.Data[elements.size()];
        int index = 0;
        for(EditorElement e : elements) {
            data[index] = e.data;
            index++;
        }
        elementsTree.setItems(data);
        elementsTree.setBounds(5, 250, leftPaneWidth - 10, Gdx.graphics.getHeight() - 250);
        buttonScroll.setBounds(5, elementsTree.getY()-155, leftPaneWidth-10, 150);
    }

    protected void addElementToStage(EditorElement e) {
        controller.remove();
        uiStage.addActor(e);
        uiStage.addActor(controller);
        elements.add(e);
        regrowTree();
        e.redraw(uiStageOffset);
    }

    public static class SelectionListener extends ClickListener {
        Actor element;
        GenericDisplay parent;
        public SelectionListener(Actor editorElement, GenericDisplay parent) {
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
