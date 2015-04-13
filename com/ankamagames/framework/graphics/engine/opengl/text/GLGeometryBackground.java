package com.ankamagames.framework.graphics.engine.opengl.text;

import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.*;

public class GLGeometryBackground extends GeometryBackground
{
    public static final ObjectFactory Factory;
    
    @Override
    public void render(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        EngineStats.getInstance().addRenderedGeometry(this);
        this.updateVertices();
        final RenderStateManager renderStateManager = RenderStateManager.getInstance();
        renderStateManager.setBlendFunc(this.m_source, this.m_destination);
        renderStateManager.applyStates(renderer);
        final GLRenderer glRenderer = (GLRenderer)renderer;
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        final GL gl = glRenderer.getDevice();
        this.drawBuffer(gl, 7, this.m_vertexBuffer, this.m_indexBuffer);
        final float borderWidth = this.getBorderWidth();
        if (borderWidth > 0.0f) {
            renderStateManager.setLineWidth(borderWidth);
            renderStateManager.applyStates(renderer);
            this.drawBuffer(gl, 3, this.m_borderVertexBuffer, this.m_borderIndexBuffer);
        }
    }
    
    private void drawBuffer(final GL gl, final int Type, final VertexBufferPCT vertexBuffer, final IndexBuffer indexBuffer) {
        gl.glVertexPointer(2, 5126, 0, (Buffer)vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)vertexBuffer.getTexCoord0Buffer());
        gl.glDrawElements(Type, indexBuffer.getNumIndices(), 5123, (Buffer)indexBuffer.getBuffer());
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<GLGeometryBackground>
    {
        public ObjectFactory() {
            super(GLGeometryBackground.class);
        }
        
        @Override
        public GLGeometryBackground create() {
            return new GLGeometryBackground(null);
        }
    }
}
