package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.characteristics.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.part.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DestructibleMachine extends WakfuClientMapInteractiveElement implements CriterionUser, FightObstacle, FightEffectUser, EffectUser, ChaosInteractiveElement
{
    private static final Logger m_logger;
    protected IEDestructibleParameter m_info;
    protected FillableCharacteristicManager m_characteristics;
    private byte m_obstacleId;
    private static final CharacteristicType[] DESTRUCTIBLE_CHARACTERISTICS;
    private final BinarSerialPart SHARED_DATAS;
    
    public DestructibleMachine() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(7) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => client par de s\u00e9rialisation");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                DestructibleMachine.this.unserializeFightInfo(buffer);
                switch (DestructibleMachine.this.m_state) {
                    case 0:
                    case 1: {
                        DestructibleMachine.this.setBlockingLineOfSight(true);
                        DestructibleMachine.this.setBlockingMovements(true);
                        DestructibleMachine.this.setTransitionModel(TransitionModel.FORCE_TRANS);
                        break;
                    }
                    case 2: {
                        DestructibleMachine.this.setBlockingLineOfSight(false);
                        DestructibleMachine.this.setBlockingMovements(false);
                        final FightInfo fight = FightManager.getInstance().getFightById(DestructibleMachine.this.m_currentFightId);
                        final FightMap fightMap = fight.getFightMap();
                        if (!DestructibleMachine.this.isAlreadyInMapObstacles(fight)) {
                            if (fight instanceof Fight) {
                                ((Fight)fight).removeAdditionalTarget(DestructibleMachine.this.getId());
                            }
                            return;
                        }
                        FightActionGroupManager.getInstance().addActionToPendingGroup(DestructibleMachine.this.m_currentFightId, new TimedAction(TimedAction.getNextUid(), 0, 0) {
                            @Override
                            protected long onRun() {
                                final FightInfo concernedFight = FightManager.getInstance().getFightById(DestructibleMachine.this.m_currentFightId);
                                if (concernedFight == null) {
                                    return 0L;
                                }
                                if (concernedFight instanceof Fight) {
                                    ((Fight)concernedFight).removeAdditionalTarget(this.getId());
                                }
                                DestructibleMachine.this.removeFromObstacles(fightMap);
                                return 0L;
                            }
                            
                            @Override
                            protected void onActionFinished() {
                            }
                        });
                        DestructibleMachine.this.setTransitionModel(TransitionModel.NORMAL);
                        break;
                    }
                }
            }
        };
    }
    
    @Override
    public boolean isBlockingWholeCell() {
        return true;
    }
    
    private void removeFromObstacles(final FightMap fightMap) {
        if (fightMap != null) {
            fightMap.removeObstacle(this);
        }
    }
    
    private void unserializeFightInfo(final ByteBuffer buffer) {
        this.m_currentFightId = buffer.getInt();
        this.m_obstacleId = buffer.get();
        if (this.m_currentFightId <= 0) {
            return;
        }
        final FightInfo fight = FightManager.getInstance().getFightById(this.m_currentFightId);
        if (this.isAlreadyInMapObstacles(fight)) {
            return;
        }
        FightActionGroupManager.getInstance().addActionToPendingGroup(this.m_currentFightId, new TimedAction(TimedAction.getNextUid(), 0, 0) {
            @Override
            protected long onRun() {
                DestructibleMachine.this.addObstacleToFight();
                return 0L;
            }
            
            @Override
            protected void onActionFinished() {
            }
        });
    }
    
    private boolean isAlreadyInMapObstacles(final FightInfo fight) {
        return fight != null && fight.getFightMap() != null && fight.getFightMap().getObstacleFromId(this.m_obstacleId) == this;
    }
    
    private void addObstacleToFight() {
        final FightInfo fight = FightManager.getInstance().getFightById(this.m_currentFightId);
        if (fight == null) {
            DestructibleMachine.m_logger.error((Object)("Impossible d'ajouter l'obstacle a un combat inconnu " + this.m_currentFightId));
        }
        this.addObstacleToFight(fight);
    }
    
    private void addObstacleToFight(final FightInfo fight) {
        final FightMap fightMap = fight.getFightMap();
        if (fightMap == null) {
            DestructibleMachine.m_logger.error((Object)"Le combat n'a pas de FightMap, impossible d'ajouter l'obstacle");
            return;
        }
        final FightObstacle oldObstacle = fightMap.getObstacleFromId(this.m_obstacleId);
        if (oldObstacle == null) {
            DestructibleMachine.m_logger.info((Object)("Ajout de l'obstacle = " + this));
            fightMap.assignObstacleWithId(this);
        }
        else if (oldObstacle != this) {
            DestructibleMachine.m_logger.error((Object)"Un autre obstacle diff\u00e9rent a le meme ID, ce n'est pas normal");
        }
    }
    
    @Override
    public boolean canBeTargeted() {
        return true;
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.NONE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(81, this.m_info.getId(), new Object[0]);
    }
    
    @Override
    public String getOverHeadText() {
        final int hp = this.m_characteristics.getCharacteristicValue(FighterCharacteristicType.HP);
        final int hpMax = this.m_characteristics.getCharacteristicMaxValue(FighterCharacteristicType.HP);
        return (this.m_currentFightId != -1) ? (" (" + hp + " / " + hpMax + ")") : "";
    }
    
    @Override
    public boolean isOverHeadable() {
        return this.m_state != 2 && super.isOverHeadable();
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean addView(final ClientInteractiveElementView view) {
        if (view instanceof AnimatedElement) {
            ((AnimatedElement)view).setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
        }
        return super.addView(view);
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void initializeWithParameter() {
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            DestructibleMachine.m_logger.error((Object)("[LD] La Machine Destructible " + this.m_id + " doit avoir 1 param\u00e8tre"));
            return;
        }
        final IEDestructibleParameter param = (IEDestructibleParameter)IEParametersManager.INSTANCE.getParam(IETypes.DESTRUCTIBLE, Integer.valueOf(params[0]));
        if (param == null) {
            DestructibleMachine.m_logger.error((Object)("[LD] La Machine Destructible " + this.m_id + " \u00e0 un param\u00e8tre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_info = param;
        this.initializeCharacteristics();
    }
    
    private void initializeCharacteristics() {
        this.m_characteristics.makeDefault();
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.HP).setMax(this.m_info.getPdv());
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.HP).toMax();
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.RES_WATER_PERCENT).set(this.m_info.getResWater());
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.RES_FIRE_PERCENT).set(this.m_info.getResFire());
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.RES_EARTH_PERCENT).set(this.m_info.getResEarth());
        this.m_characteristics.getCharacteristic(FighterCharacteristicType.RES_AIR_PERCENT).set(this.m_info.getResWind());
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_currentFightId = -1;
        this.m_obstacleId = -1;
        assert this.m_characteristics == null;
        this.m_characteristics = new FillableCharacteristicManager(DestructibleMachine.DESTRUCTIBLE_CHARACTERISTICS);
        this.setVisible(true);
        this.setUseSpecificAnimTransition(true);
        this.setTransitionModel(TransitionModel.FORCE_TRANS);
        this.setState((short)0);
        this.setOverHeadable(true);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setDirection(Direction8.SOUTH_EAST);
        this.setSelectable(true);
        assert this.m_info == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.setVisible(false);
        this.setState((short)2);
        this.m_characteristics = null;
        this.m_info = null;
    }
    
    @Override
    public byte getPhysicalRadius() {
        return 0;
    }
    
    @Override
    public void setObstacleId(final byte id) {
        this.m_obstacleId = id;
    }
    
    @Override
    public byte getObstacleId() {
        return this.m_obstacleId;
    }
    
    @Override
    public boolean canBlockMovementOrSight() {
        return true;
    }
    
    @Override
    public boolean isBlockingMovement() {
        return this.isBlockingMovements();
    }
    
    @Override
    public boolean isBlockingSight() {
        return this.isBlockingLineOfSight();
    }
    
    @Override
    public byte getEffectUserType() {
        return 10;
    }
    
    @Override
    public RunningEffectManager getRunningEffectManager() {
        return null;
    }
    
    @Override
    public boolean isValidForEffectExecution() {
        return true;
    }
    
    @Override
    public byte[] serializeEffectUser() {
        return new byte[0];
    }
    
    @Override
    public void unserializeEffectUser(final byte[] serializedEffectUserDatas) {
    }
    
    @Override
    public void setPosition(final int x, final int y, final short alt) {
    }
    
    @Override
    public void teleport(final int x, final int y, final short z) {
    }
    
    @Override
    public boolean hasCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.contains(charac);
    }
    
    @Override
    public boolean hasProperty(final PropertyType property) {
        return false;
    }
    
    @Override
    public boolean hasState(final long stateId) {
        return false;
    }
    
    @Override
    public int getStateLevel(final long stateId) {
        return -1;
    }
    
    @Override
    public boolean hasState(final long stateId, final long stateLevel) {
        return false;
    }
    
    @Override
    public boolean hasStateFromUser(final long stateId, final CriterionUser casterUser) {
        return false;
    }
    
    @Override
    public boolean hasStateFromUser(final long stateId, final long stateLevel, final CriterionUser casterUser) {
        return false;
    }
    
    @Override
    public boolean hasStateFromLevel(final long stateId, final long level) {
        return false;
    }
    
    @Override
    public boolean is(final CriterionUserType type) {
        return CriterionUserType.DESTRUCTIBLE == type;
    }
    
    @Override
    public int getSummoningsCount() {
        return 0;
    }
    
    @Override
    public int getSummoningsCount(final int summonBreedId) {
        return 0;
    }
    
    @Override
    public boolean isSummonedFromSymbiot() {
        return false;
    }
    
    @Override
    public AbstractCharacteristic getCharacteristic(final CharacteristicType charac) {
        return this.m_characteristics.getCharacteristic(charac);
    }
    
    @Override
    public Breed getBreed() {
        return null;
    }
    
    @Override
    public byte getTeamId() {
        return -1;
    }
    
    @Override
    public void setTeamId(final byte teamId) {
    }
    
    @Override
    public long getOriginalControllerId() {
        return 0L;
    }
    
    @Override
    public boolean isSummoned() {
        return false;
    }
    
    @Override
    public int getCharacteristicValue(final CharacteristicType charac) throws UnsupportedOperationException {
        return this.m_characteristics.getCharacteristicValue(charac);
    }
    
    @Override
    public int getCharacteristicMax(final CharacteristicType charac) {
        return this.m_characteristics.getCharacteristicMaxValue(charac);
    }
    
    @Override
    public boolean isActiveProperty(final PropertyType property) {
        return false;
    }
    
    @Override
    public byte getPropertyValue(final PropertyType property) {
        return 0;
    }
    
    @Override
    public void setPropertyValue(final PropertyType property, final byte value) {
    }
    
    @Override
    public void addProperty(final PropertyType property) {
    }
    
    @Override
    public void substractProperty(final PropertyType property) {
    }
    
    @Override
    public void removeProperty(final PropertyType property) {
    }
    
    @Override
    public void setDirection(final Direction8 direction) {
        super.setDirection(direction);
    }
    
    @Override
    public Direction8 getMovementDirection() {
        return null;
    }
    
    @Override
    public void setSpecialMovementDirection(final Direction8 direction) {
    }
    
    @Override
    public PartLocalisator getPartLocalisator() {
        return null;
    }
    
    @Override
    public void goOffPlay(final EffectUser killer) {
    }
    
    @Override
    public void goBackInPlay(final EffectUser killer) {
    }
    
    @Override
    public void goOutOfPlay(final EffectUser killer) {
    }
    
    @Override
    public boolean mustGoBackInPlay() {
        return false;
    }
    
    @Override
    public boolean mustGoOffPlay() {
        return false;
    }
    
    @Override
    public void onGoesOffPlay() {
    }
    
    @Override
    public boolean isOnFight() {
        return true;
    }
    
    @Override
    public boolean isInPlay() {
        return false;
    }
    
    @Override
    public boolean isOffPlay() {
        return false;
    }
    
    @Override
    public void onBackInPlay() {
    }
    
    @Override
    public boolean mustGoOutOfPlay() {
        return false;
    }
    
    @Override
    public void onGoesOutOfPlay() {
    }
    
    @Override
    public boolean isOutOfPlay() {
        return false;
    }
    
    @Override
    public boolean canChangePlayStatus() {
        return true;
    }
    
    @Override
    public void setUnderChange(final boolean bool) {
    }
    
    @Override
    public EffectUser getResistanceTarget() {
        return this;
    }
    
    @Override
    public boolean trigger(final BitSet triggers, final RunningEffect triggerer, final byte options) {
        return false;
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_info;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DestructibleMachine.class);
        DESTRUCTIBLE_CHARACTERISTICS = new CharacteristicType[] { FighterCharacteristicType.HP, FighterCharacteristicType.RES_FIRE_PERCENT, FighterCharacteristicType.RES_WATER_PERCENT, FighterCharacteristicType.RES_EARTH_PERCENT, FighterCharacteristicType.RES_AIR_PERCENT };
    }
    
    public static class DestructibleFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public DestructibleMachine makeObject() {
            DestructibleMachine ie;
            try {
                ie = (DestructibleMachine)DestructibleFactory.m_pool.borrowObject();
                ie.setPool(DestructibleFactory.m_pool);
            }
            catch (Exception e) {
                DestructibleMachine.m_logger.error((Object)"Erreur lors de l'extraction d'un Destructible du pool", (Throwable)e);
                ie = new DestructibleMachine();
            }
            return ie;
        }
        
        static {
            DestructibleFactory.m_pool = new MonitoredPool(new ObjectFactory<DestructibleMachine>() {
                @Override
                public DestructibleMachine makeObject() {
                    return new DestructibleMachine();
                }
            });
        }
    }
}
