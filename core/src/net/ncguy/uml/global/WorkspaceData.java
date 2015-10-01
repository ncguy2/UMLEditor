package net.ncguy.uml.global;

import net.ncguy.uml.elements.EditorElement;

import java.util.ArrayList;

/**
 * Created by Nick on 01/10/2015 at 20:31,
 * Project: UMLEditor.
 */
public class WorkspaceData {

    public ArrayList<EditorElement.Data> elementData;
    transient public ArrayList<EditorElement> elements;

    public WorkspaceData() {
        elementData = new ArrayList<>();
        elements = new ArrayList<>();
    }

}
