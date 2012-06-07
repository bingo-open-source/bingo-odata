package odata.consumer.behaviors;

import odata.consumer.ODataClientRequest;
import bingo.utils.codec.binary.Base64;

public class BasicAuthenticationBehavior implements OClientBehavior {

    private final String user;
    private final String password;

    public BasicAuthenticationBehavior(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public ODataClientRequest transform(ODataClientRequest request) {
        String userPassword = user + ":" + password;
        String encoded = Base64.encode(userPassword.getBytes());
        encoded = encoded.replaceAll("\r\n?", "");
        return request.header("Authorization", "Basic " + encoded);

    }

}