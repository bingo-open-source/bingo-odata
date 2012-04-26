package bingo.odata.producer.command;

import bingo.odata.OEntityId;
import bingo.odata.OEntityKey;

public interface UpdateLinkCommandContext extends ProducerCommandContext<Void> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

    OEntityKey getOldTargetEntityKey();

    OEntityId getNewTargetEntity();

}
