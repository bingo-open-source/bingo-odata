package odata.format.xml;

import java.util.List;

import bingo.lang.Enumerable;
import bingo.lang.Func1;

public class AtomWorkspaceInfo {

    public static final Func1<AtomWorkspaceInfo, Enumerable<AtomCollectionInfo>> GET_COLLECTIONS = new Func1<AtomWorkspaceInfo, Enumerable<AtomCollectionInfo>>() {
                                                                                                     public Enumerable<AtomCollectionInfo> apply(AtomWorkspaceInfo workspace) {
                                                                                                         return Enumerable.of(workspace.getCollections());
                                                                                                     }
                                                                                                 };

    private final String                                                         title;
    private final List<AtomCollectionInfo>                                       collections;

    AtomWorkspaceInfo(String title, List<AtomCollectionInfo> collections) {
        this.title = title;
        this.collections = collections;
    }

    public String getTitle() {
        return title;
    }

    public List<AtomCollectionInfo> getCollections() {
        return collections;
    }

}
