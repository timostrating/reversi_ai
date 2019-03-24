package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class BattleShaderTest implements ApplicationListener {

    /*  THIS IS FROM import com.badlogic.gdx.graphics.glutils.ShaderProgram
        String POSITION_ATTRIBUTE = "a_position"; // default name for position attributes
        String NORMAL_ATTRIBUTE = "a_normal"; // default name for normal attributes
        String COLOR_ATTRIBUTE = "a_color"; // default name for color attributes
        String TEXCOORD_ATTRIBUTE = "a_texCoord"; // default name for texcoords attributes, append texture unit number
        String TANGENT_ATTRIBUTE = "a_tangent"; // default name for tangent attribute
        String BINORMAL_ATTRIBUTE = "a_binormal"; // default name for binormal attribute
        String BONEWEIGHT_ATTRIBUTE = "a_boneWeight"; // default name for boneweight attribute
    */

    String vertexShader;
    String fragmentShader;

    Texture tex;
    SpriteBatch batch;
    OrthographicCamera cam;
    ShaderProgram shader;

    @Override
    public void create() {
        tex = new Texture(Gdx.files.internal("badlogic.jpg"));

        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        vertexShader = Gdx.files.internal("battleshader/vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("battleshader/fragment.glsl").readString();

        //print it out for clarity
        System.out.println("Vertex Shader:\n-------------\n\n"+vertexShader);
        System.out.println("\n");
        System.out.println("Fragment Shader:\n-------------\n\n"+fragmentShader);

        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }

        if (shader.getLog().length()!=0)
            System.out.println(shader.getLog());

        batch = new SpriteBatch(1000, shader);
        batch.setShader(shader);

        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.setToOrtho(false);
    }

    @Override
    public void resize(int width, int height) {
        cam.setToOrtho(false, width, height);
        batch.setProjectionMatrix(cam.combined);

        //bind the shader, then set the uniform, then unbind the shader
        shader.begin();
        shader.setUniformf("resolution", width, height);
        shader.end();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        //notice that LibGDX coordinate system origin is lower-left
        batch.draw(tex, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void dispose() {
        batch.dispose();
        shader.dispose();
        tex.dispose();
    }
}
