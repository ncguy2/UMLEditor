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
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
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
import net.ncguy.uml.global.WorkspaceData;
import net.ncguy.uml.io.JSONHandler;
import net.ncguy.uml.util.ListIndexer;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Nick on 06/10/2015 at 11:22.
 * Project: UMLEditor
 * Package: net.ncguy.uml.useCaseDisplay
 */
public class GenericDisplay implements Screen {

    public static WorkspaceData data = new WorkspaceData();

    public GenericDisplay instance;

    JSONHandler jsonHandler;
    TaskMenu taskMenu;
    public Separator vertLeft;

    public Stage stage, uiStage;
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

    public VisTextButton zoomBtn, offsetBtn;
    private VisImageButton openFileBtn, saveFileBtn;

    private FileChooser openFileChooser;
    private FileChooser saveFileChooser;

    VisTable buttonTable;
    VisScrollPane buttonScroll;
    VisList<EditorElement.Data> elementsTree;

    Panel leftPanelBg;

    public GenericDisplay() {
        this.instance = this;
        elements = new ArrayList<>();
        controller = new ElementController(this);
        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
    }

    @Override
    public void show() {
        jsonHandler = new JSONHandler();
        taskMenu = new TaskMenu();
        lineDialog = new LineDialog("Line editor");

        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        uiStage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));

        openFileChooser = new FileChooser("Load file", FileChooser.Mode.OPEN);
        openFileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(FileHandle file) {
                super.selected(file);
                if(!file.exists()) return;
                try {
                    data = jsonHandler.loadElements(file.file().getAbsolutePath());
                    // Use case display
                    ArrayList<EditorElement> loadedElements = data.useCase_elements;
                    UMLLauncher.instance.useCaseDisplay.elements.clear();
                    UMLLauncher.instance.useCaseDisplay.controller.remove();
                    for(EditorElement e : loadedElements) {
                        e.addListener(new SelectionListener(e, UMLLauncher.instance.useCaseDisplay));
                        UMLLauncher.instance.useCaseDisplay.elements.add(e);
                        UMLLauncher.instance.useCaseDisplay.uiStage.addActor(e);
                    }
                    UMLLauncher.instance.useCaseDisplay.uiStage.addActor(controller);
                    regrowTree();
                    for(EditorElement e : loadedElements) {
                        e.loadLinesFromData();
                        e.redraw(UMLLauncher.instance.useCaseDisplay.uiStageOffset);
                    }

                    // Class diagram display
                    loadedElements = data.classDiagram_elements;
                    UMLLauncher.instance.classDiagramDisplay.elements.clear();
                    UMLLauncher.instance.classDiagramDisplay.controller.remove();
                    for(EditorElement e : loadedElements) {
                        e.addListener(new SelectionListener(e, UMLLauncher.instance.classDiagramDisplay));
                        UMLLauncher.instance.classDiagramDisplay.elements.add(e);
                        UMLLauncher.instance.classDiagramDisplay.uiStage.addActor(e);
                    }
                    UMLLauncher.instance.classDiagramDisplay.uiStage.addActor(controller);
                    regrowTree();
                    for(EditorElement e : loadedElements) {
                        e.loadLinesFromData();
                        e.redraw(UMLLauncher.instance.classDiagramDisplay.uiStageOffset);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }
                controller = new ElementController(instance);
                uiStage.addActor(controller.addedToStage(uiStage));
            }
        });
        saveFileChooser = new FileChooser("Save file", FileChooser.Mode.SAVE);
        saveFileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(FileHandle file) {
                super.selected(file);
                data.useCase_elements = UMLLauncher.instance.useCaseDisplay.elements;
                data.classDiagram_elements = UMLLauncher.instance.classDiagramDisplay.elements;
                jsonHandler.save(file.file().getAbsolutePath(), data);
            }
        });

        openFileBtn = new VisImageButton(Assets.getIcon(Icons.LOAD));
        saveFileBtn = new VisImageButton(Assets.getIcon(Icons.SAVE));

        openFileBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openFileChooser.setDirectory(new File(""));
                uiStage.addActor(openFileChooser.fadeIn());
                openFileChooser.setZIndex(10000);
            }
        });
        saveFileBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveFileChooser.setDirectory(new File(""));
                uiStage.addActor(saveFileChooser.fadeIn());
                saveFileChooser.setZIndex(10000);
            }
        });

        lineIndex = new ArrayList<>();
        zoomBtn = new VisTextButton("Zoom: ");
        offsetBtn = new VisTextButton("Offset: ");
        zoomBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                zoom = 1;
                for(EditorElement e : elements)
                    e.redraw(uiStageOffset, zoom);
                controller.assertBody(false);
            }
        });
        offsetBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                uiStageOffset.set(0, 0);
                for(EditorElement e : elements)
                    e.redraw(uiStageOffset, zoom);
                controller.assertBody(false);
            }
        });

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

        stage.addActor(zoomBtn);
        stage.addActor(offsetBtn);

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
                stage.addActor(UMLLauncher.colourWindow.fadeIn("updateColour." + UMLLauncher.instance.getDisplay().getClass().getSimpleName()));
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
                if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
                    zoom -= Float.parseFloat(amount + "f") / 100;
                else zoom -= Float.parseFloat(amount + "f") / 10;
                if(zoom < .01f) zoom = .01f;
                if(zoom > 100) zoom = 100;
                for(EditorElement e : elements)
                    e.redraw(uiStageOffset, zoom);
                controller.assertBody(false);
                return super.scrolled(event, x, y, amount);
            }
        });

        index = 0;

        addButton(openFileBtn);
        addButton(saveFileBtn);

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
        zoomBtn.setText(String.format("Zoom: %.2f", zoom));
        zoomBtn.pack();
        zoomBtn.setPosition(Gdx.graphics.getWidth() - (zoomBtn.getWidth() + 5), 5);
        offsetBtn.setText(String.format("Offset: {%.2f, %.2f}", uiStageOffset.x, uiStageOffset.y));
        offsetBtn.pack();
        offsetBtn.setPosition(Gdx.graphics.getWidth()-(offsetBtn.getWidth()+5), (zoomBtn.getY()+zoomBtn.getHeight())+5);
        try {
            uiStage.act(delta);
            uiStage.draw();
        }catch(Exception e) {
            e.printStackTrace();
        }
        try{
            stage.act(delta);
            stage.draw();
        }catch(Exception e) {
            e.printStackTrace();
        }

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
        if(index % 3 == 0)
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
            if(parent == null) {
                GenericDisplay d = UMLLauncher.instance.getDisplay();
                if(d.currentElement != element)
                    d.currentElement = element;
                d.elementsTree.setSelectedIndex(-1);
                d.controller.onAllocate(element);
            }else{
                if(parent.currentElement != element)
                    parent.currentElement = element;
                parent.elementsTree.setSelectedIndex(-1);
                parent.controller.onAllocate(element);
            }
        }

    }
}
