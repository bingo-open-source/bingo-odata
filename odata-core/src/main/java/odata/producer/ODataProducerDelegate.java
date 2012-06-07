package odata.producer;

import java.util.Map;

import odata.OEntity;
import odata.OEntityId;
import odata.OEntityKey;
import odata.OFunctionParameter;
import odata.edm.EdmDataServices;
import odata.edm.EdmFunctionImport;
import odata.producer.edm.MetadataProducer;

import bingo.lang.Decorator;

/** Abstract base {@link Decorator} for {@link ODataProducer}. */
public abstract class ODataProducerDelegate implements Decorator<ODataProducer>, ODataProducer {

	public EdmDataServices getMetadata() {
		return getDecorated().getMetadata();
	}

	public MetadataProducer getMetadataProducer() {
		return getDecorated().getMetadataProducer();
	}

	public EntitiesResponse getEntities(String entitySetName, QueryInfo queryInfo) {
		return getDecorated().getEntities(entitySetName, queryInfo);
	}

	public CountResponse getEntitiesCount(String entitySetName, QueryInfo queryInfo) {
		return getDecorated().getEntitiesCount(entitySetName, queryInfo);
	}

	public EntityResponse getEntity(String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
		return getDecorated().getEntity(entitySetName, entityKey, queryInfo);
	}

	public BaseResponse getNavProperty(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
		return getDecorated().getNavProperty(entitySetName, entityKey, navProp, queryInfo);
	}

	public CountResponse getNavPropertyCount(String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
		return getDecorated().getNavPropertyCount(entitySetName, entityKey, navProp, queryInfo);
	}

	public void close() {
		getDecorated().close();
	}

	public EntityResponse createEntity(String entitySetName, OEntity entity) {
		return getDecorated().createEntity(entitySetName, entity);
	}

	public EntityResponse createEntity(String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
		return getDecorated().createEntity(entitySetName, entityKey, navProp, entity);
	}

	public void deleteEntity(String entitySetName, OEntityKey entityKey) {
		getDecorated().deleteEntity(entitySetName, entityKey);
	}

	public void mergeEntity(String entitySetName, OEntity entity) {
		getDecorated().mergeEntity(entitySetName, entity);
	}

	public void updateEntity(String entitySetName, OEntity entity) {
		getDecorated().updateEntity(entitySetName, entity);
	}

	public EntityIdResponse getLinks(OEntityId sourceEntity, String targetNavProp) {
		return getDecorated().getLinks(sourceEntity, targetNavProp);
	}

	public void createLink(OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
		getDecorated().createLink(sourceEntity, targetNavProp, targetEntity);
	}

	public void updateLink(OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
		getDecorated().updateLink(sourceEntity, targetNavProp, oldTargetEntityKey, newTargetEntity);
	}

	public void deleteLink(OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
		getDecorated().deleteLink(sourceEntity, targetNavProp, targetEntityKey);
	}

	public BaseResponse callFunction(EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
		return getDecorated().callFunction(name, params, queryInfo);
	}

}
