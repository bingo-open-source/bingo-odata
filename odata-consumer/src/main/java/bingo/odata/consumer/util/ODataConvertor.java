package bingo.odata.consumer.util;

import java.io.CharArrayReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Converts;
import bingo.lang.Func1;
import bingo.lang.json.JSON;
import bingo.odata.ODataError;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.consumer.ODataConsumerContext;
import bingo.odata.consumer.exceptions.ResolveFailedException;
import bingo.odata.model.ODataComplexObject;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataNamedValue;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataRawValue;
import bingo.odata.model.ODataValue;
import bingo.odata.model.ODataValueImpl;

public class ODataConvertor {

	public static <T extends ODataObject> ODataValue convertToODataValue(Class<T> t,
			ODataConsumerContext context, Map<String, Object> valueJson) {
		return convertToODataValue(t, context, JSON.encode(valueJson, true));
	}
	
	public static <T extends ODataObject> ODataValue convertToODataValue(Class<T> t,
			ODataConsumerContext context, String valueJson) {
		return convertToODataValue(t, context, new CharArrayReader(valueJson.toCharArray()));
	}
	
	public static <T extends ODataObject> ODataValue convertToODataValue(Class<T> t,
			ODataConsumerContext context, Reader valueJson) {
		ODataObjectKind kind = getObjectKindFromObject(t);
		T value = convertTo(t, context, valueJson);
		return new ODataValueImpl(kind, value);
	}
	
	public static <T extends ODataObject> T convertTo(Class<T> t,
			ODataConsumerContext context, Map<String, Object> valueJson) {
		return convertTo(t, context, new CharArrayReader(JSON.encode(valueJson, true).toCharArray()));
	}
	
	public static <T extends ODataObject> T convertTo(Class<T> t,
			ODataConsumerContext context, String valueJson) {
		return convertTo(t, context, new CharArrayReader(valueJson.toCharArray()));
	}
	
	public static <T extends ODataObject> T convertTo(Class<T> t,
			ODataConsumerContext context, Reader valueJson) {
		Assert.notNull(context);
		Assert.notNull(valueJson);
		ODataObjectKind objectKind = getObjectKindFromObject(t);
		ODataReader<T> reader = context.getProtocol().getReader(
				context.getVersion(), context.getFormat(), objectKind);
		try {
			T instance = reader.read((ODataReaderContext)context, valueJson);
			return instance;
		} catch (Throwable e) {
			throw new ResolveFailedException(e);
		}
	}
	
	public static <T extends ODataObject> ODataObjectKind getObjectKindFromObject(Class<T> t) {
		Assert.notNull(t);
		ODataObjectKind objectKind = null;
		if(ODataEntity.class.isAssignableFrom(t)) objectKind = ODataObjectKind.Entity;
		if(ODataEntitySet.class.isAssignableFrom(t)) objectKind = ODataObjectKind.EntitySet;
		if(ODataError.class.isAssignableFrom(t)) objectKind = ODataObjectKind.Error;
		if(ODataComplexObject.class.isAssignableFrom(t)) objectKind = ODataObjectKind.ComplexObject;
		if(ODataNamedValue.class.isAssignableFrom(t)) objectKind = ODataObjectKind.NamedValue;
		if(ODataProperty.class.isAssignableFrom(t)) objectKind = ODataObjectKind.Property;
		if(ODataNavigationProperty.class.isAssignableFrom(t)) objectKind = ODataObjectKind.Property;
		if(ODataRawValue.class.isAssignableFrom(t)) objectKind = ODataObjectKind.Raw;
		return objectKind;
	}
	
	public static <T> T convertEntityToType(ODataEntity entity, Class<T> clazz) {
		Assert.notNull(entity);
		return Converts.convert(entity.toMap(), clazz);
	}
	
	public static <T> List<T> convertEntitySetToTypeList(ODataEntitySet entitySet, Class<T> clazz) {
		Assert.notNull(entitySet);
		List<T> result = new ArrayList<T>();
		for (ODataEntity entity : entitySet.getEntities()) {
			result.add(Converts.convert(entity, clazz));
		}
		return result;
	}
	
	public static <T> List<T> convertMapListToTypeList(List<Map<String, Object>> mapList, Class<T> clazz) {
		Assert.notNull(mapList);
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : mapList) {
			result.add(Converts.convert(map, clazz));
		}
		return result;
	}
}
