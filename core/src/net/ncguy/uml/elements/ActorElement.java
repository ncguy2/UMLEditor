package net.ncguy.uml.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.uml.global.Sprites;

/**
 * Created by Nick on 01/10/2015 at 20:21,
 * Project: UMLEditor.
 */
public class ActorElement extends EditorElement {

    public ActorElement() {
        super();
        sprite = new Sprite(Sprites.actor);
        data.type = ElementTypes.ACTOR;
    }

    @Override
    public VisTable getConfigTable() {
        VisTable cfg = new VisTable(true);

        return cfg;
    }
}
