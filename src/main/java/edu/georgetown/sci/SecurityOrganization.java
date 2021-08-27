/**
 * Author: "Jianan Su"
 * Copyright: 
 * Credits: ["Jianan Su"]
 * Email: "js4488@georgetown.edu"
 * Status: "Prototype"
 */

/***
 * import edu.georgetown.sci.contracts.generated.SIManagment4_1, generated source by Maven plugin
 * Robert Song
 *
 */

 package edu.georgetown.sci;

import edu.georgetown.sci.contracts.generated.SIManagement;

import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import java.math.BigInteger;
import org.web3j.tuples.generated.Tuple4;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;


public class SecurityOrganization extends BusinessEntity{
	protected SIManagement contract;
	
	public SecurityOrganization(){}
	
	protected void connectToRemoteNode(String httpService){
		System.out.println("Security Orgsnization...");
		super.connectToRemoteNode(httpService);
	}
	
	protected void loadCredential(String password, String pathToWalletFile){
		super.loadCredential(password, pathToWalletFile);
	}
	
	protected void loadContract(String contractAddress){
		ContractGasProvider contractGasProvider = new DefaultGasProvider();
        this.contract = SIManagement.load(contractAddress, super.web3j, super.credential, contractGasProvider); 
        System.out.println("Contract address: " + contract.getContractAddress());
	}
	
	protected void storeSecurityInforToBlockchain(String cveID, String vendorName, String productName, String versionAffected, String impactScore, String timeStamp){
		byte[] byteValue = cveID.getBytes();
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        try {
			this.contract.creatProduct(byteValueLen32, vendorName, productName, versionAffected).send();
			System.out.println("Value stored in remote smart contract.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Robert Song
	 * get the CVE by given CVE ID
	 */
	 protected List<String> getCVE(String cveID){
		 byte[] byteValue = cveID.getBytes();
		 byte[] byteValueLen32 = new byte[32];
		 System.arraycopy(byteValue,0,byteValueLen32,0,byteValue.length);

		 List<String> result = new ArrayList<String>();
		 JsonArray json_array = new JsonArray();
		 try {
		 	 Tuple4<byte[], String, String, String> results = this.contract.getProduct(byteValueLen32,BigInteger.valueOf(0)).send();
			 result.add(results.getValue2());
			 result.add(results.getValue3());
			 result.add(results.getValue4());
			 json_array.add(results.getValue2());
			 json_array.add(results.getValue3());
			 json_array.add(results.getValue4());
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 System.out.println("=================== "+result);
		 System.out.println("json array =    " + json_array);
		 return result;
	 }
	
	protected void setup(){
		//this.connectToRemoteNode("http://172.16.145.164:8543");
		this.connectToRemoteNode("http://localhost:8545");
		this.loadCredential("1234", "/Users/admin/demo/node1/keystore/UTC--2021-07-27T19-45-33.832755000Z--475d0e60e7dc24d2892815662506cbad40a5f4cb");
		this.loadContract("0xd826fc37a0badAC53B790f81FA16d6E2a3eD3dE3");
	}
	
	
	public static void main(String[] args){
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
		
	}
}
