package net.ncguy.uml.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ncguy.uml.UMLLauncher;
import net.ncguy.uml.elements.EditorElement;
import net.ncguy.uml.global.WorkspaceData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Created by Nick on 01/10/2015 at 18:49,
 * Project: UMLEditor.
 */
public class JSONHandler {

    Gson gson;

    public JSONHandler() {
        if(UMLLauncher.prettyJson)
            gson = new GsonBuilder().setPrettyPrinting().create();
        else gson = new Gson();
    }

    public String load(String filePath) {
        String json = "";
        try {
            for(String line : Files.readAllLines(new File(filePath).toPath())) {
                json += line;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public WorkspaceData loadElements(String filepath) {
        String json = load(filepath);
        WorkspaceData workspaceData = gson.fromJson(json, WorkspaceData.class);
        for(EditorElement.Data data : workspaceData.elementData) {
            try {
                Object obj = data.type.getCtor().newInstance();
                if(obj instanceof EditorElement) {
                    EditorElement e = (EditorElement)obj;
                    data.element = e;

                    e.data = data;
                    e.baseLocation.x = data.baseX;
                    e.baseLocation.y = data.baseY;
                    e.baseSize.x = data.baseW;
                    e.baseSize.y = data.baseH;
                    workspaceData.elements.add(e);
                }
            }catch(Exception exc) {
                exc.printStackTrace();
            }
        }
        return workspaceData;
    }

    public EditorElement getElementFromName(String name, WorkspaceData data) {
        for(EditorElement e : data.elements) {
            if(e.data.name.equalsIgnoreCase(name)) return e;
        }
        return null;
    }

    public void save(String filePath, WorkspaceData workspaceData) {
        ArrayList<EditorElement> elements = workspaceData.elements;
        EditorElement.Data[] datas = new EditorElement.Data[elements.size()];
        int index = 0;

        for(EditorElement e : elements) {
            e.prepareData();
            datas[index++] = e.data;
        }
        for(EditorElement.Data data : datas) {
            workspaceData.elementData.add(data);
        }
        String json = gson.toJson(workspaceData);
        try{
            Files.write(new File(filePath).toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
