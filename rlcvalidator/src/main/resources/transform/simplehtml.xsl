<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:beans="http://www.springframework.org/schema/beans">
	<xsl:output method="html" include-content-type="no" />
	<xsl:template match="/">
		<html>
			<head>
				<title>RLC Validator</title>
				<style type="text/css">
					/* Table colors */
					.TableHeadingColor     { background: #CCCCFF; color:#000000 } /* Dark mauve */
					.TableSubHeadingColor  { background: #EEEEFF; color:#000000 } /* Light mauve */
					.TableRowColor         { background: #FFFFFF; color:#000000 } /* White */
					table {border: 1; with : 100%; cellpadding: 3; cellspacing: 0;}
					
					@page { margin: 0.79in } 
					p { margin-bottom: 0.08in } 
					h1 { margin-bottom:	0.08in } 
					h2 { margin-bottom: 0.08in } 
					td p { margin-bottom: 0in } 
					th p { margin-bottom: 0in } 
					a:link { so-language: zxx } 
					
				</style>
			</head>
			<body>
				
				<xsl:apply-templates select="/beans:beans/beans:bean[@class='com.icoegroup.rlcvalidator.ValidationProfile']"/>
				
				<table border="1">
					<tr>
						<th colspan="2" class="TableHeadingColor">
							<p align="left">
								<font size="5">
									<b>Rules</b>
								</font>
							</p>
						</th>
					</tr>
					<xsl:apply-templates mode="full"
						select="beans:beans/beans:bean[contains(@class, 'Validator')]"></xsl:apply-templates>
				</table>
					
			</body>
		</html>
	</xsl:template>
	<xsl:template match="beans:bean[@class='com.icoegroup.rlcvalidator.ValidationProfile']">
				<table border="1">
					<tr>
						<th class="TableHeadingColor">
							<p align="left">
								<font size="5">
									<b><xsl:value-of select="@name"></xsl:value-of></b>
								</font>
							</p>
						</th>
						<th class="TableHeadingColor">
							<p align="left">
								<font size="5">
									<b><xsl:value-of select="./beans:description"></xsl:value-of></b>
								</font>
							</p>
						</th>
					</tr>
					  <tr>
						<th colspan="2" class="TableSubHeadingColor">
							<p align="left">
								<font size="5">
									<b>Contained rules</b>
								</font>
							</p>
						</th>
					</tr>
					
					<xsl:for-each select=".//beans:ref">
					<xsl:apply-templates select="."/>
					</xsl:for-each>
				</table>
	</xsl:template>
	
	<xsl:template match="beans:ref">
		<xsl:variable select="@bean" name="validatorName"/>
		
		<tr class="TableRowColor">
			<td>
				<a href="#{$validatorName}">
					<xsl:value-of select="$validatorName"></xsl:value-of>
				</a>
			</td>
			<td>
				<xsl:value-of select="substring-before(/beans:beans/beans:bean[@name=$validatorName]/beans:description,'.')"></xsl:value-of>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="beans:bean" mode="full">
		<xsl:variable name="beanName" select="@name"></xsl:variable>
		<tr class="TableRowColor">
			<td>
				<a id="{$beanName}">
					<xsl:value-of select="$beanName"></xsl:value-of>
				</a>
			</td>
			<td>
				<xsl:value-of select="./beans:description"></xsl:value-of>
			</td>
		</tr>
	</xsl:template>
	
</xsl:stylesheet>