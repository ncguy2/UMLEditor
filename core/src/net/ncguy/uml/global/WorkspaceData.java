package net.ncguy.uml.global;

import net.ncguy.uml.elements.EditorElement;

import java.util.ArrayList;

/**
 * Created by Nick on 01/10/2015 at 20:31,
 * Project: UMLEditor.
 */
public class WorkspaceData {

    public ArrayList<EditorElement.Data> useCase_elementData;
    transient public ArrayList<EditorElement> useCase_elements;

    public ArrayList<EditorElement.Data> classDiagram_elementData;
    transient public ArrayList<EditorElement> classDiagram_elements;

    public WorkspaceData() {
        useCase_elementData = new ArrayList<>();
        useCase_elements = new ArrayList<>();

        classDiagram_elementData = new ArrayList<>();
        classDiagram_elements = new ArrayList<>();
    }

}
