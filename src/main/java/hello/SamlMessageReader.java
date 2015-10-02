package hello;

import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.core.Response;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Created by Hans on 1-10-2015.
 */
public class SamlMessageReader extends SamlMessage {
    public SamlMessageReader(String encoded) {
        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        BasicParserPool ppMgr = new BasicParserPool();
        ppMgr.setNamespaceAware(true);

        try {
            Document doc = ppMgr.parse(new ByteArrayInputStream(Base64.getDecoder().decode(encoded)));
            Element rootElement = doc.getDocumentElement();

            UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
            Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(rootElement);
            response = (Response) unmarshaller.unmarshall(rootElement);
        } catch (XMLParserException | UnmarshallingException e) {
            e.printStackTrace();
        }
    }

    public String getNameID() {
        if(!response.getAssertions().isEmpty()) {
            return response.getAssertions().get(0).getSubject().getNameID().getValue();
        }
        return null;
    }

}
