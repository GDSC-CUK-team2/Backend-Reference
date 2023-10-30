//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gdsc.team2.matna.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"api/restaurants"})
public class RestaurantController {
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    public RestaurantController() {
    }

    @RequestMapping(
            value = {""},
            method = {RequestMethod.GET}
    )
    public String restaurant(Model model) {
        return "hello test";
    }
}
