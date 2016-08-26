package info.ernestas.eventmonitor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EventMonitorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:event-monitor";
    }

    @RequestMapping(value = "/event-monitor", method = RequestMethod.GET)
    public String getEventMonitorPage() {
        return "eventMonitor";
    }

}
