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
package org.fudgemsg;

import java.util.List;

/**
 * An immutable message in the Fudge system.
 * <p>
 * The message consists of a list of {@link FudgeMsgField Fudge fields}.
 * This class holds the entire message in memory.
 * <p>
 * Applications are recommended to store and manipulate a {@link ImmutableFudgeFieldContainer}
 * instance rather than this class for future flexibility.
 * <p>
 * This class can be created as a copy of an existing {@link FudgeFieldContainer}.
 * For efficiency, the reference to a {@link FudgeContext} is kept and the context is not copied.
 * In that scenario, changes made to the context will be made visible through this class, for
 * example the behavior of {@link #getFieldValue}. If this is not desired, create a
 * {@link MutableFudgeContext} from your underlying {@code FudgeContext} for use in cloning messages.
 * Message fields are copied at one level deep only.
 * Any sub-messages, or referenced objects may be still be mutable.
 * <p>
 * This class is intended to be immutable but not all contents will necessarily be immutable.
 */

public class MutableFudgeMsg extends FudgeMsgBase implements MutableFudgeFieldContainer {
    /**
     * Constructor taking a Fudge context.
     *
     * @param fudgeContext the context to use for type resolution and other services, not null
     */
    public MutableFudgeMsg(FudgeContext fudgeContext) {
        super(fudgeContext);
    }

    /**
     * Adds a field to this container.
     *
     * @param field the field to add, not null
     */
    @Override
    public void add(FudgeField field) {
        if (!super.hasField(field.getName()))
            super.getAllFields().add(field);
    }

    /**
     * Adds a field to this container with a name, no ordinal, and type determined by the context's type dictionary.
     *
     * @param name  the name of the field, null for none
     * @param value the field value, not null
     */
    @Override
    public void add(String name, Object value) {
        if (!super.hasField(name)){
            FudgeFieldType type = new FudgeFieldType((int)super.getAllFields().stream().count(), value.getClass(), true, 0);
            super.getAllFields().add(FudgeMsgField.of(type, value, name));
        }
    }

    /**
     * Adds a field to this container with an ordinal, no name, and type determined by the context's type dictionary.
     *
     * @param ordinal the ordinal for the field, null for none
     * @param value   the field value, not null
     */
    @Override
    public void add(Integer ordinal, Object value) {
        FudgeFieldType type = new FudgeFieldType(ordinal, value.getClass(), true, 0);
        super.getAllFields().add(FudgeMsgField.of(type, value));
    }

    /**
     * Adds a field to this container with the given name, ordinal and type determined by the context's type dictionary.
     *
     * @param name    the name of the field, null for none
     * @param ordinal the ordinal index for the field, null for none
     * @param value   the field value, not null
     */
    @Override
    public void add(String name, Integer ordinal, Object value) {
        FudgeFieldType type = new FudgeFieldType(ordinal, value.getClass(), true, 0);
        super.getAllFields().add(FudgeMsgField.of(type, value, name));
    }

    /**
     * Adds a field to this container with the given name, ordinal, and type.
     *
     * @param name    the name of the field, null for none
     * @param ordinal the ordinal for the field, null for none
     * @param type    the field type, not null
     * @param value   the field value, not null
     */
    @Override
    public void add(String name, Integer ordinal, FudgeFieldType<?> type, Object value) {
        super.getAllFields().add(FudgeMsgField.of(type, value, name));
    }

    /**
     * Add SubMessage to this message.
     * @param name the name for SubMessage
     * @param fudgeMsg
     */
    public MutableFudgeMsg addSubMessage(String name, FudgeMsgBase fudgeMsg) {
        add(name, (int)FudgeTypeDictionary.FUDGE_MSG_TYPE_ID, fudgeMsg);
        return this;
    }

    /**
     * Removes all fields with the given name.
     *
     * @param name name of the fields, null matches fields without a name
     */
    @Override
    public void remove(String name) {
        List<FudgeField> list = super.getAllFields();

        for(int i = list.size(); i >= 0 ; i--)
        {
            if (list.get(i).getName() == name)
            super.getAllFields().remove(list.get(i));
        }
    }

    /**
     * Removes all fields with the given ordinal.
     *
     * @param ordinal ordinal index of fields, null matches fields without an ordinal
     */
    @Override
    public void remove(Short ordinal) {
        List<FudgeField> list = super.getAllFields();

        for(int i = list.size(); i >= 0 ; i--)
        {
            if (list.get(i).getOrdinal() == ordinal)
                super.getAllFields().remove(list.get(i));
        }
    }

    /**
     * Removes all fields matching both the name and ordinal supplied.
     *
     * @param name    the name of the fields to remove
     * @param ordinal the ordinal of the fields to remove
     */
    @Override
    public void remove(String name, Short ordinal) {
        List<FudgeField> list = super.getAllFields();

        for(int i = list.size(); i >= 0 ; i--)
        {
            if ((list.get(i).getOrdinal() == ordinal) && (list.get(i).getName() == name))
                super.getAllFields().remove(list.get(i));
        }
    }

    /**
     * Removes all fields from the message.
     */
    @Override
    public void clear() {
        super.getAllFields().clear();
    }

    public FudgeMsg toFudgeMsg()
    {
        FudgeMsg fudgeMsg = new FudgeMsg(super.getFudgeContext());
        List<FudgeField> fields = getFields();

        for (FudgeField field : fields)
        {
            fudgeMsg.add(field);
        }

        return fudgeMsg;
    }
}
