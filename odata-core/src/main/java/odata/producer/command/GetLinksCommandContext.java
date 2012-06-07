package odata.producer.command;

import odata.OEntityId;
import odata.producer.EntityIdResponse;

public interface GetLinksCommandContext extends ProducerCommandContext<EntityIdResponse> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

}
