package bingo.odata.producer.command;

import bingo.odata.OEntityKey;

public interface DeleteEntityCommandContext extends ProducerCommandContext<Void> {

    String getEntitySetName();

    OEntityKey getEntityKey();

}
