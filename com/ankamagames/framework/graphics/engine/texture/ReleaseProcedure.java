package com.ankamagames.framework.graphics.engine.texture;

import gnu.trove.*;
import java.util.*;

final class ReleaseProcedure implements TObjectProcedure<Texture>
{
    private final ArrayList<Texture> m_texturesFree;
    private float m_freeTextureSize;
    
    ReleaseProcedure() {
        super();
        this.m_texturesFree = new ArrayList<Texture>(64);
    }
    
    @Override
    public boolean execute(final Texture texture) {
        if (texture == null) {
            return true;
        }
        texture.reduceLife();
        if (texture.getLife() <= 0) {
            assert texture.getNumReferences() == 0;
            this.m_texturesFree.add(texture);
            this.m_freeTextureSize += texture.getMemorySize();
        }
        return true;
    }
    
    void reset() {
        this.m_texturesFree.clear();
        this.m_freeTextureSize = 0.0f;
    }
    
    float selectToRelease(final ArrayList<Texture> texturesToRelease, float textureSizeToFree) {
        float releasedSize = 0.0f;
        if (this.m_freeTextureSize > textureSizeToFree) {
            for (int numElements = this.sortByScore(), i = 0; i < numElements; ++i) {
                final Texture texture = this.m_texturesFree.get(i);
                textureSizeToFree -= texture.getMemorySize();
                texturesToRelease.add(texture);
                releasedSize += texture.getMemorySize();
                if (textureSizeToFree < 0.0f) {
                    break;
                }
            }
        }
        else {
            for (int numElements = this.m_texturesFree.size(), i = 0; i < numElements; ++i) {
                final Texture texture = this.m_texturesFree.get(i);
                texturesToRelease.add(texture);
                releasedSize += texture.getMemorySize();
            }
        }
        return releasedSize;
    }
    
    private int sortByScore() {
        final int numElements = this.m_texturesFree.size();
        for (int i = 0; i < numElements; ++i) {
            for (int j = i + 1; j < numElements; ++j) {
                final Texture texture = this.m_texturesFree.get(i);
                final Texture texture2 = this.m_texturesFree.get(j);
                if (texture2.getScore() > texture.getScore()) {
                    this.m_texturesFree.set(i, texture2);
                    this.m_texturesFree.set(j, texture);
                }
            }
        }
        return numElements;
    }
}
