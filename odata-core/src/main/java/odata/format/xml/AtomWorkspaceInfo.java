package odata.format.xml;

import java.util.List;

import bingo.lang.Func1;
import bingo.lang.enumerable.IterableEnumerable;

public class AtomWorkspaceInfo {

    public static final Func1<AtomWorkspaceInfo, IterableEnumerable<AtomCollectionInfo>> GET_COLLECTIONS = new Func1<AtomWorkspaceInfo, IterableEnumerable<AtomCollectionInfo>>() {
                                                                                                     public IterableEnumerable<AtomCollectionInfo> apply(AtomWorkspaceInfo workspace) {
                                                                                                         return IterableEnumerable.of(workspace.getCollections());
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
