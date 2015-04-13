package com.ankamagames.baseImpl.common.clientAndServer.game.HMIAction;

import org.apache.log4j.*;

public abstract class HMIAction
{
    private static final Logger m_logger;
    private static int m_nextFreeId;
    private final int m_id;
    private boolean m_targetOnly;
    private boolean m_isInstant;
    
    public HMIAction() {
        super();
        this.m_id = HMIAction.m_nextFreeId++;
        this.m_targetOnly = false;
        this.m_isInstant = false;
    }
    
    protected abstract boolean initialize(final String p0);
    
    public abstract HMIActionType getType();
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isTargetOnly() {
        return this.m_targetOnly;
    }
    
    public void setTargetOnly(final boolean targetOnly) {
        this.m_targetOnly = targetOnly;
    }
    
    public void setInstant(final boolean instant) {
        this.m_isInstant = instant;
    }
    
    public boolean isInstant() {
        return this.m_isInstant;
    }
    
    public static HMIAction fromString(final String hmiAction) {
        final String[] params = hmiAction.split("\\|", -1);
        if (params.length % 2 != 0) {
            HMIAction.m_logger.error((Object)("HMI error : Nombre de param\u00e8tres d\u00e9cod\u00e9s: " + params.length + " Attendu: pair "));
            return null;
        }
        Byte hmiType = 0;
        String hmiData = "";
        boolean hmiBroadcast = false;
        for (int i = 0; i < params.length; i += 2) {
            final String paramName = params[i];
            final String paramValue = params[i + 1];
            if (paramName.equals("type")) {
                hmiType = Byte.parseByte(paramValue);
            }
            else if (paramName.equals("data")) {
                hmiData = paramValue;
            }
            else if (paramName.equals("broadcast")) {
                hmiBroadcast = Boolean.parseBoolean(paramValue);
            }
        }
        HMIActionManager.getInstance();
        return HMIActionManager.createNewAction(HMIActionType.getFromId(hmiType), hmiData, hmiBroadcast);
    }
    
    @Override
    public String toString() {
        return "HMIAction{m_id=" + this.m_id + ", m_targetOnly=" + this.m_targetOnly + ", m_isInstant=" + this.m_isInstant + "} ";
    }
    
    static {
        m_logger = Logger.getLogger((Class)HMIAction.class);
        HMIAction.m_nextFreeId = 1;
    }
}
