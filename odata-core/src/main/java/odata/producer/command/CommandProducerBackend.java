package odata.producer.command;

import java.util.Map;

import odata.OEntity;
import odata.OEntityId;
import odata.OEntityKey;
import odata.OFunctionParameter;
import odata.command.Command;
import odata.command.CommandContext;
import odata.command.CommandExecution;
import odata.edm.EdmFunctionImport;
import odata.producer.EntityQueryInfo;
import odata.producer.QueryInfo;


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