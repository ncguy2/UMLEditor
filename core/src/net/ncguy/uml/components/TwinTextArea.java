package net.ncguy.uml.components;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import net.ncguy.uml.elements.ClassElement;
import net.ncguy.uml.event.IEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 09/10/2015 at 20:53,
 * Project: UMLEditor.
 */
public class TwinTextArea extends Group {

    private VisTable table;
    private VisLabel leftColLbl, rightColLbl;
    private VisTextArea leftColTxt;
    private VisTextArea rightColTxt;

    private IEvent leftChangeEvent;
    private IEvent rightChangeEvent;

    public TwinTextArea() {
        super();
        table = new VisTable(true);
        leftColLbl = new VisLabel();
        rightColLbl = new VisLabel();
        leftColTxt = new VisTextArea();
        rightColTxt = new VisTextArea();

        table.add(leftColLbl).width(300).align(Align.center);
        table.add(rightColLbl).width(300).align(Align.center);
        table.row();
        table.add(leftColTxt).width(300).height(350);
        table.add(rightColTxt).width(300).height(350);

        addActor(table);
        table.setFillParent(true);

        leftChangeEvent = (args) -> {};
        rightChangeEvent = (args) -> {};

        leftColTxt.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char c) {
                leftChangeEvent.run(leftColTxt);
                return super.keyTyped(event, c);
            }
        });
        rightColTxt.addListener(new InputListener() {
            @Override public boolean keyTyped(InputEvent event, char c) {
                rightChangeEvent.run(rightColTxt);
                return super.keyTyped(event, c);
            }
        });
    }

    public void setColLbls(String left, String right) {
        setLeftColLbl(left);
        setRightColLbl(right);
    }
    public void setLeftColLbl(String txt) {
        leftColLbl.setText(txt);
    }
    public void setRightColLbl(String txt) {
        rightColLbl.setText(txt);
    }

    public void setColLines(ClassElement.ClassData data) {
        setLeftColLines(data.attributes);
        setRightColLines(data.methods);
    }

    public void setLeftColLines(List<String> lines) {
        String str = "";
        for(String line : lines)
            str += line+"\n";
        leftColTxt.setText(str);
    }
    public void setRightColLines(List<String> lines) {
        String str = "";
        for(String line : lines)
            str += line+"\n";
        rightColTxt.setText(str);
    }

    public void setLeftChangeEvent(IEvent event) {
        leftChangeEvent = event;
    }
    public void setRightChangeEvent(IEvent event) {
        rightChangeEvent = event;
    }

    public ArrayList<String> getLines(VisTextArea area) {
        ArrayList<String> lines = new ArrayList<>();
        String[] lineArr = area.getText().split("\n");
        for(String s : lineArr) {
            if(s.length() >= 1) lines.add(s);
        }
        return lines;
    }

    public ArrayList<String> getLeftLines() {
        return getLines(leftColTxt);
    }
    public ArrayList<String> getRightLines() {
        return getLines(rightColTxt);
    }

}
