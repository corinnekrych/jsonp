package org.toto

class JSONPWrapper {
	Object wrappedElement
	String callback
	JSONPWrapper(elementArg, callbackArg) {
		wrappedElement = elementArg
		callback = callbackArg
	}
}
