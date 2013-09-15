<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
	
	<import resource="classpath:rlcvalidator.xml"/>
	
	<bean name="DefaultProfile" class="com.icoegroup.rlcvalidator.ValidationProfile">
		<property name="validators">
			<list>
				<ref bean="RouteValidator"/>
				<ref bean="CreateAckScriptValidator"/>
				<ref bean="FolderValidator"/>
				<ref bean="TCPServerValueValidator"/>
				<ref bean="TCPClientValueValidator"/>
				<ref bean="CommPointsFolderValidator"/>
				<ref bean="FilesSectionNotExistsValidator"/>
			</list>
		</property>
	</bean>
</beans>