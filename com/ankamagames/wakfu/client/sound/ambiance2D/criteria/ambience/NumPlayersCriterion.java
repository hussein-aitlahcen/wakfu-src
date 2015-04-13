package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.ambience;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public class NumPlayersCriterion extends SimpleContainerCriterion
{
    public static final byte CRITERION_ID = 7;
    private int m_minNumPlayers;
    private int m_maxNumPlayers;
    
    public int getMinNumPlayers() {
        return this.m_minNumPlayers;
    }
    
    public void setMinNumPlayers(final int minNumPlayers) {
        this.m_minNumPlayers = minNumPlayers;
    }
    
    public int getMaxNumPlayers() {
        return this.m_maxNumPlayers;
    }
    
    public void setMaxNumPlayers(final int maxNumPlayers) {
        this.m_maxNumPlayers = maxNumPlayers;
    }
    
    public boolean _isValid() {
        final int numPlayers = ContainerCriterionParameterManager.getInstance().getNumPlayers();
        return this.m_minNumPlayers <= numPlayers && this.m_maxNumPlayers >= numPlayers;
    }
    
    @Override
    public byte getCriterionId() {
        return 7;
    }
    
    public void _load(final ExtendedDataInputStream is) {
        this.m_minNumPlayers = is.readInt();
        this.m_maxNumPlayers = is.readInt();
    }
    
    public void _save(final OutputBitStream os) throws IOException {
        os.writeInt(this.m_minNumPlayers);
        os.writeInt(this.m_maxNumPlayers);
    }
    
    public String _toString() {
        return "Nombre de joueurs alentours - [" + this.m_minNumPlayers + " - " + this.m_maxNumPlayers + "]";
    }
    
    @Override
    public ContainerCriterion clone() {
        final NumPlayersCriterion sc = new NumPlayersCriterion();
        sc.setMinNumPlayers(this.m_minNumPlayers);
        sc.setMaxNumPlayers(this.m_maxNumPlayers);
        sc.setNegated(this.isNegated());
        return sc;
    }
}
