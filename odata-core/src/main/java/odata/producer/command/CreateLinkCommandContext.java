package odata.producer.command;

import odata.OEntityId;

public interface CreateLinkCommandContext extends ProducerCommandContext<Void> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

    OEntityId getTargetEntity();

}
