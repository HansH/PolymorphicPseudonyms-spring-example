package hello;

import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml1.core.NameIdentifier;
import org.opensaml.saml2.core.*;
import org.opensaml.xml.ConfigurationException;

import javax.xml.namespace.QName;

/**
 * Created by Hans on 29-9-2015.
 */
public class SamlMessageBuilder extends SamlMessage {

    public SamlMessageBuilder(String issuerId, String destinationUrl, String ep) {
        createSaml(issuerId, destinationUrl, ep);
    }

    private void createSaml(String issuerId, String destinationUrl, String ep) {
        try {
            DefaultBootstrap.bootstrap();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        response = createObject(Response.DEFAULT_ELEMENT_NAME);

        Issuer issuer = createObject(Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(issuerId);
        response.setIssuer(issuer);

        response.setDestination(destinationUrl);

        Status status = createObject(Status.DEFAULT_ELEMENT_NAME);
        StatusCode statusCode = createObject(StatusCode.DEFAULT_ELEMENT_NAME);
        statusCode.setValue(StatusCode.SUCCESS_URI);
        status.setStatusCode(statusCode);

        response.setStatus(status);

        Assertion assertion = createObject(Assertion.DEFAULT_ELEMENT_NAME);

        Issuer assertionIssuer = createObject(Issuer.DEFAULT_ELEMENT_NAME);
        assertionIssuer.setValue(issuerId);
        assertion.setIssuer(assertionIssuer);

        Subject subject = createObject(Subject.DEFAULT_ELEMENT_NAME);

        NameID nameID = createObject(NameID.DEFAULT_ELEMENT_NAME);
        nameID.setFormat(NameIdentifier.UNSPECIFIED);
        nameID.setValue(ep);
        subject.setNameID(nameID);

        SubjectConfirmation subjectConfirmation = createObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
        SubjectConfirmationData subjectConfirmationData = createObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
        subjectConfirmationData.setRecipient(destinationUrl);
        subjectConfirmation.setSubjectConfirmationData(subjectConfirmationData);
        subject.getSubjectConfirmations().add(subjectConfirmation);

        assertion.setSubject(subject);

        AuthnStatement authnStatement = createObject(AuthnStatement.DEFAULT_ELEMENT_NAME);
        AuthnContext authnContext = createObject(AuthnContext.DEFAULT_ELEMENT_NAME);
        AuthnContextClassRef authnContextClassRef = createObject(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
        authnContextClassRef.setAuthnContextClassRef(AuthnContext.PASSWORD_AUTHN_CTX);
        authnContext.setAuthnContextClassRef(authnContextClassRef);
        authnStatement.setAuthnContext(authnContext);
        assertion.getAuthnStatements().add(authnStatement);

        response.getAssertions().add(assertion);
    }

    @SuppressWarnings ("unchecked")
    private <T> T createObject(QName qname)
    {
        return (T) Configuration.getBuilderFactory().getBuilder(qname).buildObject(qname);
    }

//    public static void main(String[] args){
//        SamlMessageBuilder samlMessage = new SamlMessageBuilder("myidp", "mysp");
//        System.out.println(samlMessage.toXmlString());
//        System.out.println(samlMessage.toEncodedString());
//    }
}
