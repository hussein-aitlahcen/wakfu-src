package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.script.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.ui.protocol.message.spells.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.common.game.hero.*;

public class UIFightTurnFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UIFightTurnFrame m_instance;
    private CharacterInfo m_outTurnCharacter;
    private CharacterInfo m_concernedFighter;
    private UIAbstractFightCastInteractionFrame m_currentCastFrame;
    private SpellSelectedListener m_spellSelectedListener;
    
    public static UIFightTurnFrame getInstance() {
        return UIFightTurnFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16441: {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer != null) {
                    localPlayer.getShortcutBarManager().useLeftHandWeapon();
                }
                return false;
            }
            case 16442: {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer != null) {
                    localPlayer.getShortcutBarManager().useRightHandWeapon();
                }
                return false;
            }
            case 18001: {
                final Fight fight = this.m_concernedFighter.getCurrentFight();
                if (fight == null) {
                    return false;
                }
                fight.requestEndTurn(this.m_concernedFighter);
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventAskEndTurn());
                return false;
            }
            case 18012: {
                final UIFighterSelectSpellOverItemMessage msg = (UIFighterSelectSpellOverItemMessage)message;
                final byte itemPos = msg.getEquipmentPos();
                final Item item = msg.getItem();
                final SpellLevel spell = msg.getSpell();
                if (spell != null && item != null) {
                    final SpellLevel selectedSpell = UIFightSpellCastInteractionFrame.getInstance().getSelectedSpell();
                    if (WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance()) && selectedSpell != null && selectedSpell.equals(spell)) {
                        WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance());
                    }
                    else {
                        if (WakfuGameEntity.getInstance().hasFrame(UIFightItemUseInteractionFrame.getInstance())) {
                            WakfuGameEntity.getInstance().removeFrame(UIFightItemUseInteractionFrame.getInstance());
                        }
                        if (WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastInteractionFrame.getInstance())) {
                            WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastInteractionFrame.getInstance());
                        }
                        if (this.m_concernedFighter.getCurrentFight().getItemAndSpellCastValidity(this.m_concernedFighter, item, spell, null) == CastValidity.OK) {
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().setSelectedSpell(spell);
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().setCharacter(this.m_concernedFighter);
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().selectRange();
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().setSelectedItem(item, itemPos);
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().setCharacter(this.m_concernedFighter);
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().selectRange();
                            UIFightSpellCastAndItemUseInteractionFrame.getInstance().refreshRangeDisplay();
                            this.m_currentCastFrame = UIFightSpellCastAndItemUseInteractionFrame.getInstance();
                            WakfuGameEntity.getInstance().pushFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance());
                        }
                    }
                }
                return false;
            }
            case 18002: {
                final UISpellLevelSelectionMessage msg2 = (UISpellLevelSelectionMessage)message;
                final SpellLevel spell2 = msg2.getSpell();
                if (spell2 != null) {
                    if (this.m_spellSelectedListener != null) {
                        this.m_spellSelectedListener.run();
                        if (this.m_spellSelectedListener.isBlockingSelection()) {
                            return false;
                        }
                    }
                    final SpellLevel selectedSpell2 = UIFightSpellCastInteractionFrame.getInstance().getSelectedSpell();
                    if (WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastInteractionFrame.getInstance()) && selectedSpell2 != null && selectedSpell2.equals(spell2)) {
                        WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastInteractionFrame.getInstance());
                    }
                    else {
                        if (WakfuGameEntity.getInstance().hasFrame(UIFightItemUseInteractionFrame.getInstance())) {
                            WakfuGameEntity.getInstance().removeFrame(UIFightItemUseInteractionFrame.getInstance());
                        }
                        if (WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance())) {
                            WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance());
                        }
                        final SpellCastValidatorForShortcuts validator = new SpellCastValidatorForShortcuts();
                        validator.setLinkedFight(this.m_concernedFighter.getCurrentFight());
                        if (validator.getSpellCastValidity(this.m_concernedFighter, spell2, null, true) == CastValidity.OK) {
                            UIFightSpellCastInteractionFrame.getInstance().setSelectedSpell(spell2);
                            UIFightSpellCastInteractionFrame.getInstance().setCharacter(this.m_concernedFighter);
                            UIFightSpellCastInteractionFrame.getInstance().selectRange();
                            UIFightSpellCastInteractionFrame.getInstance().refreshRangeDisplay();
                            this.m_currentCastFrame = UIFightSpellCastInteractionFrame.getInstance();
                            WakfuGameEntity.getInstance().pushFrame(UIFightSpellCastInteractionFrame.getInstance());
                        }
                    }
                }
                return false;
            }
            case 18009: {
                final UIFighterSelectAttackMessage msg3 = (UIFighterSelectAttackMessage)message;
                final Item item2 = msg3.getItem();
                final byte itemPos2 = msg3.getEquipmentPos();
                if (item2 != null) {
                    final Item selectedItem = UIFightItemUseInteractionFrame.getInstance().getItem();
                    if (WakfuGameEntity.getInstance().hasFrame(UIFightItemUseInteractionFrame.getInstance()) && selectedItem != null && selectedItem.equals(item2)) {
                        WakfuGameEntity.getInstance().removeFrame(UIFightItemUseInteractionFrame.getInstance());
                        GraphicalMouseManager.getInstance().hide();
                        CursorFactory.getInstance().unlock();
                    }
                    else {
                        if (WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastInteractionFrame.getInstance())) {
                            WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastInteractionFrame.getInstance());
                        }
                        if (WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance())) {
                            WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance());
                        }
                        if (this.m_concernedFighter.getCurrentFight().getItemCastValidity(this.m_concernedFighter, item2, null, true) == CastValidity.OK) {
                            UIFightItemUseInteractionFrame.getInstance().setSelectedItem(item2, itemPos2);
                            UIFightItemUseInteractionFrame.getInstance().setCharacter(this.m_concernedFighter);
                            UIFightItemUseInteractionFrame.getInstance().selectRange();
                            UIFightItemUseInteractionFrame.getInstance().refreshRangeDisplay();
                            this.m_currentCastFrame = UIFightItemUseInteractionFrame.getInstance();
                            WakfuGameEntity.getInstance().pushFrame(UIFightItemUseInteractionFrame.getInstance());
                        }
                    }
                }
                return false;
            }
            case 18010: {
                final UIFighterSelectAttackMessage msg3 = (UIFighterSelectAttackMessage)message;
                UIFightTurnFrame.m_logger.info((Object)"UIFightTurnFrame : onMessage : Methode a compl\u00e9ter (LIM)");
                return false;
            }
            case 18006: {
                final FighterActorDirectionChangeRequestMessage netMessage = new FighterActorDirectionChangeRequestMessage();
                netMessage.setFighterId(this.m_concernedFighter.getId());
                netMessage.setDirection8(Direction8.NORTH_EAST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 18003: {
                final FighterActorDirectionChangeRequestMessage netMessage = new FighterActorDirectionChangeRequestMessage();
                netMessage.setFighterId(this.m_concernedFighter.getId());
                netMessage.setDirection8(Direction8.NORTH_WEST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 18004: {
                final FighterActorDirectionChangeRequestMessage netMessage = new FighterActorDirectionChangeRequestMessage();
                netMessage.setFighterId(this.m_concernedFighter.getId());
                netMessage.setDirection8(Direction8.SOUTH_EAST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 18005: {
                final FighterActorDirectionChangeRequestMessage netMessage = new FighterActorDirectionChangeRequestMessage();
                netMessage.setFighterId(this.m_concernedFighter.getId());
                netMessage.setDirection8(Direction8.SOUTH_WEST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private short getTableTurn() {
        final Fight fight = this.m_concernedFighter.getCurrentFight();
        if (fight == null) {
            return 0;
        }
        final Timeline timeline = fight.getTimeline();
        if (timeline == null) {
            return 0;
        }
        return timeline.getCurrentTableturn();
    }
    
    public void countDownFeedback() {
        if (this.m_concernedFighter != null) {
            WakfuSoundManager.getInstance().playGUISound(600128L);
        }
    }
    
    private static CharacterInfo getOutTurnCharacter(final CharacterInfo current, final CharacterInfo localPlayer) {
        if (current.isActiveProperty(FightPropertyType.SUMMON_FORCE_CHARACTERISTIC_DISPLAY)) {
            return current;
        }
        if (current.isActiveProperty(FightPropertyType.STILL_LIFE)) {
            return localPlayer;
        }
        if (localPlayer.isActiveProperty(FightPropertyType.STILL_LIFE)) {
            return current;
        }
        return localPlayer;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            try {
                this.m_concernedFighter = localPlayer.getCurrentFight().getTimeline().getCurrentFighter();
                this.m_outTurnCharacter = getOutTurnCharacter(this.m_concernedFighter, localPlayer);
                if (!this.m_concernedFighter.isControlledByLocalPlayer()) {
                    UIFightTurnFrame.m_logger.warn((Object)"Frame de tour de combattant pouss\u00e9 pour le localPlayer alors que ce n'est pas lui le propri\u00e9taire de ce combattant");
                    this.m_concernedFighter = null;
                }
                if (this.m_concernedFighter != null && this.m_concernedFighter.isControlledByAI()) {
                    UIFightTurnFrame.m_logger.warn((Object)"frame de tour pouss\u00e9, pour le combattant local, mais pourtant g\u00e9r\u00e9 par l'IA");
                }
                prepareDisplayedFighter(this.m_concernedFighter);
                if (!this.m_concernedFighter.isActiveProperty(FightPropertyType.SKIP_TURN)) {
                    WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.FIGHT_INFO, WakfuTranslator.getInstance().getString("fight.yourTurn.0"), 2000));
                }
            }
            catch (Exception e) {
                UIFightTurnFrame.m_logger.error((Object)"Frame de tour de combat pouss\u00e9 alors qu'un param\u00e8tre est null (combat ou combattant)");
            }
            if (!WakfuGameEntity.getInstance().hasFrame(UIFightMovementFrame.getInstance())) {
                WakfuGameEntity.getInstance().pushFrame(UIFightMovementFrame.getInstance());
            }
            WakfuGameEntity.getInstance().pushFrame(UIFightEndTurnFrame.getInstance());
            UIControlCenterContainerFrame.getInstance().setDirectionButtonsEnabled(true);
            if (isLocalPlayerHero(this.m_concernedFighter, localPlayer)) {
                this.m_concernedFighter.getSpellInventoryManager().updateSpellsField();
                ((LocalPlayerCharacter)this.m_concernedFighter).updateShortcutBars();
                this.m_concernedFighter.updateCharacteristicViews(FighterCharacteristicType.AP, FighterCharacteristicType.HP, FighterCharacteristicType.MP, FighterCharacteristicType.WP);
            }
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("equipmentDialog");
            if (map != null) {
                PropertiesProvider.getInstance().firePropertyValueChanged("itemDetail", "usableNow", map);
            }
        }
    }
    
    public static void prepareDisplayedFighter(final CharacterInfo fighter) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        WakfuGameEntity.getInstance().setObservedCharacter(fighter);
        if (fighter.getBreedId() == 1550 || fighter.getBreedId() == 2382) {
            return;
        }
        final boolean localPlayerHero = isLocalPlayerHero(fighter, player);
        if (!localPlayerHero || player.getSpellInventoryManager().hasTemporarySpellInventory()) {
            final SymbiotShortcutBar bar = player.getShortcutBarManager().getSymbiotShortcutBar();
            if (bar != null) {
                bar.setControlledCharacter(fighter);
            }
            player.getShortcutBarManager().showSymbiotBar();
        }
        else {
            player.getShortcutBarManager().hideSymbiotBar();
        }
        if (fighter instanceof LocalPlayerCharacter) {
            final LocalPlayerCharacter lpc = (LocalPlayerCharacter)fighter;
            final ShortcutBarManager shortcutBarManager = lpc.getShortcutBarManager();
            UIShortcutBarFrame.getInstance().changeShortcutBarManager(shortcutBarManager);
            PropertiesProvider.getInstance().firePropertyValueChanged(lpc, LocalPlayerCharacter.LOCAL_ALL_FIELDS);
        }
    }
    
    private static boolean isLocalPlayerHero(final CharacterInfo fighter, final LocalPlayerCharacter player) {
        final BasicCharacterInfo hero = HeroesManager.INSTANCE.getHero(fighter.getId());
        return hero != null && hero.getOwnerId() == player.getOwnerId();
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        this.m_concernedFighter = null;
        if (!isAboutToBeRemoved) {
            UIFightFrame.clearMovementDisplay();
            this.m_currentCastFrame = null;
            GraphicalMouseManager.getInstance().hide();
            CursorFactory.getInstance().unlock();
            UIControlCenterContainerFrame.getInstance().setDirectionButtonsEnabled(false);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final CharacterInfo player = (this.m_outTurnCharacter != null) ? this.m_outTurnCharacter : localPlayer;
            prepareDisplayedFighter(player);
            WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightItemUseInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightSpellCastAndItemUseInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightEndTurnFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIFightMovementFrame.getInstance());
            if (localPlayer != null) {
                UIShortcutBarFrame.getInstance().changeShortcutBarManager(localPlayer.getShortcutBarManager());
                localPlayer.getSpellInventoryManager().updateSpellsField();
                localPlayer.updateShortcutBars();
                localPlayer.updateCharacteristicViews(FighterCharacteristicType.AP, FighterCharacteristicType.HP, FighterCharacteristicType.MP, FighterCharacteristicType.WP);
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public CharacterInfo getConcernedFighter() {
        return this.m_concernedFighter;
    }
    
    public UIAbstractFightCastInteractionFrame getCurrentCastFrame() {
        return this.m_currentCastFrame;
    }
    
    public void setSpellSelectedListener(final SpellSelectedListener spellSelectedListener) {
        this.m_spellSelectedListener = spellSelectedListener;
    }
    
    public SpellSelectedListener getSpellSelectedListener() {
        return this.m_spellSelectedListener;
    }
    
    public void cleanUp() {
        this.m_outTurnCharacter = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFightTurnFrame.class);
        m_instance = new UIFightTurnFrame();
    }
}
