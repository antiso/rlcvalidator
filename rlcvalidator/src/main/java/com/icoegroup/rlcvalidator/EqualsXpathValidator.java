package com.icoegroup.rlcvalidator;

import org.w3c.dom.NodeList;

public class EqualsXpathValidator extends XPathValidator {

	@Override
	public boolean validate() {
		NodeList resultNodes = getResultNodes();
		if (resultNodes.getLength() < 1) {
			error("Incorrect amount of  " + xpath + " nodes.");
			return false;
		}

		String mainValue = resultNodes.item(0).getNodeValue();
		for (int i = 1; i< resultNodes.getLength(); i++) {
			String nodeValue = resultNodes.item(i).getNodeValue();
			if (!mainValue.equals(nodeValue)) {
				error("Values aren't same: '"+ mainValue + "' - '" + nodeValue + "'");
				return false;
			}
		}

		return true;
	}
}
