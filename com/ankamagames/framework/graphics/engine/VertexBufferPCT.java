package com.ankamagames.framework.graphics.engine;

import com.ankamagames.framework.kernel.core.common.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.pools.*;

public final class VertexBufferPCT extends MemoryObject
{
    public static final int VERTEX_POSITION_SIZE = 2;
    public static final int VERTEX_COLOR_SIZE = 4;
    public static final int VERTEX_TEX_COORD_SIZE = 2;
    public static final int VERTEX_SIZE = 8;
    public static final ObjectFactory Factory;
    private FloatBufferPool m_positionBufferPool;
    private FloatBufferPool m_colorBufferPool;
    private FloatBufferPool m_texCoord0BufferPool;
    private FloatBuffer m_positionBuffer;
    private FloatBuffer m_colorBuffer;
    private FloatBuffer m_texCoord0Buffer;
    private int m_numVertices;
    private int m_maxVertices;
    private float[] position;
    private float[] colors;
    private float[] texCoords;
    private int p;
    private int c;
    private int t;
    
    private void init(final int maxVertices) {
        this.m_maxVertices = 0;
        this.m_numVertices = 0;
        this.setSize(maxVertices);
        assert maxVertices <= IndexBuffer.INDICES.getMaxIndices();
    }
    
    private void setSize(final int maxVertices) {
        if (this.m_positionBufferPool != null) {
            this.m_positionBufferPool.release();
        }
        if (this.m_colorBufferPool != null) {
            this.m_colorBufferPool.release();
        }
        if (this.m_texCoord0BufferPool != null) {
            this.m_texCoord0BufferPool.release();
        }
        this.m_maxVertices = maxVertices;
        final DirectBufferPoolManager bufferPoolManager = DirectBufferPoolManager.getInstance();
        final int maxVerticeSize = 4 * this.m_maxVertices;
        this.m_positionBufferPool = bufferPoolManager.getFloatBufferPool(maxVerticeSize * 2);
        this.m_colorBufferPool = bufferPoolManager.getFloatBufferPool(maxVerticeSize * 4);
        this.m_texCoord0BufferPool = bufferPoolManager.getFloatBufferPool(maxVerticeSize * 2);
        this.m_positionBuffer = (FloatBuffer)this.m_positionBufferPool.getBuffer();
        this.m_colorBuffer = (FloatBuffer)this.m_colorBufferPool.getBuffer();
        this.m_texCoord0Buffer = (FloatBuffer)this.m_texCoord0BufferPool.getBuffer();
    }
    
    public final void clear() {
        this.m_numVertices = 0;
    }
    
    public final FloatBuffer getPositionBuffer() {
        this.m_positionBuffer.rewind();
        return this.m_positionBuffer;
    }
    
    public final FloatBuffer getColorBuffer() {
        this.m_colorBuffer.rewind();
        return this.m_colorBuffer;
    }
    
    public final FloatBuffer getTexCoord0Buffer() {
        this.m_texCoord0Buffer.rewind();
        return this.m_texCoord0Buffer;
    }
    
    public final void rewindPositionBuffer() {
        this.m_positionBuffer.rewind();
    }
    
    public final void putPositionBuffer(final float[] vertexPositionArray) {
        this.m_positionBuffer.put(vertexPositionArray);
    }
    
    public final void setPositionBuffer(final float[] vertexPositionArray) {
        this.m_positionBuffer.rewind();
        this.m_positionBuffer.put(vertexPositionArray);
    }
    
    public final void addPositions(final float[] vertexPositionArray) {
        this.m_positionBuffer.position(this.m_numVertices * 2);
        this.m_positionBuffer.put(vertexPositionArray);
    }
    
    public final void addPositions(final float[] vertexPositionArray, final int length) {
        assert length <= vertexPositionArray.length;
        this.m_positionBuffer.position(this.m_numVertices * 2);
        this.m_positionBuffer.put(vertexPositionArray, 0, length);
    }
    
    public final void setPositionBuffer(final float[] vertexPositionArray, final int length) {
        this.m_positionBuffer.rewind();
        this.m_positionBuffer.put(vertexPositionArray, 0, length);
    }
    
    public final void addColors(final float[] colorPositionArray) {
        this.m_colorBuffer.position(this.m_numVertices * 4);
        this.m_colorBuffer.put(colorPositionArray);
    }
    
    public final void addColors(final float[] colorPositionArray, final int length) {
        assert length <= colorPositionArray.length;
        this.m_colorBuffer.position(this.m_numVertices * 4);
        this.m_colorBuffer.put(colorPositionArray, 0, length);
    }
    
    public final void rewindColorBuffer() {
        this.m_colorBuffer.rewind();
    }
    
    public final void putColorBuffer(final float[] vertexColorArray) {
        this.m_colorBuffer.put(vertexColorArray);
    }
    
    public final void setColorBuffer(final float[] vertexColorArray) {
        this.m_colorBuffer.rewind();
        this.m_colorBuffer.put(vertexColorArray);
    }
    
    public final void setColorBuffer(final float[] vertexColorArray, final int length) {
        this.m_colorBuffer.rewind();
        this.m_colorBuffer.put(vertexColorArray, 0, length);
    }
    
    public final void addTexCoords(final float[] texCoordsArray) {
        this.m_texCoord0Buffer.position(this.m_numVertices * 2);
        this.m_texCoord0Buffer.put(texCoordsArray);
    }
    
    public final void addTexCoords(final float[] texCoordsArray, final int length) {
        assert length <= texCoordsArray.length;
        this.m_texCoord0Buffer.position(this.m_numVertices * 2);
        this.m_texCoord0Buffer.put(texCoordsArray, 0, length);
    }
    
    public final void setTexCoord0Buffer(final float[] vertexTexCoord0Array) {
        this.m_texCoord0Buffer.rewind();
        this.m_texCoord0Buffer.put(vertexTexCoord0Array);
    }
    
    public final void setTexCoord0Buffer(final float[] vertexTexCoord0Array, final int length) {
        this.m_texCoord0Buffer.rewind();
        this.m_texCoord0Buffer.put(vertexTexCoord0Array, 0, length);
    }
    
    public final void setVertexColor(final int vertexIndex, final float r, final float g, final float b, final float a) {
        this.m_colorBuffer.position(vertexIndex * 4);
        this.m_colorBuffer.put(r);
        this.m_colorBuffer.put(g);
        this.m_colorBuffer.put(b);
        this.m_colorBuffer.put(a);
    }
    
    public final void setVertexColor(final int vertexIndex, final float[] colors) {
        this.m_colorBuffer.position(vertexIndex * 4);
        this.m_colorBuffer.put(colors);
    }
    
    public final void setVertexColor(final float[] colors) {
        this.m_colorBuffer.rewind();
        this.m_colorBuffer.put(colors);
    }
    
    public final void rewindTexCoordBuffer() {
        this.m_texCoord0Buffer.rewind();
    }
    
    public final void putTexCoord(final float[] texCoords) {
        this.m_texCoord0Buffer.put(texCoords);
    }
    
    public final void setVertexTexCoord0(final int vertexIndex, final float u, final float v) {
        this.m_texCoord0Buffer.position(vertexIndex * 2);
        this.m_texCoord0Buffer.put(u);
        this.m_texCoord0Buffer.put(v);
    }
    
    public final void setVertexPosition(final int vertexIndex, final float x, final float y) {
        this.m_positionBuffer.position(vertexIndex * 2);
        this.m_positionBuffer.put(x);
        this.m_positionBuffer.put(y);
    }
    
    public final void setVertexPositions(final int vertexOffset, final float[] positions) {
        this.m_positionBuffer.position(vertexOffset * 2);
        this.m_positionBuffer.put(positions);
    }
    
    public final void optimize() {
        this.m_positionBuffer.limit(this.m_numVertices * 2);
        this.m_colorBuffer.limit(this.m_numVertices * 4);
        this.m_texCoord0Buffer.limit(this.m_numVertices * 2);
        this.m_maxVertices = this.m_numVertices;
    }
    
    public final void setNumVertices(final int numVertices) {
        assert this.m_numVertices <= this.m_maxVertices;
        this.m_numVertices = numVertices;
    }
    
    public final int getNumVertices() {
        return this.m_numVertices;
    }
    
    public final int getMaxVertices() {
        return this.m_maxVertices;
    }
    
    @Override
    protected void checkout() {
        this.m_maxVertices = 0;
        this.m_numVertices = 0;
    }
    
    @Override
    protected void checkin() {
        if (this.m_positionBufferPool != null) {
            this.m_positionBufferPool.release();
            this.m_positionBufferPool = null;
        }
        if (this.m_colorBufferPool != null) {
            this.m_colorBufferPool.release();
            this.m_colorBufferPool = null;
        }
        if (this.m_texCoord0BufferPool != null) {
            this.m_texCoord0BufferPool.release();
            this.m_texCoord0BufferPool = null;
        }
        this.m_colorBuffer = null;
        this.m_positionBuffer = null;
        this.m_texCoord0Buffer = null;
        this.p = 0;
        this.c = 0;
        this.t = 0;
        this.position = null;
        this.colors = null;
        this.texCoords = null;
    }
    
    public final void begin() {
        if (this.position == null) {
            assert this.colors == null;
            assert this.texCoords == null;
            this.position = new float[this.m_maxVertices * 2];
            this.colors = new float[this.m_maxVertices * 4];
            this.texCoords = new float[this.m_maxVertices * 2];
        }
        this.m_numVertices = 0;
        this.m_positionBuffer.rewind();
        this.m_colorBuffer.rewind();
        this.m_texCoord0Buffer.rewind();
        this.p = 0;
        this.c = 0;
        this.t = 0;
    }
    
    public final void pushVertex(final float x, final float y, final float tx, final float ty, final float[] color) {
        this.pushVertex(x, y, tx, ty, color[0], color[1], color[2], color[3]);
    }
    
    public final void pushVertex(final float x, final float y, final float tx, final float ty, final float r, final float g, final float b, final float a) {
        this.position[this.p++] = x;
        this.position[this.p++] = y;
        this.colors[this.c++] = r;
        this.colors[this.c++] = g;
        this.colors[this.c++] = b;
        this.colors[this.c++] = a;
        this.texCoords[this.t++] = tx;
        this.texCoords[this.t++] = ty;
        ++this.m_numVertices;
        assert this.m_numVertices <= this.m_maxVertices;
    }
    
    public final void end() {
        this.m_positionBuffer.put(this.position, 0, this.p);
        this.m_colorBuffer.put(this.colors, 0, this.c);
        this.m_texCoord0Buffer.put(this.texCoords, 0, this.t);
        assert this.m_positionBuffer.position() % 2 == 0;
        assert this.m_positionBuffer.position() / 2 == this.m_numVertices;
        assert this.m_colorBuffer.position() % 4 == 0;
        assert this.m_colorBuffer.position() / 4 == this.m_numVertices;
        assert this.m_texCoord0Buffer.position() % 2 == 0;
        assert this.m_texCoord0Buffer.position() / 2 == this.m_numVertices;
    }
    
    public final void rewind() {
        this.m_positionBuffer.rewind();
        this.m_colorBuffer.rewind();
        this.m_texCoord0Buffer.rewind();
        this.m_numVertices = 0;
    }
    
    public void put(final VertexBufferPCT vb) {
        final int num = vb.getNumVertices();
        assert this.m_numVertices + num <= this.m_maxVertices;
        vb.m_positionBuffer.flip();
        this.m_positionBuffer.put(vb.m_positionBuffer);
        vb.m_colorBuffer.flip();
        this.m_colorBuffer.put(vb.m_colorBuffer);
        vb.m_texCoord0Buffer.flip();
        this.m_texCoord0Buffer.put(vb.m_texCoord0Buffer);
        this.m_numVertices += num;
    }
    
    static {
        Factory = new ObjectFactory();
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<VertexBufferPCT>
    {
        public ObjectFactory() {
            super(VertexBufferPCT.class);
        }
        
        @Override
        public VertexBufferPCT create() {
            return new VertexBufferPCT(null);
        }
        
        public VertexBufferPCT newInstance(final int maxVertices) {
            final VertexBufferPCT object = this.newInstance();
            object.init(maxVertices);
            return object;
        }
        
        public VertexBufferPCT newPooledInstance(final int maxVertices) {
            final VertexBufferPCT object = this.newPooledInstance();
            object.init(maxVertices);
            object.rewind();
            return object;
        }
    }
}
