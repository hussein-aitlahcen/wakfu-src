package com.ankamagames.framework.graphics.engine.opengl;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.states.*;
import javax.media.opengl.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.*;

public class GLGeometrySprite extends GeometrySprite
{
    public static final ObjectFactory Factory;
    
    @Override
    public void render(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        EngineStats.getInstance().addRenderedGeometry(this);
        final RenderStateManager renderStateManager = RenderStateManager.getInstance();
        renderStateManager.setBlendFunc(this.m_source, this.m_destination);
        renderStateManager.applyStates(renderer);
        final GLRenderer glRenderer = (GLRenderer)renderer;
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        final GL gl = glRenderer.getDevice();
        gl.glVertexPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)this.m_vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getTexCoord0Buffer());
        gl.glDrawArrays(7, 0, 4);
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<GLGeometrySprite>
    {
        public ObjectFactory() {
            super(GLGeometrySprite.class);
        }
        
        @Override
        public final GLGeometrySprite create() {
            return new GLGeometrySprite(null);
        }
        
        @Override
        public final String toString() {
            return "Factory pour les GLGeometrySprite";
        }
    }
}
