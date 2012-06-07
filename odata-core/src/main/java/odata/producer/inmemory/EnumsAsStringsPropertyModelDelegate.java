package odata.producer.inmemory;

public class EnumsAsStringsPropertyModelDelegate extends PropertyModelDelegate {

    private final PropertyModel propertyModel;

    public EnumsAsStringsPropertyModelDelegate(PropertyModel propertyModel) {
        this.propertyModel = propertyModel;
    }

    public PropertyModel getDecorated() {
        return propertyModel;
    }

    @Override
    public Class<?> getPropertyType(String propertyName) {
        Class<?> rt = super.getPropertyType(propertyName);
        if (rt != null && rt.isEnum())
            return String.class;
        return rt;
    }

    @Override
    public Object getPropertyValue(Object target, String propertyName) {
        Class<?> baseType = super.getPropertyType(propertyName);
        Object rt = super.getPropertyValue(target, propertyName);
        if (baseType != null && baseType.isEnum() && rt != null)
            return ((Enum<?>) rt).name();
        return rt;
    }

}
