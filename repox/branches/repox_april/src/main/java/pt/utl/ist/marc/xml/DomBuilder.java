/*
 * DOMBuilder.java
 *
 * Created on 4 de Janeiro de 2002, 10:44
 */

package pt.utl.ist.marc.xml;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pt.utl.ist.marc.Field;
import pt.utl.ist.marc.Record;
import pt.utl.ist.marc.Subfield;
import pt.utl.ist.util.DomUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

/** Utility class to code Marc records in XML
 * @author Nuno Freire
 */
public class DomBuilder {
	private static Logger log = Logger.getLogger(DomBuilder.class);
    
    /** Creates a new instance of DOMBuilder */
    public DomBuilder() {
    }

    /** Creates an XML representation of a marc record
     * @param rec a marc record
     * @return the record in xml
     */    
    public static String record2XMLString(Record rec){ 
        return record2XMLString(rec,true);        
    }
    /** Creates an XML representation of a marc record
     * @param rec a marc record
     * @param withXmlDeclaration include the xml declaration
     * @return the record in xml
     */    
    public static String record2XMLString(Record rec, boolean withXmlDeclaration){ 
        Document doc=record2Dom(rec);
        return DomUtil.domToString(doc,withXmlDeclaration);
    }   

    /** Creates an XML representation of a marc record
     * @param recs a list of marc records
     * @return the records in xml
     */  
    public static String record2XMLString(List recs){ 
        return record2XMLString(recs,true);        
    }   
    

    /** Creates an XML representation of a marc record
     * @param recs a list of marc records
     * @param withXmlDeclaration include the xml declaration
     * @return the records in xml
     */  
    public static String record2XMLString(List recs, boolean withXmlDeclaration){ 
        Document doc=record2Dom(recs);
        return DomUtil.domToString(doc,withXmlDeclaration);
    }
    
    
    
    
    
    /** Creates an XML representation of a marc record
     * @param rec a marc record
     * @return the record in xml
     */    
    public static byte[] record2XMLBytes(Record rec){ 
        return record2XMLBytes(rec,true);        
    }
    /** Creates an XML representation of a marc record
     * @param rec a marc record
     * @param withXmlDeclaration include the xml declaration
     * @return the record in xml
     */    
    public static byte[] record2XMLBytes(Record rec, boolean withXmlDeclaration){ 
        Document doc=record2Dom(rec);
        return DomUtil.domToBytes(doc,withXmlDeclaration);
    }   

    /** Creates an XML representation of a marc record
     * @param recs a list of marc records
     * @return the record in xml
     */    
    public static byte[] record2XMLBytes(List recs){ 
        return record2XMLBytes(recs,true);        
    }   

    /** Creates an XML representation of several marc records
     * @param recs marc records
     * @param withXmlDeclaration include the xml declaration
     * @return the record in xml
     */    
    public static byte[] record2XMLBytes(List recs, boolean withXmlDeclaration){ 
        Document doc=record2Dom(recs);
        return DomUtil.domToBytes(doc,withXmlDeclaration);
    }    
    
    
    
    

    

    public static Document record2Dom(List recs){ 
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument(); // Create from whole cloth
            Element collection =  document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","collection");
            collection.setAttribute("xmlns","http://www.bn.pt/standards/metadata/marcxml/1.0/");
            collection.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
            collection.setAttribute("xsi:schemaLocation","http://www.bn.pt/standards/metadata/marcxml/1.0/ http://xml.bn.pt/schemas/Unimarc-1.0.xsd");
            document.appendChild(collection);
            for (Object rec1 : recs) {
                Record rec = (Record) rec1;
                if (rec != null)
                    collection.appendChild(createRecordDom(document, rec));
            }
            return document;
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }
    }    
    
    
    
        
    
    
    
    /** Creates an XML representation of a marc record
     * @param rec a marc record
     *
     * @return Dom Document representing the record
     *
     */    
    public static Document record2Dom(Record rec){ 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument(); // Create from whole cloth
            Element collection =  document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","collection");
            collection.setAttribute("xmlns","http://www.bn.pt/standards/metadata/marcxml/1.0/");
            collection.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
            collection.setAttribute("xsi:schemaLocation","http://www.bn.pt/standards/metadata/marcxml/1.0/ http://xml.bn.pt/schemas/Unimarc-1.0.xsd");
            document.appendChild(collection);
            collection.appendChild(createRecordDom(document,rec));
            return document;
        } catch (UnsupportedEncodingException e) { 
            throw new RuntimeException(e);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }        
    } 
    
    
    
    /** Creates an XML representation of a marc record
     * @param rec a marc record
     *
     * @return Dom Document representing the record
     *
     */    
    public static Element record2DomElement(Record rec, Document document){ 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (document==null)
            	document = builder.newDocument(); // Create from whole cloth
//            Element collection =  document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","collection");
            Element recElement=createRecordDom(document,rec);
//            recElement.setAttribute("xmlns","http://www.bn.pt/standards/metadata/marcxml/1.0/");
//            recElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
//            recElement.setAttribute("xsi:schemaLocation","http://www.bn.pt/standards/metadata/marcxml/1.0/ http://xml.bn.pt/schemas/Unimarc-1.0.xsd");
            return recElement;
        } catch (UnsupportedEncodingException e) { 
            throw new RuntimeException(e);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }        
    } 
    
//    /** Transforms a DOM into a String
//     * @param doc a DOM Document
//     * @param withXmlDeclaration include the xml declaration?
//     * @return String representation of the DOM
//     * @deprecated use same method in pt.utl.ist.util.DomUtil
//     */    
//    public static String DomToString(Node doc, boolean withXmlDeclaration){        
//        try {            
//            TransformerFactory tFactory = TransformerFactory.newInstance();
//            Transformer transformer = tFactory.newTransformer();
//            if (! withXmlDeclaration)
//                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
//            DOMSource source = new DOMSource(doc);
//            ByteArrayOutputStream output=new ByteArrayOutputStream();
//            StreamResult result = new StreamResult(output);
//            transformer.transform(source, result);
//            return output.toString();
//        } catch (TransformerConfigurationException tce) {
//            // Error generated by the parser
//            System.out.println("\n** Transformer Factory error");
//            System.out.println(" " + tce.getMessage() );
//            // Use the contained exception, if any
//            Throwable x = tce;
//            if (tce.getException() != null)
//                x = tce.getException();
//            x.printStackTrace();
//            log.error(x);
//        } catch (TransformerException te) {
//            // Error generated by the parser
//            System.out.println("\n** Transformation error");
//            System.out.println(" " + te.getMessage() );
//            // Use the contained exception, if any
//            Throwable x = te;
//            if (te.getException() != null)
//                x = te.getException();
//            x.printStackTrace();
//            log.error(x);            
//        }
//        return null;
//    }    

    
    
//    
//    /** Transforms a DOM via a stylesheet, and returns the result in a String
//     * @param doc a DOM Document
//     * @param stylesheet complete path to the stylesheet
//     * @return transformed DOM as a String
//     * @deprecated use same method in pt.utl.ist.util.DomUtil
//     */    
////    public static String TransformDom(Document doc, String stylesheet){        
////        return TransformDom(doc, stylesheet, true);
////    }
//    /** Transforms a DOM via a stylesheet, and returns the result in a String
//     * @param doc a DOM Document
//     * @param stylesheet complete path to the stylesheet
//     * @param withXmlDeclaration include the XML declaration?
//     * @return transformed DOM as a String
//     * @deprecated use same method in pt.utl.ist.util.DomUtil
//     */    
////    public static String TransformDom(Document doc, String stylesheet, boolean withXmlDeclaration){        
////        try {            
////            TransformerFactory tFactory = TransformerFactory.newInstance();
////            StreamSource stylesource = new StreamSource(stylesheet);            
////            Transformer transformer = tFactory.newTransformer(stylesource);
////            if (! withXmlDeclaration)
////                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
////            DOMSource source = new DOMSource(doc);
////            ByteArrayOutputStream output=new ByteArrayOutputStream();
////            StreamResult result = new StreamResult(output);
////            transformer.transform(source, result);
////            return output.toString();
////        } catch (TransformerConfigurationException tce) {
////            Throwable x = tce;
////            if (tce.getException() != null)
////                x = tce.getException();
////            throw new RuntimeException(x);
////        } catch (TransformerException te) {
////            Throwable x = te;
////            if (te.getException() != null)
////                x = te.getException();
////            throw new RuntimeException(x);
////        }
////    }    
//
//    
//    /** Transforms a DOM via a stylesheet, and returns the result in another DOM
//     * Document
//     * @param doc a DOM Document
//     * @param stylesheet complete path to the stylesheet
//     * @return transformed DOM
//     * @deprecated use same method in pt.utl.ist.util.DomUtil
//     */    
////    public static Document TransformDomIntoDom(Document doc, String stylesheet){        
////        try {            
////            TransformerFactory tFactory = TransformerFactory.newInstance();
////            StreamSource stylesource = new StreamSource(stylesheet);            
////            Transformer transformer = tFactory.newTransformer(stylesource);
////            DOMSource source = new DOMSource(doc);
////            DOMResult result = new DOMResult();
////            transformer.transform(source, result);
////            return (Document) result.getNode();
////        } catch (TransformerConfigurationException tce) {
////            log.debug(tce.getMessage(),tce);            
////            Throwable x = tce;
////            if (tce.getException() != null)
////                x = tce.getException();
////            throw new RuntimeException(x);
////        } catch (TransformerException te) { 
////            log.debug(te.getMessage(),te);            
////            Throwable x = te;
////            if (te.getException() != null)
////                x = te.getException();
////            throw new RuntimeException(x);
////        }
////    }    
// 
//    /** Creates a DOM from a string representation of an xml record
//     * @param doc the xml string
//     * @return the DOM document
//     * @deprecated use same method in pt.utl.ist.util.DomUtil
//     */    
//    public static Document parseDomFromString(String doc){        
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();        
//            return builder.parse(new org.xml.sax.InputSource(new StringReader(doc)));  
//        }catch(javax.xml.parsers.ParserConfigurationException e){
//            throw new RuntimeException(e);
//        }catch(org.xml.sax.SAXException e){
//            throw new RuntimeException(e);
//        }catch(java.io.IOException e){
//            throw new RuntimeException(e);
//		}
//    }
/*    
    
    public static String DomToString(Document doc){        
        try {            
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource source = new DomSource(doc);
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            DOMResult result = new DOMResult();
            transformer.transform(source, result);
            return (Document) result.getNode();
        } catch (TransformerConfigurationException tce) {
            Throwable x = tce;
            if (tce.getException() != null)
                x = tce.getException();
            x.printStackTrace();
        } catch (TransformerException te) {
            Throwable x = te;
            if (te.getException() != null)
                x = te.getException();
            x.printStackTrace();
        }
        return null;
    }
    
  */  
    
    
    private static Element createRecordDom(Document document, Record rec) throws java.io.UnsupportedEncodingException{
        Element root =  document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","record");            

        List fields=rec.getFields();

        Element leadElem =  document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","leader");
        if(rec.getLeader()==null)
        	leadElem.appendChild(document.createTextNode(Record.DEFAULT_LEADER));
        else
        	leadElem.appendChild(document.createTextNode(rec.getLeader()));
        	
        root.appendChild(leadElem);

        // append control fields to directory and data
        boolean inDataFields=false;
        for (Object field : fields) {
            Field f = (Field) field;
            if (f.isControlField()) {
                if (inDataFields) {
                    log.error("Datafields and controlfields not sorted: " + rec.getNc());
                    rec.sortFields();
                    return createRecordDom(document, rec);
                }
                Element el = (Element) document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/", "controlfield");
//                el.setAttributeNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","tag",f.getTagAsString());
                el.setAttribute("tag", f.getTagAsString());
                byte[] bytes = f.getValue().getBytes();
                String data = new String(bytes, System.getProperty("file.encoding"));
                el.appendChild(document.createTextNode(data));
                root.appendChild(el);
            } else {
                inDataFields = true;
                Element el = (Element) document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/", "datafield");
//                el.setAttributeNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","tag",f.getTagAsString());
//                el.setAttributeNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","ind1",String.valueOf(f.getInd1()));
//                el.setAttributeNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","ind2",String.valueOf(f.getInd2()));            
                el.setAttribute("tag", f.getTagAsString());
                el.setAttribute("ind1", String.valueOf(f.getInd1()));
                el.setAttribute("ind2", String.valueOf(f.getInd2()));
                for (Object o : f.getSubfields()) {
                    Subfield sf = (Subfield) o;
                    Element elsf = (Element) document.createElementNS("http://www.bn.pt/standards/metadata/marcxml/1.0/", "subfield");
//                    elsf.setAttributeNS("http://www.bn.pt/standards/metadata/marcxml/1.0/","code",String.valueOf(sf.getCode()));
                    elsf.setAttribute("code", String.valueOf(sf.getCode()));
                    elsf.appendChild(document.createTextNode(String.valueOf(sf.getValue())));
                    el.appendChild(elsf);
                }
                root.appendChild(el);
            }
        }
        return root;
    }
    
    public static void main(String[] args){
        try{

            
//            Document document = new org.apache.xerces.dom.DocumentImpl();
//
//            
////            Document document = new gnu.xml.dom.DomDocument();
//            Element element=  document.createElement("test");
//            element.appendChild(document.createTextNode(new String(new byte[]{Short.decode("0xc3").byteValue(),Short.decode("0x81").byteValue()},"utf-8")+"�"));
//            document.appendChild(element);
//            System.err.println(document.getFirstChild().getFirstChild().getNodeValue());
//            
//pt.utl.ist.characters.ByteInspector.printBytes(pt.utl.ist.util.DomUtil.domToString(document,true),"ISO8859_1");


//            
//            File f=new File("c:\\teste.xml");
//            FileWriter w=new FileWriter(f);
//            w.write(pt.utl.ist.util.DomUtil.domToString(document,true));
//            w.close();
            
//            System.err.println((record2XMLString(pt.utl.ist.marc.util.MarcUtil.createTestBibliographicRecord(new pt.utl.ist.marc.impl.MarcObjectFactoryImpl()))));
            
//            System.out.println(new String(new byte[]{Short.decode("0xc3").byteValue(),Short.decode("0x81").byteValue()},"utf-8"));
//             
//            pt.utl.ist.characters.ByteInspector.printBytes(new byte[]{Short.decode("0xc3").byteValue(),Short.decode("0x81").byteValue()});
        }catch(Throwable e){
            e.printStackTrace();
        }
    }     
}
