package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;

public class ProtectorStaticData
{
    private int m_id;
    private final Point3 m_position;
    private final ArrayList<ProtectorSecret> m_secrets;
    private int[] m_craft;
    
    public ProtectorStaticData(final int id) {
        super();
        this.m_position = new Point3();
        this.m_secrets = new ArrayList<ProtectorSecret>();
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setPosition(final int x, final int y, final short z) {
        this.m_position.set(x, y, z);
    }
    
    public Point3 getPositionConst() {
        return this.m_position;
    }
    
    public void addSecret(final ProtectorSecret secret) {
        this.m_secrets.add(secret);
    }
    
    public ArrayList<ProtectorSecret> getSecrets() {
        return this.m_secrets;
    }
    
    public int[] getCraft() {
        return this.m_craft;
    }
    
    public void setCraft(final int[] craft) {
        this.m_craft = craft;
    }
}
