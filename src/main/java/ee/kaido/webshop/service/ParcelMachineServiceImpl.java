package ee.kaido.webshop.service;

import ee.kaido.webshop.model.request.input.OmnivaParcelMachine;
import ee.kaido.webshop.model.request.input.SmartpostParcelMachine;
import ee.kaido.webshop.model.request.output.ParcelMachines;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ParcelMachineServiceImpl implements ParcelMachineService {
    String omnivaUrl = "https://www.omniva.ee/locations.json";
    String smartpostUrl = "https://www.smartpost.ee/places.json";

    @Autowired
    RestTemplate restTemplate;

    public ParcelMachines getParcelMachines(String country) {
        ParcelMachines parcelMachines = new ParcelMachines();
        parcelMachines.setOmnivaParcelMachines(fetchOmnivaParcelMachines(country));
        if (country.equals("EE")) {
            parcelMachines.setSmartpostParcelMachines(fetchSmartpostParcelMachines());
        } else {
            parcelMachines.setSmartpostParcelMachines(new ArrayList<>());
        }
        return parcelMachines;
    }

    private List<SmartpostParcelMachine> fetchSmartpostParcelMachines() {
        log.info("Taking Smartpost parcel machines");
        ResponseEntity<SmartpostParcelMachine[]> response;
        List<SmartpostParcelMachine> smartpostParcelMachines = new ArrayList<>();
        try {
            response = restTemplate
                    .exchange(smartpostUrl, HttpMethod.GET, null, SmartpostParcelMachine[].class);
            if (response.getBody() != null) {
                smartpostParcelMachines = Arrays.asList(response.getBody());
            }
        } catch (RestClientException e) {
            log.error("SmartPost API endpointiga ei saanud ühendust");
        }
        return smartpostParcelMachines;
    }

    private List<OmnivaParcelMachine> fetchOmnivaParcelMachines(String country) {
        log.info("Taking Omniva parcel machines");
        ResponseEntity<OmnivaParcelMachine[]> response = restTemplate
                .exchange(omnivaUrl, HttpMethod.GET, null, OmnivaParcelMachine[].class);

        List<OmnivaParcelMachine> omnivaParcelMachine = new ArrayList<>();
        if (response.getBody() != null) {

            omnivaParcelMachine = Arrays.asList(response.getBody());
            omnivaParcelMachine = omnivaParcelMachine.stream()
                    .filter(p -> {
                        return p.getA0_NAME().equals(country);
                    })
                    .collect(Collectors.toList());
        }
        return omnivaParcelMachine;
    }
}
