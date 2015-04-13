package com.ankamagames.wakfu.client.core.effectArea;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;
import java.util.*;
import java.lang.reflect.*;

final class EffectAreaFactory
{
    private static final Logger m_logger;
    public static final EffectAreaFactory INSTANCE;
    private final THashMap<String, Factory> m_factories;
    
    private EffectAreaFactory() {
        super();
        this.m_factories = new THashMap<String, Factory>();
        this.initializeFactories();
    }
    
    private void initializeFactories() {
        try {
            this.m_factories.put(EffectAreaType.TRAP.getEnumLabel(), new Factory(TrapEffectArea.class));
            this.m_factories.put("OTHER", new Factory(TrapEffectArea.class));
            this.m_factories.put(EffectAreaType.HOUR.getEnumLabel(), new Factory(HourEffectArea.class));
            this.m_factories.put(EffectAreaType.BEACON.getEnumLabel(), new Factory(BeaconEffectArea.class));
            this.m_factories.put(EffectAreaType.AURA.getEnumLabel(), new Factory(AuraEffectArea.class));
            this.m_factories.put(EffectAreaType.GLYPH.getEnumLabel(), new Factory(GlyphEffectArea.class));
            this.m_factories.put(EffectAreaType.WALL.getEnumLabel(), new Factory(WallEffectArea.class));
            this.m_factories.put(EffectAreaType.BATTLEGROUND_BORDER.getEnumLabel(), new Factory(BattlegroundBorderEffectArea.class));
            this.m_factories.put(EffectAreaType.WARP.getEnumLabel(), new Factory(WarpEffectArea.class));
            this.m_factories.put(EffectAreaType.BOMB.getEnumLabel(), new Factory(BombEffectArea.class));
            this.m_factories.put(EffectAreaType.BARREL.getEnumLabel(), new Factory(BarrelEffectArea.class));
            this.m_factories.put(EffectAreaType.SPELL_TUNNEL.getEnumLabel(), new Factory(ChangeSpellTargetCellArea.class));
            this.m_factories.put(EffectAreaType.SIMPLE.getEnumLabel(), new Factory(SimpleEffectArea.class));
            this.m_factories.put(EffectAreaType.SPELL_TUNNEL_MARKER.getEnumLabel(), new Factory(SimpleEffectArea.class) {
                @Override
                public AbstractEffectArea create(final AreaEffectBinaryData bs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                    final SimpleEffectArea area = (SimpleEffectArea)super.create(bs);
                    area.setTypeId(EffectAreaType.SPELL_TUNNEL_MARKER.getTypeId());
                    return area;
                }
            });
            this.m_factories.put(EffectAreaType.ENUTROF_DEPOSIT.getEnumLabel(), new Factory(DepositEffectArea.class));
            this.m_factories.put(EffectAreaType.LOOT_AREA.getEnumLabel(), new Factory(LootEffectArea.class));
            this.m_factories.put(EffectAreaType.SIMPLE_WITH_REM.getEnumLabel(), new Factory(WithREMArea.class));
            this.m_factories.put(EffectAreaType.FAKE_FIGHTER.getEnumLabel(), new Factory(FakeFighterEffectArea.class));
            this.m_factories.put(EffectAreaType.FECA_GLYPH.getEnumLabel(), new Factory(FecaGlyphEffectArea.class));
            this.m_factories.put(EffectAreaType.BOMB_COMBO.getEnumLabel(), new Factory(BombComboEffectArea.class));
            this.m_factories.put(EffectAreaType.GATE.getEnumLabel(), new Factory(GateEffectAreaEffectArea.class));
        }
        catch (NoSuchMethodException e) {
            EffectAreaFactory.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    AbstractEffectArea createEffectArea(final AreaEffectBinaryData bs) {
        final Factory factory = this.m_factories.get(bs.getType());
        if (factory == null) {
            EffectAreaFactory.m_logger.error((Object)("Type d'effet inconnu : " + bs.getType()));
            return null;
        }
        try {
            final AbstractEffectArea effectArea = factory.create(bs);
            effectArea.setDestructionTriggers(toBitSet(bs.getDestructionTriggers()));
            effectArea.setShouldStopMover(bs.isShouldStopMovement());
            effectArea.addProperties(bs.getProperties());
            return effectArea;
        }
        catch (Exception e) {
            EffectAreaFactory.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    private static AreaOfEffect getAOE(final AreaEffectBinaryData bs) {
        AreaOfEffect aoe = null;
        try {
            aoe = AreaOfEffectEnum.newInstance(bs.getAreaAreaId(), bs.getAreaAreaParams(), (short)0);
            if (aoe == null) {
                throw new NullPointerException("AOE incorrecte");
            }
        }
        catch (IllegalArgumentException e) {
            EffectAreaFactory.m_logger.error((Object)"erreur au chargement des effets statiques : zone d'effet incorrecte");
        }
        return aoe;
    }
    
    private static BitSet toBitSet(final int[] triggers) {
        final BitSet bitSet = new BitSet();
        if (triggers != null) {
            for (final int trigger : triggers) {
                bitSet.set(trigger);
            }
        }
        return bitSet;
    }
    
    private static String getAps(final String apsName) {
        if (apsName == null) {
            return "";
        }
        if (apsName.endsWith("\n")) {
            return apsName.substring(0, apsName.length() - 1);
        }
        return apsName;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectAreaFactory.class);
        INSTANCE = new EffectAreaFactory();
    }
    
    private static class Factory
    {
        private final Constructor m_constructor;
        
        Factory(final Class aClass) throws NoSuchMethodException {
            super();
            this.m_constructor = findConstructor(aClass);
        }
        
        private static Constructor findConstructor(final Class aClass) throws NoSuchMethodException {
            for (final Constructor c : aClass.getConstructors()) {
                if (c.getParameterTypes().length == 16) {
                    return c;
                }
            }
            throw new NoSuchMethodException();
        }
        
        public AbstractEffectArea create(final AreaEffectBinaryData bs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            return this.m_constructor.newInstance(bs.getId(), getAOE(bs), toBitSet(bs.getApplicationTriggers()), toBitSet(bs.getUnapplicationTriggers()), bs.getMaxExecutionCount(), bs.getScriptId(), bs.getTargetsToShow(), bs.getDeactivationDelay(), bs.getAreaGfx(), bs.getAreaCellGfx(), getAps(bs.getAps()), getAps(bs.getCellAps()), bs.getParams(), bs.isCanBeTargeted(), bs.isCanBeDestroyed(), bs.getMaxLevel());
        }
    }
}
