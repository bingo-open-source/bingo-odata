package bingo.odata.consumer.behaviors;

import bingo.odata.ODataConstants;
import bingo.odata.consumer.ODataClientRequest;

public class MethodTunnelingBehavior implements OClientBehavior {

    private final String[] methodsToTunnel;

    public MethodTunnelingBehavior(String... methodsToTunnel) {
        this.methodsToTunnel = methodsToTunnel;
    }

    public ODataClientRequest transform(ODataClientRequest request) {
        String method = request.getMethod();
        for (String methodToTunnel : methodsToTunnel) {
            if (method.equals(methodToTunnel)) {
                return request.header(ODataConstants.Headers.X_HTTP_METHOD, method).method("POST");
            }
        }
        return request;
    }

}
