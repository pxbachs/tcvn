package utils.io.xml;

/* Copyright (c) 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The  above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE. */

import java.util.Vector;

/**
 * A common base class for Document and Element, also used for storing XML
 * fragments.
 */

public class Node { // implements XmlIO{

	public static final int DOCUMENT = 0;

	public static final int ELEMENT = 2;

	public static final int TEXT = 4;

	public static final int CDSECT = 5;

	public static final int ENTITY_REF = 6;

	public static final int IGNORABLE_WHITESPACE = 7;

	public static final int PROCESSING_INSTRUCTION = 8;

	public static final int COMMENT = 9;

	public static final int DOCDECL = 10;

	protected Vector children;

	protected StringBuffer types;

	/**
	 * inserts the given child object of the given type at the given index.
	 */

	public void addChild(int index, int type, Object child) {

		if (child == null)
			throw new NullPointerException();

		if (children == null) {
			children = new Vector();
			types = new StringBuffer();
		}

		if (type == ELEMENT) {
			if (!(child instanceof Element))
				throw new RuntimeException("Element obj expected)");

			((Element) child).setParent(this);
		} else if (!(child instanceof String))
			throw new RuntimeException("String expected");

		children.insertElementAt(child, index);
		types.insert(index, (char) type);
	}

	/** convenience method for addChild (getChildCount (), child) */

	public void addChild(int type, Object child) {
		addChild(getChildCount(), type, child);
	}

	/**
	 * Builds a default element with the given properties. Elements should
	 * always be created using this method instead of the constructor in order
	 * to enable construction of specialized subclasses by deriving custom
	 * Document classes. Please note: For no namespace, please use
	 * Xml.NO_NAMESPACE, null is not a legal value. Currently, null is converted
	 * to Xml.NO_NAMESPACE, but future versions may throw an exception.
	 */

	public Element createElement(String name) {

		Element e = new Element();
		e.name = name;
		return e;
	}

	/**
	 * Returns the child object at the given index. For child elements, an
	 * Element object is returned. For all other child types, a String is
	 * returned.
	 */

	public Object getChild(int index) {
		return children.elementAt(index);
	}

	/** Returns the number of child objects */

	public int getChildCount() {
		return children == null ? 0 : children.size();
	}

	/**
	 * returns the element at the given index. If the node at the given index is
	 * a text node, null is returned
	 */

	public Element getElement(int index) {
		Object child = getChild(index);
		return (child instanceof Element) ? (Element) child : null;
	}

	/**
	 * Returns the element with the given namespace and name. If the element is
	 * not found, or more than one matching elements are found, an exception is
	 * thrown.
	 */

	public Element getElement(String name) {

		int i = indexOf(name, 0);

		if (i == -1)
			throw new RuntimeException("Element " + name + (i == -1 ? " not found in " : " more than once in ") + this);

		return getElement(i);
	}

	/*
	 * returns "#document-fragment". For elements, the element name is returned
	 * 
	 * public String getName() { return "#document-fragment"; }
	 * 
	 * /** Returns the namespace of the current element. For Node and Document,
	 * Xml.NO_NAMESPACE is returned.
	 * 
	 * public String getNamespace() { return ""; }
	 * 
	 * public int getNamespaceCount () { return 0; }
	 * 
	 * /** returns the text content if the element has text-only content. Throws
	 * an exception for mixed content
	 * 
	 * public String getText() {
	 * 
	 * StringBuffer buf = new StringBuffer(); int len = getChildCount();
	 * 
	 * for (int i = 0; i < len; i++) { if (isText(i)) buf.append(getText(i));
	 * else if (getType(i) == ELEMENT) throw new RuntimeException("not text-only
	 * content!"); }
	 * 
	 * return buf.toString(); }
	 */

	public String getContent() {
		return getText(0);
	}

	/**
	 * Returns the text node with the given index or null if the node with the
	 * given index is not a text node.
	 */

	private String getText(int index) {
		if (index == 0) {
			StringBuffer buff = new StringBuffer("");
			for (int i = 0; i < getChildCount(); i++)
				if (isText(i))
					buff.append((String) getChild(i));

			return buff.toString();
		}

		return (isText(index)) ? (String) getChild(index) : null;
	}

	/**
	 * Returns the type of the child at the given index. Possible types are
	 * ELEMENT, TEXT, COMMENT, and PROCESSING_INSTRUCTION
	 */

	public int getType(int index) {
		return types.charAt(index);
	}

	/**
	 * Convenience method for indexOf (getNamespace (), name, startIndex).
	 * 
	 * public int indexOf(String name, int startIndex) { return
	 * indexOf(getNamespace(), name, startIndex); }
	 */

	/**
	 * Performs search for an element with the given namespace and name,
	 * starting at the given start index. A null namespace matches any
	 * namespace, please use Xml.NO_NAMESPACE for no namespace). returns -1 if
	 * no matching element was found.
	 */

	public int indexOf(String name, int startIndex) {

		int len = getChildCount();

		for (int i = startIndex; i < len; i++) {

			Element child = getElement(i);

			if (child != null && name.equals(child.getName()))
				return i;
		}
		return -1;
	}

	public boolean isText(int i) {
		int t = getType(i);
		return t == TEXT || t == IGNORABLE_WHITESPACE || t == CDSECT;
	}

	/**
	 * Recursively builds the child elements from the given parser until an end
	 * tag or end document is found. The end tag is not consumed.
	 */

	/** Removes the child object at the given index */

	public void removeChild(int idx) {
		children.removeElementAt(idx);

		/** * Modification by HHS - start ** */
		// types.deleteCharAt (index);
		/***/
		int n = types.length() - 1;

		for (int i = idx; i < n; i++)
			types.setCharAt(i, types.charAt(i + 1));

		types.setLength(n);

		/** * Modification by HHS - end ** */
	}
}
