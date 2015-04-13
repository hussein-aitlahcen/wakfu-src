package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.common.game.item.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;

public class WakfuCharacterFunctionLibrary extends JavaFunctionsLibrary
{
    private static final LuaScriptParameterDescriptor[] APPLY_COLOR_FROM_PARAMS;
    private static final LuaScriptParameterDescriptor[] CREATE_ACTOR_PARAMS;
    private static final LuaScriptParameterDescriptor[] SET_ACTOR_HEIGHT_PARAMS;
    private static final LuaScriptParameterDescriptor[] DELETE_ACTOR_PARAMS;
    private static final LuaScriptParameterDescriptor[] CHANGE_ACTOR_DEFAULT_DELTA_Z_PARAMS;
    private static final LuaScriptParameterDescriptor[] STOP_MOVING_ACTOR_PARAMS;
    private static final LuaScriptParameterDescriptor[] GET_SEX_PARAMS;
    private static final LuaScriptParameterDescriptor[] GET_SEX_RESULTS;
    private static final LuaScriptParameterDescriptor[] APPLY_PART_PARAMS;
    private static final LuaScriptParameterDescriptor[] CHANGE_SKIN_PARAMS;
    private static final LuaScriptParameterDescriptor[] EQUIP_PARAMS;
    private static final LuaScriptParameterDescriptor[] GET_WEAPON_TYPE_PARAMS;
    private static final LuaScriptParameterDescriptor[] GET_WEAPON_TYPE_RESULTS;
    private static final LuaScriptParameterDescriptor[] GET_HANDS_OCCUPED_PARAMS;
    private static final LuaScriptParameterDescriptor[] GET_HANDS_OCCUPED_RESULTS;
    private static final LuaScriptParameterDescriptor[] COPY_ACTOR_FROM_PARAMS;
    private static final WakfuCharacterFunctionLibrary m_instance;
    
    @Override
    public final String getName() {
        return "Actor";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public static WakfuCharacterFunctionLibrary getInstance() {
        return WakfuCharacterFunctionLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new ApplyColorFrom(luaState), new CreateActor(luaState), new DeleteActor(luaState), new SetActorHeight(luaState), new StopMovingActor(luaState), new GetSex(luaState), new ChangeActorDefaultDeltaZ(luaState), new ApplyParts(luaState), new ChangeSkin(luaState), new Equip(luaState), new GetEquippedWeaponType(luaState), new GetHandsOccupedByWeapon(luaState), new CopyActorFrom(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        APPLY_COLOR_FROM_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("fromMobileId", null, LuaScriptParameterType.LONG, false) };
        CREATE_ACTOR_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du personage", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("spriteName", "Nom de Gfx", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("worldX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("worldY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("altitude", "Position z", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("availableDirection", "nb de directions autoris?s pour les d?placements", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("setId", "Id de panoplie ?quip?", LuaScriptParameterType.INTEGER, true) };
        SET_ACTOR_HEIGHT_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("height", null, LuaScriptParameterType.INTEGER, false) };
        DELETE_ACTOR_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du personnage", LuaScriptParameterType.LONG, false) };
        CHANGE_ACTOR_DEFAULT_DELTA_Z_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", "Id du personnage", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("numero du layer", "Nouveau plan", LuaScriptParameterType.INTEGER, false) };
        STOP_MOVING_ACTOR_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", "Id du personnage", LuaScriptParameterType.LONG, false) };
        GET_SEX_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du personnage", LuaScriptParameterType.LONG, false) };
        GET_SEX_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("sex", "Genre du personnage (0 = MALE, 1 = FEMALE)", LuaScriptParameterType.INTEGER, false) };
        APPLY_PART_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("source", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("partNames", null, LuaScriptParameterType.BLOOPS, true) };
        CHANGE_SKIN_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("skinId", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("callback", null, LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("args", null, LuaScriptParameterType.BLOOPS, true) };
        EQUIP_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("source", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("partNames", null, LuaScriptParameterType.BLOOPS, true) };
        GET_WEAPON_TYPE_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.LONG, false) };
        GET_WEAPON_TYPE_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("weaponTypeId", null, LuaScriptParameterType.INTEGER, false) };
        GET_HANDS_OCCUPED_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("characterId", null, LuaScriptParameterType.LONG, false) };
        GET_HANDS_OCCUPED_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("handsOccuped", null, LuaScriptParameterType.INTEGER, false) };
        COPY_ACTOR_FROM_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("srcId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("destId", null, LuaScriptParameterType.LONG, false) };
        m_instance = new WakfuCharacterFunctionLibrary();
    }
    
    private static class ApplyColorFrom extends JavaFunctionEx
    {
        ApplyColorFrom(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "applyColorFrom";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.APPLY_COLOR_FROM_PARAMS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final long fromId = this.getParamLong(1);
            final Mobile mobile = MobileManager.getInstance().getMobile(id);
            final Mobile fromMobile = MobileManager.getInstance().getMobile(fromId);
            if (mobile == null) {
                WakfuCharacterFunctionLibrary.m_logger.error((Object)("impossible de trouver le mobile " + id));
                return;
            }
            if (fromMobile == null) {
                WakfuCharacterFunctionLibrary.m_logger.error((Object)("impossible de trouver le mobile " + fromId));
                return;
            }
            mobile.setCustomColorFrom(fromMobile);
        }
    }
    
    private static class CreateActor extends JavaFunctionEx
    {
        CreateActor(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "createActor";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute un perso dans le monde. Attention le serveur ne connait pas cet actor.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.CREATE_ACTOR_PARAMS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final String spriteName = this.getParamString(1);
            final int worldX = this.getParamInt(2);
            final int worldY = this.getParamInt(3);
            final int altitude = this.getParamInt(4);
            final byte dirCount = (byte)((paramCount >= 6) ? this.getParamInt(5) : 8);
            final int fightId = this.getScriptObject().getFightId();
            final Actor actor = new Actor(id) {
                @Override
                public void onInventoryEvent(final InventoryEvent event) {
                }
                
                @Override
                public int getIconId() {
                    return -1;
                }
                
                @Override
                public String getFormatedOverheadText() {
                    return "";
                }
                
                @Override
                public int getCurrentFightId() {
                    return fightId;
                }
            };
            actor.setSoundValidator(SoundValidatorAll.INSTANCE);
            byte sex = 0;
            if (spriteName != null && spriteName.length() > 0 && spriteName.charAt(spriteName.length() - 1) == '1') {
                sex = 1;
            }
            actor.setGfx(spriteName);
            actor.setDirection(Direction8.SOUTH_EAST);
            actor.setAnimation("AnimStatique");
            actor.setWorldPosition(worldX, worldY, altitude);
            actor.setDeltaZ(LayerOrder.MOBILE.getDeltaZ());
            if (dirCount != 4 && dirCount != 8) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "nombre de direction (" + dirCount + ") inconnu, forc?e ? 8 ");
                actor.setAvailableDirections((byte)8);
            }
            else {
                actor.setAvailableDirections(dirCount);
            }
            if (paramCount >= 7) {
                PlayerCharacter.applySet((short)this.getParamInt(6), actor, sex);
            }
            WakfuGameEntity.checkMob(actor);
            UIFightFrame.getInstance().hide(actor);
            MobileManager.getInstance().addMobile(actor);
            this.getScriptObject().addCreatedObject(actor);
        }
    }
    
    private static class SetActorHeight extends JavaFunctionEx
    {
        SetActorHeight(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setActorHeight";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.SET_ACTOR_HEIGHT_PARAMS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final short height = (short)this.getParamInt(1);
            final Mobile mobile = MobileManager.getInstance().getMobile(id);
            if (mobile == null) {
                WakfuCharacterFunctionLibrary.m_logger.warn((Object)("impossible de trouver le mobile " + id));
            }
            else {
                mobile.setVisualHeight(height);
            }
        }
    }
    
    private static class DeleteActor extends JavaFunctionEx
    {
        DeleteActor(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "deleteActor";
        }
        
        @Override
        public String getDescription() {
            return "Supprime un perso dans le monde. Attention le serveur ne connait pas cet actor.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.DELETE_ACTOR_PARAMS;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            MobileManager.getInstance().removeMobile(mobileId);
            MapManagerHelper.removePoint(0, mobileId, MapManager.getInstance());
        }
    }
    
    private static class ChangeActorDefaultDeltaZ extends JavaFunctionEx
    {
        ChangeActorDefaultDeltaZ(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "changeActorDefaultDeltaZ";
        }
        
        @Override
        public String getDescription() {
            return "Place le mobile sur un autre plan. Le plan par d?faut est le 7.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.CHANGE_ACTOR_DEFAULT_DELTA_Z_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final int nLayer = this.getParamInt(1);
            final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(mobileId);
            if (info == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "Mobile introuvable " + mobileId);
                return;
            }
            info.getActor().setDeltaZ(nLayer);
        }
    }
    
    private static class StopMovingActor extends JavaFunctionEx
    {
        StopMovingActor(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "stopMovingActor";
        }
        
        @Override
        public String getDescription() {
            return "Arr?te un personnage";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.STOP_MOVING_ACTOR_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(mobileId);
            if (info == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "Mobile introuvable " + mobileId);
                return;
            }
            info.getActor().stopMoving();
        }
    }
    
    private static class GetSex extends JavaFunctionEx
    {
        GetSex(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getSex";
        }
        
        @Override
        public String getDescription() {
            return "Renvoi le genre d'un personnage";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.GET_SEX_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return WakfuCharacterFunctionLibrary.GET_SEX_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(mobileId);
            if (info == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "Mobile introuvable " + mobileId);
                this.addReturnNilValue();
                return;
            }
            this.addReturnValue(info.getSex());
        }
    }
    
    private static class ApplyParts extends JavaFunctionEx
    {
        ApplyParts(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPart";
        }
        
        @Override
        public String getDescription() {
            return "Positionne une portion d'un anm dans l'anm courant";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.APPLY_PART_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final String anmSource = this.getParamString(1);
            final String[] partNames = new String[paramCount - 2];
            for (int i = 2; i < paramCount; ++i) {
                partNames[i - 2] = this.getParamString(i);
            }
            final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
            if (mobile != null) {
                mobile.applyParts(anmSource, AnmPartHelper.getParts(partNames));
            }
            else {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le mobile " + mobileId + " n'existe pas ");
            }
        }
    }
    
    private static class ChangeSkin extends JavaFunctionEx
    {
        ChangeSkin(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "changeSkin";
        }
        
        @Override
        public String getDescription() {
            return "Change le skin du mobile";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.CHANGE_SKIN_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final String anmSource = this.getParamString(1);
            final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
            if (mobile instanceof Actor) {
                final Actor actor = (Actor)mobile;
                actor.setGfx(anmSource);
                if (paramCount > 2) {
                    final LuaScript script = this.getScriptObject();
                    final String func = this.getParamString(2);
                    final LuaValue[] params = this.getParams(3, paramCount);
                    final int taskId = script.registerWaitingTask(func, params);
                    mobile.onAnmLoaded(new Runnable() {
                        @Override
                        public void run() {
                            script.executeWaitingTask(taskId);
                        }
                    });
                }
            }
            else {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le mobile " + mobileId + " n'existe pas ");
            }
        }
    }
    
    private static class Equip extends JavaFunctionEx
    {
        Equip(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "equip";
        }
        
        @Override
        public String getDescription() {
            return "Applique un ?quipement dans un slot sur un mobile";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.EQUIP_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final String anmSource = this.getParamString(1);
            final String[] partNames = new String[paramCount - 2];
            for (int i = 2; i < paramCount; ++i) {
                partNames[i - 2] = this.getParamString(i);
            }
            final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
            if (mobile != null) {
                try {
                    String equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
                    equipmentFileName = String.format(equipmentFileName, anmSource);
                    mobile.applyParts(equipmentFileName, AnmPartHelper.getParts(partNames));
                }
                catch (PropertyException e) {
                    WakfuCharacterFunctionLibrary.m_logger.error((Object)"Erreur au chargement d'une propri?t?", (Throwable)e);
                }
            }
            else {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le mobile " + mobileId + " n'existe pas ");
            }
        }
    }
    
    private static class GetEquippedWeaponType extends JavaFunctionEx
    {
        GetEquippedWeaponType(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getEquippedWeaponType";
        }
        
        @Override
        public String getDescription() {
            return "Renvoie le type de l'arme equipp?e";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.GET_WEAPON_TYPE_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return WakfuCharacterFunctionLibrary.GET_WEAPON_TYPE_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long characterId = this.getParamLong(0);
            final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(characterId);
            if (characterInfo == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le characterInfo " + characterId + " n'existe pas ");
                this.addReturnValue(0);
                return;
            }
            final TByteIntHashMap equipements = characterInfo.getEquipmentAppearance();
            if (equipements == null) {
                this.addReturnValue(0);
                return;
            }
            final int weaponId = equipements.get(EquipmentPosition.FIRST_WEAPON.m_id);
            final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(weaponId);
            final int weaponTypeId = WeaponAttack.getWeaponTypeId(referenceItem.getItemType());
            if (weaponTypeId == 237) {
                this.addReturnValue(0);
                return;
            }
            this.addReturnValue(weaponTypeId);
        }
    }
    
    private static class GetHandsOccupedByWeapon extends JavaFunctionEx
    {
        GetHandsOccupedByWeapon(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getHandsOccupedByWeapon";
        }
        
        @Override
        public String getDescription() {
            return "Renvoie le nombre de mains utilis?es par l'arme actuelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.GET_HANDS_OCCUPED_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return WakfuCharacterFunctionLibrary.GET_HANDS_OCCUPED_RESULTS;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long characterId = this.getParamLong(0);
            final CharacterInfo characterInfo = CharacterInfoManager.getInstance().getCharacter(characterId);
            if (characterInfo == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le characterInfo " + characterId + " n'existe pas ");
                this.addReturnValue(0);
                return;
            }
            final TByteIntHashMap equipements = characterInfo.getEquipmentAppearance();
            if (equipements == null) {
                this.addReturnValue(0);
                return;
            }
            final int weaponId = equipements.get(EquipmentPosition.FIRST_WEAPON.m_id);
            final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(weaponId);
            final int weaponTypeId = WeaponAttack.getWeaponTypeId(referenceItem.getItemType());
            if (weaponTypeId == 237) {
                this.addReturnValue(0);
                return;
            }
            this.addReturnValue(referenceItem.isTwoHandedWeapon() ? 2 : 1);
        }
    }
    
    private static class CopyActorFrom extends JavaFunctionEx
    {
        CopyActorFrom(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "copyActorFrom";
        }
        
        @Override
        public String getDescription() {
            return "Recopie un mobile dans un autre mobile";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return WakfuCharacterFunctionLibrary.COPY_ACTOR_FROM_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long srcId = this.getParamLong(0);
            final long destId = this.getParamLong(1);
            final Mobile src = MobileManager.getInstance().getMobile(srcId);
            final Mobile dest = MobileManager.getInstance().getMobile(destId);
            if (src == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le mobile " + srcId + " n'existe pas ");
                return;
            }
            if (dest == null) {
                this.writeError(WakfuCharacterFunctionLibrary.m_logger, "le mobile " + destId + " n'existe pas ");
                return;
            }
            dest.copyData(src);
        }
    }
}
