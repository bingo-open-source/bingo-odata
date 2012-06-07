package odata.producer.inmemory;

public class BeanBasedPropertyModel implements PropertyModel {

    private final BeanModel beanModel;

    public BeanBasedPropertyModel(Class<?> clazz) {
        beanModel = new BeanModel(clazz);
    }

    
    public Iterable<String> getPropertyNames() {
        return beanModel.getPropertyNames();
    }

    
    public Class<?> getPropertyType(String propertyName) {
        return beanModel.getPropertyType(propertyName);
    }

    
    public Object getPropertyValue(Object target, String propertyName) {
        return beanModel.getPropertyValue(target, propertyName);
    }

    
    public Iterable<String> getCollectionNames() {
        return beanModel.getCollectionNames();
    }

    
    public Iterable<?> getCollectionValue(Object target, String collectionName) {
        return beanModel.getCollectionValue(target, collectionName);
    }

    
    public Class<?> getCollectionElementType(String collectionName) {
        return beanModel.getCollectionElementType(collectionName);
    }

}
