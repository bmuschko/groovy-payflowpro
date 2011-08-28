package groovy.paypal.payflow

import groovy.paypal.payflow.response.PayflowResponseMap

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/21/11
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
interface PayflowClient {
    PayflowResponseMap submitSale(Map extra)
    PayflowResponseMap submitAuthorization(Map extra)
    PayflowResponseMap submitDelayedCapture(Map extra)
    PayflowResponseMap submitVoiceAuthorization(Map extra)
    PayflowResponseMap submitCredit(Map extra)
    PayflowResponseMap submitVoid(Map extra)
    PayflowResponseMap submitInquiry(Map extra)
    PayflowResponseMap submitReferenceTransaction(Map extra)
    PayflowResponseMap addProfile(Map extra)
    PayflowResponseMap modifyProfile(Map extra)
    PayflowResponseMap reactivateProfile(Map extra)
    PayflowResponseMap cancelProfile(Map extra)
    PayflowResponseMap sendProfileInquiry(Map extra)
    PayflowResponseMap retryProfilePayment(Map extra)
}