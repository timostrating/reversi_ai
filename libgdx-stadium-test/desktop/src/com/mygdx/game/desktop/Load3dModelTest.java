package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Load3dModelTest implements ApplicationListener {
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private ModelBatch model3dBatch;
    private AssetManager assets;
    private Array<ModelInstance> instances = new Array<ModelInstance>();
    private Environment environment;
    private boolean loading = false;

    @Override
    public void create () {
        model3dBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        assets.load("stadium.obj", Model.class);
        loading = true;

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
        builder.setColor(Color.RED);
        builder.line(3.0f, 3.0f, 3.0f,  3.0f, 7.0f, -8.0f);
        instances.add(new ModelInstance(modelBuilder.end()));
    }

    private void doneLoading() {
        Model model3d = assets.get("stadium.obj", Model.class);
        ModelInstance model3dInstance = new ModelInstance(model3d);
        instances.add(model3dInstance);
        loading = false;
    }

    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        model3dBatch.begin(cam);
        model3dBatch.render(instances, environment);
        model3dBatch.render(instances, environment);
        model3dBatch.end();
    }

    @Override
    public void dispose () {
        model3dBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    public void resume () { }
    public void resize (int width, int height) { }
    public void pause () { }
}
