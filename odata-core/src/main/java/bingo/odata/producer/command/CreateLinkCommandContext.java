package bingo.odata.producer.command;

import bingo.odata.OEntityId;

public interface CreateLinkCommandContext extends ProducerCommandContext<Void> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

    OEntityId getTargetEntity();

}
