package com.ankamagames.wakfu.client.core.script.fightLibrary.cast;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.common.datas.Breed.*;

final class GetCasterInformation extends CastFunction
{
    private static final Logger m_logger;
    private static final String NAME = "getCasterInformation";
    private static final String DESC = "Permet de r?cup?rer un objet contenant des informations sur le lanceur";
    private static final LuaScriptParameterDescriptor[] GET_CASTER_INFORMATION_RESULTS;
    
    GetCasterInformation(final LuaState luaState, final AbstractFightCastAction castAction) {
        super(luaState, castAction);
    }
    
    @Override
    public String getName() {
        return "getCasterInformation";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetCasterInformation.GET_CASTER_INFORMATION_RESULTS;
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer un objet contenant des informations sur le lanceur";
    }
    
    public void run(final int paramCount) throws LuaException {
        final BasicCharacterInfo fighter = this.m_castAction.getFighterById(this.m_castAction.getInstigatorId());
        if (fighter != null) {
            CasterInformation casterInfo = null;
            switch (fighter.getType()) {
                case 2: {
                    casterInfo = new CasterInformation(fighter.getBreed(), fighter.getHeight(), fighter.getControllerName(), fighter.getSex(), fighter.getType(), 0);
                    break;
                }
                case 0: {
                    casterInfo = new CasterInformation(fighter.getBreed(), fighter.getHeight(), fighter.getControllerName(), fighter.getSex(), fighter.getType(), 0);
                    break;
                }
                case 1:
                case 5: {
                    casterInfo = new CasterInformation(fighter.getBreed(), fighter.getHeight(), fighter.getControllerName(), fighter.getSex(), fighter.getType(), ((NonPlayerCharacter)fighter).getTypeId());
                    break;
                }
                default: {
                    casterInfo = null;
                    break;
                }
            }
            if (casterInfo != null) {
                this.addReturnValue(casterInfo);
            }
            else {
                GetCasterInformation.m_logger.error((Object)"pas de caster");
                this.addReturnNilValue();
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)GetCasterInformation.class);
        GET_CASTER_INFORMATION_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("casterInfo", null, LuaScriptParameterType.OBJECT, false) };
    }
    
    private static class CasterInformation
    {
        protected int m_typeId;
        protected byte m_type;
        protected String m_name;
        protected int m_height;
        protected Breed m_breed;
        protected byte m_sex;
        
        public CasterInformation(final Breed breed, final int height, final String name, final byte sex, final byte type, final int typeId) {
            super();
            this.m_breed = breed;
            this.m_height = height;
            this.m_name = name;
            this.m_sex = sex;
            this.m_type = type;
            this.m_typeId = typeId;
        }
        
        public Breed getBreed() {
            return this.m_breed;
        }
        
        public int getHeight() {
            return this.m_height;
        }
        
        public String getName() {
            return this.m_name;
        }
        
        public byte getSex() {
            return this.m_sex;
        }
        
        public boolean isMonster() {
            return this.m_type == 1;
        }
        
        public boolean isSummon() {
            return this.m_type == 2;
        }
        
        public boolean isPlayer() {
            return this.m_type == 0;
        }
        
        public boolean isCompanion() {
            return this.m_type == 5;
        }
        
        public int getTypeId() {
            return this.m_typeId;
        }
    }
}
