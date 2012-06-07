package odata.producer.inmemory;

import bingo.lang.Enumerable;
import bingo.lang.Func1;

public class EntityIdFunctionPropertyModelDelegate<TEntity, TKey> extends PropertyModelDelegate {

    private final PropertyModel        propertyModel;
    private final String               idPropertyName;
    private final Class<TKey>          idPropertyType;
    private final Func1<TEntity, TKey> id;

    public EntityIdFunctionPropertyModelDelegate(PropertyModel propertyModel, String idPropertyName, Class<TKey> idPropertyType, Func1<TEntity, TKey> id) {
        this.propertyModel = propertyModel;
        this.idPropertyName = idPropertyName;
        this.idPropertyType = idPropertyType;
        this.id = id;
    }

    public PropertyModel getDecorated() {
        return propertyModel;
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return Enumerable.of(idPropertyName).concat(Enumerable.of(super.getPropertyNames()));
    }

    @Override
    public Class<?> getPropertyType(String propertyName) {
        if (propertyName.equals(idPropertyName))
            return idPropertyType;
        return super.getPropertyType(propertyName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getPropertyValue(Object target, String propertyName) {
        if (propertyName.equals(idPropertyName))
            return id.apply((TEntity) target);
        return super.getPropertyValue(target, propertyName);
    }

}