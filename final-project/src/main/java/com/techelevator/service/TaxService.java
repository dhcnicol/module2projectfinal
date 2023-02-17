package com.techelevator.service;

import com.techelevator.dao.UserDao;
import com.techelevator.model.TaxRate;
import com.techelevator.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class TaxService {

    private final BigDecimal TAX_DIVISOR = BigDecimal.valueOf(100);
    public static String API_BASE_URL = "https://teapi.netlify.app/api/statetax?state=";

    private RestTemplate restTemplate = new RestTemplate();

    private UserDao userDao;

    public TaxService(UserDao userDao) {
        this.userDao = userDao;
    }

    public BigDecimal getTaxPercentage(Principal principal) {
        User user = getUser(principal);
        TaxRate taxRate = restTemplate.getForObject(API_BASE_URL + user.getStateCode(), TaxRate.class);
        return taxRate.getSalesTax().divide(TAX_DIVISOR);
    }

    private User getUser(Principal principal) {
        String username = principal.getName();
        User user = userDao.findByUsername(principal.getName());
        return user;
    }
}
