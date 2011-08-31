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
 * Defines all available Payflow transactions.
 *
 * @author Benjamin Muschko
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