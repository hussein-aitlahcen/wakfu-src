package com.ankamagames.baseImpl.common.clientAndServer.world.ecosystem;

import java.nio.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import org.jdom.*;
import org.apache.tools.ant.*;

public class MonsterBalancing
{
    public final DataBalancing[] m_monsterFamilies;
    public final int m_stasisThreshold;
    public final int m_maxQuantity;
    
    public MonsterBalancing(final DataBalancing[] monsterFamilies, final int stasisThreshold, final int maxQuantity) {
        super();
        this.m_monsterFamilies = monsterFamilies;
        this.m_stasisThreshold = stasisThreshold;
        this.m_maxQuantity = maxQuantity;
        this.checkMaxQuantity();
    }
    
    public MonsterBalancing(final ByteBuffer buffer) {
        super();
        this.m_maxQuantity = (buffer.getShort() & 0xFFFF);
        this.m_stasisThreshold = (buffer.getShort() & 0xFFFF);
        final int count = buffer.get() & 0xFF;
        this.m_monsterFamilies = new DataBalancing[count];
        for (int i = 0; i < count; ++i) {
            final int id = buffer.getInt();
            final int minQuantity = buffer.getInt();
            final int maxQuantity = buffer.getInt();
            this.m_monsterFamilies[i] = new DataBalancing(id, minQuantity, maxQuantity);
        }
    }
    
    public DataBalancing getMonsterFamily(final int familyId) {
        for (final DataBalancing dataBalancing : this.m_monsterFamilies) {
            if (dataBalancing.m_id == familyId) {
                return dataBalancing;
            }
        }
        return null;
    }
    
    public void write(final OutputBitStream stream) throws IOException {
        stream.writeShort((short)this.m_maxQuantity);
        stream.writeShort((short)this.m_stasisThreshold);
        stream.writeByte((byte)this.m_monsterFamilies.length);
        for (int i = 0; i < this.m_monsterFamilies.length; ++i) {
            stream.writeInt(this.m_monsterFamilies[i].m_id);
            stream.writeInt(this.m_monsterFamilies[i].m_minQuantity);
            stream.writeInt(this.m_monsterFamilies[i].m_maxQuantity);
        }
    }
    
    public Element writeXml() {
        final Element node = new Element("monsterBalancing");
        node.setAttribute("max_quantity", String.valueOf(this.m_maxQuantity));
        node.setAttribute("stasis_threshold", String.valueOf(this.m_stasisThreshold));
        for (int i = 0; i < this.m_monsterFamilies.length; ++i) {
            final Element elt = this.m_monsterFamilies[i].writeXml("monsterFamily");
            node.addContent(elt);
        }
        return node;
    }
    
    private void checkMaxQuantity() {
        int quantity = 0;
        for (final DataBalancing data : this.m_monsterFamilies) {
            quantity += data.m_maxQuantity;
        }
        if (quantity > this.m_maxQuantity) {
            throw new BuildException("le nombre max de monstres d\u00e9passe la quantit\u00e9 max");
        }
        if (this.m_stasisThreshold > this.m_maxQuantity) {
            throw new BuildException("le seuil est sup\u00e9rieur \u00e0 la quantit\u00e9 max de monstres");
        }
    }
}
