package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.actions;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction.*;

public class HMIParticleSystemAction extends HMIAction
{
    private static final Logger m_logger;
    private int m_particleSystemId;
    private boolean m_alwaysActivated;
    private ParticleLocalisation m_localisation;
    
    public HMIParticleSystemAction() {
        super();
        this.m_localisation = ParticleLocalisation.NONE;
    }
    
    @Override
    protected boolean initialize(final String parameters) {
        try {
            if (!parameters.contains(";")) {
                this.m_particleSystemId = Integer.parseInt(parameters);
            }
            else if (!this.extractParams(parameters)) {
                return false;
            }
            return true;
        }
        catch (IllegalArgumentException e) {
            HMIParticleSystemAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + " : " + parameters + " invalid  " + e.getMessage()));
            return false;
        }
    }
    
    private boolean extractParams(final String parameters) {
        final String[] params = StringUtils.split(parameters, ';');
        if (params.length != 2 && params.length != 3) {
            HMIParticleSystemAction.m_logger.error((Object)("Impossible d'initialiser un " + this.getClass().getName() + " nombre de param\u00e8tre invalide: " + parameters + "(attendu: idParticule(int)[;alwaysActivated(boolean)][;localisation(string)])"));
            return false;
        }
        this.m_particleSystemId = Integer.parseInt(params[0]);
        final String param_1 = params[1];
        String localisationParam = null;
        if (params.length == 3) {
            localisationParam = params[2];
        }
        if (param_1.equalsIgnoreCase("true") || param_1.equalsIgnoreCase("false")) {
            this.m_alwaysActivated = Boolean.parseBoolean(param_1);
        }
        else if (params.length == 2) {
            localisationParam = param_1;
        }
        if (localisationParam == null) {
            return true;
        }
        if (localisationParam.equalsIgnoreCase("tete")) {
            this.m_localisation = ParticleLocalisation.HEAD;
        }
        else if (localisationParam.equalsIgnoreCase("corps")) {
            this.m_localisation = ParticleLocalisation.MIDDLE;
        }
        else {
            if (!localisationParam.equalsIgnoreCase("pied")) {
                HMIParticleSystemAction.m_logger.error((Object)("Erreur de parametre, localisation inconnue " + localisationParam + ", " + this.toString()));
                return false;
            }
            this.m_localisation = ParticleLocalisation.FOOT;
        }
        return true;
    }
    
    @Override
    public HMIActionType getType() {
        return HMIActionType.PARTICLE_SYSTEM;
    }
    
    public int getParticleSystemId() {
        return this.m_particleSystemId;
    }
    
    public boolean isAlwaysActivated() {
        return this.m_alwaysActivated;
    }
    
    public ParticleLocalisation getLocalisation() {
        return this.m_localisation;
    }
    
    @Override
    public String toString() {
        return "HMIParticleSystemAction{m_particleSystemId=" + this.m_particleSystemId + ", m_alwaysActivated=" + this.m_alwaysActivated + ", m_localisation=" + this.m_localisation + "} " + super.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIParticleSystemAction.class);
    }
}
