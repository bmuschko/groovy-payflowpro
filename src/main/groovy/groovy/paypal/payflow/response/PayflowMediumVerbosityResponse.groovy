package groovy.paypal.payflow.response

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/27/11
 * Time: 6:29 PM
 * To change this template use File | Settings | File Templates.
 */
class PayflowMediumVerbosityResponse {
    String hostCode
    String responseText
    String procAvs
    String procCvv2
    String procCardSecure
    String addLmnsgs
    Integer transState
    Date dateToSettle
    Integer batchId
    Date settleDate
    String amexId
    String amexPosData
    String visaCardLevel
}
