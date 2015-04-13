package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class RunningEffectGroupLevelFunctionCharacteristic extends RunningEffectGroup
{
    public static final int CURRENT_VALUE = 0;
    public static final int MAX_VALUE = 1;
    public static final int PERCENT_VALUE = 2;
    public static final int MISSING_PERCENT_VALUE = 3;
    public static final int MISSING_VALUE = 4;
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET_FUNCTION_CHARAC;
    private FighterCharacteristicType m_characType;
    protected boolean m_checkOnCaster;
    protected boolean m_basedOnStep;
    protected int m_characStep;
    protected float m_levelBase;
    protected float m_levelIncrement;
    protected int m_valueToUse;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return RunningEffectGroupLevelFunctionCharacteristic.PARAMETERS_LIST_SET_FUNCTION_CHARAC;
    }
    
    public RunningEffectGroupLevelFunctionCharacteristic() {
        super();
        this.m_checkOnCaster = false;
        this.m_basedOnStep = false;
        this.m_valueToUse = 0;
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupLevelFunctionCharacteristic newInstance() {
        RunningEffectGroupLevelFunctionCharacteristic re;
        try {
            re = (RunningEffectGroupLevelFunctionCharacteristic)RunningEffectGroupLevelFunctionCharacteristic.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupLevelFunctionCharacteristic.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupLevelFunctionCharacteristic();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupLevelFunctionCharacteristic.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupLevelFunctionCharacteristic : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        super.effectiveComputeValue(triggerRE);
        final int characId = ((WakfuEffect)this.m_genericEffect).getParam(6, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_characType = FighterCharacteristicType.getCharacteristicTypeFromId((byte)characId);
        this.m_valueToUse = 0;
        if (this.m_characType == null) {
            RunningEffectGroupLevelFunctionCharacteristic.m_logger.error((Object)("Unable to get characteristic with id " + characId));
        }
        this.m_checkOnCaster = (((WakfuEffect)this.m_genericEffect).getParam(7, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 9) {
            this.m_basedOnStep = true;
            this.m_characStep = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else {
            this.m_basedOnStep = false;
            this.m_levelBase = ((WakfuEffect)this.m_genericEffect).getParam(8, this.getContainerLevel());
            this.m_levelIncrement = ((WakfuEffect)this.m_genericEffect).getParam(9, this.getContainerLevel());
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 11) {
                this.m_valueToUse = ((WakfuEffect)this.m_genericEffect).getParam(10, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            }
        }
    }
    
    @Override
    protected WakfuEffectExecutionParameters getExecutionParameters(final WakfuRunningEffect linkedRE, final boolean disableProbabilityComputation) {
        final WakfuEffectExecutionParameters params = super.getExecutionParameters(linkedRE, disableProbabilityComputation);
        if (this.m_characType == null) {
            return params;
        }
        EffectUser characOwner;
        if (this.m_checkOnCaster) {
            characOwner = this.m_caster;
        }
        else {
            characOwner = this.m_target;
        }
        if (characOwner == null) {
            return params;
        }
        final AbstractCharacteristic characteristic = characOwner.getCharacteristic(this.m_characType);
        int characValue;
        if (characteristic != null) {
            if (this.m_valueToUse == 1) {
                characValue = characteristic.max();
            }
            else if (this.m_valueToUse == 2) {
                characValue = characteristic.value() * 100 / characteristic.max();
            }
            else if (this.m_valueToUse == 3) {
                characValue = (characteristic.max() - characteristic.value()) * 100 / characteristic.max();
            }
            else if (this.m_valueToUse == 4) {
                characValue = characteristic.max() - characteristic.value();
            }
            else {
                characValue = characteristic.value();
            }
        }
        else {
            characValue = 0;
        }
        int forcedLevel;
        if (this.m_basedOnStep) {
            if (this.m_characStep == 0) {
                forcedLevel = characValue;
            }
            else {
                forcedLevel = (int)Math.floor(characValue / this.m_characStep);
            }
        }
        else {
            forcedLevel = Math.round(this.m_levelBase + this.m_levelIncrement * characValue);
        }
        params.setForcedLevel(forcedLevel);
        return params;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_characType = null;
        this.m_valueToUse = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupLevelFunctionCharacteristic>() {
            @Override
            public RunningEffectGroupLevelFunctionCharacteristic makeObject() {
                return new RunningEffectGroupLevelFunctionCharacteristic();
            }
        });
        PARAMETERS_LIST_SET_FUNCTION_CHARAC = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Niveau des effets du groupe bas\u00e9 sur une caract\u00e9ristique : la carac sert de 'faux niveau'", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de a Charact\u00e9ristique \u00e0 regarder", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac prise sur Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau : Base", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau : Incr\u00e9ment par point de charac", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Niveau des effets du groupe bas\u00e9 sur une caract\u00e9ristique : niveau par paliers de la carac (+1 tous les x carac)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de a Charact\u00e9ristique \u00e0 regarder", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac prise sur Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Palier de Charac pour avoir +1 niveau", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Niveau des effets du groupe bas\u00e9 sur une caract\u00e9ristique (ou son max) : la carac sert de 'faux niveau'", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("nb maximum d'effect \u00e0 executer", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("avec r\u00e9ussite (1 : oui, 0 : non = standard", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("0 = cellule, 1 = cibles (default)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("proba relative = 1 (on regarde les effets reellement executable et on recalcule leurs proba entre eux) ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Transmission de la cible originale aux effets du groupe : oui = 1 (d\u00e9faut), non = 0 ", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Change le caster du groupe d'effet par sa cible oui = 1, non = 0 (d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Id de a Charact\u00e9ristique \u00e0 regarder", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Charac prise sur Target = 0 (defaut), Caster = 1", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Niveau : Base", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau : Incr\u00e9ment par point de charac", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Valeur \u00e0 utiliser (default: 0=valeur courante, 1=maximum, 2=valeur courante en %, 3=valeur manquante en %), 4=valeur manquante )", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
