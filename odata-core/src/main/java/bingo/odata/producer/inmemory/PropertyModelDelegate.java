package bingo.odata.producer.inmemory;

import bingo.lang.Decorator;

public abstract class PropertyModelDelegate implements Decorator<PropertyModel>, PropertyModel {

	public Object getPropertyValue(Object target, String propertyName) {
		return getDecorated().getPropertyValue(target, propertyName);
	}

	public Iterable<String> getPropertyNames() {
		return getDecorated().getPropertyNames();
	}

	public Class<?> getPropertyType(String propertyName) {
		return getDecorated().getPropertyType(propertyName);
	}

	public Iterable<?> getCollectionValue(Object target, String collectionName) {
		return getDecorated().getCollectionValue(target, collectionName);
	}

	public Iterable<String> getCollectionNames() {
		return getDecorated().getCollectionNames();
	}

	public Class<?> getCollectionElementType(String collectionName) {
		return getDecorated().getCollectionElementType(collectionName);
	}

}
