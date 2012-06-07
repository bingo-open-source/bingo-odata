package odata.producer.command;

import odata.OEntityKey;

public interface DeleteEntityCommandContext extends ProducerCommandContext<Void> {

    String getEntitySetName();

    OEntityKey getEntityKey();

}
