package net.ncguy.uml.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.components.CornerActor;
import net.ncguy.uml.components.LineActor;
import net.ncguy.uml.display.MainDisplay;

import static net.ncguy.uml.elements.ElementController.PointIndex.*;

/**
 * Created by Nick on 30/09/2015 at 20:22,
 * Project: UMLEditor.
 */
public class ElementController extends Actor {

    public Vector2[] points;
    public CornerActor[] cornerActors;
    public Actor controlledElement;
    public MainDisplay parent;
    public Stage stage;
    public LineActor[] lines;
    public boolean cornersAdded = false, linesAdded = false;

    public int cornerSize = 12;
    public int halfCornerSize;

    private ObjectDragListener dragListener;

    public ElementController(MainDisplay parent) {
        super();
        this.parent = parent;
        points = new Vector2[PointIndex.values().length];
        cornerActors = new CornerActor[points.length];
        lines = new LineActor[points.length];
        addListener(new OpenDataDialog(this));
    }

    public void onAllocate(Actor element) {
        halfCornerSize = cornerSize/2;
        controlledElement = element;
        if(getListeners().contains(dragListener, true))
            removeListener(dragListener);
        addListener(dragListener = new ObjectDragListener(this, element, parent));
        points[PointIndex.BOTLEFT.ordinal()]    = new Vector2(element.getX()-halfCornerSize,                    element.getY()-halfCornerSize);
        points[PointIndex.BOTRIGHT.ordinal()]   = new Vector2(element.getX()+element.getWidth()-halfCornerSize, element.getY()-halfCornerSize);
        points[PointIndex.TOPLEFT.ordinal()]    = new Vector2(element.getX()-halfCornerSize,                    element.getY()+element.getHeight()-halfCornerSize);
        points[PointIndex.TOPRIGHT.ordinal()]   = new Vector2(element.getX()+element.getWidth()-halfCornerSize, element.getY()+element.getHeight()-halfCornerSize);

        setBounds(element.getX(), element.getY(), element.getWidth(), element.getHeight());

//        if(element instanceof EditorElement) {
//            Vector2 o = UMLLauncher.instance.display.uiStageOffset;
//            ((EditorElement) element).setBasePosition(element.getX()-o.x, element.getY()-o.y);
//        }

        for(PointIndex key : PointIndex.values()) {
            CornerActor actor;
            if(cornerActors[key.ordinal()] == null) {
                actor = new CornerActor(this, cornerSize);
            }else{
                actor = cornerActors[key.ordinal()];
            }
            actor.setPosition(points[key.ordinal()]);
            actor.setSize(cornerSize, cornerSize);
            actor.addListener(new CornerDragListener(actor, this, element, parent));
            cornerActors[key.ordinal()] = actor;
        }
        setupLines();
        setupCorners();
    }

    public ElementController addedToStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public void setupCorners() {
        if(cornersAdded) return;
        for(CornerActor a : cornerActors) {
            this.stage.addActor(a);
        }
        cornersAdded = true;
    }
    public void setupLines() {
        lines[BOTLEFT.ordinal()] = new LineActor(cornerActors[BOTLEFT.ordinal()], cornerActors[TOPLEFT.ordinal()], this);
        lines[TOPLEFT.ordinal()] = new LineActor(cornerActors[TOPLEFT.ordinal()], cornerActors[TOPRIGHT.ordinal()], this);
        lines[TOPRIGHT.ordinal()] = new LineActor(cornerActors[TOPRIGHT.ordinal()], cornerActors[BOTRIGHT.ordinal()], this);
        lines[BOTRIGHT.ordinal()] = new LineActor(cornerActors[BOTRIGHT.ordinal()], cornerActors[BOTLEFT.ordinal()], this);

        for(PointIndex key : PointIndex.values()) {
            if(lines[key.ordinal()].resizeListener != null) {
                lines[key.ordinal()].removeListener(lines[key.ordinal()].resizeListener);
            }
            lines[key.ordinal()].initResizeListener();
//            lines[key.ordinal()].addListener(lines[key.ordinal()].resizeListener)
        }

        if(linesAdded) return;
        for(PointIndex key : PointIndex.values()) {
            stage.addActor(lines[key.ordinal()]);
            lines[key.ordinal()].key = key;
        }
        linesAdded = true;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        drawActorArray(lines, batch, alpha);
        drawActorArray(cornerActors, batch, alpha);
    }

    private void drawActorArray(Actor[] actors, Batch batch, float alpha) {
        if(actors != null) {
            for(Actor a : actors) {
                if(a != null)
                    a.draw(batch, alpha);
            }
        }
    }

    public void assertBody() { assertBody(true); }
    public void assertBody(boolean updateElement) {
        Actor e = controlledElement;
        if(e == null) return;

        cornerActors[BOTLEFT.ordinal()].setPosition(e.getX()-halfCornerSize,               e.getY()-halfCornerSize);
        cornerActors[BOTRIGHT.ordinal()].setPosition(e.getX() + e.getWidth() - halfCornerSize, e.getY() - halfCornerSize);
        cornerActors[TOPLEFT.ordinal()].setPosition(e.getX() - halfCornerSize, e.getY() + e.getHeight() - halfCornerSize);
        cornerActors[TOPRIGHT.ordinal()].setPosition(e.getX() + e.getWidth() - halfCornerSize, e.getY() + e.getHeight() - halfCornerSize);

        setBounds(e.getX(), e.getY(), e.getWidth(), e.getHeight());

//        setBounds(element.getX(), element.getY(), element.getWidth(), element.getHeight());

        if(updateElement) {
            if(controlledElement instanceof EditorElement) {
                Vector2 o = UMLLauncher.instance.display.uiStageOffset;
                System.out.println("PreChangeBounds: " + ((EditorElement) controlledElement).baseLocation);
                ((EditorElement) controlledElement).setBasePosition(getX() - o.x, getY() - o.y);
                System.out.println("PostChangeBounds: " + ((EditorElement) controlledElement).baseLocation);
//                ((EditorElement) controlledElement).redraw(o);
            }else{
                System.out.println("ControlledElement is not an instance of EditorElement");
            }
        }

    }

    public void assertPoints(CornerActor origin) {
        PointIndex originKey = null;
        for(PointIndex key : PointIndex.values()) {
            if(cornerActors[key.ordinal()] == origin) {
                originKey = key;
                break;
            }
        }
        if(originKey == null) return;
        switch(originKey) {
            case BOTLEFT:
                cornerActors[BOTRIGHT.ordinal()].setY(origin.getY());
                cornerActors[TOPLEFT.ordinal()].setX(origin.getX());
                break;
            case BOTRIGHT:
                cornerActors[BOTLEFT.ordinal()].setY(origin.getY());
                cornerActors[TOPRIGHT.ordinal()].setX(origin.getX());
                break;
            case TOPLEFT:
                cornerActors[TOPRIGHT.ordinal()].setY(origin.getY());
                cornerActors[BOTLEFT.ordinal()].setX(origin.getX());
                break;
            case TOPRIGHT:
                cornerActors[TOPLEFT.ordinal()].setY(origin.getY());
                cornerActors[BOTRIGHT.ordinal()].setX(origin.getX());
                break;
        }

        float w = cornerActors[BOTRIGHT.ordinal()].getX() - cornerActors[BOTLEFT.ordinal()].getX();
        float h = cornerActors[TOPLEFT.ordinal()].getY() - cornerActors[BOTLEFT.ordinal()].getY();

        setX(cornerActors[BOTLEFT.ordinal()].getX() + halfCornerSize);
        setY(cornerActors[BOTLEFT.ordinal()].getY() + halfCornerSize);
        setWidth(w);
        setHeight(h);

        controlledElement.setSize(w, h);

        if(controlledElement instanceof EditorElement){
            Vector2 o = UMLLauncher.instance.display.uiStageOffset;
            ((EditorElement)controlledElement).setBasePosition(getX()-o.x, getY()-o.y);
        }else{
            controlledElement.setPosition(getX(), getY());
        }
    }

    public PointIndex getKeyOfCorner(CornerActor corner) {
        PointIndex originKey = null;
        for(PointIndex key : PointIndex.values()) {
            if(cornerActors[key.ordinal()] == corner) {
                originKey = key;
                break;
            }
        }
        return originKey;
    }

    public static class OpenDataDialog extends ClickListener {
        ElementController ctrl;
        public OpenDataDialog(ElementController ctrl) {
            super();
            this.ctrl = ctrl;
        }
        @Override public void clicked(InputEvent event, float x, float y) {
            if(getTapCount() < 2) return;
            if(ctrl.parent.currentElement == null) return;
            if(ctrl.parent.currentElement instanceof EditorElement) {
                EditorElement e = (EditorElement)ctrl.parent.currentElement;
                ctrl.parent.dataDialog.getTitleLabel().setText(e.data.name);
                ctrl.parent.dataDialog_name.setText(e.data.name);
                ctrl.parent.dataDialog_contents.setText(e.data.contents);
                ctrl.parent.dataDialog.setBounds(100, 100, Gdx.graphics.getWidth()-200, Gdx.graphics.getHeight()-200);
                ctrl.parent.dataDialog.setVisible(true);
            }
        }
    }

    public static class ObjectDragListener extends DragListener {
        ElementController controller;
        Actor a;
        MainDisplay parent;
        public ObjectDragListener(ElementController controller, Actor a, MainDisplay parent) {
            super();
            this.controller = controller;
            this.a = a;
            this.parent = parent;
        }
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            parent.changeActiveActor(a);
            System.out.println("X: " + x + ", Y: " + y);
            a.setOrigin(x, y);
            return true;
        }
        @Override
        public void touchDragged(InputEvent event1, float x1, float y1, int pointer) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            float modX = (x - a.getOriginX())-parent.uiStageOffset.x;
            float modY = (y - a.getOriginY())-parent.uiStageOffset.y;
            if(a instanceof EditorElement){
                EditorElement e = (EditorElement)a;
                e.setBaseX(modX);
                e.setBaseY(modY);
            }else{
                a.setX(modX);
                a.setY(modY);
            }
            controller.assertBody();
        }
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
//            if (Tracker.isCursorOverActor(parent.delElementBtn)) {
//                a.remove();
//                parent.elements.removeValue(a, true);
//                parent.currentElement = null;
//            }
        }
    }

    public static class CornerDragListener extends ObjectDragListener {
        CornerActor c;
        public CornerDragListener(CornerActor c, ElementController controller, Actor a, MainDisplay parent) {
            super(controller, a, parent);
            this.c = c;
        }
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            parent.changeActiveActor(a);
            System.out.println("X: " + x + ", Y: " + y);
            c.setOrigin(x, y);
            return true;
        }
        @Override
        public void touchDragged(InputEvent event1, float x1, float y1, int pointer) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            float modX = (x - c.getOriginX());
            float modY = (y - c.getOriginY());
            PointIndex key = controller.getKeyOfCorner(c);
            CornerActor opposite = controller.cornerActors[key.oppositeOrdinal];
            if(key.name().toLowerCase().contains("left")) {
                if(modX > opposite.getX()-controller.halfCornerSize)
                    modX = opposite.getX()-controller.halfCornerSize;
            }else{
                if(modX < opposite.getX()+controller.halfCornerSize)
                    modX = opposite.getX()+controller.halfCornerSize;
            }
            if(key.name().toLowerCase().contains("top")) {
                if(modY < opposite.getY()+controller.halfCornerSize)
                    modY = opposite.getY()+controller.halfCornerSize;
            }else{
                if(modY > opposite.getY()-controller.halfCornerSize)
                    modY = opposite.getY()-controller.halfCornerSize;
            }
            c.setX(modX);
            c.setY(modY);
            c.controller.assertPoints(c);
        }
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
//            if (Tracker.isCursorOverActor(parent.delElementBtn)) {
//                a.remove();
//                parent.elements.removeValue(a, true);
//                parent.currentElement = null;
//            }
        }
    }

    public enum PointIndex {
        TOPLEFT(),
        TOPRIGHT(),
        BOTLEFT(TOPRIGHT),
        BOTRIGHT(TOPLEFT),
        ;

        private PointIndex() {}
        private PointIndex(PointIndex opposite) {
            opposite.oppositeOrdinal = this.ordinal();
            this.oppositeOrdinal = opposite.ordinal();
        }

        int oppositeOrdinal;

        public int opposite() {
            return this.oppositeOrdinal;
        }
    }

}
