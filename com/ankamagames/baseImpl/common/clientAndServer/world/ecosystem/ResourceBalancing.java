package com.ankamagames.baseImpl.common.clientAndServer.world.ecosystem;

import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import org.jdom.*;
import org.apache.tools.ant.*;

public class ResourceBalancing
{
    public final DataBalancing[] m_resourceFamilies;
    public final int m_stasisThreshold;
    public final int m_maxQuantity;
    
    public ResourceBalancing(final DataBalancing[] resourceFamilies, final int stasisThreshold, final int maxQuantity) {
        super();
        this.m_resourceFamilies = resourceFamilies;
        this.m_stasisThreshold = stasisThreshold;
        this.m_maxQuantity = maxQuantity;
        this.checkMaxQuantity();
    }
    
    public ResourceBalancing(final ByteBuffer buffer) {
        super();
        this.m_maxQuantity = (buffer.getShort() & 0xFFFF);
        this.m_stasisThreshold = (buffer.getShort() & 0xFFFF);
        final int count = buffer.get() & 0xFF;
        this.m_resourceFamilies = new DataBalancing[count];
        for (int i = 0; i < count; ++i) {
            final int id = buffer.getInt();
            final int minQuantity = buffer.getInt();
            final int maxQuantity = buffer.getInt();
            this.m_resourceFamilies[i] = new DataBalancing(id, minQuantity, maxQuantity);
        }
    }
    
    public DataBalancing getResourceFamily(final int familyId) {
        for (final DataBalancing dataBalancing : this.m_resourceFamilies) {
            if (dataBalancing.m_id == familyId) {
                return dataBalancing;
            }
        }
        return null;
    }
    
    public void write(final OutputBitStream stream) throws IOException {
        stream.writeShort((short)this.m_maxQuantity);
        stream.writeShort((short)this.m_stasisThreshold);
        stream.writeByte((byte)this.m_resourceFamilies.length);
        for (int i = 0; i < this.m_resourceFamilies.length; ++i) {
            stream.writeInt(this.m_resourceFamilies[i].m_id);
            stream.writeInt(this.m_resourceFamilies[i].m_minQuantity);
            stream.writeInt(this.m_resourceFamilies[i].m_maxQuantity);
        }
    }
    
    public Element writeXml() {
        final Element node = new Element("resourceBalancing");
        node.setAttribute("max_quantity", String.valueOf(this.m_maxQuantity));
        node.setAttribute("stasis_threshold", String.valueOf(this.m_stasisThreshold));
        for (int i = 0; i < this.m_resourceFamilies.length; ++i) {
            final Element elt = this.m_resourceFamilies[i].writeXml("resourceFamily");
            node.addContent(elt);
        }
        return node;
    }
    
    private void checkMaxQuantity() {
        int quantity = 0;
        for (final DataBalancing data : this.m_resourceFamilies) {
            quantity += data.m_maxQuantity;
        }
        if (quantity > this.m_maxQuantity) {
            throw new BuildException("le nombre max de resources d\u00e9passe la quantit\u00e9 max");
        }
        if (this.m_stasisThreshold > this.m_maxQuantity) {
            throw new BuildException("le seuil est sup\u00e9rieur \u00e0 la quantit\u00e9 max de resources");
        }
    }
}
