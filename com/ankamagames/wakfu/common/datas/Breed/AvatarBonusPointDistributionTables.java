package com.ankamagames.wakfu.common.datas.Breed;

import gnu.trove.*;
import java.util.*;

public class AvatarBonusPointDistributionTables
{
    private static AvatarBonusPointDistributionTables m_instance;
    private final TIntObjectHashMap<TIntObjectHashMap<List<Step>>> m_stepsForAllCharacteristicsBreeds;
    
    public AvatarBonusPointDistributionTables() {
        super();
        this.m_stepsForAllCharacteristicsBreeds = new TIntObjectHashMap<TIntObjectHashMap<List<Step>>>();
    }
    
    public static AvatarBonusPointDistributionTables getInstance() {
        return AvatarBonusPointDistributionTables.m_instance;
    }
    
    public void addStep(final int breedId, final int characId, final short lowerStepLimit, final short characteristicPointsGain, final short bonusPointsNeeded) {
        TIntObjectHashMap<List<Step>> characMap = this.m_stepsForAllCharacteristicsBreeds.get(breedId);
        if (characMap == null) {
            characMap = new TIntObjectHashMap<List<Step>>();
            this.m_stepsForAllCharacteristicsBreeds.put(breedId, characMap);
        }
        List<Step> list = characMap.get(characId);
        if (list == null) {
            list = new ArrayList<Step>();
            characMap.put(characId, list);
        }
        final Iterator<Step> stepIterator = list.iterator();
        int i = 0;
        while (stepIterator.hasNext()) {
            final Step step = stepIterator.next();
            if (step.getLowerStepLimit() == lowerStepLimit) {
                throw new UnsupportedOperationException("Impossible d'ajouter deux fois un step de m\u00eame limite");
            }
            if (step.getLowerStepLimit() > lowerStepLimit) {
                break;
            }
            ++i;
        }
        list.add(i, new Step(lowerStepLimit, characteristicPointsGain, bonusPointsNeeded));
    }
    
    public short getBonusPointNeededToIncrementCharacteristic(final int breedId, final int characteristicId, final short currentCharacteristicValue) {
        final Step step = this.getStep(breedId, characteristicId, currentCharacteristicValue);
        if (step != null) {
            return step.getBonusPointsNeeded();
        }
        return 32767;
    }
    
    public short getCharacteristicPointGainWhenUpgrade(final int breedId, final int characteristicId, final short currentCharacteristicValue) {
        final Step step = this.getStep(breedId, characteristicId, currentCharacteristicValue);
        if (step != null) {
            return step.getCharacteristicPointsGain();
        }
        return 0;
    }
    
    private Step getStep(final int breedId, final int characteristicId, final short currentCharacteristicValue) {
        Step step = null;
        final TIntObjectHashMap<List<Step>> characmap = this.m_stepsForAllCharacteristicsBreeds.get(breedId);
        if (characmap != null) {
            final List<Step> list = characmap.get(characteristicId);
            if (list != null) {
                for (final Step currentStep : list) {
                    if (currentStep.getLowerStepLimit() > currentCharacteristicValue) {
                        return step;
                    }
                    step = currentStep;
                }
            }
        }
        return step;
    }
    
    public void forceclear() {
        this.m_stepsForAllCharacteristicsBreeds.clear();
    }
    
    static {
        AvatarBonusPointDistributionTables.m_instance = new AvatarBonusPointDistributionTables();
    }
    
    static class Step
    {
        private short m_lowerStepLimit;
        private short m_characteristicPointsGain;
        private short m_bonusPointsNeeded;
        
        Step() {
            super();
        }
        
        Step(final short lowerStepLimit, final short characteristicPointsGain, final short bonusPointsNeeded) {
            super();
            this.m_lowerStepLimit = lowerStepLimit;
            this.m_characteristicPointsGain = characteristicPointsGain;
            this.m_bonusPointsNeeded = bonusPointsNeeded;
        }
        
        public short getLowerStepLimit() {
            return this.m_lowerStepLimit;
        }
        
        public short getCharacteristicPointsGain() {
            return this.m_characteristicPointsGain;
        }
        
        public short getBonusPointsNeeded() {
            return this.m_bonusPointsNeeded;
        }
    }
}
