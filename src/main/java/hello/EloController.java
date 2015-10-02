package hello;

import nl.surfnet.polymorphic.PPHelper;
import nl.surfnet.polymorphic.Pseudonym;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * Created by Hans on 29-9-2015.
 */
@Controller
public class EloController {

    public static final String idpUrl = "http://shibbolethidp.pt-74.utr.surfcloud.nl:8080/elo";

    @RequestMapping("/elo")
    public String hello(Model model) {
        HashMap<String, Pseudonym> users = PPHelper.loadUsers();
        if(users == null) {
            return null;
        }
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!users.containsKey(userDetails.getUsername())) {
            System.err.printf("User '%s' is not registered\n", userDetails.getUsername());
            return null;
        }
        Pseudonym pp = users.get(userDetails.getUsername());
        Pseudonym ep = PPHelper.authenticateUser(userDetails.getUsername(), Constants.spUrl);

        SamlMessageBuilder samlMessageBuilder = new SamlMessageBuilder(idpUrl,
                Constants.spUrl,
                ep.encode());
        model.addAttribute("saml", samlMessageBuilder.toXmlString());
        model.addAttribute("samlEncoded", samlMessageBuilder.toEncodedString());
        model.addAttribute("destination", Constants.spUrl);
        model.addAttribute("pp", pp.encode());
        model.addAttribute("ep", ep.encode());
        return "elo";
    }
}
