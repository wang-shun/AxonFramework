/*
 * Copyright (c) 2010-2019. Axon Framework
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

package org.axonframework.axonserver.connector.event.axon;

import io.axoniq.axonserver.grpc.ErrorMessage;
import org.axonframework.axonserver.connector.AxonServerException;
import org.axonframework.axonserver.connector.ErrorCode;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.common.AxonException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Author: marc
 */
class ErrorCodeTest {

    @Test
    void testConvert4002FromCodeAndMessage() {
        ErrorCode errorCode = ErrorCode.getFromCode("AXONIQ-4002");
        AxonException exception = errorCode.convert(ErrorMessage.newBuilder().setMessage("myMessage").build(), () -> "myCustomObject");
        assertTrue(exception instanceof CommandExecutionException);
        assertEquals("myMessage", exception.getMessage());
        assertEquals("myCustomObject", ((CommandExecutionException)exception).getDetails().orElse("null"));
    }

    @Test
    void testConvertUnknownFromCodeAndMessage() {
        ErrorCode errorCode = ErrorCode.getFromCode("????????");
        AxonException exception = errorCode.convert(ErrorMessage.newBuilder().setMessage("myMessage").build());
        assertTrue(exception instanceof AxonServerException);
        assertEquals("myMessage", exception.getMessage());
    }

    @Test
    void testConvertWithoutSource() {
        RuntimeException exception = new RuntimeException("oops");
        AxonException axonException = ErrorCode.getFromCode("AXONIQ-4002").convert(exception);
        assertEquals(exception.getMessage(), axonException.getMessage());
    }
}
