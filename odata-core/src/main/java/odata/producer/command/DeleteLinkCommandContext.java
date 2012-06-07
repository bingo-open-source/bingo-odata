package odata.producer.command;

import odata.OEntityId;
import odata.OEntityKey;

public interface DeleteLinkCommandContext extends ProducerCommandContext<Void> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

    OEntityKey getTargetEntityKey();

}
