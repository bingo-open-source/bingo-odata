package bingo.odata.producer.command;

import java.util.Map;

import bingo.odata.OEntity;
import bingo.odata.OEntityId;
import bingo.odata.OEntityKey;
import bingo.odata.OFunctionParameter;
import bingo.odata.command.Command;
import bingo.odata.command.CommandContext;
import bingo.odata.command.CommandExecution;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.producer.EntityQueryInfo;
import bingo.odata.producer.QueryInfo;

public interface CommandProducerBackend {

    CommandExecution getCommandExecution();

    <TContext extends CommandContext> Command<TContext> getCommand(Class<TContext> contextType);

    GetMetadataCommandContext newGetMetadataCommandContext();

    GetMetadataProducerCommandContext newGetMetadataProducerCommandContext();

    GetEntitiesCommandContext newGetEntitiesCommandContext(String entitySetName, QueryInfo queryInfo);

    GetEntitiesCountCommandContext newGetEntitiesCountCommandContext(String entitySetName, QueryInfo queryInfo);

    GetEntityCommandContext newGetEntityCommandContext(String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo);

    GetNavPropertyCommandContext newGetNavPropertyCommandContext(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo);

    GetNavPropertyCountCommandContext newGetNavPropertyCountCommandContext(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo);

    CloseCommandContext newCloseCommandContext();

    CreateEntityCommandContext newCreateEntityCommandContext(String entitySetName, OEntity entity);

    CreateEntityAtPropertyCommandContext newCreateEntityAtPropertyCommandContext(String entitySetName, OEntityKey entityKey, String navProp, OEntity entity);

    DeleteEntityCommandContext newDeleteEntityCommandContext(String entitySetName, OEntityKey entityKey);

    MergeEntityCommandContext newMergeEntityCommandContext(String entitySetName, OEntity entity);

    UpdateEntityCommandContext newUpdateEntityCommandContext(String entitySetName, OEntity entity);

    GetLinksCommandContext newGetLinksCommandContext(OEntityId sourceEntity, String targetNavProp);

    CreateLinkCommandContext newCreateLinkCommandContext(OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity);

    UpdateLinkCommandContext newUpdateLinkCommandContext(OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity);

    DeleteLinkCommandContext newDeleteLinkCommandContext(OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey);

    CallFunctionCommandContext newCallFunctionCommandContext(EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo);

}