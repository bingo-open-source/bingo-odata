package odata.producer.command;

import odata.OEntity;

public interface UpdateEntityCommandContext extends ProducerCommandContext<Void> {

    String getEntitySetName();

    OEntity getEntity();

}
