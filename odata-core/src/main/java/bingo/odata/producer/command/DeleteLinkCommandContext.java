package bingo.odata.producer.command;

import bingo.odata.OEntityId;
import bingo.odata.OEntityKey;

public interface DeleteLinkCommandContext extends ProducerCommandContext<Void> {

    OEntityId getSourceEntity();

    String getTargetNavProp();

    OEntityKey getTargetEntityKey();

}
