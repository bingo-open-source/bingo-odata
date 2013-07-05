package bingo.odata.consumer.util;

import bingo.meta.edm.EdmType;

public class OdataJudger {

	public static boolean isEntity(EdmType edmType) {
		if(edmType.isEntity() || edmType.isEntityRef()) return true;
		return false;
	}
	
	public static boolean isEntitySet(EdmType edmType) {
		if(edmType.isCollection()) return true;
		return false;
	}
}
