package bingo.odata.producer.command;

import bingo.odata.OEntityId;
import bingo.odata.producer.EntityIdResponse;

public interface GetLinksCommandContext extends ProducerCommandContext<EntityIdResponse> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

}
