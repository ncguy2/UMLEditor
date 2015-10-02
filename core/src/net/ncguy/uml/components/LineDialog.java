package net.ncguy.uml.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import net.ncguy.uml.drawable.Assets;
import net.ncguy.uml.drawable.Icons;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.elements.data.LineData;

import java.util.ArrayList;

/**
 * Created by Nick on 02/10/2015 at 15:32,
 * Project: UMLEditor.
 */
public class LineDialog extends VisWindow {

    public EditorElement currentElement;
    private ArrayList<LineData> lineCollection;

    private VisImageButton closeBtn;

    private static final Vector2 size = new Vector2(400, 225);

    public LineDialog(String title) {
        super(title);
        setVisible(false);
        setModal(false);
        initUI();
    }

    private void initUI() {
        closeBtn = new VisImageButton(Assets.getIcon(Icons.EXIT));

        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        getTitleTable().add(closeBtn);
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

    }

}
