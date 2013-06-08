/**
 * file created at 2013-6-5
 */
package bingo.odata;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.odata.model.ODataKey;

public abstract class ODataContextBase implements ODataContext {

	protected ODataProtocol     protocol = ODataProtocols.DEFAULT;
	protected ODataVersion      version;
	protected ODataFormat       format;
	protected ODataUrlInfo      urlInfo;
	protected ODataServices     services;
	protected EdmEntitySet      entitySet;
	protected EdmEntityType     entityType;
	protected ODataKey          entityKey;
	protected EdmFunctionImport functionImport;
	
	public ODataProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(ODataProtocol protocol) {
		this.protocol = protocol;
	}

	public ODataVersion getVersion() {
		return version;
	}

	public void setVersion(ODataVersion version) {
		this.version = version;
	}

	public ODataFormat getFormat() {
		return format;
	}

	public void setFormat(ODataFormat format) {
		this.format = format;
	}

	public ODataUrlInfo getUrlInfo() {
		return urlInfo;
	}

	public void setUrlInfo(ODataUrlInfo urlInfo) {
		this.urlInfo = urlInfo;
	}

	public ODataServices getServices() {
		return services;
	}

	public void setServices(ODataServices services) {
		this.services = services;
	}

	public EdmEntitySet getEntitySet() {
		return entitySet;
	}

	public void setEntitySet(EdmEntitySet entitySet) {
		this.entitySet = entitySet;
	}

	public EdmEntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EdmEntityType entityType) {
		this.entityType = entityType;
	}

	public ODataKey getEntityKey() {
		return entityKey;
	}

	public void setEntityKey(ODataKey entityKey) {
		this.entityKey = entityKey;
	}

	public EdmFunctionImport getFunctionImport() {
		return functionImport;
	}

	public void setFunctionImport(EdmFunctionImport functionImport) {
		this.functionImport = functionImport;
	}
}
