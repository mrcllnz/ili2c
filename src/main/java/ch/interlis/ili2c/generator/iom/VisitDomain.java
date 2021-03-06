package ch.interlis.ili2c.generator.iom;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.HashMap;

import ch.interlis.ili2c.metamodel.*;

/**
 * @author ce
 */
public class VisitDomain implements Visitor, ObjWriter {
	public void visitObject(Object obj1, VisitorCallback cb) {
		Domain obj=(Domain)obj1;
		Object refobj=obj.getContainer();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getExtending();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
		refobj=obj.getType();
		if(refobj!=null){
			cb.addPendingObject(refobj);
		}
	}

	public void writeObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Domain obj=(Domain)obj1;
		String tag=IomGenerator.MODEL+"."+IomGenerator.TOPIC+".Domain";
		out.write("<"+tag+" TID=\""+cb.encodeOid(oid)+"\">");
		out.write("<name>"+cb.encodeString(obj.getName())+"</name>");
		out.write("<container REF=\""+cb.encodeOid(cb.getobjid(obj.getContainer()))+"\"/>");
		out.write("<isAbstract>"+cb.encodeBoolean(obj.isAbstract())+"</isAbstract>");
		out.write("<isFinal>"+cb.encodeBoolean(obj.isFinal())+"</isFinal>");
		if(obj.getExtending()!=null){
			out.write("<base REF=\""+cb.encodeOid(cb.getobjid(obj.getExtending()))+"\"/>");
		}
		if(obj.getType()!=null){
			out.write("<type REF=\""+cb.encodeOid(cb.getobjid(obj.getType()))+"\"/>");
		}
		out.write("</"+tag+">");
	}
	public void bootstrapWriteObject(Writer out, Object obj1, WriterCallback cb)
		throws IOException {
		String oid=cb.getobjid(obj1);
		Domain obj=(Domain)obj1;
		String tagPrefix=IomGenerator.MODEL+"."+IomGenerator.TOPIC;
		String tag=tagPrefix+".Domain";
		
		out.write("obj=new iom_object();"+cb.newline());
		out.write("obj->setOid(X(\""+oid+"\"));"+cb.newline());
		out.write("obj->setTag(ParserHandler::getTagId(\""+tag+"\"));"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"name\"),X(\""+obj.getName()+"\"));"+cb.newline());

		out.write("link=new iom_object(true);"+cb.newline());
		out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ContainerElements"+"\"));"+cb.newline());
		out.write("objref=new iom_objref();"+cb.newline());
		out.write("objref->setOid(X(\""+cb.getobjid(obj.getContainer())+"\"));"+cb.newline());
		out.write("link->setLinkEnd(ParserHandler::getTagId(\"elements\"),obj);"+cb.newline());
		out.write("link->setLinkEndR(ParserHandler::getTagId(\"container\"),objref);"+cb.newline());
		out.write("metamodel->addObject(link);"+cb.newline());

		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isAbstract\"),X(\""+cb.encodeBoolean(obj.isAbstract())+"\"));"+cb.newline());
		out.write("obj->setAttrValue(ParserHandler::getTagId(\"isFinal\"),X(\""+cb.encodeBoolean(obj.isFinal())+"\"));"+cb.newline());
		
		
		if(obj.getExtending()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".ExtendedByBaseContainer"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getExtending())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"extendedBy\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"base\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}

		if(obj.getType()!=null){
			out.write("link=new iom_object(true);"+cb.newline());
			out.write("link->setTag(ParserHandler::getTagId(\""+tagPrefix+".DomainDefType"+"\"));"+cb.newline());
			out.write("objref=new iom_objref();"+cb.newline());
			out.write("objref->setOid(X(\""+cb.getobjid(obj.getType())+"\"));"+cb.newline());
			out.write("link->setLinkEnd(ParserHandler::getTagId(\"domainDef\"),obj);"+cb.newline());
			out.write("link->setLinkEndR(ParserHandler::getTagId(\"type\"),objref);"+cb.newline());
			out.write("metamodel->addObject(link);"+cb.newline());
		}
		
		out.write("metamodel->addObject(obj);"+cb.newline());
		
	}
}
