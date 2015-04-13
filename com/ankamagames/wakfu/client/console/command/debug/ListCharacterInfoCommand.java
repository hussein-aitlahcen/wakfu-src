package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class ListCharacterInfoCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.trace("Liste des personnages enregistr\u00e9s : ");
        final TIntObjectHashMap<ArrayList<CharacterInfo>> map = new TIntObjectHashMap<ArrayList<CharacterInfo>>();
        CharacterInfoManager.getInstance().forEachCharacter(new TLongObjectProcedure<CharacterInfo>() {
            @Override
            public boolean execute(final long id, final CharacterInfo characterInfo) {
                final int familyId = characterInfo.getBreed().getFamilyId();
                ArrayList<CharacterInfo> list = map.get(familyId);
                if (list == null) {
                    list = new ArrayList<CharacterInfo>();
                    map.put(familyId, list);
                }
                list.add(characterInfo);
                return true;
            }
        });
        final ArrayList<FamilyPack> packs = new ArrayList<FamilyPack>();
        final TIntObjectIterator<ArrayList<CharacterInfo>> it2 = map.iterator();
        while (it2.hasNext()) {
            it2.advance();
            final ArrayList<CharacterInfo> list = it2.value();
            packs.addAll(this.computeFamily(list));
        }
        final int x = WakfuGameEntity.getInstance().getLocalPlayer().getPositionConst().getX();
        final int y = WakfuGameEntity.getInstance().getLocalPlayer().getPositionConst().getY();
        for (int i = 0, size = packs.size(); i < size; ++i) {
            final FamilyPack pack = packs.get(i);
            final int dist = (int)Vector2.sqrLength(pack.getX() - x, pack.getY() - y);
            if (dist <= 200) {
                manager.trace(" Liste pour la famille " + pack.getFamilyId() + " (pos : x=" + pack.getX() + ", y=" + pack.getY() + ")");
                int num = 0;
                for (int j = 0, jsize = pack.getInfos().size(); j < jsize; ++j) {
                    final CharacterInfo characterInfo = pack.getInfos().get(j);
                    manager.trace("    " + ++num + "- " + characterInfo.getName());
                }
            }
        }
    }
    
    private ArrayList<FamilyPack> computeFamily(final ArrayList<CharacterInfo> list) {
        final ArrayList<FamilyPack> packList = new ArrayList<FamilyPack>();
        for (int i = list.size() - 1; i >= 0; --i) {
            final CharacterInfo info = list.get(i);
            FamilyPack found = null;
            for (int j = packList.size() - 1; j >= 0; --j) {
                final FamilyPack pack = packList.get(j);
                if (pack.isIsPack(info)) {
                    found = pack;
                    break;
                }
            }
            if (found == null) {
                found = new FamilyPack();
                packList.add(found);
            }
            found.addToPack(info);
        }
        return packList;
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    private static class FamilyPack
    {
        public static final int MAX_DIST = 40;
        private int m_familyId;
        private int m_weight;
        private int m_x;
        private int m_y;
        private ArrayList<CharacterInfo> m_infos;
        
        private FamilyPack() {
            super();
            this.m_infos = new ArrayList<CharacterInfo>();
        }
        
        public boolean isIsPack(final CharacterInfo info) {
            if (this.m_weight == 0) {
                return true;
            }
            final Point3 pos = info.getPositionConst();
            final float dist = Vector2.sqrLength(pos.getX() - this.m_x, pos.getY() - this.m_y);
            return dist <= 40.0f;
        }
        
        public void addToPack(final CharacterInfo info) {
            ++this.m_weight;
            this.m_infos.add(info);
            final Point3 pos = info.getPositionConst();
            if (this.m_weight == 1) {
                this.m_x = pos.getX();
                this.m_y = pos.getY();
                this.m_familyId = info.getBreed().getFamilyId();
                return;
            }
            final int deltaX = pos.getX() - this.m_x;
            final int deltaY = pos.getY() - this.m_y;
            this.m_x += deltaX / this.m_weight;
            this.m_y += deltaY / this.m_weight;
        }
        
        public int getFamilyId() {
            return this.m_familyId;
        }
        
        public int getWeight() {
            return this.m_weight;
        }
        
        public int getX() {
            return this.m_x;
        }
        
        public int getY() {
            return this.m_y;
        }
        
        public ArrayList<CharacterInfo> getInfos() {
            return this.m_infos;
        }
    }
}
