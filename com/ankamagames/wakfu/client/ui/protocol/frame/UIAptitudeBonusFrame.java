package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.aptitude.*;
import com.ankamagames.wakfu.client.core.game.aptitude.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.aptitudenew.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import java.util.*;
import gnu.trove.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIAptitudeBonusFrame implements MessageFrame
{
    public static final UIAptitudeBonusFrame INSTANCE;
    protected static final Logger m_logger;
    private ParticleDecorator m_commonDecorator;
    private ParticleDecorator m_spellDecorator;
    private DialogUnloadListener m_dialogUnloadListener;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17691: {
                final UIAptitudeBonusMessage msg = (UIAptitudeBonusMessage)message;
                final AptitudeBonusView bonus = msg.getView();
                final boolean hadNoModifications = AptitudesView.INSTANCE.hasNoModifications();
                AptitudesView.INSTANCE.incrementBonus(bonus);
                if (!bonus.canBeIncreased()) {
                    ModificationRunnable.INSTANCE.cleanModificationRunnable();
                }
                if (hadNoModifications) {
                    if (msg.getButtonContainer() != null) {
                        msg.getButtonContainer().setVisible(true);
                    }
                    if (msg.getValidateButton() != null) {
                        final ParticleDecorator deco = new ParticleDecorator();
                        deco.onCheckOut();
                        deco.setFile("6001009.xps");
                        deco.setAlignment(Alignment9.CENTER);
                        msg.getValidateButton().getAppearance().add(deco);
                        this.m_commonDecorator = deco;
                    }
                }
                return false;
            }
            case 17692: {
                final UIAptitudeBonusMessage msg = (UIAptitudeBonusMessage)message;
                final AptitudeBonusView bonus = msg.getView();
                final boolean hadNoModifications = AptitudesView.INSTANCE.hasNoModifications();
                AptitudesView.INSTANCE.decrementBonus(bonus);
                if (!bonus.canBeDecreased()) {
                    ModificationRunnable.INSTANCE.cleanModificationRunnable();
                }
                if (hadNoModifications) {
                    if (msg.getButtonContainer() != null) {
                        msg.getButtonContainer().setVisible(true);
                    }
                    if (msg.getValidateButton() != null) {
                        final ParticleDecorator deco = new ParticleDecorator();
                        deco.onCheckOut();
                        deco.setFile("6001009.xps");
                        deco.setAlignment(Alignment9.CENTER);
                        msg.getValidateButton().getAppearance().add(deco);
                        this.m_commonDecorator = deco;
                    }
                }
                if (AptitudesView.INSTANCE.hasNoModifications()) {
                    this.reinitializeAptitudeButtons(msg, AptitudeType.COMMON);
                }
                return false;
            }
            case 17693: {
                final AbstractUIMessage msg2 = (AbstractUIMessage)message;
                final AptitudeBonusCategoryView category = msg2.getObjectValue();
                AptitudesView.INSTANCE.selectCategory(category);
                return false;
            }
            case 17694: {
                final UIAptitudeCommonMessage msg3 = (UIAptitudeCommonMessage)message;
                AptitudesView.INSTANCE.validateChanges();
                this.reinitializeAptitudeButtons(msg3, AptitudeType.COMMON);
                return false;
            }
            case 17695: {
                final UIAptitudeCommonMessage msg3 = (UIAptitudeCommonMessage)message;
                AptitudesView.INSTANCE.resetChanges();
                this.reinitializeAptitudeButtons(msg3, AptitudeType.COMMON);
                return false;
            }
            case 17686: {
                final UIAptitudeMessage msg4 = (UIAptitudeMessage)message;
                final Aptitude aptitude = msg4.getAptitude();
                if (aptitude == null) {
                    return false;
                }
                final AptitudeType type = aptitude.getReferenceAptitude().getType();
                final int numModifiedAptitudes = AptitudeDisplayerImpl.getInstance().getAptitudeLevelIncreaseMap(type).size();
                AptitudeDisplayerImpl.getInstance().increaseAptitudeLevel(aptitude);
                if (!AptitudeDisplayerImpl.getInstance().canBeIncreased(aptitude)) {
                    ModificationRunnable.INSTANCE.cleanModificationRunnable();
                }
                final AptitudeInventory aptitudeInventory = WakfuGameEntity.getInstance().getLocalPlayer().getAptitudeInventory();
                for (final Aptitude otherAptitude : aptitudeInventory) {
                    if (otherAptitude == aptitude) {
                        continue;
                    }
                    AptitudeDisplayerImpl.getInstance().fireAvailablePointsChanged(otherAptitude);
                }
                final int newNumModifiedAptitudes = AptitudeDisplayerImpl.getInstance().getAptitudeLevelIncreaseMap(type).size();
                if (numModifiedAptitudes == 0 && newNumModifiedAptitudes == 1) {
                    if (msg4.getButtonContainer() != null) {
                        msg4.getButtonContainer().setVisible(true);
                    }
                    if (msg4.getValidateButton() != null) {
                        final ParticleDecorator deco2 = new ParticleDecorator();
                        deco2.onCheckOut();
                        deco2.setFile("6001009.xps");
                        deco2.setAlignment(Alignment9.CENTER);
                        msg4.getValidateButton().getAppearance().add(deco2);
                        switch (type) {
                            case COMMON: {
                                this.m_commonDecorator = deco2;
                                break;
                            }
                            case SPELL: {
                                this.m_spellDecorator = deco2;
                                break;
                            }
                        }
                    }
                }
                return false;
            }
            case 17687: {
                final UIAptitudeMessage msg4 = (UIAptitudeMessage)message;
                final Aptitude aptitude = msg4.getAptitude();
                if (aptitude == null) {
                    return false;
                }
                final AptitudeType type = aptitude.getReferenceAptitude().getType();
                final int numModifiedAptitudes = AptitudeDisplayerImpl.getInstance().getAptitudeLevelIncreaseMap(type).size();
                AptitudeDisplayerImpl.getInstance().decreaseAptitudeLevel(aptitude);
                if (!AptitudeDisplayerImpl.getInstance().canBeDecreased(aptitude)) {
                    ModificationRunnable.INSTANCE.cleanModificationRunnable();
                }
                final AptitudeInventory aptitudeInventory = WakfuGameEntity.getInstance().getLocalPlayer().getAptitudeInventory();
                for (final Aptitude otherAptitude : aptitudeInventory) {
                    if (otherAptitude == aptitude) {
                        continue;
                    }
                    AptitudeDisplayerImpl.getInstance().fireAvailablePointsChanged(otherAptitude);
                }
                final int newNumModifiedAptitudes = AptitudeDisplayerImpl.getInstance().getAptitudeLevelIncreaseMap(type).size();
                if (numModifiedAptitudes == 1 && newNumModifiedAptitudes == 0) {
                    this.reinitializeAptitudeButtons(msg4, type);
                }
                return false;
            }
            case 17688: {
                final UIAptitudeCommonMessage msg3 = (UIAptitudeCommonMessage)message;
                final AptitudeType aptitudeType = AptitudeType.getFromId(msg3.getByteValue());
                final TShortShortHashMap aptitudesModifications = AptitudeDisplayerImpl.getInstance().getAptitudeLevelIncreaseMap(aptitudeType);
                if (aptitudesModifications.size() > 0) {
                    final ActorLevelUpAptitudeRequestMessage request = new ActorLevelUpAptitudeRequestMessage();
                    request.setAptitudeModifications(aptitudesModifications);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(request);
                    UIAptitudeBonusFrame.m_logger.info((Object)("Envoi d'une requ\u00eate pour leveler " + aptitudesModifications.size() + " aptitude(s)."));
                }
                AptitudeDisplayerImpl.getInstance().resetAptitudeLevelModifications(aptitudeType, false);
                this.reinitializeAptitudeButtons(msg3, aptitudeType);
                return false;
            }
            case 17689: {
                final UIAptitudeCommonMessage msg3 = (UIAptitudeCommonMessage)message;
                final AptitudeType aptitudeType = AptitudeType.getFromId(msg3.getByteValue());
                AptitudeDisplayerImpl.getInstance().resetAptitudeLevelModifications(aptitudeType);
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "aptitudes", "breedAptitudes", "commonAptitudes", "availableAptitudePoints", "hasAptitudePoints", "availableCommonPoints");
                this.reinitializeAptitudeButtons(msg3, aptitudeType);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void reinitializeAptitudeButtons(final UIAptitudeCommonMessage msg, final AptitudeType type) {
        if (msg.getButtonContainer() != null) {
            msg.getButtonContainer().setVisible(false);
        }
        if (msg.getValidateButton() != null) {
            ParticleDecorator deco = null;
            switch (type) {
                case COMMON: {
                    deco = this.m_commonDecorator;
                    this.m_commonDecorator = null;
                    break;
                }
                case SPELL: {
                    deco = this.m_spellDecorator;
                    this.m_spellDecorator = null;
                    break;
                }
            }
            if (deco != null) {
                msg.getValidateButton().getAppearance().destroyDecorator(deco);
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("aptitudeBonusDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIAptitudeBonusFrame.INSTANCE);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("aptitudeBonusDialog", Dialogs.getDialogPath("aptitudeBonusDialog"), 32768L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", 0, "aptitudeBonusDialog");
            Xulor.getInstance().putActionClass("wakfu.aptitudeBonus", AptitudeBonusDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("aptitudeBonusDialog");
            Xulor.getInstance().removeActionClass("wakfu.aptitudeBonus");
            this.m_commonDecorator = null;
            this.m_spellDecorator = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIAptitudeBonusFrame();
        m_logger = Logger.getLogger((Class)UIAptitudeBonusFrame.class);
    }
}
