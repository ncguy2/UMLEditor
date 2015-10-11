package net.ncguy.uml.global;

import net.ncguy.uml.UMLLauncher;
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

    public void prepareForLoad() {
        for(EditorElement.Data data : useCase_elementData) {
            try {
                Object obj = data.type.getCtor().newInstance();
                if(obj instanceof EditorElement) {
                    EditorElement e = (EditorElement) obj;
                    data.element = e;
                    e.data = data;
                    e.fetchData();
                    useCase_elements.add(e);
                }
            }catch(Exception exc) {
                exc.printStackTrace();
            }
        }
        for(EditorElement.Data data : classDiagram_elementData) {
            try{
                Object obj = data.type.getCtor().newInstance();
                if(obj instanceof EditorElement) {
                    EditorElement e = (EditorElement)obj;
                    data.element = e;
                    e.data = data;
                    e.fetchData();
                    classDiagram_elements.add(e);
                }
            }catch(Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public void prepareForSave() {
        useCase_elements = UMLLauncher.instance.useCaseDisplay.elements;
        classDiagram_elements = UMLLauncher.instance.classDiagramDisplay.elements;
        // Use case
        ArrayList<EditorElement> elements = useCase_elements;
        EditorElement.Data[] datas = new EditorElement.Data[elements.size()];
        int index = 0;

        for(EditorElement e : elements) {
            e.prepareData();
            datas[index++] = e.data;
        }
        useCase_elementData.clear();
        for(EditorElement.Data data : datas) {
            useCase_elementData.add(data);
        }
        // Class diagram
        elements = classDiagram_elements;
        datas = new EditorElement.Data[elements.size()];
        index = 0;
        for(EditorElement e : elements) {
            e.prepareData();
            datas[index++] = e.data;
        }
        classDiagram_elementData.clear();
        for(EditorElement.Data data : datas) {
            classDiagram_elementData.add(data);
        }
    }

}
