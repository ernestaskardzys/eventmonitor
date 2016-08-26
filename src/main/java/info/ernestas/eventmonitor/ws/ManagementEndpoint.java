package info.ernestas.eventmonitor.ws;

import info.ernestas.eventmonitor.model.Action;
import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.ResultStatus;
import info.ernestas.eventmonitor.model.dto.ErrorInformationDto;
import info.ernestas.eventmonitor.model.dto.EventDto;
import info.ernestas.eventmonitor.model.dto.HealthCheckInfoDto;
import info.ernestas.eventmonitor.model.dto.VersionDto;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import info.ernestas.eventmonitor.service.EventService;
import info.ernestas.eventmonitor.service.HealthCheckService;
import info.ernestas.eventmonitor.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest")
public class ManagementEndpoint {

    private static final String SUCCESS_FLAG = "OK";

    private EventService eventService;

    private HealthCheckService healthCheckService;

    private SimpMessagingTemplate simpMessagingTemplate;

    private VersionService versionService;

    @Autowired
    public ManagementEndpoint(EventService eventService, HealthCheckService healthCheckService, SimpMessagingTemplate simpMessagingTemplate, VersionService versionService) {
        this.eventService = eventService;
        this.healthCheckService = healthCheckService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.versionService = versionService;
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonResult<String> upload(@RequestBody EventDto eventDto) {
        eventService.saveIncomingEvent(eventDto);
        return new JsonResult<>(SUCCESS_FLAG);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonResult<VersionDto> info() {
        return new JsonResult<>(versionService.getVersionInformation());
    }

    @RequestMapping(value = "/health-check", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JsonResult<HealthCheckInfoDto> getHealthCheck() {
        return new JsonResult<>(healthCheckService.checkHealth());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonResult<ErrorInformationDto> exceptionHandler(HttpServletRequest request, Exception exception) {
        // On Error - inform users thru WebSockets and REST
        final JsonResult<WebSocketDto> jsonMessage = new JsonResult<>(new WebSocketDto(Action.ERROR.toString() + " " + exception.toString()));
        simpMessagingTemplate.convertAndSend("/server/list", jsonMessage);

        return new JsonResult<>(ResultStatus.ERROR, new ErrorInformationDto(exception.toString(), request.getRequestURI()));
    }

}
