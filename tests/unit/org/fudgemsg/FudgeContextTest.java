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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.fudgemsg.taxon.FudgeTaxonomy;
import org.fudgemsg.taxon.ImmutableMapTaxonomyResolver;
import org.fudgemsg.taxon.MapFudgeTaxonomy;
import org.fudgemsg.taxon.TaxonomyResolver;
import org.fudgemsg.test.FudgeUtils;
import org.junit.jupiter.api.Test;

/**
 * 
 *
 * @author Kirk Wylie
 */
public class FudgeContextTest {
  private static final int[] ORDINALS = new int[] {5, 14, 928, 74}; 
  private static final String[] NAMES = new String[] {"Kirk", "Wylie", "Jim", "Moores"}; 

  /**
   * 
   */
  @Test
  public void allNamesCodecNoTaxonomy() {
    FudgeContext context = new FudgeContext();
    FudgeFieldContainer inputMsg = StandardFudgeMessages.createMessageAllNames(context);
    FudgeFieldContainer outputMsg = cycleMessage(inputMsg, context, null);
    
    assertNotNull(outputMsg);
    
    FudgeUtils.assertAllFieldsMatch(inputMsg, outputMsg);
  }
  
  /**
   * 
   */
  @Test
  public void allNamesCodecWithTaxonomy() {
    FudgeContext context = new FudgeContext();
    MutableFudgeFieldContainer inputMsg = context.newMessage();
    inputMsg.add(NAMES[0], "value1");
    inputMsg.add(NAMES[1], "value2");
    inputMsg.add(NAMES[2], "value3");
    inputMsg.add(NAMES[3], "value4");
    
    Map<Short, FudgeTaxonomy> resolverMap = new HashMap<Short, FudgeTaxonomy>();
    resolverMap.put((short)45, new MapFudgeTaxonomy(ORDINALS, NAMES));
    context.setTaxonomyResolver(new ImmutableMapTaxonomyResolver(resolverMap));
    
    //FudgeMsgFormatter.outputToSystemOut(inputMsg);
    FudgeFieldContainer outputMsg = cycleMessage(inputMsg, context, (short)45);
    //FudgeMsgFormatter.outputToSystemOut(outputMsg);
    assertEquals("value1", outputMsg.getString(NAMES[0]));
    assertEquals("value1", outputMsg.getString(ORDINALS[0]));
    assertEquals("value2", outputMsg.getString(NAMES[1]));
    assertEquals("value2", outputMsg.getString(ORDINALS[1]));
    assertEquals("value3", outputMsg.getString(NAMES[2]));
    assertEquals("value3", outputMsg.getString(ORDINALS[2]));
    assertEquals("value4", outputMsg.getString(NAMES[3]));
    assertEquals("value4", outputMsg.getString(ORDINALS[3]));
  }
  
  private TaxonomyResolver createTaxonomyResolver () {
    Map<Short, FudgeTaxonomy> resolverMap = new HashMap<Short, FudgeTaxonomy>();
    resolverMap.put((short)45, new MapFudgeTaxonomy(ORDINALS, NAMES));
    return new ImmutableMapTaxonomyResolver (resolverMap);
  }

  /**
   * 
   */
  @Test
  public void allOrdinalsCodecWithTaxonomy() {
    FudgeContext context = new FudgeContext();
    MutableFudgeFieldContainer inputMsg = context.newMessage();
    inputMsg.add(ORDINALS[0], "value1");
    inputMsg.add(ORDINALS[1], "value2");
    inputMsg.add(ORDINALS[2], "value3");
    inputMsg.add(ORDINALS[3], "value4");
    
    context.setTaxonomyResolver(createTaxonomyResolver ());
    
    FudgeFieldContainer outputMsg = cycleMessage(inputMsg, context, (short)45);
    assertEquals("value1", outputMsg.getString(NAMES[0]));
    assertEquals("value1", outputMsg.getString(ORDINALS[0]));
    assertEquals("value2", outputMsg.getString(NAMES[1]));
    assertEquals("value2", outputMsg.getString(ORDINALS[1]));
    assertEquals("value3", outputMsg.getString(NAMES[2]));
    assertEquals("value3", outputMsg.getString(ORDINALS[2]));
    assertEquals("value4", outputMsg.getString(NAMES[3]));
    assertEquals("value4", outputMsg.getString(ORDINALS[3]));
  }

  /**
   * @param inputMsg
   * @param context
   * @return
   */
  private FudgeFieldContainer cycleMessage(FudgeFieldContainer inputMsg, FudgeContext context, Short taxonomy) {
    byte[] content = context.toByteArray (inputMsg, taxonomy);
    FudgeMsgEnvelope outputMsgEnvelope = context.deserialize(content);
    assertNotNull(outputMsgEnvelope);
    assertNotNull(outputMsgEnvelope.getMessage());
    return outputMsgEnvelope.getMessage ();
  }
  
  /**
   * 
   */
  @Test
  public void readerAllocation() {
    FudgeContext context = new FudgeContext();
    FudgeStreamReader reader1 = context.createReader(System.in);
    assertNotNull(reader1);
    FudgeStreamReader reader2 = context.createReader(System.in);
    assertNotNull(reader2);
    assertNotSame(reader1, reader2);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_setTypeDictionary () {
    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.setTypeDictionary (null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_addType () {
    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getTypeDictionary ().addType (null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }

  /**
   * 
   */
  @Test
  public void immutableContextTest_addTypeConverter () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getTypeDictionary ().addTypeConverter (null, (Class<?>)null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }

  /**
   * 
   */
  @Test
  public void immutableContextTest_typeDictionary () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> {
              FudgeContext.GLOBAL_DEFAULT.setTypeDictionary (null);
              FudgeContext.GLOBAL_DEFAULT.getTypeDictionary ().addType (null);
              FudgeContext.GLOBAL_DEFAULT.getTypeDictionary ().addTypeConverter (null, (Class<?>)null);
            },
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }

  /**
   * 
   */
  @Test
  public void immutableContextTest_setTaxonomyResolver () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.setTaxonomyResolver (createTaxonomyResolver ()),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_setObjectDictionary () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.setObjectDictionary (null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_setDefaultBuilderFactory () {
    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getObjectDictionary ().setDefaultBuilderFactory (null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_addGenericBuilder () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getObjectDictionary ().getDefaultBuilderFactory ().addGenericBuilder (null, null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_addObjectBuilder () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getObjectDictionary ().addObjectBuilder (null, null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_addMessageBuilder () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getObjectDictionary ().addMessageBuilder (null, null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
  /**
   * 
   */
  @Test
  public void immutableContextTest_addBuilder () {

    Exception thrown = assertThrows(
            java.lang.UnsupportedOperationException.class,
            () -> FudgeContext.GLOBAL_DEFAULT.getObjectDictionary ().addBuilder (null, null),
            "Expected doThing() to throw, but it didn't"
    );
    assertEquals(thrown.getClass(), java.lang.UnsupportedOperationException.class);
  }
  
}
