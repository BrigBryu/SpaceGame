package com.BrigBryu.SpaceShooter.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager {
    private static TextureAtlas atlas;

    public static void load(String atlasPath) {
        atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
    }

    public static TextureRegion getTexture(String name) {
        return atlas.findRegion(name);
    }

    public static void dispose() {
        if (atlas != null) {
            atlas.dispose();
        }
    }
}
