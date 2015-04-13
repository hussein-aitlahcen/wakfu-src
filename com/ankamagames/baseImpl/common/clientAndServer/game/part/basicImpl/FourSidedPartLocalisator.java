package com.ankamagames.baseImpl.common.clientAndServer.game.part.basicImpl;

import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public class FourSidedPartLocalisator implements PartLocalisator<CharacterPart>
{
    protected static final Logger m_logger;
    private int m_posx;
    private int m_posy;
    private short m_posz;
    private Direction8 m_direction;
    private static final TIntObjectHashMap<CharacterPart> m_parts;
    
    @Override
    public CharacterPart getPartFromId(final int id) {
        return FourSidedPartLocalisator.m_parts.get(id);
    }
    
    public void update(final int x, final int y, final short z, final Direction8 currentOrientation) {
        this.m_posx = x;
        this.m_posy = y;
        this.m_posz = z;
        this.m_direction = currentOrientation;
    }
    
    @Override
    public List<CharacterPart> getPartsInSightFromPoint(final int x, final int y, final short z) {
        final List<CharacterPart> list = new Vector<CharacterPart>();
        list.add(this.getMainPartInSightFromPosition(x, y, z));
        return list;
    }
    
    @Override
    public CharacterPart getMainPartInSightFromPosition(final int x, final int y, final short z) {
        if (this.m_direction == null) {
            FourSidedPartLocalisator.m_logger.error((Object)"direction ou position null : update partLocalisator first");
            return null;
        }
        if (this.m_posx == x && this.m_posy == y && this.m_posz == z) {
            return FourSidedPartLocalisator.m_parts.get(0);
        }
        final Vector3 vDir1 = new Vector3(this.m_direction.m_x, this.m_direction.m_y, 0.0f);
        Vector3 vDir2 = new Vector3(this.m_posx - x, this.m_posy - y, 0.0f);
        vDir2 = vDir2.normalize();
        final double result = vDir2.dot(vDir1);
        if (result >= 0.5) {
            return FourSidedPartLocalisator.m_parts.get(2);
        }
        if (result >= -0.5) {
            return FourSidedPartLocalisator.m_parts.get(3);
        }
        return FourSidedPartLocalisator.m_parts.get(0);
    }
    
    @Override
    public CharacterPart getMainPartInSightFromVector(Vector3 vector) {
        if (this.m_direction == null) {
            FourSidedPartLocalisator.m_logger.error((Object)"direction null : update partLocalisator first");
            return null;
        }
        if (vector.getX() == 0.0f && vector.getY() == 0.0f) {
            return FourSidedPartLocalisator.m_parts.get(0);
        }
        final Vector3 vDir1 = new Vector3(this.m_direction.m_x, this.m_direction.m_y, 0.0f);
        vector = vector.normalize();
        final double result = vector.dot(vDir1);
        if (result >= 0.5) {
            return FourSidedPartLocalisator.m_parts.get(2);
        }
        if (result >= -0.5) {
            return FourSidedPartLocalisator.m_parts.get(3);
        }
        return FourSidedPartLocalisator.m_parts.get(0);
    }
    
    @Override
    public void reset() {
        this.m_posx = 0;
        this.m_posy = 0;
        this.m_posz = 0;
        this.m_direction = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FourSidedPartLocalisator.class);
        (m_parts = new TIntObjectHashMap<CharacterPart>()).put(0, new CharacterPart(0));
        FourSidedPartLocalisator.m_parts.put(1, new CharacterPart(1));
        FourSidedPartLocalisator.m_parts.put(2, new CharacterPart(2));
        FourSidedPartLocalisator.m_parts.put(3, new CharacterPart(3));
    }
}
