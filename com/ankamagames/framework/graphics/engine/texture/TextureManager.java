package com.ankamagames.framework.graphics.engine.texture;

import org.apache.log4j.*;
import java.util.*;
import java.util.concurrent.locks.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public final class TextureManager
{
    private static final Logger m_logger;
    private static final TextureManager m_instance;
    private static final int MIN_CACHE_SIZE = 131072;
    private final TLongObjectHashMap<Texture> m_textures;
    private final ArrayList<Texture> m_texturesToRelease;
    private float m_textureSize;
    private float m_cacheSize;
    private final PrepareProcedure m_prepareProcedure;
    private final ReleaseProcedure m_releaseProcedure;
    private final Lock m_mutex;
    
    private TextureManager() {
        super();
        this.m_cacheSize = 131072.0f;
        this.m_prepareProcedure = new PrepareProcedure();
        this.m_releaseProcedure = new ReleaseProcedure();
        this.m_mutex = new ReentrantLock();
        this.m_textures = new TLongObjectHashMap<Texture>(1024);
        this.m_texturesToRelease = new ArrayList<Texture>(128);
    }
    
    public static TextureManager getInstance() {
        return TextureManager.m_instance;
    }
    
    public Texture createTexture(final Renderer renderer, final long name, final String fileName, final boolean keepData, final boolean async) {
        Texture texture = this.getTexture(name);
        if (texture == null) {
            texture = renderer.createTexture(name, fileName, keepData);
            texture.load(async);
            this.putTexture(name, texture);
        }
        return texture;
    }
    
    public Texture createTexture(final Renderer renderer, final long name, final String fileName, final boolean keepData) {
        return this.createTexture(renderer, name, fileName, keepData, true);
    }
    
    public Texture createTexture(final Renderer renderer, final long name, final Image image, final boolean keepData) {
        Texture texture = this.getTexture(name);
        if (texture == null) {
            texture = renderer.createTexture(name, image, keepData);
            this.putTexture(name, texture);
        }
        return texture;
    }
    
    public Texture createTexturePowerOfTwo(final Renderer renderer, final long name, final Image image, final boolean keepData) {
        final Texture texture = this.getTexturePowerOfTwo(name);
        if (texture != null) {
            return texture;
        }
        return this.createTexturePowerOfTwoFromImage(renderer, name, image, keepData);
    }
    
    public Texture createTexturePowerOfTwo(final Renderer renderer, final long name, final String filePath, final boolean keepData) {
        Texture texture = this.getTexturePowerOfTwo(name);
        if (texture != null) {
            return texture;
        }
        final Image image = new Image();
        if (image.readFromFile(filePath)) {
            texture = this.createTexturePowerOfTwoFromImage(renderer, name, image, keepData);
            texture.m_fileName = filePath;
        }
        image.removeReference();
        return texture;
    }
    
    public Texture createTexturePowerOfTwo(final Renderer renderer, final long name, final String filePath, final boolean keepData, final boolean async) {
        if (!async) {
            return this.createTexturePowerOfTwo(renderer, name, filePath, keepData);
        }
        Texture texture = this.getTexturePowerOfTwo(name);
        if (texture != null) {
            return texture;
        }
        texture = renderer.createTexture(name, filePath, keepData);
        texture.setPowerOfTwo(true);
        texture.load(async);
        this.putTexture(name, texture);
        return texture;
    }
    
    private void putTexture(final long name, final Texture texture) {
        if (texture == null) {
            TextureManager.m_logger.error((Object)("Essaye d'ins\u00e9rer une texture null id=" + name), (Throwable)new Exception());
            return;
        }
        this.m_mutex.lock();
        this.m_textures.put(name, texture);
        texture.m_managerState = Texture.ManagerState.PUT;
        this.m_mutex.unlock();
    }
    
    public Texture createTexturePowerOfTwo(final Renderer renderer, final long name, final String fileName, final String filePath, final boolean keepData) {
        Texture texture = this.getTexturePowerOfTwo(name);
        if (texture != null) {
            return texture;
        }
        final Image image = new Image();
        if (image.readFromFile(filePath + fileName)) {
            texture = this.createTexturePowerOfTwoFromImage(renderer, name, image, keepData);
        }
        image.removeReference();
        return texture;
    }
    
    public Texture createRenderTarget(final Renderer renderer, final long name, final int width, final int height, final boolean generateMipMaps) {
        return renderer.createRenderTarget(name, width, height, generateMipMaps);
    }
    
    public int getNumTextures() {
        return this.m_textures.size();
    }
    
    public Texture getTexture(final long textureName) {
        this.m_mutex.lock();
        final Texture texture = this.m_textures.get(textureName);
        this.m_mutex.unlock();
        return texture;
    }
    
    public int loadTextures(final String texturePath) {
        assert texturePath != null;
        this.m_mutex.lock();
        int numTextureLoaded = 0;
        try {
            numTextureLoaded = 0;
            final TLongObjectIterator<Texture> iterator = this.m_textures.iterator();
            while (iterator.hasNext()) {
                iterator.advance();
                final Texture texture = iterator.value();
                if (texture.isReady()) {
                    continue;
                }
                if (!texture.isEmpty()) {
                    continue;
                }
                if (!texture.load(texturePath)) {
                    continue;
                }
                ++numTextureLoaded;
            }
        }
        catch (Exception e) {
            TextureManager.m_logger.error((Object)"Exception raised while loading textures : ", (Throwable)e);
        }
        finally {
            this.m_mutex.unlock();
        }
        return numTextureLoaded;
    }
    
    public void prepareTextures(final Renderer renderer) {
        this.m_mutex.lock();
        try {
            this.m_prepareProcedure.reset(renderer);
            this.m_textures.forEachValue(this.m_prepareProcedure);
            this.m_textureSize += this.m_prepareProcedure.getPreparedSize();
        }
        catch (Exception e) {
            TextureManager.m_logger.error((Object)"Exception raised while preparing textures : ", (Throwable)e);
        }
        finally {
            this.m_mutex.unlock();
        }
    }
    
    public void forceEmptyCache() {
        this.m_mutex.lock();
        final TLongObjectIterator<Texture> textureIter = this.m_textures.iterator();
        while (textureIter.hasNext()) {
            textureIter.advance();
            final Texture texture = textureIter.value();
            if (texture == null) {
                continue;
            }
            if (texture.getNumReferences() != 0) {
                continue;
            }
            for (int i = texture.getLife(); i > 0; --i) {
                texture.reduceLife();
            }
            assert texture.getLife() == 0;
        }
        this.m_mutex.unlock();
        final float oldSize = this.m_cacheSize;
        this.m_cacheSize = 0.0f;
        this.releaseTextures();
        this.m_cacheSize = oldSize;
    }
    
    public void releaseTextures() {
        final float textureSizeToFree = this.m_textureSize - this.m_cacheSize;
        if (textureSizeToFree <= 0.0f) {
            return;
        }
        this.m_mutex.lock();
        try {
            this.m_releaseProcedure.reset();
            this.m_textures.forEachValue(this.m_releaseProcedure);
            final float releasedSize = this.m_releaseProcedure.selectToRelease(this.m_texturesToRelease, textureSizeToFree);
            this.m_textureSize -= releasedSize;
            for (int i = 0, size = this.m_texturesToRelease.size(); i < size; ++i) {
                final Texture textureToRelease = this.m_texturesToRelease.get(i);
                textureToRelease.m_managerState = Texture.ManagerState.REMOVING;
                this.m_textures.remove(textureToRelease.getName());
                textureToRelease.removeReference();
            }
            this.m_texturesToRelease.clear();
        }
        catch (Exception e) {
            TextureManager.m_logger.error((Object)"Exception raised while releasing textures : ", (Throwable)e);
        }
        finally {
            this.m_mutex.unlock();
        }
    }
    
    public float getTextureMemoryUsed() {
        return this.m_textureSize / 1024.0f;
    }
    
    public void setCacheSize(final float size) {
        this.m_cacheSize = Math.max(131072.0f, size);
    }
    
    private Texture getTexturePowerOfTwo(final long name) {
        return this.getTexture(name);
    }
    
    private Texture createTexturePowerOfTwoFromImage(final Renderer renderer, final long name, final Image image, final boolean keepData) {
        final Point2i originalSize = new Point2i(image.getSize(0));
        image.toPowerOfTwo();
        final Texture texture = this.createTexture(renderer, name, image, keepData);
        texture.getSize().set(originalSize);
        this.putTexture(name, texture);
        return texture;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TextureManager.class);
        m_instance = new TextureManager();
    }
}
