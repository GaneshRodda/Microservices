package com.ganesh.rodda.currencyconversionmicroservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calcualteCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		HashMap<String,String> uriVariables=new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8001/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class,uriVariables);
		CurrencyConversion cc = responseEntity.getBody();
		return new CurrencyConversion(cc.getId(),from,to,quantity,cc.getConversionMultiple(),quantity.multiply(cc.getConversionMultiple()),cc.getEnvironment()+" restTemplate");
	}
	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calcualteCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
		CurrencyConversion cc = proxy.retriveExchangeValue(from, to);
		return new CurrencyConversion(cc.getId(),from,to,quantity,cc.getConversionMultiple(),quantity.multiply(cc.getConversionMultiple()),cc.getEnvironment()+" feign");
	}
}
