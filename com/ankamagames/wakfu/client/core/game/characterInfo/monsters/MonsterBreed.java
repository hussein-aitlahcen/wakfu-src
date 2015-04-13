package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import com.ankamagames.wakfu.client.core.game.craft.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.movement.*;
import com.ankamagames.wakfu.common.game.wakfu.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class MonsterBreed extends AbstractMonsterBreed<CollectAction>
{
    public static final MonsterBreed NONE;
    private static final Logger m_logger;
    private final boolean m_hasDeadEvolution;
    private final int m_gfx;
    private final int m_timelineGfx;
    private final byte m_height;
    private final TByteIntHashMap m_equipmentAppearance;
    private final ArrayList<IntObjectPair<Spell>> m_spells;
    private final MonsterRankConstants m_rank;
    private final float m_arcadePointMultiplicator;
    private MonsterSpecialGfx m_specialGfx;
    private final ArrayList<AbstractClientMonsterAction> m_actions;
    
    public MonsterBreed(final short breedId, final int familyId, final boolean hasDeadEvolution, final short levelMin, final short levelMax, final EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>> fightCharacteristics, final int gfx, final int timelineGfx, final int[] worldProperties, final int[] fightProperties, final int[] gfxEquipments, final int height, final short[] naturalStates, final short aggroRadius, final short sightRadius, final byte physicalRadius, final byte rank, final int defeatScriptId, final MovementSpeed walkSpeed, final MovementSpeed runSpeed, final int maxWalkDistance, final int maxFightWalkDistance, final WakfuMonsterAlignment wakfuAlignment, final float arcadePointMultiplicator) {
        super(breedId, familyId, levelMin, levelMax, fightCharacteristics, fightProperties, worldProperties, naturalStates, aggroRadius, sightRadius, physicalRadius, defeatScriptId, walkSpeed, runSpeed, maxWalkDistance, maxFightWalkDistance, wakfuAlignment);
        this.m_spells = new ArrayList<IntObjectPair<Spell>>();
        this.m_actions = new ArrayList<AbstractClientMonsterAction>();
        this.m_gfx = gfx;
        this.m_timelineGfx = timelineGfx;
        if (!ActorUtils.isNpcGfx(this.m_gfx) && gfxEquipments != null && gfxEquipments.length != 0) {
            if (gfxEquipments.length % 2 != 0) {
                MonsterBreed.m_logger.error((Object)" le tableau d'\u00e9quippement doit contenir des paires Id / Nombre");
            }
            this.m_equipmentAppearance = new TByteIntHashMap(gfxEquipments.length / 2);
            for (int i = 0; i < gfxEquipments.length; i += 2) {
                this.m_equipmentAppearance.put((byte)gfxEquipments[i + 2], gfxEquipments[i]);
            }
        }
        else {
            this.m_equipmentAppearance = null;
            if (ActorUtils.isNpcGfx(this.m_gfx) && gfxEquipments != null && gfxEquipments.length != 0) {
                MonsterBreed.m_logger.error((Object)("ON TENTE DE METTRE DES EQUIPEMENTS (VISUEL) SUR UN GFX DE TYPE MONSTER (>1000), monsterId=" + breedId));
            }
        }
        this.m_height = (byte)height;
        this.m_hasDeadEvolution = hasDeadEvolution;
        this.m_rank = MonsterRankConstants.getRankById(rank);
        this.m_arcadePointMultiplicator = arcadePointMultiplicator;
    }
    
    public boolean hasDeadEvolution() {
        return this.m_hasDeadEvolution;
    }
    
    public int getGfx() {
        return this.m_gfx;
    }
    
    public int getTimelineGfx() {
        return this.m_timelineGfx;
    }
    
    @Override
    public byte getHeight() {
        return this.m_height;
    }
    
    @Nullable
    public TByteIntHashMap getEquipmentAppearance() {
        return this.m_equipmentAppearance;
    }
    
    public String getName() {
        final short breedId = this.getBreedId();
        if (breedId != MonsterBreed.NONE.getBreedId()) {
            return WakfuTranslator.getInstance().getString(7, breedId, new Object[0]);
        }
        return "";
    }
    
    public ArrayList<IntObjectPair<Spell>> getSpells() {
        return this.m_spells;
    }
    
    public boolean spellIsLoad(final int id) {
        return !this.m_spells.isEmpty();
    }
    
    public void addSpell(final int id, final int level) {
        final Spell spell = SpellManager.getInstance().getSpell(id);
        if (spell != null) {
            this.m_spells.add(new IntObjectPair<Spell>(level, spell));
        }
    }
    
    public void addAction(final AbstractClientMonsterAction monsterAction) {
        this.m_actions.add(monsterAction);
    }
    
    public ArrayList<AbstractClientMonsterAction> getActions() {
        return this.m_actions;
    }
    
    @Nullable
    public AbstractClientMonsterAction getAction(final long actionId) {
        for (int i = 0; i < this.m_actions.size(); ++i) {
            final AbstractClientMonsterAction action = this.m_actions.get(i);
            if (action.getId() == actionId) {
                return action;
            }
        }
        return null;
    }
    
    public MonsterRankConstants getRank() {
        return this.m_rank;
    }
    
    public MonsterSpecialGfx getSpecialGfx() {
        return this.m_specialGfx;
    }
    
    public void setSpecialGfx(final MonsterSpecialGfx specialGfx) {
        this.m_specialGfx = specialGfx;
    }
    
    public void setRequiredLeadershipPoints(final int requiredLeadershipPoints) {
        super.setRequiredLeadershipPoints(requiredLeadershipPoints);
    }
    
    public float getArcadePointMultiplicator() {
        return this.m_arcadePointMultiplicator;
    }
    
    static {
        NONE = new MonsterBreed((short)(-2), 0, false, (short)0, (short)0, new EnumMap<FighterCharacteristicType, ObjectPair<Integer, Float>>(FighterCharacteristicType.class), 0, 0, new int[0], new int[0], new int[0], 0, new short[0], (short)0, (short)0, (byte)0, (byte)0, 30000, MovementSpeed.NORMAL_WALK_SPEED, MovementSpeed.NORMAL_RUN_SPEED, 5, 0, WakfuMonsterAlignment.NEUTRAL, 1.0f);
        m_logger = Logger.getLogger((Class)MonsterBreed.class);
    }
}
