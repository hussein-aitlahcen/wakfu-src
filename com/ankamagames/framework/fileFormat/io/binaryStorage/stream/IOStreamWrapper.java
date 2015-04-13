package com.ankamagames.framework.fileFormat.io.binaryStorage.stream;

import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import java.util.zip.*;

public abstract class IOStreamWrapper implements Poolable
{
    protected static final Logger m_logger;
    public static final ObjectPool PLAIN;
    public static final ObjectPool COMPRESSED;
    protected final BufferedDataOutputStreamEx m_out;
    protected final BufferedDataInputStreamEx m_in;
    private ObjectPool m_pool;
    
    private IOStreamWrapper() {
        super();
        this.m_out = new BufferedDataOutputStreamEx();
        this.m_in = new BufferedDataInputStreamEx();
    }
    
    public abstract DataOutputStream wrapOutputStream(final OutputStream p0);
    
    public abstract DataInputStream wrapInputStream(final FileInputStream p0);
    
    public static synchronized IOStreamWrapper checkOut(final ObjectPool pool) {
        IOStreamWrapper iow = null;
        try {
            iow = (IOStreamWrapper)pool.borrowObject();
            iow.setPool(pool);
        }
        catch (Exception e) {
            IOStreamWrapper.m_logger.error((Object)"Exception", (Throwable)e);
        }
        return iow;
    }
    
    public void setPool(final ObjectPool pool) {
        this.m_pool = pool;
    }
    
    public void release() {
        try {
            this.m_pool.returnObject(this);
            this.m_pool = null;
        }
        catch (Exception e) {
            IOStreamWrapper.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)IOStreamWrapper.class);
        PLAIN = new MonitoredPool(new ObjectFactory<IOStreamWrapper>() {
            @Override
            public IOStreamWrapper makeObject() {
                return new IOStreamWrapper() {
                    @Override
                    public DataOutputStream wrapOutputStream(final OutputStream stream) {
                        this.m_out.setStream(stream);
                        return this.m_out;
                    }
                    
                    @Override
                    public DataInputStream wrapInputStream(final FileInputStream stream) {
                        this.m_in.setStream(stream);
                        return this.m_in;
                    }
                    
                    @Override
                    public void onCheckOut() {
                    }
                    
                    @Override
                    public void onCheckIn() {
                    }
                };
            }
        });
        COMPRESSED = new MonitoredPool(new ObjectFactory<IOStreamWrapper>() {
            @Override
            public IOStreamWrapper makeObject() {
                return new IOStreamWrapper() {
                    private final Deflater deflater = new Deflater(1);
                    private final Inflater inflater = new Inflater();
                    
                    @Override
                    public DataOutputStream wrapOutputStream(final OutputStream stream) {
                        this.deflater.reset();
                        this.m_out.setStream(new DeflaterOutputStream(stream, this.deflater));
                        return this.m_out;
                    }
                    
                    @Override
                    public DataInputStream wrapInputStream(final FileInputStream stream) {
                        this.inflater.reset();
                        this.m_in.setStream(new InflaterInputStream(stream, this.inflater));
                        return this.m_in;
                    }
                    
                    @Override
                    public void onCheckOut() {
                    }
                    
                    @Override
                    public void onCheckIn() {
                    }
                };
            }
        });
    }
}
