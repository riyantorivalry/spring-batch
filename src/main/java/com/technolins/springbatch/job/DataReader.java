package com.technolins.springbatch.job;

import com.technolins.springbatch.entity.Employee;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class DataReader implements ItemReader<Employee> {
    private List<Employee> employees;
    private int index=0;

    public DataReader(){
        initialize();
    }

    private void initialize(){
        employees = new ArrayList<>();
        Employee dummy = new Employee("Riyanto","Developer and Application Manager","riyanto@technolins.id",20000000L);
        employees.add(dummy);
    }

    @Override
    public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Employee dummy = null;
        if(index < employees.size()){
            dummy = employees.get(index);
            index++;
        }
        System.out.println("Read data: ");
        for(Employee item: employees){
            System.out.println(item.toString());
        }
        return dummy;
    }
}
