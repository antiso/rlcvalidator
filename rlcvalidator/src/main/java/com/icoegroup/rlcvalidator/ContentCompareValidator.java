package com.icoegroup.rlcvalidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class ContentCompareValidator extends XPathValidator {
	
	// Name of XML reference file
	private String refFileName;

    private static List<String> toLines(String value) {
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
                BufferedReader in = new BufferedReader(new StringReader(value));
                while ((line = in.readLine()) != null) {
                        lines.add(line);
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return lines;
}

	
	@Override
	public boolean validate() {
		NodeList resultNodes = getResultNodes();
		if (resultNodes.getLength()==0) {
			info("No nodes found.");
			return true;
		}

		List<String> original = toLines(resultNodes.item(0).getNodeValue());
        List<String> revised  = toLines(getRefValue());
        Patch patch = DiffUtils.diff(original, revised);
        boolean result = true;
        
        for (Delta delta: patch.getDeltas()) {
        	result = false;
            error(delta.toString());
        }

		
		return result;
	}

	public String getRefValue() {
		ClassPathResource refResource = new ClassPathResource(refFileName);
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(refResource.getInputStream()));
			return getXpathExpression().evaluate(doc);
		} catch (Exception e) {
			error("Erorr", e);
			throw new ValidatorConfigurationException(e);
		}

	}

	public String getRefFileName() {
		return refFileName;
	}

	public void setRefFileName(String refFileName) {
		this.refFileName = refFileName;
	}
}
