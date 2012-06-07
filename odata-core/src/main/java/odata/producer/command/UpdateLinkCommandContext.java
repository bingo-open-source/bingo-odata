package odata.producer.command;

import odata.OEntityId;
import odata.OEntityKey;

public interface UpdateLinkCommandContext extends ProducerCommandContext<Void> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

    OEntityKey getOldTargetEntityKey();

    OEntityId getNewTargetEntity();

}
