package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.java.util.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import java.util.*;

public class HMICostumeAction extends HMIAction
{
    private static final Pattern PATTERN;
    private static final String[] EMPTY_STRINGS;
    private static final Logger m_logger;
    private int m_weight;
    private int m_particleId;
    private ArrayList<Appearance> m_appearances;
    private boolean m_hideNotEquiped;
    
    public HMICostumeAction() {
        super();
        this.m_appearances = new ArrayList<Appearance>();
    }
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            final Matcher matcher = HMICostumeAction.PATTERN.matcher(parameters);
            while (matcher.find()) {
                final String key = matcher.group(1);
                final String value = matcher.group(2);
                if ("weight".equalsIgnoreCase(key)) {
                    this.m_weight = PrimitiveConverter.getInteger(value);
                }
                else if ("particleid".equalsIgnoreCase(key)) {
                    this.m_particleId = PrimitiveConverter.getInteger(value);
                }
                else {
                    if (!"appearance".equalsIgnoreCase(key)) {
                        continue;
                    }
                    final String[] appearanceParams = value.split(",");
                    if (appearanceParams.length == 0) {
                        continue;
                    }
                    if (appearanceParams.length == 1) {
                        this.m_appearances.add(new Appearance(appearanceParams[0], null));
                    }
                    else {
                        final String[] parts = new String[appearanceParams.length - 1];
                        System.arraycopy(appearanceParams, 1, parts, 0, parts.length);
                        this.m_appearances.add(new Appearance(appearanceParams[0], parts));
                    }
                }
            }
            return true;
        }
        catch (NumberFormatException e) {
            HMICostumeAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + ", mauvaise saisi des param\u00e8tres  : " + parameters));
            return false;
        }
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.COSTUME;
    }
    
    public ArrayList<Appearance> getAppearances() {
        return this.m_appearances;
    }
    
    public int getWeight() {
        return this.m_weight;
    }
    
    public int getParticleId() {
        return this.m_particleId;
    }
    
    static {
        PATTERN = Pattern.compile("([a-zA-Z]+)=([^;]+);?");
        EMPTY_STRINGS = new String[0];
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
    
    public static class Appearance
    {
        private String m_appearanceId;
        private String[] m_partsToChange;
        
        public Appearance(final String appearanceId, final String[] partsToChange) {
            super();
            this.m_appearanceId = appearanceId;
            this.m_partsToChange = this.getCleanParts(partsToChange);
        }
        
        private String[] getCleanParts(final String[] parts) {
            if (parts == null) {
                return HMICostumeAction.EMPTY_STRINGS;
            }
            final int partCount = parts.length;
            final ArrayList<String> listParts = new ArrayList<String>(partCount);
            for (int i = 0; i < partCount; ++i) {
                for (final String key : AnmPartHelper.getParts(parts[i])) {
                    if (!listParts.contains(key)) {
                        listParts.add(key);
                    }
                }
            }
            return listParts.toArray(new String[listParts.size()]);
        }
        
        public String getAppearanceId() {
            return this.m_appearanceId;
        }
        
        public String[] getPartsToChange() {
            return this.m_partsToChange;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Appearance that = (Appearance)o;
            return this.m_appearanceId.equals(that.m_appearanceId) && Arrays.equals(this.m_partsToChange, that.m_partsToChange);
        }
        
        @Override
        public int hashCode() {
            int result = this.m_appearanceId.hashCode();
            result = 31 * result + Arrays.hashCode(this.m_partsToChange);
            return result;
        }
    }
}
