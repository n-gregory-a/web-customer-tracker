package gn.springDemo.controller;

import gn.springDemo.dao.CustomerDAO;
import gn.springDemo.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    // need to inject the customer dao
    @Autowired
    private CustomerDAO customerDAO;

    @GetMapping("/list")
    public String ListCustomers(Model model) {

        // get customers from dao
        List<Customer> customers = customerDAO.getCustomers();

        // add the customers to the model
        model.addAttribute("customers", customers);

        return "list-customers";
    }
}