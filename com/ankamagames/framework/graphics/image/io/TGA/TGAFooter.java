package com.ankamagames.framework.graphics.image.io.TGA;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class TGAFooter
{
    public static final int m_signatureSize = 18;
    public int m_extArea;
    public int m_devArea;
    public byte[] m_signature;
    
    public TGAFooter() {
        super();
        this.m_signature = new byte[18];
    }
    
    public void SetFromBuffer(final byte[] buffer, final TypeRef<Integer> offset) {
        this.m_extArea = Caster.toInt(buffer, offset);
        this.m_devArea = Caster.toInt(buffer, offset);
        System.arraycopy(buffer, offset.get(), this.m_signature, 0, 18);
        offset.set(offset.get() + 18);
    }
    
    public void toStream(final OutputBitStream stream) throws IOException {
        stream.writeInt(this.m_extArea);
        stream.writeInt(this.m_devArea);
        stream.writeBytes(this.m_signature);
    }
}
