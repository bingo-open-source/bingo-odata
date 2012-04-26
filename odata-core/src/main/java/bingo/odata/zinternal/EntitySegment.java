package bingo.odata.zinternal;

import bingo.odata.OEntityKey;

public class EntitySegment {

    public final String     segment;
    public final OEntityKey key;

    public EntitySegment(String segment, OEntityKey key) {
        this.segment = segment;
        this.key = key;
    }

    @Override
    public String toString() {
        return this.segment + (key == null ? "" : key.toKeyString());
    }

}
