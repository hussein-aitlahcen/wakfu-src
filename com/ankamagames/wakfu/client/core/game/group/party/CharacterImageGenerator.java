package com.ankamagames.wakfu.client.core.game.group.party;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.java.util.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;

public class CharacterImageGenerator
{
    private static final Logger m_logger;
    private static final CharacterImageGenerator m_instance;
    private static final String[] PARTS_TO_REMOVE;
    private static long UID;
    private static long DEFAULT_TEXTURE_NAME;
    private final LongObjectLightWeightMap<ArrayList<Listener>> m_listeners;
    private final LongLongLightWeightMap m_textureNamesMap;
    private final LongObjectLightWeightMap<AnmRastorize.OpenGL> m_rastorizerMap;
    private static final int[] BIG_TRANSFORMATION;
    private static final int[] SMALL_TRANSFORMATION;
    private static final int[] ALTERNATE_ANIMATION_GFX_ID;
    
    private static void addPartsToList(final ArrayList<String> list, final String[] parts) {
        for (final String part : parts) {
            list.add(part);
        }
    }
    
    private CharacterImageGenerator() {
        super();
        this.m_listeners = new LongObjectLightWeightMap<ArrayList<Listener>>();
        this.m_textureNamesMap = new LongLongLightWeightMap();
        this.m_rastorizerMap = new LongObjectLightWeightMap<AnmRastorize.OpenGL>();
    }
    
    public static CharacterImageGenerator getInstance() {
        return CharacterImageGenerator.m_instance;
    }
    
    public void addListener(final long characterId, final Listener l) {
        ArrayList<Listener> list = this.m_listeners.get(characterId);
        if (list == null) {
            list = new ArrayList<Listener>();
            this.m_listeners.put(characterId, list);
        }
        if (!list.contains(l)) {
            list.add(l);
        }
    }
    
    public void removeListener(final long characterId, final Listener l) {
        final ArrayList<Listener> list = this.m_listeners.get(characterId);
        if (list == null) {
            return;
        }
        list.remove(l);
        if (list.size() == 0) {
            this.m_listeners.remove(characterId);
        }
    }
    
    private long generateTextureName() {
        return Engine.getTextureName("CharacterImage-" + CharacterImageGenerator.UID++);
    }
    
    public Texture getCharacterImage(final long id) {
        return this.getCharacterImage(id, false);
    }
    
    public Texture getCharacterImage(final long id, final boolean reset) {
        if (!reset && this.m_textureNamesMap.contains(id)) {
            final Texture tex = TextureManager.getInstance().getTexture(this.m_textureNamesMap.get(id));
            if (tex != null) {
                return tex;
            }
        }
        final AnmRastorize.OpenGL rasterizer = this.m_rastorizerMap.remove(id);
        if (rasterizer != null) {
            rasterizer.cancel();
        }
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(id);
        if (info != null) {
            this.createCharacterImage(info.getActor(), id);
            return null;
        }
        final Texture tex2 = TextureManager.getInstance().getTexture(CharacterImageGenerator.DEFAULT_TEXTURE_NAME);
        if (tex2 != null) {
            return tex2;
        }
        final String url = WakfuConfiguration.getInstance().getIconUrl("defaultSmallMonsterIllustrationPath", "defaultIconPath", new Object[0]);
        return TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), CharacterImageGenerator.DEFAULT_TEXTURE_NAME, url, false);
    }
    
    private void dispatchTextureLoaded(final long characterId, final Texture texture, final String errMsg) {
        final ArrayList<Listener> list = this.m_listeners.get(characterId);
        if (list == null) {
            return;
        }
        for (int i = 0, size = list.size(); i < size; ++i) {
            final Listener l = list.get(i);
            l.onDone(texture, errMsg);
        }
    }
    
    private Image readImage(final byte[] imgData, final long characterId) {
        final Image img = Image.createFromByteArray(imgData, "PNG");
        if (img == null) {
            this.dispatchTextureLoaded(characterId, null, "Impossible de charger l'image.");
            return null;
        }
        return img;
    }
    
    private void createCharacterImage(final Actor actor, final long characterId) {
        float offsetX = 0.7f;
        float offsetY = -6.0f;
        float zoom = 2.0f;
        final int actorGfxId = PrimitiveConverter.getInteger(actor.getGfxId());
        if (Arrays.binarySearch(CharacterImageGenerator.BIG_TRANSFORMATION, actorGfxId) >= 0) {
            offsetX = -0.5f;
            offsetY = -3.0f;
            zoom = 2.0f;
        }
        if (Arrays.binarySearch(CharacterImageGenerator.SMALL_TRANSFORMATION, actorGfxId) >= 0) {
            offsetX = 0.3f;
            offsetY = -1.0f;
            zoom = 2.0f;
        }
        final AnmRastorize.OpenGL rasterizer = new AnmRastorize.OpenGL(64, 64, zoom, offsetX, offsetY, WakfuClientInstance.getInstance().getRenderer());
        this.m_rastorizerMap.put(characterId, rasterizer);
        String animationName;
        if (Arrays.binarySearch(CharacterImageGenerator.ALTERNATE_ANIMATION_GFX_ID, actorGfxId) >= 0) {
            animationName = "1_AnimStatique-HorsCombat";
        }
        else {
            animationName = "1_AnimStatique";
        }
        final AnmInstance anmInstance = actor.getAnmInstance();
        if (anmInstance == null) {
            String name = "";
            if (actor instanceof CharacterActor) {
                final CharacterInfo characterInfo = ((CharacterActor)actor).getCharacterInfo();
                name = characterInfo.toString();
            }
            CharacterImageGenerator.m_logger.error((Object)("On veut g\u00e9n\u00e9rer une image d'un anm null pour actor " + actor.getId() + " name=" + name));
            return;
        }
        rasterizer.prepare(anmInstance, animationName, CharacterImageGenerator.PARTS_TO_REMOVE);
        rasterizer.run("png", new AnmRastorize.OnTerminated() {
            @Override
            public void run(final byte[] imgData) {
                final Image img = CharacterImageGenerator.this.readImage(imgData, characterId);
                if (img != null) {
                    final long textureName = CharacterImageGenerator.this.generateTextureName();
                    final Texture texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), textureName, img, true);
                    CharacterImageGenerator.this.m_textureNamesMap.put(characterId, textureName);
                    img.removeReference();
                    CharacterImageGenerator.this.dispatchTextureLoaded(characterId, texture, null);
                    CharacterImageGenerator.this.m_rastorizerMap.remove(characterId);
                    return;
                }
                CharacterImageGenerator.this.m_rastorizerMap.remove(characterId);
            }
        });
    }
    
    public void deleteCharacterImage(final long id) {
        if (this.m_listeners.contains(id)) {
            this.getCharacterImage(id, true);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)CharacterImageGenerator.class);
        m_instance = new CharacterImageGenerator();
        final ArrayList<String> parts = new ArrayList<String>();
        addPartsToList(parts, AnmPartHelper.getParts("JAMBE"));
        addPartsToList(parts, AnmPartHelper.getParts("PIED"));
        PARTS_TO_REMOVE = parts.toArray(new String[parts.size()]);
        CharacterImageGenerator.UID = Long.MIN_VALUE;
        CharacterImageGenerator.DEFAULT_TEXTURE_NAME = Engine.getTextureName(new StringBuilder("CharacterImage-Default").toString());
        BIG_TRANSFORMATION = new int[] { 1036, 1037, 1038, 1039, 1040 };
        SMALL_TRANSFORMATION = new int[] { 1007, 1011, 1051 };
        ALTERNATE_ANIMATION_GFX_ID = new int[0];
    }
    
    public interface Listener
    {
        void onDone(Texture p0, String p1);
    }
}
