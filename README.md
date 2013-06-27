RLC Validator
============

Tool for validating RLC files

Build
======

1. ```  git clone https://github.com/antiso/rlcvalidator.git```
2. ``` cd rlcvalidator/rlcvalidator```
3. ``` mvn clean package ```

The result of actions above is Java JAR file in _target_ folder: ```target/rlcvalidator-0.0.1-SNAPSHOT.jar``` 
You can execute it using following syntax:

```java -jar rlcvalidator-0.0.1-SNAPSHOT.jar <rlcfile>```

where _<rlcfile>_ is fully qualified path to RLC file
The result of validation will be in a folder where tool was launched and will contain two files: _&lt;rlcfile>.html_ and 
_&lt;rlcfile>.txt_ These files contains result of validation.
