package com.technolins.springbatch.job;

import com.technolins.springbatch.entity.Employee;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DataWriter implements ItemWriter<Employee> {
    @Override
    public void write(List<? extends Employee> items) throws Exception {
        for(Employee dummy: items){
            System.out.println("Writing data: " + dummy.toString());
        }
    }
}
