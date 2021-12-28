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

import org.fudgemsg.*;

public class FudgeSerializer extends FudgeSerializationContext {

    public static final int TYPES_HEADER_ORDINAL = (int)FudgeTypeDictionary.TYPES_HEADER_ID;

    /**
     * Creates a new {@link FudgeSerializationContext} for the given {@link FudgeContext}.
     *
     * @param fudgeContext the {@code FudgeContext} to use
     */
    public FudgeSerializer(FudgeContext fudgeContext) {
        super(fudgeContext);
    }

    /**
     * Adds class names to a message with ordinal 0 for use by a deserializer. The preferred class name is written first, followed by subsequent super-classes that may
     * be acceptable if the deserializer doesn't recognize them.
     *
     * @param message the message to add the fields to
     * @param clazz the Java class to add type data for
     * @return message the modified message (allows this to be used inline)
     */
    public static MutableFudgeFieldContainer addClassHeader(final MutableFudgeFieldContainer message, Class<?> clazz)
    {
        return FudgeSerializationContext.addClassHeader(message, clazz);
    }

    /**
     * Creates an initially empty message.
     *
     * @return the empty message container, not null
     */
    public MutableFudgeMsg newMessage()
    {
        return new MutableFudgeMsg(super.getFudgeContext());
    }

    /**
     * Adds a field to the message given as a argument with the given name, ordinal, and value.
     *
     * @param message  the message to add, not null
     * @param fieldName  the name of the field, null for none
     * @param ordinal  the ordinal for the field, null for none
     * @param value  the field value, not null
     */
    public void addToMessage(MutableFudgeFieldContainer message, String fieldName, Integer ordinal, Object value)
    {
        message.add(fieldName, ordinal, value);
    }

    /**
     * Adds a field to the message given as a argument with the given name, ordinal, and value.
     *
     * @param message  the message to add, not null
     * @param fieldName  the name of the field, null for none
     * @param ordinal  the ordinal for the field, null for none
     * @param value  the field value, not null
     */
    public void addToMessageWithClassHeaders(MutableFudgeFieldContainer message, String fieldName, Integer ordinal, Object value)
    {
        message.add(fieldName, ordinal, value);
        FudgeSerializationContext.addClassHeader(message, value.getClass());
    }

    /**
     * Adds a field to the message given as a argument with the given name, ordinal, and value.
     *
     * @param message  the message to add, not null
     * @param fieldName  the name of the field, null for none
     * @param ordinal  the ordinal for the field, null for none
     * @param value  the field value, not null
     * @param clazz the Java class to add type data for
     */
    public void addToMessageWithClassHeaders(MutableFudgeFieldContainer message, String fieldName, Integer ordinal, Object value, Class<?> clazz)
    {
        message.add(fieldName, ordinal, value);
        FudgeSerializationContext.addClassHeader(message, clazz);
    }

    /**
     * Adds a SubMessage to the message given as a argument with the given name.
     * @param message
     * @param fieldName
     * @param subMsg
     */
    public void addSubMessage(MutableFudgeFieldContainer message, String fieldName, MutableFudgeFieldContainer subMsg)
    {
        addToMessage(message, fieldName, (int)FudgeTypeDictionary.FUDGE_MSG_TYPE_ID, subMsg);
    }
}
