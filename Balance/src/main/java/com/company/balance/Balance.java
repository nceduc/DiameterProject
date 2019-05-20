package com.company.balance;

import com.company.cassandraAPI.domain.Customer;
import com.company.cassandraAPI.repositories.CustomerRepository;
import com.company.cassandraAPI.services.CustomerService;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class Balance{

    private static Logger logger = LogManager.getLogger(Balance.class);
    private CustomerService customerService;
    public static Session connection = CassandraConnector.getInstance().connect(); //connection with Cassandra

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public String getBalance(String clientID){
        String balance = null;
        try {
            balance = getBalanceDB(clientID).toString(); //олучаем баланс
        }catch (NullPointerException ex){
            logger.error("There isn't balance for the clientID");
        }
        return balance;
    }


    private String selectDB(String clientID){
        String answer = null;
        BigDecimal balance = null;
        final String query = "SELECT * from company.balance WHERE number = ?";
        ResultSet resultSet = null;
        Row row = null;
        try {
            if(connection == null){
                connection = CassandraConnector.getInstance().connect();
            }
            resultSet = connection.execute(query, clientID);
            row = resultSet.one();
            balance = row.getDecimal("balance");
        }
        catch (Exception e){
            logger.error("Error select balance from Cassandra" + e.getMessage());
        }

        if(balance != null){
            answer = String.valueOf(balance);
        }
        return answer;
    }


    public static boolean isCassandraRunning(){
        boolean result = false;
        final String query = "SELECT now() FROM system.local"; //test query

        try {
            if(connection != null){
                connection.execute(query);
                result = true;
            }
        }
        catch (NoHostAvailableException ex){
            logger.error("Cassandra failed " + ex.getMessage());
        }
        return result;
    }


    private BigDecimal getBalanceDB(String number) {
        BigDecimal balance = null;
        Optional<Customer> data = customerService.getByNumber(number);
        if (data.isPresent()) {
            balance = data.get().getBalance();
        }
        return balance;
    }


}





