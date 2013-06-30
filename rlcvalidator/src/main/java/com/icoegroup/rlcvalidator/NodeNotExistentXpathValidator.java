package com.icoegroup.rlcvalidator;

public class NodeNotExistentXpathValidator extends XPathValidator {

	@Override
	public boolean validate() {
		if (getResultNodes().getLength()>0) {
			error("Node exists: " + getXpath());
			return false;
		}
		return true;
			
	}

}
