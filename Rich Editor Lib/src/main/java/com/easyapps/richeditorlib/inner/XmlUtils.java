/*
 * Copyright (C) 2006 The Android Open Source Project
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

package com.easyapps.richeditorlib.inner;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlUtils {

    public static int
    convertValueToInt(CharSequence charSeq, int defaultValue)
    {
        if (null == charSeq)
            return defaultValue;

        String nm = charSeq.toString();

        // XXX This code is copied from Integer.decode() so we don't
        // have to instantiate an Integer!
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;

        if ('-' == nm.charAt(0)) {
            sign = -1;
            index++;
        }

        if ('0' == nm.charAt(index)) {
            //  Quick check for a zero by itself
            if (index == (len - 1))
                return 0;

            char    c = nm.charAt(index + 1);

            if ('x' == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        }
        else if ('#' == nm.charAt(index))
        {
            index++;
            base = 16;
        }

        return Integer.parseInt(nm.substring(index), base) * sign;
    }

    /**
     * Flatten a Map into an XmlSerializer.  The map can later be read back
     * with readThisMapXml().
     *
     * @param val The map to be flattened.
     * @param name Name attribute to include with this list's tag, or null for
     *             none.
     * @param out XmlSerializer to write the map into.
     *
     * @see #writeListXml
     * @see #writeValueXml
     */
    public static void writeMapXml(Map val, String name, XmlSerializer out)
    throws XmlPullParserException, java.io.IOException
    {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }

        Set s = val.entrySet();
        Iterator i = s.iterator();

        out.startTag(null, "map");
        if (name != null) {
            out.attribute(null, "name", name);
        }

        while (i.hasNext()) {
            Map.Entry e = (Map.Entry)i.next();
            writeValueXml(e.getValue(), (String)e.getKey(), out);
        }

        out.endTag(null, "map");
    }

    /**
     * Flatten a List into an XmlSerializer.  The list can later be read back
     * with readThisListXml().
     *
     * @param val The list to be flattened.
     * @param name Name attribute to include with this list's tag, or null for
     *             none.
     * @param out XmlSerializer to write the list into.
     *
     * @see #writeMapXml
     * @see #writeValueXml
     */
    public static void writeListXml(List val, String name, XmlSerializer out)
    throws XmlPullParserException, java.io.IOException
    {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }

        out.startTag(null, "list");
        if (name != null) {
            out.attribute(null, "name", name);
        }

        int N = val.size();
        int i=0;
        while (i < N) {
            writeValueXml(val.get(i), null, out);
            i++;
        }

        out.endTag(null, "list");
    }
    
    public static void writeSetXml(Set val, String name, XmlSerializer out)
            throws XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }
        
        out.startTag(null, "set");
        if (name != null) {
            out.attribute(null, "name", name);
        }
        
        for (Object v : val) {
            writeValueXml(v, null, out);
        }
        
        out.endTag(null, "set");
    }

    /**
     * Flatten a byte[] into an XmlSerializer.  The list can later be read back
     * with readThisByteArrayXml().
     *
     * @param val The byte array to be flattened.
     * @param name Name attribute to include with this array's tag, or null for
     *             none.
     * @param out XmlSerializer to write the array into.
     *
     * @see #writeMapXml
     * @see #writeValueXml
     */
    public static void writeByteArrayXml(byte[] val, String name,
                                         XmlSerializer out)
            throws java.io.IOException {

        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }

        out.startTag(null, "byte-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }

        final int N = val.length;
        out.attribute(null, "num", Integer.toString(N));

        StringBuilder sb = new StringBuilder(val.length*2);
        for (int b : val) {
            int h = b >> 4;
            sb.append(h >= 10 ? ('a' + h - 10) : ('0' + h));
            h = b & 0xff;
            sb.append(h >= 10 ? ('a' + h - 10) : ('0' + h));
        }

        out.text(sb.toString());

        out.endTag(null, "byte-array");
    }

    /**
     * Flatten an int[] into an XmlSerializer.  The list can later be read back
     * with readThisIntArrayXml().
     *
     * @param val The int array to be flattened.
     * @param name Name attribute to include with this array's tag, or null for
     *             none.
     * @param out XmlSerializer to write the array into.
     *
     * @see #writeMapXml
     * @see #writeValueXml
     * @see #readThisIntArrayXml
     */
    public static void writeIntArrayXml(int[] val, String name,
                                        XmlSerializer out)
            throws java.io.IOException {

        if (val == null) {
            out.startTag(null, "null");
            out.endTag(null, "null");
            return;
        }

        out.startTag(null, "int-array");
        if (name != null) {
            out.attribute(null, "name", name);
        }

        final int N = val.length;
        out.attribute(null, "num", Integer.toString(N));

        for (int value : val) {
            out.startTag(null, "item");
            out.attribute(null, "value", Integer.toString(value));
            out.endTag(null, "item");
        }

        out.endTag(null, "int-array");
    }

    /**
     * Flatten an object's value into an XmlSerializer.  The value can later
     * be read back with readThisValueXml().
     *
     * Currently supported value types are: null, String, Integer, Long,
     * Float, Double Boolean, Map, List.
     *
     * @param v The object to be flattened.
     * @param name Name attribute to include with this value's tag, or null
     *             for none.
     * @param out XmlSerializer to write the object into.
     *
     * @see #writeMapXml
     * @see #writeListXml
     */
    public static void writeValueXml(Object v, String name, XmlSerializer out)
    throws XmlPullParserException, java.io.IOException
    {
        String typeStr;
        if (v == null) {
            out.startTag(null, "null");
            if (name != null) {
                out.attribute(null, "name", name);
            }
            out.endTag(null, "null");
            return;
        } else if (v instanceof String) {
            out.startTag(null, "string");
            if (name != null) {
                out.attribute(null, "name", name);
            }
            out.text(v.toString());
            out.endTag(null, "string");
            return;
        } else if (v instanceof Integer) {
            typeStr = "int";
        } else if (v instanceof Long) {
            typeStr = "long";
        } else if (v instanceof Float) {
            typeStr = "float";
        } else if (v instanceof Double) {
            typeStr = "double";
        } else if (v instanceof Boolean) {
            typeStr = "boolean";
        } else if (v instanceof byte[]) {
            writeByteArrayXml((byte[])v, name, out);
            return;
        } else if (v instanceof int[]) {
            writeIntArrayXml((int[])v, name, out);
            return;
        } else if (v instanceof Map) {
            writeMapXml((Map)v, name, out);
            return;
        } else if (v instanceof List) {
            writeListXml((List)v, name, out);
            return;
        } else if (v instanceof Set) {
            writeSetXml((Set)v, name, out);
            return;
        } else if (v instanceof CharSequence) {
            // XXX This is to allow us to at least write something if
            // we encounter styled text...  but it means we will drop all
            // of the styling information. :(
            out.startTag(null, "string");
            if (name != null) {
                out.attribute(null, "name", name);
            }
            out.text(v.toString());
            out.endTag(null, "string");
            return;
        } else {
            throw new RuntimeException("writeValueXml: unable to write value " + v);
        }

        out.startTag(null, typeStr);
        if (name != null) {
            out.attribute(null, "name", name);
        }
        out.attribute(null, "value", v.toString());
        out.endTag(null, typeStr);
    }

    /**
     * Read a HashMap object from an XmlPullParser.  The XML data could
     * previously have been generated by writeMapXml().  The XmlPullParser
     * must be positioned <em>after</em> the tag that begins the map.
     *
     * @param parser The XmlPullParser from which to read the map data.
     * @param endTag Name of the tag that will end the map, usually "map".
     * @param name An array of one string, used to return the name attribute
     *             of the map's tag.
     *
     * @return HashMap The newly generated map.
     *
     */
    public static HashMap<String, ?> readThisMapXml(XmlPullParser parser, String endTag,
                                                    String[] name) throws XmlPullParserException, java.io.IOException
    {
        HashMap<String, Object> map = new HashMap<String, Object>();

        int eventType = parser.getEventType();
        do {
            if (eventType == parser.START_TAG) {
                Object val = readThisValueXml(parser, name);
                map.put(name[0], val);
            } else if (eventType == parser.END_TAG) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                throw new XmlPullParserException(
                    "Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != parser.END_DOCUMENT);

        throw new XmlPullParserException(
            "Document ended before " + endTag + " end tag");
    }

    /**
     * Read an ArrayList object from an XmlPullParser.  The XML data could
     * previously have been generated by writeListXml().  The XmlPullParser
     * must be positioned <em>after</em> the tag that begins the list.
     *
     * @param parser The XmlPullParser from which to read the list data.
     * @param endTag Name of the tag that will end the list, usually "list".
     * @param name An array of one string, used to return the name attribute
     *             of the list's tag.
     *
     * @return HashMap The newly generated list.
     *
     */
    public static ArrayList readThisListXml(XmlPullParser parser, String endTag, String[] name)
    throws XmlPullParserException, java.io.IOException
    {
        ArrayList list = new ArrayList();

        int eventType = parser.getEventType();
        do {
            if (eventType == parser.START_TAG) {
                Object val = readThisValueXml(parser, name);
                list.add(val);
                //System.out.println("Adding to list: " + val);
            } else if (eventType == parser.END_TAG) {
                if (parser.getName().equals(endTag)) {
                    return list;
                }
                throw new XmlPullParserException(
                    "Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != parser.END_DOCUMENT);

        throw new XmlPullParserException(
            "Document ended before " + endTag + " end tag");
    }
    
    /**
     * Read a HashSet object from an XmlPullParser. The XML data could previously
     * have been generated by writeSetXml(). The XmlPullParser must be positioned
     * <em>after</em> the tag that begins the set.
     * 
     * @param parser The XmlPullParser from which to read the set data.
     * @param endTag Name of the tag that will end the set, usually "set".
     * @param name An array of one string, used to return the name attribute
     *             of the set's tag.
     *
     * @return HashSet The newly generated set.
     * 
     */
    public static HashSet readThisSetXml(XmlPullParser parser, String endTag, String[] name)
            throws XmlPullParserException, java.io.IOException {
        HashSet set = new HashSet();
        
        int eventType = parser.getEventType();
        do {
            if (eventType == parser.START_TAG) {
                Object val = readThisValueXml(parser, name);
                set.add(val);
                //System.out.println("Adding to set: " + val);
            } else if (eventType == parser.END_TAG) {
                if (parser.getName().equals(endTag)) {
                    return set;
                }
                throw new XmlPullParserException(
                        "Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != parser.END_DOCUMENT);
        
        throw new XmlPullParserException(
                "Document ended before " + endTag + " end tag");
    }

    /**
     * Read an int[] object from an XmlPullParser.  The XML data could
     * previously have been generated by writeIntArrayXml().  The XmlPullParser
     * must be positioned <em>after</em> the tag that begins the list.
     *
     * @param parser The XmlPullParser from which to read the list data.
     * @param endTag Name of the tag that will end the list, usually "list".
     * @param name An array of one string, used to return the name attribute
     *             of the list's tag.
     *
     * @return Returns a newly generated int[].
     *
     */
    public static int[] readThisIntArrayXml(XmlPullParser parser,
                                            String endTag, String[] name)
            throws XmlPullParserException, java.io.IOException {

        int num;
        try {
            num = Integer.parseInt(parser.getAttributeValue(null, "num"));
        } catch (NullPointerException e) {
            throw new XmlPullParserException(
                    "Need num attribute in byte-array");
        } catch (NumberFormatException e) {
            throw new XmlPullParserException(
                    "Not a number in num attribute in byte-array");
        }

        int[] array = new int[num];
        int i = 0;

        int eventType = parser.getEventType();
        do {
            if (eventType == parser.START_TAG) {
                if (parser.getName().equals("item")) {
                    try {
                        array[i] = Integer.parseInt(
                                parser.getAttributeValue(null, "value"));
                    } catch (NullPointerException e) {
                        throw new XmlPullParserException(
                                "Need value attribute in item");
                    } catch (NumberFormatException e) {
                        throw new XmlPullParserException(
                                "Not a number in value attribute in item");
                    }
                } else {
                    throw new XmlPullParserException(
                            "Expected item tag at: " + parser.getName());
                }
            } else if (eventType == parser.END_TAG) {
                if (parser.getName().equals(endTag)) {
                    return array;
                } else if (parser.getName().equals("item")) {
                    i++;
                } else {
                    throw new XmlPullParserException(
                        "Expected " + endTag + " end tag at: "
                        + parser.getName());
                }
            }
            eventType = parser.next();
        } while (eventType != parser.END_DOCUMENT);

        throw new XmlPullParserException(
            "Document ended before " + endTag + " end tag");
    }

    private static Object readThisValueXml(XmlPullParser parser, String[] name)
    throws XmlPullParserException, java.io.IOException
    {
        final String valueName = parser.getAttributeValue(null, "name");
        final String tagName = parser.getName();

        //System.out.println("Reading this value tag: " + tagName + ", name=" + valueName);

        Object res;

        switch (tagName) {
            case "null":
                res = null;
                break;
            case "string":
                StringBuilder value = new StringBuilder();
                int eventType;
                while ((eventType = parser.next()) != parser.END_DOCUMENT) {
                    if (eventType == parser.END_TAG) {
                        if (parser.getName().equals("string")) {
                            name[0] = valueName;
                            //System.out.println("Returning value for " + valueName + ": " + value);
                            return value.toString();
                        }
                        throw new XmlPullParserException(
                                "Unexpected end tag in <string>: " + parser.getName());
                    } else if (eventType == parser.TEXT) {
                        value.append(parser.getText());
                    } else if (eventType == parser.START_TAG) {
                        throw new XmlPullParserException(
                                "Unexpected start tag in <string>: " + parser.getName());
                    }
                }
                throw new XmlPullParserException(
                        "Unexpected end of document in <string>");

                // all work already done by readThisPrimitiveValueXml
            case "int-array":
                parser.next();
                res = readThisIntArrayXml(parser, "int-array", name);
                name[0] = valueName;
                //System.out.println("Returning value for " + valueName + ": " + res);
                return res;
            case "map":
                parser.next();
                res = readThisMapXml(parser, "map", name);
                name[0] = valueName;
                //System.out.println("Returning value for " + valueName + ": " + res);
                return res;
            case "list":
                parser.next();
                res = readThisListXml(parser, "list", name);
                name[0] = valueName;
                //System.out.println("Returning value for " + valueName + ": " + res);
                return res;
            case "set":
                parser.next();
                res = readThisSetXml(parser, "set", name);
                name[0] = valueName;
                //System.out.println("Returning value for " + valueName + ": " + res);
                return res;
            default:
                throw new XmlPullParserException(
                        "Unknown tag: " + tagName);
        }

        // Skip through to end tag.
        int eventType;
        while ((eventType = parser.next()) != parser.END_DOCUMENT) {
            if (eventType == parser.END_TAG) {
                if (parser.getName().equals(tagName)) {
                    name[0] = valueName;
                    //System.out.println("Returning value for " + valueName + ": " + res);
                    return res;
                }
                throw new XmlPullParserException(
                    "Unexpected end tag in <" + tagName + ">: " + parser.getName());
            } else if (eventType == parser.TEXT) {
                throw new XmlPullParserException(
                "Unexpected text in <" + tagName + ">: " + parser.getName());
            } else if (eventType == parser.START_TAG) {
                throw new XmlPullParserException(
                    "Unexpected start tag in <" + tagName + ">: " + parser.getName());
            }
        }
        throw new XmlPullParserException(
            "Unexpected end of document in <" + tagName + ">");
    }

    private static Object readThisPrimitiveValueXml(XmlPullParser parser, String tagName)
    throws XmlPullParserException
    {
        try {
            switch (tagName) {
                case "int":
                    return Integer.parseInt(parser.getAttributeValue(null, "value"));
                case "long":
                    return Long.valueOf(parser.getAttributeValue(null, "value"));
                case "float":
                    return Float.valueOf(parser.getAttributeValue(null, "value"));
                case "double":
                    return Double.valueOf(parser.getAttributeValue(null, "value"));
                case "boolean":
                    return Boolean.valueOf(parser.getAttributeValue(null, "value"));
                default:
                    return null;
            }
        } catch (NullPointerException e) {
            throw new XmlPullParserException("Need value attribute in <" + tagName + ">");
        } catch (NumberFormatException e) {
            throw new XmlPullParserException(
                    "Not a number in value attribute in <" + tagName + ">");
        }
    }

}