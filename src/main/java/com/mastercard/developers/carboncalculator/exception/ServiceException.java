/*
 *  Copyright (c) 2021 Mastercard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mastercard.developers.carboncalculator.exception;

import org.openapitools.client.model.ErrorWrapper;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private final transient ErrorWrapper errors = new ErrorWrapper();

    public ServiceException(String message, ErrorWrapper serviceErrors) {
        super(message);
        errors.setErrors(serviceErrors.getErrors());
    }

    public ErrorWrapper getServiceErrors() {
        return errors;
    }

    public ServiceException(String errorMessage) {
        super(errorMessage);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}