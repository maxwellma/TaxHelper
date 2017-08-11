package com.maxwell.taxhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maxwellma on 18/06/2017.
 */

public class Test {

    public class Fruit{
        public void hi(){};
    }
    public class Apple extends Fruit {}
    public class Orange extends Fruit{}

    static double sum(List<? extends Number> list) {
        double sum = 0;
        for(Number number : list) {
            sum += number.doubleValue();
        }
        return sum;
    }

    public void Main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        sum(integers);
    }

}
