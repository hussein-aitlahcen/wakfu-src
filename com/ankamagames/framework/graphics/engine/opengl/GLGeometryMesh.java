package com.ankamagames.framework.graphics.engine.opengl;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.states.*;
import javax.media.opengl.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.framework.kernel.core.common.*;

public class GLGeometryMesh extends GeometryMeshLine
{
    public static final ObjectFactory Factory;
    
    @Override
    public void render(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        EngineStats.getInstance().addRenderedGeometry(this);
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        stateManager.setLineWidth(this.m_lineWidth);
        stateManager.enableLineStipple(this.m_enableLineStipple);
        stateManager.setBlendFunc(this.m_source, this.m_destination);
        stateManager.applyStates(renderer);
        final GLRenderer glRenderer = (GLRenderer)renderer;
        glRenderer.m_stateManager.setVertexArrayComponents(13);
        final GL gl = glRenderer.getDevice();
        gl.glVertexPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)this.m_vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getTexCoord0Buffer());
        final MeshType meshType = this.getMeshType();
        switch (meshType) {
            case Triangle: {
                gl.glDrawElements(4, this.m_indexBuffer.getNumIndices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
                break;
            }
            case TriangleStrip: {
                gl.glDrawElements(5, this.m_vertexBuffer.getNumVertices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
                break;
            }
            case Quad: {
                gl.glDrawElements(7, this.m_vertexBuffer.getNumVertices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
                break;
            }
            case LineStrip: {
                gl.glDrawElements(3, this.m_vertexBuffer.getNumVertices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
                break;
            }
            case Line: {
                gl.glDrawElements(1, this.m_vertexBuffer.getNumVertices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
                break;
            }
            case Point: {
                gl.glDrawElements(0, this.m_vertexBuffer.getNumVertices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
                break;
            }
            default: {
                assert false : "Unimplemented mesh type";
                break;
            }
        }
    }
    
    @Override
    public void removeReference() {
        super.removeReference();
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<GLGeometryMesh>
    {
        public ObjectFactory() {
            super(GLGeometryMesh.class);
        }
        
        @Override
        public GLGeometryMesh create() {
            return new GLGeometryMesh(null);
        }
    }
}
