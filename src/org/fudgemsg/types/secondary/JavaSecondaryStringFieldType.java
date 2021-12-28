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
package org.fudgemsg.types.secondary;

import org.fudgemsg.FudgeFieldType;
import org.fudgemsg.types.SecondaryFieldType;
import org.fudgemsg.types.StringFieldType;

import java.math.BigDecimal;

/**
 * Secondary type for BigDecimal conversion to/from String.
 *
 * @author Kei Nakai
 */
public class JavaSecondaryStringFieldType extends SecondaryFieldType<String,String>  {

    /**
     * Singleton instance of the type.
     */
    public static final JavaSecondaryStringFieldType INSTANCE = new JavaSecondaryStringFieldType();

    /**
     * Creates a new secondary type on top of an existing Fudge type.
     */
    private JavaSecondaryStringFieldType() {
        super(StringFieldType.INSTANCE, String.class);
    }

    /**
     * Converts an object from the secondary type to a primitive Fudge type for writing. An implementation
     * may assume that the {@code object} parameter is not {@code null}.
     *
     * @param object the secondary instance
     * @return the underlying Fudge data to write out
     */
    @Override
    public String secondaryToPrimary(String object) {
        return object.toString ();
    }

    /**
     *
     */
    @Override
    public String primaryToSecondary (final String data) {
        return new String (data);
    }

}
