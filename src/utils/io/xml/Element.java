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
 * In order to create an element, please use the createElement method instead of
 * invoking the constructor directly. The right place to add user defined
 * initialization code is the init method.
 */

public class Element extends Node {

	protected String name;

	protected Vector attributes;

	protected Node parent;

	protected Vector prefixes;

	public Element() {
	}

	/**
	 * called when all properties are set, but before children are parsed.
	 * Please do not use setParent for initialization code any longer.
	 */

	public void init() {
	}

	/**
	 * removes all children and attributes
	 */

	public void clear() {
		attributes = null;
		children = null;
	}

	/**
	 * Forwards creation request to parent if any, otherwise calls
	 * super.createElement.
	 */

	public Element createElement(String name) {

		return (this.parent == null) ? super.createElement(name) : this.parent.createElement(name);
	}

	/**
	 * Returns the number of attributes of this element.
	 */

	public int getAttributeCount() {
		return attributes == null ? 0 : attributes.size();
	}

	/*
	 * public String getAttributePrefix (int index) { return ((String [])
	 * attributes.elementAt (index)) [1]; }
	 */

	public String getAttributeName(int index) {
		return ((String[]) attributes.elementAt(index))[0];
	}

	public String getAttributeValue(int index) {
		return ((String[]) attributes.elementAt(index))[1];
	}

	public String getAttributeValue(String name) {
		for (int i = 0; i < getAttributeCount(); i++) {
			if (name.equals(getAttributeName(i))) {
				return getAttributeValue(i);
			}
		}
		return null;
	}

	/**
	 * Returns the root node, determined by ascending to the all parents un of
	 * the root element.
	 */

	public Node getRoot() {

		Element current = this;

		while (current.parent != null) {
			if (!(current.parent instanceof Element))
				return current.parent;
			current = (Element) current.parent;
		}

		return current;
	}

	/**
	 * returns the (local) name of the element
	 */

	public String getName() {
		return name;
	}

	public Node getParent() {
		return parent;
	}

	/*
	 * Returns the parent element if available, null otherwise
	 * 
	 * public Element getParentElement() { return (parent instanceof Element) ?
	 * ((Element) parent) : null; }
	 */

	/**
	 * Builds the child elements from the given Parser. By overwriting parse, an
	 * element can take complete control over parsing its subtree.
	 */

	/**
	 * Sets the given attribute; a value of null removes the attribute
	 */

	public void setAttribute(String name, String value) {
		if (attributes == null)
			attributes = new Vector();

		for (int i = attributes.size() - 1; i >= 0; i--) {
			String[] attribut = (String[]) attributes.elementAt(i);
			if (attribut[0].equals(name)) {

				if (value == null) {
					attributes.removeElementAt(i);
				} else {
					attribut[1] = value;
				}
				return;
			}
		}

		attributes.addElement(new String[] { name, value });
	}

	/**
	 * sets the name of the element
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the Parent of this element. Automatically called from the add
	 * method. Please use with care, you can simply create inconsitencies in the
	 * document tree structure using this method!
	 */

	protected void setParent(Node parent) {
		this.parent = parent;
	}
}
