package com.company.CassandraAPI.controllers;

import com.company.CassandraAPI.domain.Customer;
import com.company.CassandraAPI.domain.DeltaBalance;
import com.company.CassandraAPI.exceptionhandler.exceptions.*;
import com.company.CassandraAPI.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {
    private CustomerService customerService;


    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("/")
    public String redirToList() {
        return "redirect:/customers/list";
    }

    @RequestMapping(value = {"/customers/list", "/customers"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Customer> listCustomers() {
        return customerService.listAll();
    }

    @RequestMapping(value = "/customers/show/{number}", method = RequestMethod.GET)
    @ResponseBody
    public BigDecimal getBalance(@PathVariable String number) {
        Optional<Customer> answ = customerService.getByNumber(number);
        if (!answ.isPresent()) {
            throw new CustomerNotExistException();
        }
            return answ.get().getBalance();
    }



    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Customer createCustomer(@RequestBody Customer customer) {
        if (customerService.getByNumber(customer.getNumber()).isPresent()) {
            throw new CustomerAlreadyExistException();
        }
            customerService.create(customer);
            return customer;
    }

    @RequestMapping(value = "/customers/update/{number}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String updateBalance(@PathVariable String number, @RequestBody DeltaBalance updateBalance) {
        Optional<Customer> checkCustomer = customerService.getByNumber(number);
        if (!checkCustomer.isPresent()) {
            throw new CustomerNotExistException();
        }
        Customer customer = checkCustomer.get();
        BigDecimal oldBalance = customer.getBalance();
        BigDecimal newBalance = oldBalance.add(updateBalance.getBalance());
        customer.setBalance(newBalance);
        customerService.create(customer);
        return "Customer: " + number + "\n" + "UpdateBalance before: " + oldBalance + "\n" + "UpdateBalance after: " + customer.getBalance();
    }




    @RequestMapping(value = "/customers/delete/{number}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String number){
        Optional<Customer> answ = customerService.getByNumber(number);
        if (!answ.isPresent()) {
            throw new CustomerNotExistException();
        }
        customerService.delete(number);
    }
}
