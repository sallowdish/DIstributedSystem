JFLAGS = -g
JC = javac
JVM= java
Main-Class = RDFSTServer
Class-Path = "libs/commons-cli-1.2.jar:."

.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) -cp $(Class-Path) *java

CLASSES = *.java 
    

	

default: classes

classes: $(CLASSES:.java=.class)


clean:
	$(RM) *.class
	$(RM) RDFSTServer.jar