package com.ankamagames.wakfu.client.core.game.actor;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class ActorUtils
{
    public static final int NON_PLAYER_GFX_MINIMUM_ID = 1000000;
    public static final int PET_GFX_MINIMUM_ID = 990000;
    
    public static int extractSexFromGfxLol(final int gfxId) {
        return gfxId % 10;
    }
    
    public static int extractBreedFromGfxLol(final int gfxId) {
        return gfxId / 10;
    }
    
    public static boolean isNpcGfx(final int gfxId) {
        return gfxId >= 1000000;
    }
    
    public static boolean isPetGfx(final int gfxId) {
        return gfxId >= 990000 && !isNpcGfx(gfxId);
    }
    
    public static CharacterActor getActorFromCharacterData(final AbstractCharacterData abstractCharacterData) {
        final PlayerCharacter playerCharacter = new PlayerCharacter() {
            @Override
            public void setActor(final CharacterActor actor) {
                this.m_actor = actor;
            }
            
            @Override
            public void onCheckIn() {
                this.m_actor.release();
                super.onCheckIn();
            }
        };
        playerCharacter.setBreed(AvatarBreedInfoManager.getInstance().getBreedInfo(abstractCharacterData.getBreedId()).getBreed());
        playerCharacter.setSex(abstractCharacterData.getSex());
        final CharacterDataAppearance appearance = abstractCharacterData.getAppearance();
        playerCharacter.setSkinColorIndex(appearance.getSkinColorIndex(), appearance.getSkinColorFactor(), true);
        playerCharacter.setHairColorIndex(appearance.getHairColorIndex(), appearance.getHairColorFactor(), true);
        playerCharacter.setPupilColorIndex(appearance.getPupilColorIndex(), true);
        playerCharacter.beginRefreshDisplayEquipment();
        playerCharacter.setClothIndex(appearance.getClothIndex(), true);
        playerCharacter.setFaceIndex(appearance.getFaceIndex(), true);
        playerCharacter.endRefreshDisplayEquipment();
        final AbstractReferenceItem headItem = ReferenceItemManager.getInstance().getReferenceItem(appearance.getHeadRefId());
        final AbstractReferenceItem shouldersItem = ReferenceItemManager.getInstance().getReferenceItem(appearance.getShouldersRefId());
        final AbstractReferenceItem chestItem = ReferenceItemManager.getInstance().getReferenceItem(appearance.getChestRefId());
        final CharacterActor characterActor = playerCharacter.getActor();
        if (headItem != null) {
            characterActor.applyEquipment(headItem, EquipmentPosition.HEAD.m_id);
        }
        if (shouldersItem != null) {
            characterActor.applyEquipment(shouldersItem, EquipmentPosition.SHOULDERS.m_id);
        }
        if (chestItem != null) {
            characterActor.applyEquipment(chestItem, EquipmentPosition.CHEST.m_id);
        }
        characterActor.setAnimation("AnimStatique");
        characterActor.setStaticAnimationKey("AnimStatique");
        return playerCharacter.getActor();
    }
    
    public static String getPathForGfx(final int gfxId) {
        if (isNpcGfx(gfxId)) {
            return "npcGfxPath";
        }
        if (isPetGfx(gfxId)) {
            return "petGfxPath";
        }
        return "playerGfxPath";
    }
}
