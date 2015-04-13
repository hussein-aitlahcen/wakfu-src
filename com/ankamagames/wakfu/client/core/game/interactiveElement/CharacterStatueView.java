package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import org.apache.commons.pool.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import gnu.trove.*;

public class CharacterStatueView extends WakfuClientInteractiveAnimatedElementSceneView
{
    private CharacterActor m_statueActor;
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        if (this.m_statueActor != null) {
            MobileManager.getInstance().removeMobile(this.m_statueActor);
            this.m_statueActor = null;
        }
    }
    
    private void updateAnm() {
        final CharacterDataProvider statue = (CharacterDataProvider)this.m_interactiveElement;
        final AbstractCharacterData characterData = statue.getCharacterData();
        if (this.m_statueActor != null) {
            MobileManager.getInstance().removeMobile(this.m_statueActor);
            this.m_statueActor = null;
        }
        if (characterData == null) {
            return;
        }
        final StatuePlayerCharacter playerCharacter = new StatuePlayerCharacter(characterData);
        playerCharacter.initializeStatue();
        playerCharacter.setDirection(this.m_interactiveElement.getDirection());
        final CharacterActor characterActor = playerCharacter.getActor();
        characterActor.setDirection(this.m_interactiveElement.getDirection());
        characterActor.setWorldPosition(((WakfuClientMapInteractiveElement)this.m_interactiveElement).getWorldX(), ((WakfuClientMapInteractiveElement)this.m_interactiveElement).getWorldY(), ((WakfuClientMapInteractiveElement)this.m_interactiveElement).getAltitude());
        final CharacterStatueProvider provider = statue.getStatueProvider();
        final String animation = provider.getAnimName();
        final TByteIntHashMap equipmentGfxIds = provider.getEquipmentGfxIds();
        characterActor.setAnimation(animation);
        characterActor.setStaticAnimationKey(animation);
        final TByteIntIterator it = equipmentGfxIds.iterator();
        while (it.hasNext()) {
            it.advance();
            characterActor.applyEquipment(it.value(), it.key(), true);
        }
        final Entity entity = characterActor.getEntity();
        entity.m_userFlag1 |= 0x2;
        this.m_statueActor = characterActor;
        MobileManager.getInstance().addMobile(this.m_statueActor);
    }
    
    @Override
    public void update() {
        this.updateAnm();
    }
    
    @Override
    public void setViewGfxId(final int id) {
        if (this.m_interactiveElement == null) {
            return;
        }
        final CharacterDataProvider statue = (CharacterDataProvider)this.m_interactiveElement;
        final AbstractCharacterData characterData = statue.getCharacterData();
        if (characterData == null) {
            super.setViewGfxId(id);
            return;
        }
        this.updateAnm();
    }
    
    public static class CharacterStatueViewFactory extends ObjectFactory<ClientInteractiveElementView>
    {
        private static final MonitoredPool m_pool;
        
        @Override
        public CharacterStatueView makeObject() {
            CharacterStatueView dimensionalBagView;
            try {
                dimensionalBagView = (CharacterStatueView)CharacterStatueViewFactory.m_pool.borrowObject();
                dimensionalBagView.setPool(CharacterStatueViewFactory.m_pool);
            }
            catch (Exception e) {
                CharacterStatueView.m_logger.error((Object)"Erreur lors de l'extraction d'un CharacterStatueViewFactory du pool", (Throwable)e);
                dimensionalBagView = new CharacterStatueView();
            }
            return dimensionalBagView;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<CharacterStatueView>() {
                @Override
                public CharacterStatueView makeObject() {
                    return new CharacterStatueView();
                }
            });
        }
    }
    
    private static class StatueCharacterActor extends CharacterActor
    {
        StatueCharacterActor(final StatuePlayerCharacter character) {
            super(character);
        }
        
        protected AbstractCharacterData getCharacterData() {
            return ((StatuePlayerCharacter)this.getCharacterInfo()).getCharacterData();
        }
        
        @Override
        public boolean isHighlightable() {
            return true;
        }
    }
    
    private static class StatuePlayerCharacter extends PlayerCharacter
    {
        private final AbstractCharacterData m_characterData;
        
        StatuePlayerCharacter(final AbstractCharacterData characterData) {
            super();
            this.m_characterData = characterData;
            this.setId(GUIDGenerator.getGUID());
        }
        
        @Override
        public CharacterActor getActor() {
            if (this.m_actor == null) {
                this.setActor(new StatueCharacterActor(this));
            }
            return this.m_actor;
        }
        
        public AbstractCharacterData getCharacterData() {
            return this.m_characterData;
        }
        
        public void initializeStatue() {
            this.setBreed(AvatarBreedInfoManager.getInstance().getBreedInfo(this.m_characterData.getBreedId()).getBreed());
            this.setSex(this.m_characterData.getSex());
            if (this.m_characterData instanceof Nationable) {
                this.getCitizenComportment().setNation(NationManager.INSTANCE.getNationById(((Nationable)this.m_characterData).getNationId()));
            }
            final CharacterDataAppearance appearance = this.m_characterData.getAppearance();
            this.setSkinColorIndex(appearance.getSkinColorIndex(), appearance.getSkinColorFactor(), true);
            this.setHairColorIndex(appearance.getHairColorIndex(), appearance.getHairColorFactor(), true);
            this.setPupilColorIndex(appearance.getPupilColorIndex(), true);
            this.beginRefreshDisplayEquipment();
            this.setClothIndex(appearance.getClothIndex(), true);
            this.setFaceIndex(appearance.getFaceIndex(), true);
            this.endRefreshDisplayEquipment();
            final AbstractReferenceItem headItem = ReferenceItemManager.getInstance().getReferenceItem(appearance.getHeadRefId());
            final AbstractReferenceItem shouldersItem = ReferenceItemManager.getInstance().getReferenceItem(appearance.getShouldersRefId());
            final AbstractReferenceItem chestItem = ReferenceItemManager.getInstance().getReferenceItem(appearance.getChestRefId());
            final StatueCharacterActor statueActor = (StatueCharacterActor)this.getActor();
            if (headItem != null) {
                statueActor.applyEquipment(headItem, EquipmentPosition.HEAD.m_id);
            }
            if (shouldersItem != null) {
                statueActor.applyEquipment(shouldersItem, EquipmentPosition.SHOULDERS.m_id);
            }
            if (chestItem != null) {
                statueActor.applyEquipment(chestItem, EquipmentPosition.CHEST.m_id);
            }
            this.setName(WakfuTranslator.getInstance().getString("ie.statue", this.m_characterData.getName()));
        }
        
        @Override
        public AbstractMRUAction[] getMRUActions() {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
    }
}
