package bingo.odata.producer.command;

import bingo.odata.OEntity;

public interface UpdateEntityCommandContext extends ProducerCommandContext<Void> {

    String getEntitySetName();

    OEntity getEntity();

}
