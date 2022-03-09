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

package org.fudgemsg.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.bind.annotation.XmlElement;
import javax.xml.stream.*;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

import org.fudgemsg.*;
import org.fudgemsg.taxon.FudgeTaxonomy;
import org.fudgemsg.types.IndicatorType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A FudgeStreamReader implementation for decoding a stream of XML encoded text into Fudge messages.
 *
 * @author Kei Nakai
 */

public class FudgeXMLStreamReader extends FudgeXMLSettings implements FudgeStreamReader {
    private FudgeContext _fudgeContext;

    private int _taxonomyId = 0;
    private FudgeTaxonomy _taxonomy = null;
    private int _processingDirectives = 0;
    private int _schemaVersion = 0;
    private FudgeStreamElement _currentElement = null;
    private String _fieldName = null;
    private Integer _fieldOrdinal = null;
    private Object _fieldValue = null;

    private final Stack<Document> _objectStack = new Stack<Document> ();
    private final Stack<NodeListIterator> _iteratorStack = new Stack<NodeListIterator> ();
    private final Queue<String> _fieldLookahead = new LinkedList<String> ();
    private final Queue<Object> _valueLookahead = new LinkedList<Object> ();

    private final XMLStreamReader _reader;
    private final Document _xmlDocument;


    private static XMLStreamReader createXMLStreamReader (final Reader reader) {
        try {
            return XMLInputFactory.newInstance ().createXMLStreamReader (reader);
        } catch (XMLStreamException e) {
            throw wrapException ("create", e);
        }
    }

    /**
     * Creates a new {@link FudgeXMLStreamReader} for writing a Fudge stream to an {@link XMLStreamReader}.
     *
     * @param fudgeContext the {@link FudgeContext}
     * @param reader the underlying {@link Reader}
     */
    public FudgeXMLStreamReader (final FudgeContext fudgeContext, final Reader reader) {
        this(fudgeContext, createXMLStreamReader(reader));
    }

    public FudgeXMLStreamReader (final FudgeXMLSettings settings, final FudgeContext fudgeContext, final Reader reader) {
        this(settings, fudgeContext, createXMLStreamReader(reader));
    }

    /**
     * Creates a new {@link FudgeXMLStreamReader} for writing a Fudge stream to an {@link XMLStreamReader}.
     *
     * @param fudgeContext the {@link FudgeContext}
     * @param reader the underlying {@link Reader}
     */
    public FudgeXMLStreamReader (final FudgeContext fudgeContext, final XMLStreamReader reader) {
        _fudgeContext = fudgeContext;
        _reader = reader;
        _xmlDocument = createXmlDocument(_reader);
    }

    public FudgeXMLStreamReader (final FudgeXMLSettings settings, final FudgeContext fudgeContext, final XMLStreamReader reader) {
        super (settings);
        _fudgeContext = fudgeContext;
        _reader = reader;
        _xmlDocument = createXmlDocument(_reader);
    }

    private Document createXmlDocument(final XMLStreamReader reader) {
        Document doc = null;

        try {
            // Instantiate the Factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc =  db.parse(String.valueOf(reader));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            throw wrapException("reading next element", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            throw wrapException("reading next element", e);
        }
        return doc;
    }

    private Document getXmlDocument()
    {
        return _xmlDocument;
    }

    /**
     * @param operation the operation being attempted when the exception was caught
     * @param e the exception caught
     */
    protected static FudgeRuntimeException wrapException (final String operation, Exception e) {
        if (e.getCause () instanceof IOException) {
            return new FudgeRuntimeIOException ((IOException)e.getCause ());
        } else {
            return new FudgeRuntimeException ("Couldn't " + operation + " XML stream", e);
        }
    }


    /**
     * <p>Returns true if there is at least one more element to be returned by a call to {@link #next()}. A return of {@code false}
     * indicates the end of a message (or submessage) has been reached. After the end of a sub-message, the next immediate call will
     * indicate whether there are further elements or the end of the outer message. After the end of the main message referenced by
     * the envelope header, the next immediate call may:</p>
     * <ol>
     * <li>Return {@code false} if the source does not contain any subsequent Fudge messages; or</li>
     * <li>Return {@code true} if the source may contain further Fudge messages. Calling {@code next()} will return the envelope header
     * of the next message if one is present, or {@code null} if the source does not contain any further messages.</li>
     * </ol>
     *
     * @return {@code true} if there is at least one more element to read
     */
    @Override
    public boolean hasNext() {
        try{
            return _reader.hasNext();
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int integerValue (final Object o) {
        if (o instanceof Number) {
            return ((Number)o).intValue ();
        } else {
            return 0;
        }
    }

    private void checkMessageEnd () {
        if ((_iteratorStack.size () == 1) && !_iteratorStack.peek().hasNext () && _fieldLookahead.isEmpty ()) {
            _objectStack.pop ();
            _iteratorStack.pop ();
        }
    }

    private Object xmlArrayToPrimitiveArray(final NodeList arr) throws JSONException {
        boolean arrInt = true, arrDouble = true, arrLong = true;
        for (int j = 0; j < arr.getLength(); j++) {
            Object arrValue = arr.item (j) ;
            if (JSONObject.NULL.equals (arrValue)) {
                arrInt = arrDouble = false;
                break;
            } else if (arrValue instanceof Number) {
                if (arrValue instanceof Integer) {
                } else if (arrValue instanceof Double) {
                    arrInt = false;
                    arrLong = false;
                } else if (arrValue instanceof Long) {
                    arrInt = false;
                } else {
                    arrInt = arrDouble = false;
                }
            } else if (arrValue instanceof Node) {
                arrInt = arrDouble = false;
                break;
            } else if (arrValue instanceof NodeList) {
                arrInt = arrDouble = false;
                break;
            }
        }
        if (arrInt) {
            final int[] data = new int[arr.getLength ()];
            for (int j = 0; j < data.length; j++) {
                data[j] = ((Number)arr.item(j)).intValue ();
            }
            return data;
        } else if (arrLong) {
            final long[] data = new long[arr.getLength ()];
            for (int j = 0; j < data.length; j++) {
                data[j] = ((Number)arr.item(j)).longValue ();
            }
            return data;
        } else if (arrDouble) {
            final double[] data = new double[arr.getLength ()];
            for (int j = 0; j < data.length; j++) {
                data[j] = ((Number)arr.item(j)).doubleValue ();
            }
            return data;
        } else {
            return null;
        }
    }

    /**
     * Reads the next stream element from the source and returns the element type.
     *
     * @return the type of the next element in the stream, or {@code null} if the end of stream has been reached at a message
     * boundary (i.e. attempting to read the first byte of an envelope)
     */
    @Override
    public FudgeStreamElement next() {

        Document xmlDoc = getXmlDocument();
        //if (_objectStack.isEmpty ()) {
        //    _objectStack.push (xmlDoc = getXmlDocument());
        //} else {
        //    xmlDoc = _objectStack.peek ();
        //}

        NodeListIterator ni;
        if(_iteratorStack.isEmpty ())
        {
            _iteratorStack.push (ni = new NodeListIterator(xmlDoc.getElementsByTagName("*")));
            Node node = ni.next();

            int cnt = node.getAttributes().getLength();
            int processingDirectives = 0;
            int schemaVersion = 0;
            int taxonomyId = 0;
            NamedNodeMap attr = node.getAttributes();

            for (int i = 0; i < attr.getLength(); i++) {
                final String fieldName = attr.item(i).getNodeName();
                if (fieldName.equals (getProcessingDirectivesField())) {
                    processingDirectives = integerValue (attr.item(i).getNodeValue());
                } else if (fieldName.equals (getSchemaVersionField ())) {
                    schemaVersion = integerValue (attr.item(i).getNodeValue());
                } else if (fieldName.equals (getTaxonomyField ())) {
                    taxonomyId = integerValue (attr.item(i).getNodeValue());
                } else {
                    break;
                }
            }

            setEnvelopeFields (processingDirectives, schemaVersion, taxonomyId);
            checkMessageEnd ();
            return setCurrentElement (FudgeStreamElement.MESSAGE_ENVELOPE);
        } else {
            ni = _iteratorStack.peek ();
        }


        if (ni.hasNext () || !_fieldLookahead.isEmpty ()) {
            final String fieldName;
            if (_fieldLookahead.isEmpty ()) {
                fieldName = ni.next().getNodeName();
            } else {
                fieldName = _fieldLookahead.remove ();
            }
            setCurrentFieldName (fieldName);
            final Object value;
            final boolean isValuelookahead;
            if (_valueLookahead.isEmpty ()) {
                value = xmlDoc.getElementById(fieldName);
                isValuelookahead = false;
            } else {
                value = _valueLookahead.remove ();
                isValuelookahead = true;
            }
            if (value == null) {
                setFieldValue (IndicatorType.INSTANCE);
            } else if (((Document)value).getElementsByTagName(fieldName).getLength() > 0) {
                final NodeList arr = ((Document)value).getElementsByTagName(fieldName);
                Object primArray = xmlArrayToPrimitiveArray(arr);
                if (primArray != null) {
                    setFieldValue (primArray);
                } else {
                    if (isValuelookahead) {
                        // we're interpreting the XML array as a repeated field; the data doesn't match a primitive type
                        setFieldValue (arr.toString ());
                    } else {
                        for (int j = 0; j < arr.getLength(); j++) {
                            _fieldLookahead.add (fieldName);
                            _valueLookahead.add (arr.item(j));
                        }
                        return next ();
                    }
                }
            } else if (value instanceof Document) {
                xmlDoc = (Document)value;
                _objectStack.push (xmlDoc);
                _iteratorStack.push (new NodeListIterator(xmlDoc.getElementsByTagName("*")));
                return setCurrentElement (FudgeStreamElement.SUBMESSAGE_FIELD_START);
            } else {
                setFieldValue (value);
            }
            checkMessageEnd ();
            return setCurrentElement (FudgeStreamElement.SIMPLE_FIELD);
        } else {
            _iteratorStack.pop ();
            _objectStack.pop ();
            checkMessageEnd ();
            return setCurrentElement (FudgeStreamElement.SUBMESSAGE_FIELD_END);
        }
    }

    /**
     * Returns the value last returned by {@link #next()}.
     *
     * @return the type of the current element in the stream
     */
    @Override
    public FudgeStreamElement getCurrentElement() {
        return _currentElement;
    }

    protected FudgeStreamElement setCurrentElement (final FudgeStreamElement currentElement) {
        return _currentElement = currentElement;
    }

    /**
     * If the current stream element is a field, returns the field value.
     *
     * @return current field value
     */
    @Override
    public Object getFieldValue() {
        return _fieldValue;
    }

    protected void setFieldValue (final Object object) {
        // TODO match the object to see what we've got ...
        _fieldValue = object;
    }

    /**
     * Returns the processing directivies specified in the last envelope header read.
     *
     * @return current processing directive flags
     */
    @Override
    public int getProcessingDirectives() {
        return _processingDirectives;
    }

    /**
     * Returns the schema version specified in the last envelope header read.
     *
     * @return current message schema version
     */
    @Override
    public int getSchemaVersion() {
        return _schemaVersion;
    }

    /**
     * Returns the taxonomy identifier specified in the last envelope header read.
     *
     * @return current taxonomy identifier
     */
    @Override
    public short getTaxonomyId() {
        return (short)_taxonomyId;
    }

    /**
     * If the current stream element is a field, returns the {@link FudgeFieldType}.
     *
     * @return current field type
     */
    @Override
    public FudgeFieldType<?> getFieldType() {
        return getFudgeContext ().getTypeDictionary ().getByJavaType (getFieldValue ().getClass ());
    }

    /**
     * If the current stream element is a field, returns the ordinal index, or {@code null} if the field did not include an ordinal.
     *
     * @return current field ordinal
     */
    @Override
    public Integer getFieldOrdinal() {
        return _fieldOrdinal;
    }

    /**
     * If the current stream element is a field, returns the field name. If the underlying stream does not specify a field
     * name, but the ordinal can be resolved through a taxonomy, returns the resolved name.
     *
     * @return current field name
     */
    @Override
    public String getFieldName() {
        return _fieldName;
    }

    private void setNameAndOrdinal (final String name, final Integer ordinal) {
        _fieldName = name;
        _fieldOrdinal = ordinal;
    }

    protected void setCurrentFieldName (final String name) {
        if (name.length () == 0) {
            setNameAndOrdinal (null, null);
        } else {
            try {
                int ordinal = Integer.parseInt (name);
                setNameAndOrdinal (null, ordinal);
            } catch (NumberFormatException nfe) {
                setNameAndOrdinal (name, null);
            }
        }
    }

    protected void setEnvelopeFields (final int processingDirectives, final int schemaVersion, final int taxonomyId) {
        _processingDirectives = processingDirectives;
        _schemaVersion = schemaVersion;
        _taxonomyId = taxonomyId;
        if (_taxonomyId != 0) {
            _taxonomy = getFudgeContext ().getTaxonomyResolver ().resolveTaxonomy ((short)_taxonomyId);
        } else {
            _taxonomy = null;
        }
    }

    /**
     * Returns the current {@link FudgeTaxonomy} corresponding to the taxonomy identifier specified in the message envelope. Returns
     * {@code null} if the message did not specify a taxonomy or the taxonomy identifier cannot be resolved by the bound {@link FudgeContext}.
     *
     * @return current taxonomy if available
     */
    @Override
    public FudgeTaxonomy getTaxonomy() {
        return _taxonomy;
    }

    /**
     * Returns the {@link FudgeContext} bound to the reader used for type and taxonomy resolution.
     *
     * @return the {@code FudgeContext}
     */
    @Override
    public FudgeContext getFudgeContext() {
        return _fudgeContext;
    }

    /**
     * Closes the {@link FudgeStreamReader} and attempts to close the underlying data source if appropriate.
     */
    @Override
    public void close() {
        if (_reader != null) {
            try {
                _reader.close();
            } catch (XMLStreamException e)
            {
                throw new FudgeRuntimeException(e);
            }
        }
    }
}
