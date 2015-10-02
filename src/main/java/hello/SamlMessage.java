package hello;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Base64;

public class SamlMessage {
    protected Response response;

    public String toXmlString() {
        return toXmlString(true);
    }

    public String toXmlString(boolean indent) {
        MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller(response);

        try {
            Element element = marshaller.marshall(response);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            if(indent) {
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            }
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource domSource = new DOMSource(element);
            transformer.transform(domSource, result);
            return result.getWriter().toString();
        } catch (TransformerException | MarshallingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String toEncodedString() {
        return Base64.getEncoder().encodeToString(toXmlString(false).getBytes());
    }

    public Response getResponse() {
        return response;
    }
}
