package com.recommendations.config;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.microsoft.applicationinsights.extensibility.TelemetryProcessor;
import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import com.microsoft.applicationinsights.telemetry.Telemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFilter implements TelemetryProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);

    private List<String> exempturls = new ArrayList<>();

    // private String exempturls;
    // public String getUrls() { return this.exempturls; }

    public RequestFilter() {
        this.exempturls.add("probe");
        LOGGER.info("Constructor: Added excluded URL: 'probe' ");
    }

    /*
     * This method is called for each item of telemetry to be sent. Return false to
     * discard it. Return true to allow other processors to inspect it.
     */
    @Override
    public boolean process(Telemetry telemetry) {

        if (telemetry == null) {
            return true;
        }
        if (!(telemetry instanceof RequestTelemetry)) {
            return true;
        }

        RequestTelemetry requestTelemetry = (RequestTelemetry) telemetry;
        URI uri = null;

        try {
            uri = requestTelemetry.getUrl().toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (uri == null) { return true; }
        else {
            String uriPath = uri.toString();
            for (String notNeededUri : exempturls) {
                if (uriPath.contains(notNeededUri)) {
                    // LOGGER.trace("Exempting URI from telemetry: " + notNeededUri);
                    return false;
                }
            }
        }
        return true;
    }
}