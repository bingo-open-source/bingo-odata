package odata.producer.command;

import odata.OEntity;

public interface MergeEntityCommandContext extends ProducerCommandContext<Void> {

    String getEntitySetName();

    OEntity getEntity();

}
