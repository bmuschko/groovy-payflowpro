package groovy.paypal.payflow

/**
 * Created by IntelliJ IDEA.
 * User: Ben
 * Date: 8/27/11
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
enum PayflowEnvironment {
    TEST('https://pilot-payflowpro.paypal.com'), LIVE('https://payflowpro.paypal.com')

    final String url

    PayflowEnvironment(String url) {
        this.url = url
    }
}
