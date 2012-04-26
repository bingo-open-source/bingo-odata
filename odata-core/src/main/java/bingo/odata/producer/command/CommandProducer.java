package bingo.odata.producer.command;

import java.util.Map;

import bingo.odata.OEntity;
import bingo.odata.OEntityId;
import bingo.odata.OEntityKey;
import bingo.odata.OFunctionParameter;
import bingo.odata.Throwables;
import bingo.odata.command.Command;
import bingo.odata.command.CommandExecution;
import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmDataServicesProvider;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.producer.BaseResponse;
import bingo.odata.producer.CountResponse;
import bingo.odata.producer.EntitiesResponse;
import bingo.odata.producer.EntityIdResponse;
import bingo.odata.producer.EntityQueryInfo;
import bingo.odata.producer.EntityResponse;
import bingo.odata.producer.ODataProducer;
import bingo.odata.producer.QueryInfo;
import bingo.odata.producer.edm.MetadataProducer;

public class CommandProducer implements ODataProducer {

    private final CommandProducerBackend backend;

    public CommandProducer(CommandProducerBackend backend) {
        this.backend = backend;
    }

    private <TResult, TContext extends ProducerCommandContext<TResult>> TResult executeCommand(Class<TContext> contextType, Class<TResult> resultType, TContext context) {
        Command<TContext> command = backend.getCommand(contextType);
        CommandExecution execution = backend.getCommandExecution();
        try {
            execution.execute(command, context);
            TResult result = context.getResult();
            if (result != null)
                return result;
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
        if (resultType.equals(Void.class)) {
            return null; // ok for Void
        }
        throw new RuntimeException("Command " + contextType.getSimpleName() + " implementation did not return result, expected " + resultType.getSimpleName());
    }

    
    public EdmDataServices getMetadata() {
        return executeCommand(GetMetadataCommandContext.class, EdmDataServicesProvider.class, backend.newGetMetadataCommandContext()).getMetadata();
    }

    
    public MetadataProducer getMetadataProducer() {
        return executeCommand(GetMetadataProducerCommandContext.class, MetadataProducer.class, backend.newGetMetadataProducerCommandContext());
    }

    
    public EntitiesResponse getEntities(String entitySetName, QueryInfo queryInfo) {
        return executeCommand(GetEntitiesCommandContext.class, EntitiesResponse.class, backend.newGetEntitiesCommandContext(entitySetName, queryInfo));
    }

    
    public CountResponse getEntitiesCount(String entitySetName, QueryInfo queryInfo) {
        return executeCommand(GetEntitiesCountCommandContext.class, CountResponse.class, backend.newGetEntitiesCountCommandContext(entitySetName, queryInfo));
    }

    
    public EntityResponse getEntity(String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
        return executeCommand(GetEntityCommandContext.class, EntityResponse.class, backend.newGetEntityCommandContext(entitySetName, entityKey, queryInfo));
    }

    
    public BaseResponse getNavProperty(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
        return executeCommand(GetNavPropertyCommandContext.class, BaseResponse.class, backend.newGetNavPropertyCommandContext(entitySetName, entityKey, navProp, queryInfo));
    }

    
    public CountResponse getNavPropertyCount(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
        return executeCommand(GetNavPropertyCountCommandContext.class, CountResponse.class, backend.newGetNavPropertyCountCommandContext(entitySetName, entityKey, navProp, queryInfo));
    }

    
    public void close() {
        executeCommand(CloseCommandContext.class, Void.class, backend.newCloseCommandContext());
    }

    
    public EntityResponse createEntity(String entitySetName, OEntity entity) {
        return executeCommand(CreateEntityCommandContext.class, EntityResponse.class, backend.newCreateEntityCommandContext(entitySetName, entity));
    }

    
    public EntityResponse createEntity(String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
        return executeCommand(CreateEntityAtPropertyCommandContext.class, EntityResponse.class, backend.newCreateEntityAtPropertyCommandContext(entitySetName, entityKey, navProp, entity));
    }

    
    public void deleteEntity(String entitySetName, OEntityKey entityKey) {
        executeCommand(DeleteEntityCommandContext.class, Void.class, backend.newDeleteEntityCommandContext(entitySetName, entityKey));
    }

    
    public void mergeEntity(String entitySetName, OEntity entity) {
        executeCommand(MergeEntityCommandContext.class, Void.class, backend.newMergeEntityCommandContext(entitySetName, entity));
    }

    
    public void updateEntity(String entitySetName, OEntity entity) {
        executeCommand(UpdateEntityCommandContext.class, Void.class, backend.newUpdateEntityCommandContext(entitySetName, entity));
    }

    
    public EntityIdResponse getLinks(OEntityId sourceEntity, String targetNavProp) {
        return executeCommand(GetLinksCommandContext.class, EntityIdResponse.class, backend.newGetLinksCommandContext(sourceEntity, targetNavProp));
    }

    
    public void createLink(OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
        executeCommand(CreateLinkCommandContext.class, Void.class, backend.newCreateLinkCommandContext(sourceEntity, targetNavProp, targetEntity));
    }

    
    public void updateLink(OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
        executeCommand(UpdateLinkCommandContext.class, Void.class, backend.newUpdateLinkCommandContext(sourceEntity, targetNavProp, oldTargetEntityKey, newTargetEntity));
    }

    
    public void deleteLink(OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
        executeCommand(DeleteLinkCommandContext.class, Void.class, backend.newDeleteLinkCommandContext(sourceEntity, targetNavProp, targetEntityKey));
    }

    
    public BaseResponse callFunction(EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
        return executeCommand(CallFunctionCommandContext.class, BaseResponse.class, backend.newCallFunctionCommandContext(name, params, queryInfo));
    }

}
