package gn.springDemo.dao;

import gn.springDemo.entity.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

    // need to inject the session factory
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Customer> getCustomers() {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // create a query ... sort by last name
        Query<Customer> query = session.createQuery("from Customer order by lastName", Customer.class);

        // execute query a nd get result list
        List<Customer> customers = query.getResultList();

        // return the results
        return customers;
    }

    @Override
    public void saveCustomer(Customer customer) {
        // get current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // save/update the customer ... finally LOL
        session.saveOrUpdate(customer);
    }

    @Override
    public Customer getCustomer(int id) {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // retrieve/read from db using the primary key
        Customer customer = session.get(Customer.class, id);

        return customer;
    }

    @Override
    public void deleteCustomer(int id) {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        // retrieve/read from db using the primary key
        Customer customer = session.get(Customer.class, id);

        // delete customer from db
        session.delete(customer);

//        // delete object with primary key
//        Query query = session.createQuery("delete from Customer where id=:customerId");
//        query.setParameter("customerId", id);
//
//        query.executeUpdate();
    }

    @Override
    public List<Customer> searchCustomers(String theSearchName) {

        // get the current hibernate session
        Session session = sessionFactory.getCurrentSession();

        Query theQuery = null;

        //
        // only search by name if theSearchName is not empty
        //
        if (theSearchName != null && theSearchName.trim().length() > 0) {

            // search for firstName or lastName ... case insensitive
            theQuery = session.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
            theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");

        }
        else {
            // theSearchName is empty ... so just get all customers
            theQuery = session.createQuery("from Customer", Customer.class);
        }

        // execute query and get result list
        List<Customer> customers = theQuery.getResultList();

        // return the results
        return customers;
    }
}
