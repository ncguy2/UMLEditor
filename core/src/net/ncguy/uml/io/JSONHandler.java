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

/**
 * Created by Nick on 01/10/2015 at 18:49,
 * Project: UMLEditor.
 */
public class JSONHandler {

    private static Gson gson;

    public static Gson gson() {
        if(gson == null) {
            if(UMLLauncher.prettyJson)
                gson = new GsonBuilder().setPrettyPrinting().create();
            else gson = new Gson();
        }
        return gson;
    }

    public JSONHandler() {
        gson();
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
        workspaceData.prepareForLoad();
        return workspaceData;
    }

    public EditorElement getElementFromName(String name, WorkspaceData data) {
        for(EditorElement e : data.useCase_elements) {
            if(e.data.name.equalsIgnoreCase(name)) return e;
        }
        return null;
    }

    public void save(String filePath, WorkspaceData workspaceData) {
        workspaceData.prepareForSave();
        String json = gson.toJson(workspaceData);
        try{
            Files.write(new File(filePath).toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
