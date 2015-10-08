package net.ncguy.uml.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import net.ncguy.uml.global.AnchorPoints;

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
    public VisSelectBox<AnchorPoints> localAnchorSelect;
    public VisLabel remoteAnchorLbl;
    public VisSelectBox<AnchorPoints> remoteAnchorSelect;
    public VisLabel remoteActorLbl;
    public VisSelectBox<EditorElement> remoteActorSelect;
    public VisLabel remoteActorNameLbl;
    public VisTextField remoteActorNameTxt;
    public VisLabel lineTypeLbl;
    public VisSelectBox<LineData.LineType> lineTypeSelect;

    public VisTextButton changeColourBtn;

    private EditorElement blankElement;

    public LineDialog(String title) {
        super(title);
        blankElement = new EditorElement();
        blankElement.data.name = "None";
        setVisible(false);
        setModal(false);
        initUI();
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
        localAnchorSelect = new VisSelectBox<>();
        remoteAnchorLbl = new VisLabel("Remote Anchor");
        remoteAnchorSelect = new VisSelectBox<>();
        remoteActorLbl = new VisLabel("Remote Actor");
        remoteActorSelect = new VisSelectBox<>();
        remoteActorNameLbl = new VisLabel("Remote Actor Name");
        remoteActorNameTxt = new VisTextField();
        lineTypeLbl = new VisLabel("Line Type");
        lineTypeSelect = new VisSelectBox<>();
        changeColourBtn = new VisTextButton("Change Colour");
        toggleReverseBtn = new VisTextButton("Reverse search", "toggle");

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
                currentLine.localAnchor = localAnchorSelect.getSelected().offset();
            }
        });
        remoteAnchorSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(currentLine == null) return;
                currentLine.remoteAnchor = remoteAnchorSelect.getSelected().offset();
            }
        });
        remoteActorSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                EditorElement remoteActor = remoteActorSelect.getSelected();
                remoteActorNameTxt.setText(remoteActor.data.name);
                if(currentLine == null) return;
                if(remoteActor.equals(blankElement))
                    currentLine.remoteActor = null;
                else currentLine.remoteActor = remoteActor;
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
                lineCollection.remove(currentLine);
                populateList();
            }
        });
        toggleReverseBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if(lineScrollPane.getWidget().hashCode() == reverseLineDataList.hashCode()) {
                    lineScrollPane.setWidget(lineDataList);
                }else{
                    lineScrollPane.setWidget(reverseLineDataList);
                }
                System.out.println("Current Scrollable element: "+lineScrollPane.getWidget().toString());
                populateList();
            }
        });

        localAnchorSelect.setItems(AnchorPoints.values());
        remoteAnchorSelect.setItems(AnchorPoints.values());
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
        toggleReverseBtn.setBounds(204, (getHeight()-getPadTop())-96, 150, 30);
        super.draw(batch, parentAlpha);
    }

    public void changeElement(EditorElement editorElement){
        this.currentElement = editorElement;
        this.lineCollection = editorElement.linedata;
        this.currentLine = null;
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
            if(!d.parentActor.equals(currentElement) && d.remoteActor.equals(currentElement))
                revLineIndex.add(d);
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
            EditorElement[] es = new EditorElement[UMLLauncher.instance.useCaseDisplay.elements.size()];
            es[0] = blankElement;
            int index = 1;
            for(EditorElement e : UMLLauncher.instance.useCaseDisplay.elements) {
                if(e != currentElement)
                    es[index++] = e;
            }
            remoteActorSelect.setItems(es);
        }catch(Exception e) {


        }
        if(this.lineDataList.getSelectedIndex() >= 0) {
            if(this.lineDataList.getSelected() != null) {
                currentLine = this.lineDataList.getSelected();
                nameTxt.setText(currentLine.name);
                localAnchorSelect.setSelected(AnchorPoints.getPointFromVector(currentLine.localAnchor));
                remoteAnchorSelect.setSelected(AnchorPoints.getPointFromVector(currentLine.remoteAnchor));
                remoteActorSelect.setSelected(currentLine.remoteActor);
                remoteActorNameTxt.setText(remoteActorSelect.getSelected().data.name);
                lineTypeSelect.setSelected(currentLine.lineType);
                return;
            }
        }
        nameTxt.setText("");
        localAnchorSelect.setSelected(AnchorPoints.MID);
        remoteAnchorSelect.setSelected(AnchorPoints.MID);
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
