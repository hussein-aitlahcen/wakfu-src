package com.ankamagames.xulor2.core;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.xulor2.*;
import java.util.*;

public class TreeDepthManager
{
    private static final Logger m_logger;
    private static TreeDepthManager m_instance;
    private static final boolean DISPLAY_PROCESSED_DIALOGS = false;
    private static final THashSet<String> m_processedIds;
    private static final int TREE_INIT_SIZE = 100;
    private boolean m_needsSorting;
    private ArrayList<BasicElement> m_treeDepthSortedListPre;
    private ArrayList<BasicElement> m_addNextProcessPre;
    private ArrayList<BasicElement> m_addAtTheEndOfProcessPre;
    private ArrayList<BasicElement> m_treeDepthSortedListMiddle;
    private ArrayList<BasicElement> m_addNextProcessMiddle;
    private ArrayList<BasicElement> m_addAtTheEndOfProcessMiddle;
    private ArrayList<BasicElement> m_treeDepthSortedListPost;
    private ArrayList<BasicElement> m_addNextProcessPost;
    private ArrayList<BasicElement> m_addAtTheEndOfProcessPost;
    private int m_currentProcessPhase;
    public static final int SORT = 0;
    public static final int PRE_PROCESS = 1;
    public static final int MIDDLE_PROCESS = 2;
    public static final int POST_PROCESS = 3;
    public static final int NOT_PROCESSING = 4;
    private int m_frameCounter;
    private boolean m_needAnotherProcess;
    
    private TreeDepthManager() {
        super();
        this.m_needsSorting = false;
        this.m_treeDepthSortedListPre = new ArrayList<BasicElement>(100);
        this.m_addNextProcessPre = new ArrayList<BasicElement>(100);
        this.m_addAtTheEndOfProcessPre = new ArrayList<BasicElement>(100);
        this.m_treeDepthSortedListMiddle = new ArrayList<BasicElement>(100);
        this.m_addNextProcessMiddle = new ArrayList<BasicElement>(100);
        this.m_addAtTheEndOfProcessMiddle = new ArrayList<BasicElement>(100);
        this.m_treeDepthSortedListPost = new ArrayList<BasicElement>(100);
        this.m_addNextProcessPost = new ArrayList<BasicElement>(100);
        this.m_addAtTheEndOfProcessPost = new ArrayList<BasicElement>(100);
        this.m_currentProcessPhase = 4;
        this.m_frameCounter = 0;
        this.m_needAnotherProcess = false;
    }
    
    public static TreeDepthManager getInstance() {
        return TreeDepthManager.m_instance;
    }
    
    public void setToDirty() {
        this.m_needsSorting = true;
    }
    
    public boolean isProcessing() {
        return this.m_currentProcessPhase != 4;
    }
    
    private void addToList(final BasicElement e, final ArrayList<BasicElement> endOf, final ArrayList<BasicElement> regular, final int addedAtPhase) {
        if (!e.isUnloading() && !e.isInTreeDepthManager()) {
            if (this.m_currentProcessPhase == addedAtPhase) {
                if (!endOf.contains(e)) {
                    endOf.add(e);
                }
            }
            else if (!regular.contains(e)) {
                regular.add(e);
            }
        }
    }
    
    private void addToNext(final BasicElement e, final ArrayList<BasicElement> next) {
        if (!e.isUnloading() && !e.isATemplate() && !e.isAddedNextInTreeDepthManager() && !next.contains(e)) {
            next.add(e);
        }
    }
    
    public void addToNextPreProcess(final BasicElement e) {
        this.addToNext(e, this.m_addNextProcessPre);
    }
    
    public void addToNextMiddleProcess(final BasicElement e) {
        this.addToNext(e, this.m_addNextProcessMiddle);
    }
    
    public void addToNextPostProcess(final BasicElement e) {
        this.addToNext(e, this.m_addNextProcessPost);
    }
    
    public void addToPreProcessList(final BasicElement e) {
        if (!e.isATemplate()) {
            if (this.m_currentProcessPhase == 4 || (this.m_currentProcessPhase <= 1 && e.getLastPreProcessFrame() != this.m_frameCounter)) {
                this.addToList(e, this.m_addAtTheEndOfProcessPre, this.m_treeDepthSortedListPre, 1);
            }
            else {
                this.addToNextPreProcess(e);
            }
        }
    }
    
    public void addToMiddleProcessList(final BasicElement e) {
        if (!e.isATemplate()) {
            if (this.m_currentProcessPhase == 4 || (this.m_currentProcessPhase <= 2 && e.getLastMiddleProcessFrame() != this.m_frameCounter)) {
                this.addToList(e, this.m_addAtTheEndOfProcessMiddle, this.m_treeDepthSortedListMiddle, 2);
            }
            else {
                this.addToNextMiddleProcess(e);
            }
        }
    }
    
    public void addToPostProcessList(final BasicElement e) {
        if (!e.isATemplate()) {
            if (this.m_currentProcessPhase == 4 || (this.m_currentProcessPhase <= 3 && e.getLastPostProcessFrame() != this.m_frameCounter)) {
                this.addToList(e, this.m_addAtTheEndOfProcessPost, this.m_treeDepthSortedListPost, 3);
            }
            else {
                this.addToNextPostProcess(e);
            }
        }
    }
    
    private int updateTreeIndex(final EventDispatcher e, int index) {
        e.setTreePosition(index);
        if (e.m_children != null) {
            for (int size = e.m_children.size(), i = 0; i < size; ++i) {
                index = this.updateTreeIndex(e.m_children.get(i), index + 1);
            }
        }
        return index;
    }
    
    public void askForAnotherProcess() {
        this.m_needAnotherProcess = true;
    }
    
    private void addMapId(final BasicElement ed) {
        if (ed.getElementType() == ElementType.EVENT_DISPATCHER) {
            final EventDispatcher eventDispatcher = (EventDispatcher)ed;
            final ElementMap map = eventDispatcher.getElementMap();
            if (map != null) {
                TreeDepthManager.m_processedIds.add(map.getRootId());
            }
        }
    }
    
    public void processAll(final int deltaTime) {
        this.m_needAnotherProcess = true;
        while (this.m_needAnotherProcess) {
            this.m_needAnotherProcess = false;
            this.m_currentProcessPhase = 0;
            this.m_frameCounter = (this.m_frameCounter + 1) % Integer.MAX_VALUE;
            if (this.m_needsSorting) {
                this.updateTreeIndex(Xulor.getInstance().getScene().getMasterRootContainer(), 0);
                this.m_needsSorting = false;
            }
            XulorScene.resetProcessCheck();
            this.m_currentProcessPhase = 1;
            while (this.m_treeDepthSortedListPre.size() != 0) {
                final int treeSize = this.m_treeDepthSortedListPre.size();
                Collections.sort(this.m_treeDepthSortedListPre, TreeDepthComparator.COMPARATOR);
                for (int i = 0; i < treeSize; ++i) {
                    final BasicElement ed = this.m_treeDepthSortedListPre.get(i);
                    ed.setLastPreProcessFrame(this.m_frameCounter);
                    ed.doPreProcess(deltaTime);
                }
                this.m_treeDepthSortedListPre.clear();
                for (int addEndProcessPreSize = this.m_addAtTheEndOfProcessPre.size(), j = 0; j < addEndProcessPreSize; ++j) {
                    this.m_treeDepthSortedListPre.add(this.m_addAtTheEndOfProcessPre.get(j));
                }
                this.m_addAtTheEndOfProcessPre.clear();
            }
            this.m_currentProcessPhase = 2;
            while (this.m_treeDepthSortedListMiddle.size() != 0) {
                final int treeSizeMiddle = this.m_treeDepthSortedListMiddle.size();
                Collections.sort(this.m_treeDepthSortedListMiddle, TreePositionComparator.COMPARATOR);
                for (int i = 0; i < treeSizeMiddle; ++i) {
                    final BasicElement ed = this.m_treeDepthSortedListMiddle.get(i);
                    ed.setLastMiddleProcessFrame(this.m_frameCounter);
                    ed.doMiddleProcess(deltaTime);
                }
                this.m_treeDepthSortedListMiddle.clear();
                for (int addEndProcessMiddleSize = this.m_addAtTheEndOfProcessMiddle.size(), j = 0; j < addEndProcessMiddleSize; ++j) {
                    this.m_treeDepthSortedListMiddle.add(this.m_addAtTheEndOfProcessMiddle.get(j));
                }
                this.m_addAtTheEndOfProcessMiddle.clear();
            }
            this.m_currentProcessPhase = 3;
            while (this.m_treeDepthSortedListPost.size() != 0) {
                final int treeSizePost = this.m_treeDepthSortedListPost.size();
                Collections.sort(this.m_treeDepthSortedListPost, TreePositionComparator.COMPARATOR);
                for (int i = 0; i < treeSizePost; ++i) {
                    final BasicElement ed = this.m_treeDepthSortedListPost.get(i);
                    ed.setLastPostProcessFrame(this.m_frameCounter);
                    ed.doPostProcess(deltaTime);
                }
                this.m_treeDepthSortedListPost.clear();
                for (int addProcessPostSize = this.m_addAtTheEndOfProcessPost.size(), j = 0; j < addProcessPostSize; ++j) {
                    this.m_treeDepthSortedListPost.add(this.m_addAtTheEndOfProcessPost.get(j));
                }
                this.m_addAtTheEndOfProcessPost.clear();
            }
            this.m_currentProcessPhase = 4;
            for (int size = this.m_addNextProcessPre.size(), i = 0; i < size; ++i) {
                final BasicElement e = this.m_addNextProcessPre.get(i);
                this.m_treeDepthSortedListPre.add(e);
            }
            this.m_addNextProcessPre.clear();
            for (int size = this.m_addNextProcessMiddle.size(), i = 0; i < size; ++i) {
                final BasicElement e = this.m_addNextProcessMiddle.get(i);
                this.m_treeDepthSortedListMiddle.add(e);
            }
            this.m_addNextProcessMiddle.clear();
            for (int size = this.m_addNextProcessPost.size(), i = 0; i < size; ++i) {
                final BasicElement e = this.m_addNextProcessPost.get(i);
                this.m_treeDepthSortedListPost.add(e);
            }
            this.m_addNextProcessPost.clear();
        }
    }
    
    private void logProcessedDialogs() {
        if (TreeDepthManager.m_processedIds.size() > 0) {
            TreeDepthManager.m_logger.info((Object)("[Frame " + this.m_frameCounter + "] Dialog process\u00e9es : "));
            for (final String id : TreeDepthManager.m_processedIds) {
                TreeDepthManager.m_logger.info((Object)("\t\t" + id));
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)TreeDepthManager.class);
        TreeDepthManager.m_instance = new TreeDepthManager();
        m_processedIds = new THashSet<String>();
    }
    
    private static class TreeDepthComparator implements Comparator<BasicElement>
    {
        public static final TreeDepthComparator COMPARATOR;
        
        @Override
        public int compare(final BasicElement o1, final BasicElement o2) {
            return o2.getTreeDepth() - o1.getTreeDepth();
        }
        
        static {
            COMPARATOR = new TreeDepthComparator();
        }
    }
    
    private static class TreePositionComparator implements Comparator<BasicElement>
    {
        public static final TreePositionComparator COMPARATOR;
        
        @Override
        public int compare(final BasicElement o1, final BasicElement o2) {
            return o1.getTreePosition() - o2.getTreePosition();
        }
        
        static {
            COMPARATOR = new TreePositionComparator();
        }
    }
}
