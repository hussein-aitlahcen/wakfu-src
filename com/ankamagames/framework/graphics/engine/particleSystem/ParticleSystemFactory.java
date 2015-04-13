package com.ankamagames.framework.graphics.engine.particleSystem;

import com.ankamagames.framework.kernel.core.resource.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;

public abstract class ParticleSystemFactory<P extends ParticleSystem> extends FileHashCache<byte[]>
{
    public static final String PARTICLE_SYSTEM_EXTENSION = ".xps";
    public static int SYSTEM_WITHOUT_LEVEL;
    public final FilenameFilter FILENAME_FILTER;
    
    protected ParticleSystemFactory() {
        super(5242880L, true);
        this.FILENAME_FILTER = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(ParticleSystemFactory.this.getExtension());
            }
        };
    }
    
    public abstract P getFreeParticleSystem(final String p0);
    
    @Override
    public String getExtension() {
        return ".xps";
    }
    
    @Override
    public final FilenameFilter getFilenameFilter() {
        return this.FILENAME_FILTER;
    }
    
    protected String createFileName(final int particleId) {
        return this.getPath() + particleId + this.getExtension();
    }
    
    protected void createParticleSystem(final int particleSystemId, final int particleSystemLevel, final ParticleSystem particleSystem) throws Exception {
        final String fileName = this.createFileName(particleSystemId);
        this.createParticleSystem(fileName, particleSystemLevel, particleSystem);
    }
    
    protected void createParticleSystem(final String fileName, final int particleSystemLevel, final ParticleSystem particleSystem) throws Exception {
        this.setSystemGUID(particleSystem, fileName);
        this.load(fileName, particleSystemLevel, particleSystem);
        particleSystem.registerAllBaseEmitters();
        long textureName;
        try {
            textureName = (0xDD00DD0000000000L | particleSystem.m_textureId);
        }
        catch (Exception e) {
            ParticleSystemFactory.m_logger.error((Object)("The name of a particle system must be a number" + e));
            textureName = (0xDD00DD0000000000L | Engine.getTextureName(FileHelper.getNameWithoutExt(fileName)));
        }
        final String textureFileName = FileHelper.getParentPath(fileName) + '/' + particleSystem.m_textureId + ".tga";
        final TextureManager textureManager = TextureManager.getInstance();
        Texture texture = textureManager.getTexture(textureName);
        if (texture == null) {
            final Image image = new Image(textureFileName);
            texture = textureManager.createTexture(RendererType.OpenGL.getRenderer(), textureName, image, false);
            image.removeReference();
        }
        particleSystem.initialize(texture);
    }
    
    protected void load(final String fileName, final int particleSystemLevel, final ParticleSystem particleSystem) throws Exception {
        particleSystem.m_fileName = fileName;
        particleSystem.m_fileId = PrimitiveConverter.getInteger(FileHelper.getNameWithoutExt(fileName), 0);
        ParticleSystemLoader.load(fileName, particleSystem, particleSystemLevel);
    }
    
    protected void setSystemGUID(final ParticleSystem particleSystem, final String fileName) {
        final int lastSlash = fileName.lastIndexOf(47);
        final int lastCounterSlash = fileName.lastIndexOf(92);
        final int nameIndex = (lastSlash > lastCounterSlash) ? lastSlash : lastCounterSlash;
        final int extensionIndex = fileName.indexOf(46, nameIndex);
        final String name = fileName.substring(nameIndex + 1, extensionIndex);
        long systemGUID;
        try {
            systemGUID = (0xDD00DD0000000000L | Integer.parseInt(name));
        }
        catch (Exception e) {
            systemGUID = (0xDD00DD0000000000L | Engine.getTextureName(name));
        }
        particleSystem.setSystemGUID(systemGUID);
    }
    
    public final void setLevel(final int particleSystemLevel, final ParticleSystem particleSystem) {
        try {
            particleSystem.getEmitterDefinitions().clear();
            final Emitter[] emitters = particleSystem.getEmitters();
            ParticleSystemLoader.load(particleSystem.m_fileName, particleSystem, particleSystemLevel);
            for (int i = 0, size = emitters.length; i < size; ++i) {
                final EmitterDefinition definition = particleSystem.getEmitterDefinitions().get(i);
                emitters[i].setDefinition(definition);
            }
            particleSystem.reloadGeometry();
        }
        catch (Exception e) {
            ParticleSystemFactory.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    @Override
    protected final Reference createReference(final byte[] data) throws Exception {
        return new Reference(data);
    }
    
    static {
        ParticleSystemFactory.SYSTEM_WITHOUT_LEVEL = 1;
    }
    
    class Reference extends FileHashCache.Reference
    {
        public Reference(final byte[] data) {
            super((FileHashCache<byte[]>)ParticleSystemFactory.this, data);
        }
        
        @Override
        public long estimatedSize() {
            return ((byte[])(Object)this.m_data).length;
        }
    }
}
