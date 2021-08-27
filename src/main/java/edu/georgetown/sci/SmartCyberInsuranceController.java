package edu.georgetown.sci;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import com.google.gson.JsonArray;



@RestController
public class SmartCyberInsuranceController {

	@GetMapping("/")
	public String index() {

				//BasicConfigurator.configure();
		SecurityOrganization NIST = new SecurityOrganization();
		NIST.setup();
		NIST.storeSecurityInforToBlockchain("CVE-2019-1", "debian", "debian_linux", "8.0", "5.9", "08/01/2019");
		// NIST.storeSecurityInforToBlockchain("CVE-2019-2", "libexpat", "expat", "1.95.1", "3.6", "08/02/2019");
		// NIST.storeSecurityInforToBlockchain("CVE-2019-2", "canonical", "ubuntu_linux", "12.04", "3.6", "08/02/2019");
		// NIST.storeSecurityInforToBlockchain("CVE-2019-2", "google", "android", "4.4.4", "3.6", "08/02/2019");
		// NIST.storeSecurityInforToBlockchain("CVE-2019-2", "google", "android", "5.0.2", "3.6", "08/02/2019");
		// NIST.storeSecurityInforToBlockchain("CVE-2019-2", "google", "android", "6.0.1", "3.6", "08/02/2019");
		NIST.shutdown();

		return "Greetings from Spring Boot!";
	}

	@CrossOrigin
	@GetMapping("/getCVE")
	@ResponseBody
	public ResponseEntity<List> retrieveCVE(@RequestParam String id){

				SecurityOrganization NIST = new SecurityOrganization();
		NIST.setup();
	
		List<String> results = NIST.getCVE(id);
		System.out.println("Here is results: " + results);

		NIST.shutdown();

		System.out.println("json array:   " + results);
		return new ResponseEntity<>(results, HttpStatus.OK);
	}


	@CrossOrigin
	@RequestMapping(value = "/persistCVE", method = RequestMethod.POST)
    public ResponseEntity < String > persistPerson(@RequestBody CVEItem item) {
		System.out.println("Request body: " + item.cveID +" "+item.timeStamp);
        if (/*personService.isValid(person)*/ true) {
            // personRepository.persist(person);

			SecurityOrganization NIST = new SecurityOrganization();
			NIST.setup();
			NIST.storeSecurityInforToBlockchain(item.cveID,item.vendorName,item.productName,item.versionAffected, item.impactScore, item.timeStamp);
            NIST.shutdown();
			return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).build();
    }

}