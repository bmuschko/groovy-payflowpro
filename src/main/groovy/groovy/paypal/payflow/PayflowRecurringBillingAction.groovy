/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovy.paypal.payflow

/**
 * Defines all available Payflow recurring billing actions.
 *
 * @author Benjamin Muschko
 */
enum PayflowRecurringBillingAction {
    ADD('A', 'addProfileWith'), MODIFY('M', 'modifyProfileWith'), REACTIVATE('R', 'reactivateProfileWith'),
    CANCEL('C', 'cancelProfileWith'), INQUIRY('I', 'sendProfileInquiryWith'), PAYMENT('P', 'retryProfilePaymentWith')

    final String action
    final String dynamicMethodName

    PayflowRecurringBillingAction(String action, String dynamicMethodName) {
        this.action = action
        this.dynamicMethodName = dynamicMethodName
    }
}