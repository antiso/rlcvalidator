package com.icoegroup.rlcvalidator;

import org.w3c.dom.NodeList;

public class FolderValidator extends XPathValidator {

	@Override
	public boolean validate() {
		NodeList resultNodes = getResultNodes();
		if (resultNodes.getLength() != 1) {
			error("Incorrect amount of  " + xpath + " nodes.");
			return false;
		}
		String nodeValue = resultNodes.item(0).getNodeValue();
		String routeName = nodeValue.contains(";") ? nodeValue
				.substring(nodeValue.lastIndexOf(';')+1) : nodeValue;
		if (!routeName.equals(ctx.getRlcName())) {
			error("Route folder name doesn't match RLC name: " + routeName + " - " + ctx.getRlcName());
			return false;
		}
		return true;
	}

}
