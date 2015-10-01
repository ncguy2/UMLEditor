package net.ncguy.uml.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.components.Panel;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.drawable.Icons;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.ElementController;
import net.ncguy.uml.global.WorkspaceData;
import net.ncguy.uml.io.JSONHandler;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nick on 30/09/2015 at 20:19,
 * Project: UMLEditor.
 */
public class MainDisplay implements Screen {

    JSONHandler jsonHandler;

    Stage stage, uiStage;

    VisTable buttonTable;
    VisScrollPane buttonScroll;

    public ArrayList<EditorElement> elements;
    public Actor currentElement;
    public ElementController controller;

    public Vector2 uiStageOffset;

    public Separator vertLeft, horzRight;
    public VisImageButton delElementBtn;

    float leftPaneWidth = 250;

    Panel leftPanelBg;

    VisList<EditorElement.Data> elementsTree;
    VisLabel camLocLbl, selObjLocLbl;

    public VisDialog dataDialog;
    public VisTable dataDialog_table, dataDialog_configTable;
    public VisScrollPane dataDialog_configPane;
    public VisTextField dataDialog_name;
    public VisImageButton dataDialog_exit;
    public VisTextArea dataDialog_contents;

    public VisImageButton openFileBtn, saveFileBtn;
    public VisImageButton addElementBtn;

    public FileChooser openFileChooser;
    public FileChooser saveFileChooser;

    @Override
    public void show() {
        VisUI.load();
        Assets.load();
        jsonHandler = new JSONHandler();

        openFileChooser = new FileChooser("Load file", FileChooser.Mode.OPEN);
        openFileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(FileHandle file) {
                super.selected(file);
                if(!file.exists()) return;
                try{
                    WorkspaceData data = jsonHandler.loadElements(file.file().getAbsolutePath());
                    ArrayList<EditorElement> loadedElements = data.elements;
                    elements.clear();
                    controller.remove();
                    for(EditorElement e : loadedElements) {
                        e.addListener(new SelectionListener(e, UMLLauncher.instance.display));
                        elements.add(e);
                        uiStage.addActor(e);
                    }
                    uiStage.addActor(controller);
                    regrowTree();
                    for(EditorElement e : loadedElements) {
                        e.loadLinesFromData();
                        e.redraw(uiStageOffset);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        saveFileChooser = new FileChooser("Save file", FileChooser.Mode.SAVE);
        saveFileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(FileHandle file) {
                super.selected(file);
                WorkspaceData data = new WorkspaceData();
                data.elements = elements;
                jsonHandler.save(file.file().getAbsolutePath(), data);
            }
        });

        openFileBtn = new VisImageButton(Assets.getIcon(Icons.LOAD));
        saveFileBtn = new VisImageButton(Assets.getIcon(Icons.SAVE));

        openFileBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openFileChooser.setDirectory(new File(""));
                uiStage.addActor(openFileChooser.fadeIn());
            }
        });
        saveFileBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveFileChooser.setDirectory(new File(""));
                uiStage.addActor(saveFileChooser.fadeIn());
            }
        });

        addElementBtn = new VisImageButton(Assets.getIcon(Icons.LAYER_ADD));
        addElementBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EditorElement e = new EditorElement();
                e.addListener(new SelectionListener(e, UMLLauncher.instance.display));
                controller.remove();
                uiStage.addActor(e);
                uiStage.addActor(controller);
                elements.add(e);
                regrowTree();
            }
        });

        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        dataDialog = new VisDialog("Data");
        dataDialog_configTable = new VisTable(true);
        dataDialog_configPane = new VisScrollPane(dataDialog_configTable);
        dataDialog_table = new VisTable(true);
        dataDialog_name = new VisTextField("");
        dataDialog_exit = new VisImageButton(Assets.getIcon(Icons.EXIT));
        dataDialog_contents = new VisTextArea();
        dataDialog_exit.addListener(new ClickListener(){
            @Override public void clicked(InputEvent event, float x, float y) {
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
                }
                return false;
            }
        });
        dataDialog_contents.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char c) {
                if(currentElement instanceof EditorElement) {
                    EditorElement e = (EditorElement) currentElement;
                    e.data.contents = dataDialog_contents.getText();
                }
                return false;
            }
        });
        dataDialog.getTitleTable().add(dataDialog_exit);
        dataDialog_table.defaults().padLeft(5).padRight(5);
        dataDialog_table.add(new VisLabel("Element Name: "));
        dataDialog_table.add(dataDialog_name).width(400).colspan(4);
        dataDialog_table.row();
        dataDialog_table.addSeparator().colspan(5);
        dataDialog_table.row();
        dataDialog_table.add(dataDialog_contents).colspan(4).fillX().height(400);
        dataDialog_table.add(dataDialog_configPane);
//        dataDialog_table.add(dataDialog_contents).colspan(2).expandX().width(600).height(400);
        dataDialog_table.setFillParent(true);
//        dataDialog_table.setBounds(0, 0, dataDialog.getH);
        dataDialog.addActor(dataDialog_table);
        uiStageOffset = new Vector2();
        elements = new ArrayList<>();
        vertLeft = new Separator(true);
        horzRight = new Separator(false);
        delElementBtn = new VisImageButton(Assets.getIcon(Icons.WARNING));
        delElementBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(currentElement == null) return;
                if(!(currentElement instanceof EditorElement)) return;
                EditorElement e = (EditorElement)currentElement;
                e.remove();
                elements.remove(e);
                regrowTree();
            }
        });
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

                if(getTapCount() < 2) return;
                if(currentElement == null) return;
                if(currentElement instanceof EditorElement) {
                    EditorElement e = (EditorElement)currentElement;
                    dataDialog.getTitleLabel().setText(e.data.name);
                    dataDialog_name.setText(e.data.name);
                    dataDialog_contents.setText(e.data.contents);
                    dataDialog.setBounds(100, 100, Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight()-200);
                    dataDialog.setVisible(true);
                }
            }
        });
        buttonTable = new VisTable(true);
        buttonScroll = new VisScrollPane(buttonTable);

        buttonScroll.setBounds(5, elementsTree.getY() - 155, leftPaneWidth - 10, 150);
        dataDialog.setVisible(false);

        stage.addActor(leftPanelBg);
        stage.addActor(buttonScroll);
        stage.addActor(elementsTree);
        stage.addActor(vertLeft);
        stage.addActor(horzRight);
        stage.addActor(camLocLbl);
        stage.addActor(selObjLocLbl);
        stage.addActor(dataDialog);

        addButton(delElementBtn);
        addButton(addElementBtn);
        addButton(openFileBtn);
        addButton(saveFileBtn);

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

        stage.setDebugAll(false);
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
        EditorElement.Data[] data = new EditorElement.Data[elements.size()];
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
        buttonTable.add(btn).size(45);
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
