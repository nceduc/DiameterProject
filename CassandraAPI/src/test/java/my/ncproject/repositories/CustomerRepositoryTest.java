package my.ncproject.repositories;

import my.ncproject.domain.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {

    private static final BigDecimal DOUBLE_100 = BigDecimal.valueOf(100.00);
    private static final String CUSTOMER_NUMBER = "a cool customers";

    @Autowired
    private CustomerRepository customerRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testPersistence() {
        //given
        Customer customer = new Customer();
        customer.setNumber(CUSTOMER_NUMBER);
        customer.setBalance(DOUBLE_100);

        //when
        customerRepository.save(customer);

        //then
        Assert.assertNotNull(customer.getNumber());
        Customer newCustomer = customerRepository.findById(customer.getNumber()).orElse(null);
        Assert.assertEquals(CUSTOMER_NUMBER, newCustomer.getNumber());
        Assert.assertEquals(DOUBLE_100.compareTo(newCustomer.getBalance()), 0);
    }
}