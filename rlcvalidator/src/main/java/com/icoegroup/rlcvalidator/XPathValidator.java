package com.icoegroup.rlcvalidator;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

public abstract class XPathValidator extends Validator {


	String xpath;
	private XPathExpression xpathExpr;
	
	
	protected XPathExpression getXpathExpression() {
		try {
			xpathExpr = XPathFactory.newInstance().newXPath().compile(xpath);
			return xpathExpr;
		} catch (XPathExpressionException e) {
			log.error("Can't create XPath expression", e);
			throw new ValidatorConfigurationException(e);
		}
	}
	
	protected NodeList getResultNodes() {
		try {
			return (NodeList) getXpathExpression().evaluate(ctx.getDomSource().getNode(), XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new ValidatorConfigurationException(e);
		}
	}
	

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}



}
