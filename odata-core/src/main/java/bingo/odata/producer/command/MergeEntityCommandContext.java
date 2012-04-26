package bingo.odata.producer.command;

import bingo.odata.OEntity;

public interface MergeEntityCommandContext extends ProducerCommandContext<Void> {

    String getEntitySetName();

    OEntity getEntity();

}
