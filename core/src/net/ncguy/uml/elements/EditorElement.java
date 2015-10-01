package net.ncguy.uml.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kotcrab.vis.ui.widget.VisLabel;
import net.ncguy.uml.UMLLauncher;

/**
 * Created by Nick on 30/09/2015 at 20:58,
 * Project: UMLEditor.
 */
public class EditorElement extends Group {

    Sprite sprite;
    public Vector2 baseLocation;
    VisLabel baseLocationLbl;

    public EditorElement() {
        sprite = new Sprite(new Texture("assets/badlogic.jpg"));
        baseLocation = new Vector2();
        baseLocationLbl = new VisLabel();
        addActor(baseLocationLbl);
    }

    public void setBasePosition(float x, float y) {
        baseLocation.set(x, y);
        redraw(UMLLauncher.instance.display.uiStageOffset);
    }

    public void setBasePosition(float x, float y, int alignment) {
        baseLocation.set(x, y);
        redraw(UMLLauncher.instance.display.uiStageOffset);
    }

    public void setBaseBounds(float x, float y, float w, float h) {
        baseLocation.set(x, y);
        redraw(UMLLauncher.instance.display.uiStageOffset);
    }

    public void setBaseX(float x) {
        baseLocation.x = x;
        redraw(UMLLauncher.instance.display.uiStageOffset);
    }

    public void setBaseY(float y) {
        baseLocation.y = y;
        redraw(UMLLauncher.instance.display.uiStageOffset);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.draw(batch, alpha);
        baseLocationLbl.setText(String.format("X: %s\nY: %s", baseLocation.x, baseLocation.y));
        super.draw(batch, alpha);
    }

//    @Override public float getX() {
//        return baseLocation.x + UMLLauncher.instance.display.uiStageOffset.x;
//    }
//    @Override public float getY() {
//        return baseLocation.y + UMLLauncher.instance.display.uiStageOffset.y;
//    }

    public float getBaseX() { return baseLocation.x; }
    public float getBaseY() { return baseLocation.y; }

    public void redraw(Vector2 offset) {
        System.out.println("EditorElement.redraw >> "+this.hashCode());
        System.out.println(String.format("\tBase location: [%s, %s]", baseLocation.x, baseLocation.y));
        System.out.println(String.format("\tOffset: [%s, %s]", offset.x, offset.y));
        setX(baseLocation.x + offset.x);
        setY(baseLocation.y + offset.y);
    }
}
