package odata.producer.inmemory;

/**
 * information about a POJO-based complex type.
 */
public class InMemoryComplexTypeInfo<TEntity> {
    String         typeName;
    Class<TEntity> entityClass;
    PropertyModel  propertyModel;
}
