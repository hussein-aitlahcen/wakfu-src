package com.ankamagames.wakfu.client.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class SpellBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_scriptId;
    protected int m_gfxId;
    protected short m_maxLevel;
    protected short m_breedId;
    protected short m_castMaxPerTarget;
    protected float m_castMaxPerTurn;
    protected float m_castMaxPerTurnIncr;
    protected short m_castMinInterval;
    protected boolean m_testLineOfSight;
    protected boolean m_castOnlyLine;
    protected boolean m_castOnlyInDiag;
    protected boolean m_testFreeCell;
    protected boolean m_testNotBorderCell;
    protected boolean m_testDirectPath;
    protected int m_targetFilter;
    protected String m_castCriterion;
    protected float m_PA_base;
    protected float m_PA_inc;
    protected float m_PM_base;
    protected float m_PM_inc;
    protected float m_PW_base;
    protected float m_PW_inc;
    protected float m_rangeMaxBase;
    protected float m_rangeMaxInc;
    protected float m_rangeMinBase;
    protected float m_rangeMinLevelIncrement;
    protected short m_maxEffectCap;
    protected short m_element;
    protected short m_xpGainPercentage;
    protected short m_spellType;
    protected short m_uiPosition;
    protected String m_learnCriteria;
    protected byte m_passive;
    protected boolean m_useAutomaticDescription;
    protected boolean m_showInTimeline;
    protected boolean m_canCastWhenCarrying;
    protected byte m_actionOnCriticalMiss;
    protected boolean m_spellCastRangeIsDynamic;
    protected boolean m_castSpellWillBreakInvisibility;
    protected boolean m_castOnRandomCell;
    protected boolean m_tunnelable;
    protected boolean m_canCastOnCasterCell;
    protected boolean m_associatedWithItemUse;
    protected int[] m_properties;
    protected int[] m_effectIds;
    protected TByteObjectHashMap<CustomCharac> m_baseCastParameters;
    protected THashMap<String, CastParam> m_alternativeCasts;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public short getCastMaxPerTarget() {
        return this.m_castMaxPerTarget;
    }
    
    public float getCastMaxPerTurn() {
        return this.m_castMaxPerTurn;
    }
    
    public float getCastMaxPerTurnIncr() {
        return this.m_castMaxPerTurnIncr;
    }
    
    public short getCastMinInterval() {
        return this.m_castMinInterval;
    }
    
    public boolean isTestLineOfSight() {
        return this.m_testLineOfSight;
    }
    
    public boolean isCastOnlyLine() {
        return this.m_castOnlyLine;
    }
    
    public boolean isCastOnlyInDiag() {
        return this.m_castOnlyInDiag;
    }
    
    public boolean isTestFreeCell() {
        return this.m_testFreeCell;
    }
    
    public boolean isTestNotBorderCell() {
        return this.m_testNotBorderCell;
    }
    
    public boolean isTestDirectPath() {
        return this.m_testDirectPath;
    }
    
    public int getTargetFilter() {
        return this.m_targetFilter;
    }
    
    public String getCastCriterion() {
        return this.m_castCriterion;
    }
    
    public float getPA_base() {
        return this.m_PA_base;
    }
    
    public float getPA_inc() {
        return this.m_PA_inc;
    }
    
    public float getPM_base() {
        return this.m_PM_base;
    }
    
    public float getPM_inc() {
        return this.m_PM_inc;
    }
    
    public float getPW_base() {
        return this.m_PW_base;
    }
    
    public float getPW_inc() {
        return this.m_PW_inc;
    }
    
    public float getRangeMaxBase() {
        return this.m_rangeMaxBase;
    }
    
    public float getRangeMaxInc() {
        return this.m_rangeMaxInc;
    }
    
    public float getRangeMinBase() {
        return this.m_rangeMinBase;
    }
    
    public float getRangeMinLevelIncrement() {
        return this.m_rangeMinLevelIncrement;
    }
    
    public short getMaxEffectCap() {
        return this.m_maxEffectCap;
    }
    
    public short getElement() {
        return this.m_element;
    }
    
    public short getXpGainPercentage() {
        return this.m_xpGainPercentage;
    }
    
    public short getSpellType() {
        return this.m_spellType;
    }
    
    public short getUiPosition() {
        return this.m_uiPosition;
    }
    
    public String getLearnCriteria() {
        return this.m_learnCriteria;
    }
    
    public byte getPassive() {
        return this.m_passive;
    }
    
    public boolean isUseAutomaticDescription() {
        return this.m_useAutomaticDescription;
    }
    
    public boolean isShowInTimeline() {
        return this.m_showInTimeline;
    }
    
    public boolean isCanCastWhenCarrying() {
        return this.m_canCastWhenCarrying;
    }
    
    public byte getActionOnCriticalMiss() {
        return this.m_actionOnCriticalMiss;
    }
    
    public boolean isSpellCastRangeIsDynamic() {
        return this.m_spellCastRangeIsDynamic;
    }
    
    public boolean isCastSpellWillBreakInvisibility() {
        return this.m_castSpellWillBreakInvisibility;
    }
    
    public boolean isCastOnRandomCell() {
        return this.m_castOnRandomCell;
    }
    
    public boolean isTunnelable() {
        return this.m_tunnelable;
    }
    
    public boolean isCanCastOnCasterCell() {
        return this.m_canCastOnCasterCell;
    }
    
    public boolean isAssociatedWithItemUse() {
        return this.m_associatedWithItemUse;
    }
    
    public int[] getProperties() {
        return this.m_properties;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    public TByteObjectHashMap<CustomCharac> getBaseCastParameters() {
        return this.m_baseCastParameters;
    }
    
    public THashMap<String, CastParam> getAlternativeCasts() {
        return this.m_alternativeCasts;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_scriptId = 0;
        this.m_gfxId = 0;
        this.m_maxLevel = 0;
        this.m_breedId = 0;
        this.m_castMaxPerTarget = 0;
        this.m_castMaxPerTurn = 0.0f;
        this.m_castMaxPerTurnIncr = 0.0f;
        this.m_castMinInterval = 0;
        this.m_testLineOfSight = false;
        this.m_castOnlyLine = false;
        this.m_castOnlyInDiag = false;
        this.m_testFreeCell = false;
        this.m_testNotBorderCell = false;
        this.m_testDirectPath = false;
        this.m_targetFilter = 0;
        this.m_castCriterion = null;
        this.m_PA_base = 0.0f;
        this.m_PA_inc = 0.0f;
        this.m_PM_base = 0.0f;
        this.m_PM_inc = 0.0f;
        this.m_PW_base = 0.0f;
        this.m_PW_inc = 0.0f;
        this.m_rangeMaxBase = 0.0f;
        this.m_rangeMaxInc = 0.0f;
        this.m_rangeMinBase = 0.0f;
        this.m_rangeMinLevelIncrement = 0.0f;
        this.m_maxEffectCap = 0;
        this.m_element = 0;
        this.m_xpGainPercentage = 0;
        this.m_spellType = 0;
        this.m_uiPosition = 0;
        this.m_learnCriteria = null;
        this.m_passive = 0;
        this.m_useAutomaticDescription = false;
        this.m_showInTimeline = false;
        this.m_canCastWhenCarrying = false;
        this.m_actionOnCriticalMiss = 0;
        this.m_spellCastRangeIsDynamic = false;
        this.m_castSpellWillBreakInvisibility = false;
        this.m_castOnRandomCell = false;
        this.m_tunnelable = false;
        this.m_canCastOnCasterCell = false;
        this.m_associatedWithItemUse = false;
        this.m_properties = null;
        this.m_effectIds = null;
        this.m_baseCastParameters = null;
        this.m_alternativeCasts = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_scriptId = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_maxLevel = buffer.getShort();
        this.m_breedId = buffer.getShort();
        this.m_castMaxPerTarget = buffer.getShort();
        this.m_castMaxPerTurn = buffer.getFloat();
        this.m_castMaxPerTurnIncr = buffer.getFloat();
        this.m_castMinInterval = buffer.getShort();
        this.m_testLineOfSight = buffer.readBoolean();
        this.m_castOnlyLine = buffer.readBoolean();
        this.m_castOnlyInDiag = buffer.readBoolean();
        this.m_testFreeCell = buffer.readBoolean();
        this.m_testNotBorderCell = buffer.readBoolean();
        this.m_testDirectPath = buffer.readBoolean();
        this.m_targetFilter = buffer.getInt();
        this.m_castCriterion = buffer.readUTF8().intern();
        this.m_PA_base = buffer.getFloat();
        this.m_PA_inc = buffer.getFloat();
        this.m_PM_base = buffer.getFloat();
        this.m_PM_inc = buffer.getFloat();
        this.m_PW_base = buffer.getFloat();
        this.m_PW_inc = buffer.getFloat();
        this.m_rangeMaxBase = buffer.getFloat();
        this.m_rangeMaxInc = buffer.getFloat();
        this.m_rangeMinBase = buffer.getFloat();
        this.m_rangeMinLevelIncrement = buffer.getFloat();
        this.m_maxEffectCap = buffer.getShort();
        this.m_element = buffer.getShort();
        this.m_xpGainPercentage = buffer.getShort();
        this.m_spellType = buffer.getShort();
        this.m_uiPosition = buffer.getShort();
        this.m_learnCriteria = buffer.readUTF8().intern();
        this.m_passive = buffer.get();
        this.m_useAutomaticDescription = buffer.readBoolean();
        this.m_showInTimeline = buffer.readBoolean();
        this.m_canCastWhenCarrying = buffer.readBoolean();
        this.m_actionOnCriticalMiss = buffer.get();
        this.m_spellCastRangeIsDynamic = buffer.readBoolean();
        this.m_castSpellWillBreakInvisibility = buffer.readBoolean();
        this.m_castOnRandomCell = buffer.readBoolean();
        this.m_tunnelable = buffer.readBoolean();
        this.m_canCastOnCasterCell = buffer.readBoolean();
        this.m_associatedWithItemUse = buffer.readBoolean();
        this.m_properties = buffer.readIntArray();
        this.m_effectIds = buffer.readIntArray();
        final int baseCastParameterCount = buffer.getInt();
        this.m_baseCastParameters = new TByteObjectHashMap<CustomCharac>(baseCastParameterCount);
        for (int iBaseCastParameter = 0; iBaseCastParameter < baseCastParameterCount; ++iBaseCastParameter) {
            final byte baseCastParameterKey = buffer.get();
            final CustomCharac baseCastParameterValue = new CustomCharac();
            baseCastParameterValue.read(buffer);
            this.m_baseCastParameters.put(baseCastParameterKey, baseCastParameterValue);
        }
        final int alternativeCastCount = buffer.getInt();
        this.m_alternativeCasts = new THashMap<String, CastParam>(alternativeCastCount);
        for (int iAlternativeCast = 0; iAlternativeCast < alternativeCastCount; ++iAlternativeCast) {
            final String alternativeCastKey = buffer.readUTF8().intern();
            final CastParam alternativeCastValue = new CastParam();
            alternativeCastValue.read(buffer);
            this.m_alternativeCasts.put(alternativeCastKey, alternativeCastValue);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.SPELL.getId();
    }
    
    public static class CustomCharac
    {
        protected int m_base;
        protected float m_increment;
        
        public int getBase() {
            return this.m_base;
        }
        
        public float getIncrement() {
            return this.m_increment;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_base = buffer.getInt();
            this.m_increment = buffer.getFloat();
        }
    }
    
    public static class CastParam
    {
        protected TByteObjectHashMap<CustomCharac> m_costs;
        protected int m_cost;
        protected float m_PA_base;
        protected float m_PA_inc;
        protected float m_PM_base;
        protected float m_PM_inc;
        protected float m_PW_base;
        protected float m_PW_inc;
        protected float m_rangeMinBase;
        protected float m_rangeMinInc;
        protected float m_rangeMaxBase;
        protected float m_rangeMaxInc;
        protected boolean m_isLosAware;
        protected boolean m_onlyInLine;
        protected boolean m_rangeIsDynamic;
        
        public TByteObjectHashMap<CustomCharac> getCosts() {
            return this.m_costs;
        }
        
        public int getCost() {
            return this.m_cost;
        }
        
        public float getPA_base() {
            return this.m_PA_base;
        }
        
        public float getPA_inc() {
            return this.m_PA_inc;
        }
        
        public float getPM_base() {
            return this.m_PM_base;
        }
        
        public float getPM_inc() {
            return this.m_PM_inc;
        }
        
        public float getPW_base() {
            return this.m_PW_base;
        }
        
        public float getPW_inc() {
            return this.m_PW_inc;
        }
        
        public float getRangeMinBase() {
            return this.m_rangeMinBase;
        }
        
        public float getRangeMinInc() {
            return this.m_rangeMinInc;
        }
        
        public float getRangeMaxBase() {
            return this.m_rangeMaxBase;
        }
        
        public float getRangeMaxInc() {
            return this.m_rangeMaxInc;
        }
        
        public boolean isLosAware() {
            return this.m_isLosAware;
        }
        
        public boolean isOnlyInLine() {
            return this.m_onlyInLine;
        }
        
        public boolean isRangeIsDynamic() {
            return this.m_rangeIsDynamic;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            final int costCount = buffer.getInt();
            this.m_costs = new TByteObjectHashMap<CustomCharac>(costCount);
            for (int iCost = 0; iCost < costCount; ++iCost) {
                final byte costKey = buffer.get();
                final CustomCharac costValue = new CustomCharac();
                costValue.read(buffer);
                this.m_costs.put(costKey, costValue);
            }
            this.m_cost = buffer.getInt();
            this.m_PA_base = buffer.getFloat();
            this.m_PA_inc = buffer.getFloat();
            this.m_PM_base = buffer.getFloat();
            this.m_PM_inc = buffer.getFloat();
            this.m_PW_base = buffer.getFloat();
            this.m_PW_inc = buffer.getFloat();
            this.m_rangeMinBase = buffer.getFloat();
            this.m_rangeMinInc = buffer.getFloat();
            this.m_rangeMaxBase = buffer.getFloat();
            this.m_rangeMaxInc = buffer.getFloat();
            this.m_isLosAware = buffer.readBoolean();
            this.m_onlyInLine = buffer.readBoolean();
            this.m_rangeIsDynamic = buffer.readBoolean();
        }
    }
}
