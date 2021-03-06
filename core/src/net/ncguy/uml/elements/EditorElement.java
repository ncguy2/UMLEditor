package net.ncguy.uml.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.api.IConfigurable;
import net.ncguy.uml.display.GenericDisplay;
import net.ncguy.uml.elements.data.LineData;
import net.ncguy.uml.global.AnchorPoints;
import net.ncguy.uml.global.Sprites;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by Nick on 30/09/2015 at 20:58,
 * Project: UMLEditor.
 */
public class EditorElement extends Group implements IConfigurable {

    public Sprite sprite;
    public Vector2 baseLocation;
    public Vector2 baseSize;
    VisLabel baseLocationLbl;
    public Data data;
    public ArrayList<LineData> linedata;
    public GenericDisplay parentDisplay;

    public EditorElement() {
//        sprite = new Sprite(new Texture("assets/components/generic.png"));
        sprite = new Sprite(Sprites.useCase);
        setSize(sprite.getWidth(), sprite.getHeight());
        baseLocation = new Vector2();
        baseSize = new Vector2(sprite.getWidth(), sprite.getHeight());
        baseLocationLbl = new VisLabel();
        linedata = new ArrayList<>();
        data = new Data();
        data.name = this.getClass().getSimpleName()+" : "+this.hashCode();
        data.element = this;
        data.type = ElementTypes.GENERIC;
        data.lineData = new ArrayList<>();
        addActor(baseLocationLbl);
        setColor(data.colour != null ? data.colour : Color.WHITE);
        baseSize.x = baseSize.y = 128;
        sprite.setSize(baseSize.x, baseSize.y);
        setSize(baseSize.x, baseSize.y);

        if(UMLLauncher.instance.getScreen() instanceof GenericDisplay) {
            parentDisplay = (GenericDisplay)UMLLauncher.instance.getScreen();
        }else{
            parentDisplay = parentDisplay;
        }
    }

    public void removeLine(LineData line) {
        linedata.remove(line);
        data.lineData.remove(line);
    }

    public LineData addLine() {
        LineData line = new LineData();
        line.localAnchor = AnchorPoints.MID.offset();
        line.parentActor = this;
        linedata.add(line);
        data.lineData.add(line);
        return line;
    }
    public void loadLine(LineData line) {
        line.parentActor = this;
        for(EditorElement e : parentDisplay.elements) {
            if(e.data.name.equalsIgnoreCase(line.remoteActorName)) {
                line.remoteActor = e;
                break;
            }
        }
        linedata.add(line);
    }

    public void loadLinesFromData() {
        if(data.lineData == null) return;
        for(LineData line : data.lineData) {
            loadLine(line);
        }
    }

    public void setBasePosition(float x, float y) {
        baseLocation.set(x, y);
        redraw(parentDisplay.uiStageOffset);
    }

    public void setBasePosition(float x, float y, int alignment) {
        baseLocation.set(x, y);
        redraw(parentDisplay.uiStageOffset);
    }

    public void setBaseBounds(float x, float y, float w, float h) {
        baseLocation.set(x, y);
        baseSize.set(w, h);
        redraw(parentDisplay.uiStageOffset);
    }

    public void setBaseX(float x) {
        baseLocation.x = x;
        redraw(parentDisplay.uiStageOffset);
    }

    public void setBaseY(float y) {
        baseLocation.y = y;
        redraw(parentDisplay.uiStageOffset);
    }

    public void setBaseW(float w) {
        baseSize.x = w;
        redraw(parentDisplay.uiStageOffset);
    }
    public void setBaseH(float h) {
        baseSize.y = h;
        redraw(parentDisplay.uiStageOffset);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        if(data.lineData == null) {
            data.lineData = new ArrayList<>();
        }
        for(LineData line : data.lineData) {
            line.draw(batch, alpha);
        }
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch, alpha);
        baseLocationLbl.setText(String.format("X: %s\nY: %s", baseLocation.x, baseLocation.y));
//        super.draw(batch, alpha);
    }

    @Override
    public void setColor(Color colour) {
        super.setColor(colour);
        sprite.setColor(colour);
        data.colour = colour;
    }

    public void prepareData() {
        data.baseX = baseLocation.x;
        data.baseY = baseLocation.y;
        data.baseW = baseSize.x;
        data.baseH = baseSize.y;
        data.colour = data.colour != null ? data.colour : Color.WHITE;
        data.lineData = new ArrayList<>();
        for(LineData line : linedata) {
            line.remoteActorName = line.remoteActor.data.name;
            data.lineData.add(line);
        }
    }

    public float getBaseX() { return baseLocation.x; }
    public float getBaseY() { return baseLocation.y; }

    public void redraw(Vector2 offset) { redraw(offset, parentDisplay.zoom);}
    public void redraw(Vector2 offset, float zoom) {
        if(offset == null) offset = UMLLauncher.instance.getDisplay().uiStageOffset;
        if(zoom == 0) zoom = UMLLauncher.instance.getDisplay().zoom;
        setColor(data.colour != null ? data.colour : Color.WHITE);
        setX(baseLocation.x + (offset.x * zoom));
        setY(baseLocation.y + (offset.y * zoom));
        setWidth(baseSize.x * zoom);
        setHeight(baseSize.y * zoom);
    }

    @Override
    public VisTable getConfigTable() {
        VisTable cfgTable = new VisTable(true);

        VisSelectBox<LineData> linedata = new VisSelectBox<>();
        Array<LineData> lines = new Array<>();
        for(LineData line : this.linedata)
            lines.add(line);
        linedata.setItems(lines);
        cfgTable.add(linedata);

        return cfgTable;
    }

    public void contentHandle(Actor contentActor) {
        if(contentActor instanceof TextArea) {
            ((TextArea)contentActor).setText(data.contents.toString());
        }
    }

    @Override public String toString() { return data.name; }

    public void fetchData() {
        data.contents = data.contents == null ? "" : data.contents;
        baseLocation.x = data.baseX;
        baseLocation.y = data.baseY;
        baseSize.x = data.baseW;
        baseSize.y = data.baseH;
    }

    public static class Data {
        public String name;
        public Color colour;
        public Object contents;
        public ElementTypes type;
        public ArrayList<LineData> lineData;

        public float baseX, baseY;
        public float baseW, baseH;
        public transient EditorElement element;
        @Override public String toString() { return name; }
    }

    public enum ElementTypes {
        GENERIC(EditorElement.class),
        ACTOR(ActorElement.class),
        CLASS(ClassElement.class),
        ;
        ElementTypes(Class clazz) {
            try{
                ctor = clazz.getConstructor();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

        transient Constructor ctor;

        public Constructor getCtor() {
            return ctor;
        }

    }

}
