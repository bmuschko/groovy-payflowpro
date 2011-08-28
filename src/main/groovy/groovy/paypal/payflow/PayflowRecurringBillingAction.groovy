package groovy.paypal.payflow

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/20/11
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
enum PayflowRecurringBillingAction {
    ADD('A', 'addProfileWith'), MODIFY('M', 'modifyProfileWith'), REACTIVATE('R', 'reactiveProfileWith'),
    CANCEL('C', 'cancelProfileWith'), INQUIRY('I', 'sendInquiryWith'), PAYMENT('P', 'retryProfilePaymentWith')

    final String action
    final String dynamicMethodName

    PayflowRecurringBillingAction(String action, String dynamicMethodName) {
        this.action = action
        this.dynamicMethodName = dynamicMethodName
    }
}