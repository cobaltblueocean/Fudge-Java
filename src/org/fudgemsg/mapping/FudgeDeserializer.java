/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and other contributors.
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
package org.fudgemsg.mapping;

import org.fudgemsg.FudgeContext;

public class FudgeDeserializer  extends FudgeDeserializationContext {
    /**
     * Creates a new {@link FudgeDeserializationContext} for the given {@link FudgeContext}.
     *
     * @param fudgeContext the {@code FudgeContext} to use
     */
    public FudgeDeserializer(FudgeContext fudgeContext) {
        super(fudgeContext);
    }
}
