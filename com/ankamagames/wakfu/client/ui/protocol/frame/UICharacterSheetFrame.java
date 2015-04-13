package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.common.game.fight.handler.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.aptitude.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.aptitudenew.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import gnu.trove.*;

public class UICharacterSheetFrame extends UICompanionsEmbeddedFrame implements BigDialogLoadListener, FightListener
{
    private static final Logger m_logger;
    private static final UICharacterSheetFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UICharacterSheetFrame getInstance() {
        return UICharacterSheetFrame.m_instance;
    }
    
    @Override
    public void dialogLoaded(final String id) {
        if (id != null && !id.equals("characterSheetDialog")) {
            WakfuGameEntity.getInstance().removeFrame(this);
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17680: {
                final UIMessage msg = (UIMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (localPlayer.isOnFight()) {
                    return false;
                }
                final BonusPointCharacteristics bonusPoints = localPlayer.getBonusPointCharacteristics();
                if (bonusPoints == null) {
                    return false;
                }
                final byte characId = msg.getByteValue();
                final Breed breed = localPlayer.getBreed();
                final int previousValue = bonusPoints.computeXpBonusPointToValue(breed, characId);
                if (bonusPoints.addXpBonusPoint(breed, characId) != BonusPointCharacteristics.XpBonusPointError.NO_ERROR) {
                    return false;
                }
                final int newValue = bonusPoints.computeXpBonusPointToValue(breed, characId);
                final int pointsGained = newValue - previousValue;
                if (pointsGained != 0) {
                    final FighterCharacteristicType characteristicType = FighterCharacteristicType.getCharacteristicTypeFromId(characId);
                    final FighterCharacteristic characteristic = localPlayer.getCharacteristics().getCharacteristic(characteristicType);
                    if (characteristicType.isExpandable()) {
                        characteristic.updateMaxValue(pointsGained);
                    }
                    else {
                        characteristic.add(pointsGained);
                    }
                }
                final ActorAddBonusCharacPointMessage actorAddBonusCharacPointMessage = new ActorAddBonusCharacPointMessage();
                actorAddBonusCharacPointMessage.setCharacId(msg.getByteValue());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(actorAddBonusCharacPointMessage);
                PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, CharacterInfo.FIELDS);
                return false;
            }
            case 17690: {
                if (!Xulor.getInstance().isLoaded("characterSheetAptitudesDialog")) {
                    Xulor.getInstance().load("characterSheetAptitudesDialog", Dialogs.getDialogPath("characterSheetAptitudesDialog"), 32769L, (short)10000);
                }
                else {
                    Xulor.getInstance().unload("characterSheetAptitudesDialog");
                    AptitudeDisplayerImpl.getInstance().resetAptitudeLevelModifications();
                }
                return false;
            }
            default: {
                return super.onMessage(message);
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
    
    @Override
    public String getBaseDialogId() {
        return "characterSheetDialog";
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        super.onFrameAdd(frameHandler, isAboutToBeAdded);
        if (!isAboutToBeAdded) {
            this.checkCharacterButton();
            final boolean displayAllCharacteristics = !WakfuClientConfigurationManager.getInstance().containsKey((byte)3, "displayAllCharacteristics") || WakfuClientConfigurationManager.getInstance().getBooleanValue((byte)3, "displayAllCharacteristics");
            this.displayAllCharacteristics(displayAllCharacteristics);
            AptitudeDisplayerImpl.getInstance().resetAptitudeLevelModifications();
            PropertiesProvider.getInstance().setPropertyValue("aptitudeBonus", AptitudesView.INSTANCE);
            AptitudesView.INSTANCE.reset();
            PropertiesProvider.getInstance().setPropertyValue("characterSheetSecondMode", WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.APTITUDE_DISPLAY_MODE_KEY));
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("characterSheetAptitudesDialog")) {
                        AptitudeDisplayerImpl.getInstance().resetAptitudeLevelModifications();
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.characterSheet", CharacterSheetDialogActions.class);
            WakfuSoundManager.getInstance().playGUISound(600054L);
        }
    }
    
    private void checkCharacterButton() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldAndFightBarDialog");
        if (map == null) {
            return;
        }
        final Widget button = (Widget)map.getElement("fightInfoBtn");
        if (button == null) {
            return;
        }
        button.getAppearance().removeTweensOfType(ModulationColorTween.class);
    }
    
    public void displayAllCharacteristics(final boolean displayAllCharacteristics) {
        PropertiesProvider.getInstance().setPropertyValue("displayAllCharacteristics", displayAllCharacteristics);
        WakfuClientConfigurationManager.getInstance().setValue((byte)3, "displayAllCharacteristics", displayAllCharacteristics);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final Fight currentFight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight();
            final TIntObjectIterator<CharacterView> it = UICharacterSheetFrame.m_companionViews.iterator();
            while (it.hasNext()) {
                it.advance();
                final CharacterView value = it.value();
                value.getCharacterInfo().getCharacteristicViewProvider().saveToConfiguration();
                if (currentFight != null && value instanceof CharacteristicCompanionView) {
                    ((CharacteristicCompanionView)value).onLeaveFight();
                }
            }
            final TLongObjectIterator<CharacterView> it2 = UICharacterSheetFrame.m_characterViews.iterator();
            while (it2.hasNext()) {
                it2.advance();
                final CharacterView value = it2.value();
                value.getCharacterInfo().getCharacteristicViewProvider().saveToConfiguration();
                if (currentFight != null && value instanceof CharacteristicCompanionView) {
                    ((CharacteristicCompanionView)value).onLeaveFight();
                }
            }
            if (currentFight != null) {
                currentFight.unRegisterLocalFightHandler(this);
            }
            PropertiesProvider.getInstance().removeProperty("characterSheetSecondMode");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("aptitudeBonusDialog");
            Xulor.getInstance().removeActionClass("wakfu.characterSheet");
            WakfuSoundManager.getInstance().playGUISound(600013L);
        }
        super.onFrameRemove(frameHandler, isAboutToBeRemoved);
    }
    
    public void onLeaveFight() {
        final TIntObjectIterator<CharacterView> it = UICharacterSheetFrame.m_companionViews.iterator();
        while (it.hasNext()) {
            it.advance();
            final CharacterView value = it.value();
            if (value instanceof CharacteristicCompanionView) {
                PropertiesProvider.getInstance().firePropertyValueChanged(value, CharacterInfo.FIELDS);
            }
        }
        final TLongObjectIterator<CharacterView> it2 = UICharacterSheetFrame.m_characterViews.iterator();
        while (it2.hasNext()) {
            it2.advance();
            final CharacterView value = it2.value();
            if (value instanceof CharacteristicCompanionView) {
                PropertiesProvider.getInstance().firePropertyValueChanged(value, CharacterInfo.FIELDS);
            }
        }
    }
    
    public void onFight(final Fight fight) {
        if (fight == null) {
            return;
        }
        fight.registerLocalFightHandler(this);
    }
    
    @Override
    public void onPlacementStart() {
    }
    
    @Override
    public void onPlacementEnd() {
    }
    
    @Override
    public void onFightStart() {
    }
    
    @Override
    public void onFightEnd() {
    }
    
    @Override
    public void onTableTurnStart() {
    }
    
    @Override
    public void onTableTurnEnd() {
    }
    
    @Override
    public void onFighterStartTurn(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterEndTurn(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterJoinFight(final BasicCharacterInfo fighter) {
        if (!(fighter instanceof NonPlayerCharacter)) {
            return;
        }
        final NonPlayerCharacter nonPlayerCharacter = (NonPlayerCharacter)fighter;
        if (!nonPlayerCharacter.isControlledByLocalPlayer()) {
            return;
        }
        if (nonPlayerCharacter.getType() != 5) {
            return;
        }
        final CharacterView characterSheetView = UICompanionsEmbeddedFrame.getCharacterSheetView(nonPlayerCharacter.getBreedId());
        if (characterSheetView == null) {
            return;
        }
        ((CharacteristicCompanionView)characterSheetView).onJoinFight();
    }
    
    @Override
    public void onFighterOutOfPlay(final BasicCharacterInfo fighter) {
        if (!(fighter instanceof NonPlayerCharacter)) {
            return;
        }
        final NonPlayerCharacter nonPlayerCharacter = (NonPlayerCharacter)fighter;
        if (!nonPlayerCharacter.isControlledByLocalPlayer()) {
            return;
        }
        if (nonPlayerCharacter.getType() != 5) {
            return;
        }
        final CharacterView characterSheetView = UICompanionsEmbeddedFrame.getCharacterSheetView(nonPlayerCharacter.getBreedId());
        if (characterSheetView == null) {
            return;
        }
        ((CharacteristicCompanionView)characterSheetView).onLeaveFight();
    }
    
    @Override
    public void onFighterWinFight(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterLoseFight(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFighterCastSpell(final BasicCharacterInfo caster, final AbstractSpell spell) {
    }
    
    @Override
    public void onEffectAreaGoesOffPlay(final AbstractEffectArea area) {
    }
    
    @Override
    public void onFighterRemovedFromFight(final BasicCharacterInfo fighter) {
    }
    
    @Override
    public void onFightEnded() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)UICharacterSheetFrame.class);
        m_instance = new UICharacterSheetFrame();
    }
}
