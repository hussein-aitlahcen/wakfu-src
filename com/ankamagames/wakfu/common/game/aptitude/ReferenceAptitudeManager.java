package com.ankamagames.wakfu.common.game.aptitude;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import java.util.*;

public final class ReferenceAptitudeManager
{
    private static final Logger m_logger;
    private static final ReferenceAptitudeManager m_instance;
    private final TShortObjectHashMap<ReferenceAptitude> m_aptitudesById;
    private final EnumMap<AvatarBreed, ArrayList<ReferenceAptitude>> m_aptitudesByBreed;
    
    public static ReferenceAptitudeManager getInstance() {
        return ReferenceAptitudeManager.m_instance;
    }
    
    private ReferenceAptitudeManager() {
        super();
        this.m_aptitudesById = new TShortObjectHashMap<ReferenceAptitude>();
        this.m_aptitudesByBreed = new EnumMap<AvatarBreed, ArrayList<ReferenceAptitude>>(AvatarBreed.class);
        for (final AvatarBreed breed : AvatarBreed.values()) {
            if (breed != AvatarBreed.NONE && breed != AvatarBreed.COMMON) {
                this.m_aptitudesByBreed.put(breed, new ArrayList<ReferenceAptitude>(48));
            }
        }
    }
    
    public void registerReferenceAptitude(final ReferenceAptitude aptitude) {
        this.m_aptitudesById.put(aptitude.getReferenceId(), aptitude);
        for (final Map.Entry<AvatarBreed, ArrayList<ReferenceAptitude>> entry : this.m_aptitudesByBreed.entrySet()) {
            if (aptitude.hasBreed(entry.getKey())) {
                entry.getValue().add(aptitude);
            }
        }
    }
    
    public ArrayList<ReferenceAptitude> getAllAptitudesForBreed(final AvatarBreed breed) {
        return this.m_aptitudesByBreed.get(breed);
    }
    
    public ReferenceAptitude getFromReferenceId(final short referenceId) {
        return this.m_aptitudesById.get(referenceId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ReferenceAptitudeManager.class);
        m_instance = new ReferenceAptitudeManager();
    }
}
