package net.ncguy.uml.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import net.ncguy.uml.components.global.TaskMenu;
import net.ncguy.uml.io.JSONHandler;
import net.ncguy.uml.util.ListIndexer;

/**
 * Created by Nick on 06/10/2015 at 11:22.
 * Project: UMLEditor
 * Package: net.ncguy.uml.useCaseDisplay
 */
public class GenericDisplay implements Screen {

    JSONHandler jsonHandler;
    TaskMenu taskMenu;

    public float leftPaneWidth = 250;


    @Override
    public void show() {
        jsonHandler = new JSONHandler();
        taskMenu = new TaskMenu();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        taskMenu.getTable().setPosition(leftPaneWidth + 5, Gdx.graphics.getHeight() - 30);
        taskMenu.getTable().pack();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
