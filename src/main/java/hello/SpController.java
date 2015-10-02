package hello;

import nl.surfnet.polymorphic.PPHelper;
import nl.surfnet.polymorphic.Pseudonym;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Hans on 29-9-2015.
 */
@Controller
public class SpController {

    @RequestMapping(value = "/sp", method = RequestMethod.POST)
    public String mysp(@RequestParam(value="SAMLResponse")String samlResponse, Model model){
        SamlMessageReader samlMessageReader = new SamlMessageReader(samlResponse);
        Pseudonym ep = Pseudonym.decode(samlMessageReader.getNameID());
        byte[] pseudonym = PPHelper.authenticateSP(Constants.spUrl, ep);
        model.addAttribute("samlResponse", samlMessageReader.toXmlString());
        model.addAttribute("pseudonym", Hex.toHexString(pseudonym));
        model.addAttribute("ep", samlMessageReader.getNameID());
        return "sp";
    }
}
