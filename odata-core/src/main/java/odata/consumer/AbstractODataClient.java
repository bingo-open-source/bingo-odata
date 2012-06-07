package odata.consumer;

import odata.format.FormatType;

public abstract class AbstractODataClient implements ODataClient {

    protected AbstractODataClient(FormatType formatType) {
        this.formatType = formatType;
    }

    private FormatType formatType;

    public FormatType getFormatType() {
        return this.formatType;
    }

}
