package io.example;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.microsoft.applicationinsights.extensibility.TelemetryProcessor;
import com.microsoft.applicationinsights.internal.util.LocalStringsUtils;
import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import com.microsoft.applicationinsights.telemetry.Telemetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestFilter implements TelemetryProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    private final Set<String> notNeededUrls = new HashSet<String>();

    public void setNotNeededUrls(String notNeededUrls) {
        List<String> notNeededAsList = Arrays.asList(notNeededUrls.split(","));
        for (String notNeeded : notNeededAsList) {
            String ready = notNeeded.trim();
            if (LocalStringsUtils.isNullOrEmpty(ready)) {
                continue;
            }

            this.notNeededUrls.add(ready);
            LOGGER.info("Added excluded URL: " + ready);
        }
    }

    /*
     * This method is called for each item of telemetry to be sent. Return false to
     * discard it. Return true to allow other processors to inspect it.
     */
    @Override
    public boolean process(Telemetry telemetry) {

        LOGGER.trace("received telemetry");

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
            for (String notNeededUri : notNeededUrls) {
                if (uriPath.contains(notNeededUri)) {
                    LOGGER.info("Exempting URI from telemetry: " + notNeededUri);
                    return false;
                }
            }
        }
        return true;
    }
}