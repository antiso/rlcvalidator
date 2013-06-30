package com.icoegroup.rlcvalidator;

import org.w3c.dom.NodeList;

public class ValueXpathValidator extends XPathValidator {

	String refValue;

	@Override
	public boolean validate() {
		boolean result = true;
		NodeList resultNodes = getResultNodes();
		if (resultNodes.getLength() > 0) {
			for (int i = 0; i < resultNodes.getLength(); i++) {
				if (!refValue.equals(resultNodes.item(i).getNodeValue())) {
					error("Values aren't equal: '" + refValue + "' - '"
							+ resultNodes.item(i).getNodeValue() + "'");
					result = false;
				}
			}
		} else
			info("Result of XPath: " + getXpath() + " is empty.");
		return result;
	}

	public String getRefValue() {
		return refValue;
	}

	public void setRefValue(String refValue) {
		this.refValue = refValue;
	}

}
