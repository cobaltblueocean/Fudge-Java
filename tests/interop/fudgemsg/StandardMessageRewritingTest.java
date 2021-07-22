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

package fudgemsg;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeFieldContainer;
import org.fudgemsg.FudgeRuntimeException;
import org.fudgemsg.StandardFudgeMessages;
import org.junit.jupiter.api.Test;

/**
 * Writes out messages again and makes sure that we're writing out the same things as before.
 *
 * @author Kirk Wylie
 */
public class StandardMessageRewritingTest {

  private static final FudgeContext s_fudgeContext = new FudgeContext();
  
  /**
   * 
   */
  @Test
  public void allNames() {
    testFile(StandardFudgeMessages.createMessageAllNames(s_fudgeContext), "fudgemsg/allNames.dat");
  }
  
  /**
   * 
   */
  @Test
  public void allOrdinals() {
    testFile(StandardFudgeMessages.createMessageAllOrdinals(s_fudgeContext), "fudgemsg/allOrdinals.dat");
  }
  
  /**
   * 
   */
  @Test
  public void subMsg() {
    testFile(StandardFudgeMessages.createMessageWithSubMsgs(s_fudgeContext), "fudgemsg/subMsg.dat");
  }
  
  /**
   * 
   */
  @Test
  public void fixedWidthByteArrays() {
    testFile(FudgeInteropTest.createFixedWidthByteArrayMsg(s_fudgeContext), "fudgemsg/fixedWidthByteArrays.dat");
  }
  
  /**
   * 
   */
  @Test
  public void variableWidthColumnSizes() {
    testFile(FudgeInteropTest.createVariableWidthColumnSizes(s_fudgeContext), "fudgemsg/variableWidthColumnSizes.dat");
  }

  /**
   * 
   */
  @Test
  public void unknown() {
    testFile(FudgeInteropTest.createUnknown(s_fudgeContext), "fudgemsg/unknown.dat");
  }
  
  /**
   * 
   */
  @Test
  public void dateTimes () {
    testFile (FudgeInteropTest.createDateTimes (s_fudgeContext), "fudgemsg/dateTimes.dat");
  }
  
  /**
   * @param msgToWrite [documentation not available]
   * @param fileName [documentation not available]
   */
  protected static void testFile(FudgeFieldContainer msgToWrite, String fileName) {
    byte[] actualBytes = s_fudgeContext.toByteArray(msgToWrite);
    ByteArrayInputStream actualStream = new ByteArrayInputStream(actualBytes);
    InputStream expectedStream = StandardMessageRewritingTest.class.getResourceAsStream(fileName);
    try {
      int iByte = 0;
      while(true) {
        int expected = expectedStream.read();
        int actual = actualStream.read();
        assertEquals( actual, expected, "At position " + iByte + " actual was " + actual + " expected was " + expected);
        if((expected < 0) || (actual < 0)) {
          break;
        }
        iByte++;
      }
    } catch (IOException ioe) {
      throw new FudgeRuntimeException("Unable to read from streams", ioe);
    } finally {
      try {
        expectedStream.close();
      } catch (IOException ioe) {
        // Do nothing.
      }
    }
  }
}
