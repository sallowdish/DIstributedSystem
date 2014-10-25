JFLAGS = -g
JC = javac
JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
CLASSES = \
    Street.java \
    Wizard.java \
    Shop.java 

MAIN = Street

default: classes

classes: $(CLASSES:.java=.class)

run: classes
	$(JVM) $(MAIN)

clean:
	$(RM) *.class