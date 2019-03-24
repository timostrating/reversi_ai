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
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;

public class Load3dModelTest implements ApplicationListener {
    private PerspectiveCamera cam;
    private CameraInputController camController;
    private ModelBatch model3dBatch;
    private AssetManager assets;
    private Array<ModelInstance> instances = new Array<ModelInstance>();
    private Environment environment;
    private boolean loading = false;

    private Array<Vector3> curve = new Array<Vector3>();
    CatmullRomSpline<Vector3> myCatmull = null;


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

//        camController = new CameraInputController(cam);
//        Gdx.input.setInputProcessor(camController);

        // https://xoppa.github.io/blog/loading-models-using-libgdx/
        assets = new AssetManager();
        assets.load("stadium.obj", Model.class);
        loading = true;

        int scale = 5;
        for (float i=0; i<MathUtils.PI2 * 2; i+= 0.1)
            curve.add(new Vector3((scale + i) * MathUtils.sin(i), 5 + i, (scale + i) * MathUtils.cos(i)));
        myCatmull = new CatmullRomSpline<Vector3>((Vector3[]) curve.toArray(Vector3.class), true);

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        MeshPartBuilder builder = modelBuilder.part("line", GL20.GL_LINES, 3, new Material());
        // TODO: replace the magic number final long attributes = 3  the number of components of this attribute, must be between 1 and 4
        builder.setColor(Color.RED);

        for (int i=0; i<curve.size -1; i++)
            builder.line(curve.get(i),  curve.get(i+1));

        instances.add(new ModelInstance(modelBuilder.end()));
    }

    private void doneLoading() {
        Model model3d = assets.get("stadium.obj", Model.class);
        ModelInstance model3dInstance = new ModelInstance(model3d);
        instances.add(model3dInstance);
        loading = false;
    }

    Vector3 pos = new Vector3(7,1,7);
    float current = 0;

    @Override
    public void render () {
        if (loading && assets.update())
            doneLoading();

        // https://github.com/libgdx/libgdx/wiki/Path-interface-and-Splines
        current += Gdx.graphics.getDeltaTime() * 0.05f;
        if(current >= 1)
            current -= 1;
        myCatmull.valueAt(pos, current);
        cam.position.set(pos);
        cam.lookAt(Vector3.Zero);
        cam.up.set(cam.direction.y, cam.direction.x, 0).crs(cam.direction).nor();
        if (cam.up.y < 0)
            cam.up.scl(-1);
        cam.update();

//        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        model3dBatch.begin(cam);
        model3dBatch.render(instances, environment);
        model3dBatch.render(instances, environment);
        model3dBatch.end();
    }

    // https://stackoverflow.com/a/28596355
    public float angleBetween(Vector3 a, Vector3 b) {
        return MathUtils.atan2((a.sub(b)).len(), (a.add(b)).len());
    }
    public float angleBetween(Vector2 a, Vector2 b) {
        return a.sub(b).angle();
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
