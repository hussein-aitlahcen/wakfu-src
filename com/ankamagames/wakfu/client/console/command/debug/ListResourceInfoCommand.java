package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import gnu.trove.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ListResourceInfoCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        manager.trace("Liste des ressources enregistr\u00e9s : ");
        final TShortObjectHashMap<ArrayList<Resource>> resourcesByType = new TShortObjectHashMap<ArrayList<Resource>>();
        final Point3 localPlayerPos = WakfuGameEntity.getInstance().getLocalPlayer().getPositionConst();
        final int x = localPlayerPos.getX();
        final int y = localPlayerPos.getY();
        ResourceManager.getInstance().foreachResource(new TObjectProcedure<Resource>() {
            @Override
            public boolean execute(final Resource res) {
                final int dist = (int)Vector2.sqrLength(res.getWorldCellX() - x, res.getWorldCellY() - y);
                if (dist > 200) {
                    return true;
                }
                final short type = res.getReferenceResource().getResourceType();
                ArrayList<Resource> list = resourcesByType.get(type);
                if (list == null) {
                    list = new ArrayList<Resource>();
                    resourcesByType.put(type, list);
                }
                list.add(res);
                return true;
            }
        });
        final ArrayList<ResourcePack> packs = new ArrayList<ResourcePack>();
        resourcesByType.forEachValue(new TObjectProcedure<ArrayList<Resource>>() {
            @Override
            public boolean execute(final ArrayList<Resource> list) {
                packs.addAll(ListResourceInfoCommand.this.computeType(list));
                return true;
            }
        });
        for (int i = 0, size = packs.size(); i < size; ++i) {
            final ResourcePack pack = packs.get(i);
            manager.trace(" Liste pour le type " + pack.getResourceType() + " (pos : x=" + pack.getX() + ", y=" + pack.getY() + ")");
            int num = 0;
            for (int j = 0, jsize = pack.getResources().size(); j < jsize; ++j) {
                final Resource res = pack.getResources().get(j);
                manager.trace("    " + ++num + "- " + res.getReferenceResource().getResourceName());
            }
        }
    }
    
    private ArrayList<ResourcePack> computeType(final ArrayList<Resource> list) {
        final ArrayList<ResourcePack> packList = new ArrayList<ResourcePack>();
        for (int i = list.size() - 1; i >= 0; --i) {
            final Resource resource = list.get(i);
            ResourcePack found = null;
            for (int j = packList.size() - 1; j >= 0; --j) {
                final ResourcePack pack = packList.get(j);
                if (pack.isIsPack(resource)) {
                    found = pack;
                    break;
                }
            }
            if (found == null) {
                found = new ResourcePack();
                packList.add(found);
            }
            found.addToPack(resource);
        }
        return packList;
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    private static class ResourcePack
    {
        public static final int MAX_DIST = 10;
        private short m_resourceType;
        private int m_weight;
        private int m_x;
        private int m_y;
        private ArrayList<Resource> m_resources;
        
        private ResourcePack() {
            super();
            this.m_resources = new ArrayList<Resource>();
        }
        
        public boolean isIsPack(final Resource res) {
            if (this.m_weight == 0) {
                return true;
            }
            final int x = res.getWorldCellX();
            final int y = res.getWorldCellY();
            for (int i = this.m_resources.size() - 1; i >= 0; --i) {
                final Resource res2 = this.m_resources.get(i);
                final float dist = Vector2.sqrLength(x - res2.getWorldCellX(), y - res2.getWorldCellY());
                if (dist <= 10.0f) {
                    return true;
                }
            }
            return false;
        }
        
        public void addToPack(final Resource res) {
            ++this.m_weight;
            this.m_resources.add(res);
            final int x = res.getWorldCellX();
            final int y = res.getWorldCellY();
            if (this.m_weight == 1) {
                this.m_x = x;
                this.m_y = y;
                this.m_resourceType = res.getReferenceResource().getResourceType();
                return;
            }
            final int deltaX = x - this.m_x;
            final int deltaY = x - this.m_y;
            this.m_x += deltaX / this.m_weight;
            this.m_y += deltaY / this.m_weight;
        }
        
        public int getResourceType() {
            return this.m_resourceType;
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
        
        public ArrayList<Resource> getResources() {
            return this.m_resources;
        }
    }
}
