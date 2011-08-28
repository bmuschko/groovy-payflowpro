package groovy.paypal.payflow

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/20/11
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
enum PayflowTransaction {
    SALE('S', 'submitSaleWith'), AUTHORIZATION('A', 'submitAuthorizationWith'), DELAYED_CAPTURE('D', 'submitDelayedCaptureWith'),
    VOICE_AUTHORIZATION('F', 'submitVoiceAuthorizationWith'), CREDIT('C', 'submitCreditWith'), VOID('V', 'submitVoidWith'),
    INQUIRY('I', 'submitInquiryWith'), REFERENCE_TRANSACTION(null, 'submitReferenceTransactionWith')

    final String type
    final String dynamicMethodName

    PayflowTransaction(String type, String dynamicMethodName) {
        this.type = type
        this.dynamicMethodName = dynamicMethodName
    }
}