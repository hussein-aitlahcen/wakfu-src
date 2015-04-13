package com.ankamagames.wakfu.common.game.effectArea;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.collections.iterators.*;
import java.util.*;

public class StaticEffectAreaManager
{
    protected static final Logger m_logger;
    private static StaticEffectAreaManager m_instance;
    private final TLongObjectHashMap<AbstractTrapEffectArea> m_trapEffectArea;
    private final TLongObjectHashMap<AbstractBeaconEffectArea> m_beaconArea;
    private final TLongObjectHashMap<AbstractBattlegroundBorderEffectArea> m_borderCellArea;
    private final TLongObjectHashMap<AbstractHourEffectArea> m_hourArea;
    private final TLongObjectHashMap<AbstractWallEffectArea> m_wallArea;
    private final TLongObjectHashMap<AbstractAuraEffectArea> m_auraArea;
    private final TLongObjectHashMap<AbstractGlyphEffectArea> m_glyphArea;
    private final TLongObjectHashMap<AbstractWarpEffectArea> m_warpArea;
    private final TLongObjectHashMap<AbstractBombEffectArea> m_bombArea;
    private final TLongObjectHashMap<AbstractBarrelEffectArea> m_barrelArea;
    private final TLongObjectHashMap<AbstractChangeSpellTargetCellArea> m_changeSpellTargetCellArea;
    private final TLongObjectHashMap<AbstractDepositEffectArea> m_depositAreas;
    private final TLongObjectHashMap<AbstractLootEffectArea> m_lootAreas;
    private final TLongObjectHashMap<AbstractWithREMArea> m_withREMAreas;
    private final TLongObjectHashMap<AbstractFakeFighterEffectArea> m_fakeFighterEffectAreas;
    private final TLongObjectHashMap<AbstractFecaGlyphEffectArea> m_fecaGlyphAreas;
    private final TLongObjectHashMap<AbstractBombComboEffectArea> m_bombCombo;
    private final TLongObjectHashMap<AbstractGateEffectArea> m_gates;
    private final TLongObjectHashMap<AbstractEffectArea> m_globalArea;
    
    public StaticEffectAreaManager() {
        super();
        this.m_trapEffectArea = new TLongObjectHashMap<AbstractTrapEffectArea>();
        this.m_beaconArea = new TLongObjectHashMap<AbstractBeaconEffectArea>();
        this.m_borderCellArea = new TLongObjectHashMap<AbstractBattlegroundBorderEffectArea>();
        this.m_hourArea = new TLongObjectHashMap<AbstractHourEffectArea>();
        this.m_wallArea = new TLongObjectHashMap<AbstractWallEffectArea>();
        this.m_auraArea = new TLongObjectHashMap<AbstractAuraEffectArea>();
        this.m_glyphArea = new TLongObjectHashMap<AbstractGlyphEffectArea>();
        this.m_warpArea = new TLongObjectHashMap<AbstractWarpEffectArea>();
        this.m_bombArea = new TLongObjectHashMap<AbstractBombEffectArea>();
        this.m_barrelArea = new TLongObjectHashMap<AbstractBarrelEffectArea>();
        this.m_changeSpellTargetCellArea = new TLongObjectHashMap<AbstractChangeSpellTargetCellArea>();
        this.m_depositAreas = new TLongObjectHashMap<AbstractDepositEffectArea>();
        this.m_lootAreas = new TLongObjectHashMap<AbstractLootEffectArea>();
        this.m_withREMAreas = new TLongObjectHashMap<AbstractWithREMArea>();
        this.m_fakeFighterEffectAreas = new TLongObjectHashMap<AbstractFakeFighterEffectArea>();
        this.m_fecaGlyphAreas = new TLongObjectHashMap<AbstractFecaGlyphEffectArea>();
        this.m_bombCombo = new TLongObjectHashMap<AbstractBombComboEffectArea>();
        this.m_gates = new TLongObjectHashMap<AbstractGateEffectArea>();
        this.m_globalArea = new TLongObjectHashMap<AbstractEffectArea>();
    }
    
    public static StaticEffectAreaManager getInstance() {
        return StaticEffectAreaManager.m_instance;
    }
    
    public void addEffectArea(final AbstractEffectArea area) {
        if (area instanceof AbstractTrapEffectArea) {
            this.addTrapEffectArea((AbstractTrapEffectArea)area);
        }
        else if (area instanceof AbstractGlyphEffectArea) {
            this.addGlyphEffectArea((AbstractGlyphEffectArea)area);
        }
        else if (area instanceof AbstractBeaconEffectArea) {
            this.addBeaconEffectArea((AbstractBeaconEffectArea)area);
        }
        else if (area instanceof AbstractAuraEffectArea) {
            this.addAura((AbstractAuraEffectArea)area);
        }
        else if (area instanceof AbstractHourEffectArea) {
            this.addHourArea((AbstractHourEffectArea)area);
        }
        else if (area instanceof AbstractWallEffectArea) {
            this.addWallArea((AbstractWallEffectArea)area);
        }
        else if (area instanceof AbstractBattlegroundBorderEffectArea) {
            this.addBorderCellArea((AbstractBattlegroundBorderEffectArea)area);
        }
        else if (area instanceof AbstractWarpEffectArea) {
            this.addWarpArea((AbstractWarpEffectArea)area);
        }
        else if (area instanceof AbstractBombEffectArea) {
            this.addBombArea((AbstractBombEffectArea)area);
        }
        else if (area instanceof AbstractSimpleEffectArea) {
            this.addSimpleEffectArea((AbstractSimpleEffectArea)area);
        }
        else if (area instanceof AbstractChangeSpellTargetCellArea) {
            this.addChangeSpellTargetCellArea((AbstractChangeSpellTargetCellArea)area);
        }
        else if (area instanceof AbstractDepositEffectArea) {
            this.addDepositEffectArea((AbstractDepositEffectArea)area);
        }
        else if (area instanceof AbstractLootEffectArea) {
            this.addLootArea((AbstractLootEffectArea)area);
        }
        else if (area instanceof AbstractWithREMArea) {
            this.addWithREMArea((AbstractWithREMArea)area);
        }
        else if (area instanceof AbstractFakeFighterEffectArea) {
            this.addFakeFighterEffectArea((AbstractFakeFighterEffectArea)area);
        }
        else if (area instanceof AbstractBarrelEffectArea) {
            this.addBarrelArea((AbstractBarrelEffectArea)area);
        }
        else if (area instanceof AbstractFecaGlyphEffectArea) {
            this.addFecaGlyphEffectArea((AbstractFecaGlyphEffectArea)area);
        }
        else if (area instanceof AbstractBombComboEffectArea) {
            this.addBombComboEffectArea((AbstractBombComboEffectArea)area);
        }
        else if (area instanceof AbstractGateEffectArea) {
            this.addGateEffectArea((AbstractGateEffectArea)area);
        }
        else {
            StaticEffectAreaManager.m_logger.error((Object)("Type de zone d'effet inconnue du manager " + area.getClass().getSimpleName()));
        }
    }
    
    private void addGateEffectArea(final AbstractGateEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_gates.put(area.getBaseId(), area);
    }
    
    private void addWithREMArea(final AbstractWithREMArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_withREMAreas.put(area.getBaseId(), area);
    }
    
    private void addFakeFighterEffectArea(final AbstractFakeFighterEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_fakeFighterEffectAreas.put(area.getBaseId(), area);
    }
    
    private void addFecaGlyphEffectArea(final AbstractFecaGlyphEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_fecaGlyphAreas.put(area.getBaseId(), area);
    }
    
    private void addBombComboEffectArea(final AbstractBombComboEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_bombCombo.put(area.getBaseId(), area);
    }
    
    private void addLootArea(final AbstractLootEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_lootAreas.put(area.getBaseId(), area);
    }
    
    private void addDepositEffectArea(final AbstractDepositEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
        this.m_depositAreas.put(area.getBaseId(), area);
    }
    
    private void addChangeSpellTargetCellArea(final AbstractChangeSpellTargetCellArea area) {
        this.m_changeSpellTargetCellArea.put(area.getBaseId(), area);
        this.m_globalArea.put(area.getBaseId(), area);
    }
    
    private void addSimpleEffectArea(final AbstractSimpleEffectArea area) {
        this.m_globalArea.put(area.getBaseId(), area);
    }
    
    private void addBombArea(final AbstractBombEffectArea bomb) {
        this.m_bombArea.put(bomb.getBaseId(), bomb);
        this.m_globalArea.put(bomb.getBaseId(), bomb);
    }
    
    private void addBarrelArea(final AbstractBarrelEffectArea barrel) {
        this.m_barrelArea.put(barrel.getBaseId(), barrel);
        this.m_globalArea.put(barrel.getBaseId(), barrel);
    }
    
    private void addWarpArea(final AbstractWarpEffectArea warp) {
        this.m_warpArea.put(warp.getBaseId(), warp);
        this.m_globalArea.put(warp.getBaseId(), warp);
    }
    
    private void addTrapEffectArea(final AbstractTrapEffectArea trap) {
        this.m_trapEffectArea.put(trap.getBaseId(), trap);
        this.m_globalArea.put(trap.getBaseId(), trap);
    }
    
    public AbstractTrapEffectArea getTrapEffectArea(final long trapId) {
        return this.m_trapEffectArea.get(trapId);
    }
    
    private void addGlyphEffectArea(final AbstractGlyphEffectArea glyph) {
        this.m_glyphArea.put(glyph.getBaseId(), glyph);
        this.m_globalArea.put(glyph.getBaseId(), glyph);
    }
    
    public AbstractGlyphEffectArea getGlyph(final long glyphId) {
        return this.m_glyphArea.get(glyphId);
    }
    
    private void addBeaconEffectArea(final AbstractBeaconEffectArea beacon) {
        this.m_beaconArea.put(beacon.getBaseId(), beacon);
        this.m_globalArea.put(beacon.getBaseId(), beacon);
    }
    
    public AbstractBeaconEffectArea getBeacon(final long beaconId) {
        return this.m_beaconArea.get(beaconId);
    }
    
    private void addAura(final AbstractAuraEffectArea aura) {
        this.m_auraArea.put(aura.getBaseId(), aura);
        this.m_globalArea.put(aura.getBaseId(), aura);
    }
    
    public AbstractAuraEffectArea getAura(final long auraId) {
        return this.m_auraArea.get(auraId);
    }
    
    private void addBorderCellArea(final AbstractBattlegroundBorderEffectArea bc) {
        this.m_borderCellArea.put(bc.getBaseId(), bc);
        this.m_globalArea.put(bc.getBaseId(), bc);
    }
    
    public AbstractBattlegroundBorderEffectArea getBorderCellArea(final long borderCellId) {
        return this.m_borderCellArea.get(borderCellId);
    }
    
    public AbstractBattlegroundBorderEffectArea getRandomBorderCellArea() {
        return this.m_borderCellArea.get(this.m_borderCellArea.keys()[MathHelper.random(this.m_borderCellArea.size())]);
    }
    
    private void addHourArea(final AbstractHourEffectArea sc) {
        this.m_hourArea.put(sc.getBaseId(), sc);
        this.m_globalArea.put(sc.getBaseId(), sc);
    }
    
    public AbstractHourEffectArea getHourAreas(final long hourAreaBaseId) {
        return this.m_hourArea.get(hourAreaBaseId);
    }
    
    public Iterator<AbstractHourEffectArea> getHourAreas() {
        return new TroveLongHashMapValueIterator<AbstractHourEffectArea>(this.m_hourArea);
    }
    
    private void addWallArea(final AbstractWallEffectArea sc) {
        this.m_wallArea.put(sc.getBaseId(), sc);
        this.m_globalArea.put(sc.getBaseId(), sc);
    }
    
    public AbstractWallEffectArea getWallArea(final long wallBaseId) {
        return this.m_wallArea.get(wallBaseId);
    }
    
    public AbstractWarpEffectArea getWarpArea(final long warpBaseId) {
        return this.m_warpArea.get(warpBaseId);
    }
    
    public AbstractEffectArea getAreaFromId(final long areaId) {
        return this.m_globalArea.get(areaId);
    }
    
    public AbstractBombEffectArea getBombArea(final long id) {
        return this.m_bombArea.get(id);
    }
    
    public List<AbstractDepositEffectArea> getDepositAreas() {
        final AbstractDepositEffectArea[] areas = new AbstractDepositEffectArea[this.m_depositAreas.size()];
        this.m_depositAreas.getValues(areas);
        return Arrays.asList(areas);
    }
    
    public static void setInstance(final StaticEffectAreaManager instance) {
        StaticEffectAreaManager.m_instance = instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)StaticEffectAreaManager.class);
        StaticEffectAreaManager.m_instance = new StaticEffectAreaManager();
    }
}
