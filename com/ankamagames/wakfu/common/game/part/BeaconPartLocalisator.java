package com.ankamagames.wakfu.common.game.part;

import org.apache.log4j.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;

public class BeaconPartLocalisator implements PartLocalisator<BeaconPart>
{
    protected static final Logger m_logger;
    private static int PRECISION;
    private static int PI;
    private static int ANGLE_BACKRIGHT;
    private static int ANGLE_FRONTRIGHT;
    private static int ANGLE_FRONTLEFT;
    private static int ANGLE_BACKLEFT;
    private int m_posx;
    private int m_posy;
    private short m_posz;
    private Direction8 m_direction;
    private static final TIntObjectHashMap<BeaconPart> m_parts;
    
    @Override
    public BeaconPart getPartFromId(final int id) {
        return BeaconPartLocalisator.m_parts.get(id);
    }
    
    public void update(final int x, final int y, final short z, final Direction8 currentOrientation) {
        this.m_posx = x;
        this.m_posy = y;
        this.m_posz = z;
        this.m_direction = currentOrientation;
    }
    
    @Override
    public List<BeaconPart> getPartsInSightFromPoint(final int x, final int y, final short z) {
        final List<BeaconPart> list = new Vector<BeaconPart>();
        list.add(this.getMainPartInSightFromPosition(x, y, z));
        return list;
    }
    
    @Override
    public BeaconPart getMainPartInSightFromPosition(final int x, final int y, final short z) {
        if (this.m_direction == null) {
            BeaconPartLocalisator.m_logger.error((Object)"direction ou position null : update partLocalisator first");
            return null;
        }
        if (this.m_posx == x && this.m_posy == y && this.m_posz == z) {
            return BeaconPartLocalisator.m_parts.get(0);
        }
        Vector3 vDir1 = new Vector3(this.m_direction.m_x, this.m_direction.m_y, 0.0f);
        vDir1 = vDir1.normalize();
        Vector3 vDir2 = new Vector3(this.m_posx - x, this.m_posy - y, 0.0f);
        vDir2 = vDir2.normalize();
        final double dotRestult = vDir2.dot(vDir1);
        int angle = (int)(Math.acos(dotRestult) * BeaconPartLocalisator.PRECISION);
        final double detResult = vDir2.det(vDir1);
        if (detResult < 0.0) {
            angle = 2 * BeaconPartLocalisator.PI - angle;
        }
        if (angle >= 0 && angle <= BeaconPartLocalisator.ANGLE_BACKRIGHT) {
            return BeaconPartLocalisator.m_parts.get(2);
        }
        if (angle > BeaconPartLocalisator.ANGLE_BACKRIGHT && angle < BeaconPartLocalisator.ANGLE_FRONTRIGHT) {
            return BeaconPartLocalisator.m_parts.get(3);
        }
        if (angle >= BeaconPartLocalisator.ANGLE_FRONTRIGHT && angle <= BeaconPartLocalisator.ANGLE_FRONTLEFT) {
            return BeaconPartLocalisator.m_parts.get(0);
        }
        if (angle > BeaconPartLocalisator.ANGLE_FRONTLEFT && angle < BeaconPartLocalisator.ANGLE_BACKLEFT) {
            return BeaconPartLocalisator.m_parts.get(1);
        }
        if (angle >= BeaconPartLocalisator.ANGLE_BACKLEFT) {
            return BeaconPartLocalisator.m_parts.get(2);
        }
        BeaconPartLocalisator.m_logger.warn((Object)("angle non trait\u00e9 " + angle));
        return BeaconPartLocalisator.m_parts.get(0);
    }
    
    @Override
    public BeaconPart getMainPartInSightFromVector(Vector3 vector) {
        if (this.m_direction == null) {
            BeaconPartLocalisator.m_logger.error((Object)"direction null : update partLocalisator first");
            return null;
        }
        if (vector.getX() == 0.0f && vector.getY() == 0.0f) {
            return BeaconPartLocalisator.m_parts.get(0);
        }
        final Vector3 vDir1 = new Vector3(this.m_direction.m_x, this.m_direction.m_y, 0.0f);
        vector = vector.normalize();
        final double result = vector.dot(vDir1);
        if (result >= 0.31) {
            return BeaconPartLocalisator.m_parts.get(2);
        }
        if (result < -0.31) {
            return BeaconPartLocalisator.m_parts.get(0);
        }
        final double detResult = vector.det(vDir1);
        if (detResult < 0.0) {
            return BeaconPartLocalisator.m_parts.get(3);
        }
        return BeaconPartLocalisator.m_parts.get(1);
    }
    
    public static BeaconPart getOppositePart(final BeaconPart part) {
        switch (part.getPartId()) {
            case 2: {
                return BeaconPartLocalisator.m_parts.get(0);
            }
            case 0: {
                return BeaconPartLocalisator.m_parts.get(2);
            }
            case 3: {
                return BeaconPartLocalisator.m_parts.get(1);
            }
            case 1: {
                return BeaconPartLocalisator.m_parts.get(3);
            }
            default: {
                return null;
            }
        }
    }
    
    @Override
    public void reset() {
        this.m_posx = 0;
        this.m_posy = 0;
        this.m_posz = 0;
        this.m_direction = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BeaconPartLocalisator.class);
        BeaconPartLocalisator.PRECISION = 10000;
        BeaconPartLocalisator.PI = (int)(3.141592653589793 * BeaconPartLocalisator.PRECISION);
        BeaconPartLocalisator.ANGLE_BACKRIGHT = (int)(0.7853981633974483 * BeaconPartLocalisator.PRECISION);
        BeaconPartLocalisator.ANGLE_FRONTRIGHT = (int)(2.356194490192345 * BeaconPartLocalisator.PRECISION);
        BeaconPartLocalisator.ANGLE_FRONTLEFT = (int)(3.9269908169872414 * BeaconPartLocalisator.PRECISION);
        BeaconPartLocalisator.ANGLE_BACKLEFT = (int)(5.497787143782138 * BeaconPartLocalisator.PRECISION);
        (m_parts = new TIntObjectHashMap<BeaconPart>()).put(0, new BeaconPart(0));
        BeaconPartLocalisator.m_parts.put(1, new BeaconPart(1));
        BeaconPartLocalisator.m_parts.put(2, new BeaconPart(2));
        BeaconPartLocalisator.m_parts.put(3, new BeaconPart(3));
    }
}
