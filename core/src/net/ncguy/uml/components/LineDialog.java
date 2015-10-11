package net.ncguy.uml.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.drawable.Icons;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.data.LineData;
import net.ncguy.uml.event.EventHandler;

import java.util.ArrayList;

/**
 * Created by Nick on 02/10/2015 at 15:32,
 * Project: UMLEditor.
 */
public class LineDialog extends VisWindow {

    public EditorElement currentElement;
    private ArrayList<LineData> lineCollection;
    private LineData currentLine;

    private VisList<LineData> lineDataList;
    private VisList<LineData> reverseLineDataList;
    private VisScrollPane lineScrollPane;

    private VisImageButton closeBtn;

    private VisImageButton newLineBtn, delLineBtn;
    private VisTextButton toggleReverseBtn;

    private static final Vector2 size = new Vector2(700, 400);

    // FORM
    public VisTable formTable;
    public VisLabel nameLbl;
    public VisTextField nameTxt;
    public VisLabel localAnchorLbl;
    public VisTextButton localAnchorSelect;
    public VisLabel remoteAnchorLbl;
    public VisTextButton remoteAnchorSelect;
    public VisLabel remoteActorLbl;
    public VisSelectBox<EditorElement> remoteActorSelect;
    public VisLabel remoteActorNameLbl;
    public VisTextField remoteActorNameTxt;
    public VisLabel lineTypeLbl;
    public VisSelectBox<LineData.LineType> lineTypeSelect;

    public AnchorSelectionWindow localAnchorSelection;
    public AnchorSelectionWindow remoteAnchorSelection;


    public VisTextButton changeColourBtn;

    private EditorElement blankElement;

    public LineDialog(String title) {
        super(title);
        blankElement = new EditorElement();
        blankElement.data.name = "None";
        setVisible(false);
        setModal(false);
        initUI();

//        testWindow = new AnchorSelectionWindow("Local Anchor");
//        EventHandler.addEventToHandler(testWindow.eventId, (args) -> {
//            if(args == null) return;
//            if(args[0] == null) return;
//            if(args[0] instanceof Vector2) {
//                currentLine.localAnchor = (Vector2)args[0];
//            }
//        });
//        testBtn = new VisTextButton("Anchor edit");
//        testBtn.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                super.clicked(event, x, y);
//                UMLLauncher.instance.getDisplay().stage.addActor(testWindow.fadeIn());
//            }
//        });
//        addActor(testBtn);
    }

    private void initUI() {
        closeBtn = new VisImageButton(Assets.getIcon(Icons.EXIT));
        lineDataList = new VisList<>();
        reverseLineDataList = new VisList<>();
        lineScrollPane = new VisScrollPane(lineDataList);
        formTable = new VisTable(true);
        nameLbl = new VisLabel("Line ID");
        nameTxt = new VisTextField("");
        localAnchorLbl = new VisLabel("Local Anchor");
        localAnchorSelect = new VisTextButton("");
        remoteAnchorLbl = new VisLabel("Remote Anchor");
        remoteAnchorSelect = new VisTextButton("");
        remoteActorLbl = new VisLabel("Remote Actor");
        remoteActorSelect = new VisSelectBox<>();
        remoteActorNameLbl = new VisLabel("Remote Actor Name");
        remoteActorNameTxt = new VisTextField();
        lineTypeLbl = new VisLabel("Line Type");
        lineTypeSelect = new VisSelectBox<>();
        changeColourBtn = new VisTextButton("Change Colour");
        toggleReverseBtn = new VisTextButton("Reverse search", "toggle");
        localAnchorSelection = new AnchorSelectionWindow("LocalAnchor");
        remoteAnchorSelection = new AnchorSelectionWindow("RemoteAnchor");

        localAnchorSelect.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(currentLine == null) {
                    localAnchorSelection.xSlider.setValue(50);
                    localAnchorSelection.ySlider.setValue(50);
                }else{
                    localAnchorSelection.xSlider.setValue(currentLine.getLocalAnchorX());
                    localAnchorSelection.ySlider.setValue(currentLine.getLocalAnchorY());
                }
                UMLLauncher.instance.getDisplay().stage.addActor(localAnchorSelection.fadeIn());
            }
        });
        remoteAnchorSelect.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(currentLine == null) {
                    remoteAnchorSelection.xSlider.setValue(50);
                    remoteAnchorSelection.ySlider.setValue(50);
                }else{
                    remoteAnchorSelection.xSlider.setValue(currentLine.getRemoteAnchorX());
                    remoteAnchorSelection.ySlider.setValue(currentLine.getRemoteAnchorY());
                }
                UMLLauncher.instance.getDisplay().stage.addActor(remoteAnchorSelection.fadeIn());
            }
        });

        newLineBtn = new VisImageButton(Assets.getIcon(Icons.LAYER_ADD));
        delLineBtn = new VisImageButton(Assets.getIcon(Icons.LAYER_REMOVE));

        changeColourBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getStage().addActor(UMLLauncher.colourWindow.fadeIn("updateColour.lineDialog"));
            }
        });
        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });
        lineDataList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(lineDataList.getSelected() != null) {
                    currentLine = lineDataList.getSelected();
                    updateSelected();
                }
            }
        });
        reverseLineDataList.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(reverseLineDataList.getSelected() != null) {
                    currentLine = reverseLineDataList.getSelected();
                    updateSelected();
                }
            }
        });
        nameTxt.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if(currentLine == null) return super.keyTyped(event, character);
                currentLine.name = nameTxt.getText();
                return super.keyTyped(event, character);
            }
        });
        localAnchorSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentLine == null) return;
                currentLine.localAnchor = localAnchorSelection.getOffset();
            }
        });
        remoteAnchorSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentLine == null) return;
                currentLine.remoteAnchor = remoteAnchorSelection.getOffset();
            }
        });
        remoteActorSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EditorElement remoteActor = remoteActorSelect.getSelected();
                remoteActorNameTxt.setText(remoteActor.data.name);
                if(currentLine == null) return;
                if(remoteActor.equals(blankElement)) {
                    currentLine.remoteActor = null;
                    remoteAnchorSelection.setBG(new Sprite());
                }else{
                    currentLine.remoteActor = remoteActor;
                    remoteAnchorSelection.setBG(remoteActor.sprite);
                }
            }
        });
        lineTypeSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentLine == null) return;
                currentLine.lineType = lineTypeSelect.getSelected();
            }
        });
        newLineBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                currentLine = currentElement.addLine();
                populateList();
            }
        });
        delLineBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(currentLine == null) return;
                currentElement.removeLine(currentLine);
                lineCollection.remove(currentLine);
                populateList();
            }
        });
        toggleReverseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(lineScrollPane.getWidget().hashCode() == reverseLineDataList.hashCode()) {
                    lineScrollPane.setWidget(lineDataList);
                }else{
                    lineScrollPane.setWidget(reverseLineDataList);
                }
                System.out.println("Current Scrollable element: " + lineScrollPane.getWidget().toString());
                populateList();
            }
        });
        EventHandler.addEventToHandler(localAnchorSelection.eventId, (args) -> {
            if(args == null) return;
            if(args[0] == null) return;
            if(currentLine == null) return;
            if(args[0] instanceof Vector2) {
                Vector2 vec = (Vector2) args[0];
                currentLine.localAnchor = vec;
                localAnchorSelect.setText(vec.toString());
            }
        });
        EventHandler.addEventToHandler(remoteAnchorSelection.eventId, (args) -> {
            if(args == null) return;
            if(args[0] == null) return;
            if(currentLine == null) return;
            if(args[0] instanceof Vector2) {
                Vector2 vec = (Vector2) args[0];
                currentLine.remoteAnchor = vec;
                remoteAnchorSelect.setText(vec.toString());
            }
        });

        lineTypeSelect.setItems(LineData.LineType.values());
        
        float fieldX = 250;

        formTable.add(nameLbl).align(Align.right);
        formTable.add(nameTxt).width(fieldX);
        formTable.row();
        formTable.add(localAnchorLbl).align(Align.right);
        formTable.add(localAnchorSelect).width(fieldX);
        formTable.row();
        formTable.add(remoteAnchorLbl).align(Align.right);
        formTable.add(remoteAnchorSelect).width(fieldX);
        formTable.row();
        formTable.add(remoteActorLbl).align(Align.right);
        formTable.add(remoteActorSelect).width(fieldX);
        formTable.row();
        formTable.add(remoteActorNameLbl).align(Align.right);
        formTable.add(remoteActorNameTxt).width(fieldX);
        formTable.row();
        formTable.add(lineTypeLbl).align(Align.right);
        formTable.add(lineTypeSelect).width(fieldX);

        remoteActorNameTxt.setDisabled(true);

        new Tooltip(newLineBtn, "Adds a new line to the current element");
        new Tooltip(delLineBtn, "Removes the selected line from the current element");

        getTitleTable().add(closeBtn);
        add(lineScrollPane);
        add(newLineBtn, delLineBtn);
        add(formTable);
        add(changeColourBtn);
        add(toggleReverseBtn);

        EventHandler.addEventToHandler("updateColour.lineDialog", (args) -> {
            if(currentElement == null) return;
            if(currentLine == null) return;
            if(args[0] instanceof Color) {
                currentLine.colour = (Color)args[0];
            }
        });
        updateSelected();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        lineScrollPane.setBounds(2, 2, 200, (getHeight() - getPadTop()) - 4);
        formTable.setBounds(250, 2, getWidth()-252, (getHeight()-getPadTop())-4);
        newLineBtn.setBounds(204, (getHeight()-getPadTop())-32, 30, 30);
        delLineBtn.setBounds(204, (getHeight()-getPadTop())-64, 30, 30);
        changeColourBtn.setBounds(204, 2, 150, 30);
        toggleReverseBtn.setBounds(204, (getHeight() - getPadTop()) - 96, 150, 30);
        super.draw(batch, parentAlpha);
    }

    public void changeElement(EditorElement editorElement){
        this.currentElement = editorElement;
        this.lineCollection = editorElement.linedata;
        this.currentLine = null;
        localAnchorSelection.setBG(currentElement.sprite);
        remoteAnchorSelection.setBG(new Sprite());
        populateList();
    }

    public void populateList() {
        this.lineDataList.clearItems();
        LineData[] data = new LineData[this.lineCollection.size()];
        int index = 0;
        for(LineData d : this.lineCollection) {
            data[index++] = d;
        }
        this.lineDataList.setItems(data);
        this.lineDataList.setSelectedIndex(-1);

        this.reverseLineDataList.clearItems();
        ArrayList<LineData> lineIndex = UMLLauncher.instance.useCaseDisplay.lineIndex;
        ArrayList<LineData> revLineIndex = new ArrayList<>();
        index = 0;
        for(LineData d : lineIndex) {
            try {
                if(!d.parentActor.equals(currentElement) && d.remoteActor.equals(currentElement))
                    revLineIndex.add(d);
            }catch(Exception e) {}
        }
        LineData[] revData = new LineData[revLineIndex.size()];
        for(LineData d : revLineIndex)
            revData[index++] = d;

        this.reverseLineDataList.setItems(revData);
        this.reverseLineDataList.setSelectedIndex(-1);

        currentLine = null;

        updateSelected();
    }

    public void updateSelected() {
        try {
            EditorElement[] es = new EditorElement[UMLLauncher.instance.getDisplay().elements.size()+1];
            es[0] = blankElement;
            int index = 1;

            for(EditorElement e : UMLLauncher.instance.getDisplay().elements) {
                es[index++] = e;
//                if(toggleReverseBtn.isChecked()) {
//                    es[index++] = e;
//                }else{
//                    if(e != currentElement)
//                        es[index++] = e;
//                }
            }
            remoteActorSelect.setItems(es);
        }catch(Exception e) {
            e.printStackTrace();
        }
        if(this.lineDataList.getSelectedIndex() >= 0) {
            if(this.lineDataList.getSelected() != null) {
                currentLine = this.lineDataList.getSelected();
                nameTxt.setText(currentLine.name);
                localAnchorSelect.setText(currentLine.localAnchor.toString());
                remoteAnchorSelect.setText(currentLine.remoteAnchor.toString());
                remoteActorSelect.setSelected(currentLine.remoteActor);
                remoteActorNameTxt.setText(remoteActorSelect.getSelected().data.name);
                lineTypeSelect.setSelected(currentLine.lineType);

                if(currentLine.remoteActor != null) {
                    if(currentLine.parentActor != null) localAnchorSelection.setBG(currentLine.parentActor.sprite);
                    else localAnchorSelection.setBG(currentElement.sprite);
                    remoteAnchorSelection.setBG(currentLine.remoteActor.sprite);
                }else remoteAnchorSelection.setBG(new Sprite());

                return;
            }
        }else if(this.reverseLineDataList.getSelectedIndex() >= 0) {
            if(this.reverseLineDataList.getSelected() != null) {
                currentLine = this.reverseLineDataList.getSelected();
                nameTxt.setText(currentLine.name);
                localAnchorSelect.setText(currentLine.localAnchor.toString());
                remoteAnchorSelect.setText(currentLine.remoteAnchor.toString());
                remoteActorSelect.setSelected(currentLine.remoteActor);
                remoteActorNameTxt.setText(remoteActorSelect.getSelected().data.name);
                lineTypeSelect.setSelected(currentLine.lineType);

                if(currentLine.remoteActor != null) {
                    if(currentLine.parentActor != null) localAnchorSelection.setBG(currentLine.parentActor.sprite);
                    else localAnchorSelection.setBG(currentElement.sprite);
                    remoteAnchorSelection.setBG(currentLine.remoteActor.sprite);
                }else remoteAnchorSelection.setBG(new Sprite());

                return;
            }
        }
        nameTxt.setText("");
        localAnchorSelect.setText(new Vector2(.5f, .5f).toString());
        remoteAnchorSelect.setText(new Vector2(.5f, .5f).toString());
        try {
            remoteActorSelect.setSelectedIndex(0);
            remoteActorNameTxt.setText(remoteActorSelect.getSelected().data.name);
        }catch(Exception e) {}
        lineTypeSelect.setSelected(LineData.LineType.ASSOCIATE);
    }

    public void initHelperVars() {
        lineCollection = currentElement.linedata;
    }

    public void setTitle(String title) {
        getTitleLabel().setText("Line Editor >> " + title);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        setBounds((Gdx.graphics.getWidth() / 2) - (size.x / 2), (Gdx.graphics.getHeight() / 2) - (size.y / 2), size.x, size.y);
        setZIndex(100);
    }

}
