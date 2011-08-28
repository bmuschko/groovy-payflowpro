package groovy.paypal.payflow.response

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/21/11
 * Time: 9:27 AM
 * To change this template use File | Settings | File Templates.
 */
class PayflowResponse {
    Integer result
    String responseMessage
    String pnRef
    String authCode

    boolean isApproved() {
        result && result == 0
    }

    @Override
    String toString() {
        "PayflowResponse{" +
        "result=" + result +
        ", responseMessage='" + responseMessage + '\'' +
        ", pnRef='" + pnRef + '\'' +
        ", authCode='" + authCode + '\'' +
        '}'
    }
}
