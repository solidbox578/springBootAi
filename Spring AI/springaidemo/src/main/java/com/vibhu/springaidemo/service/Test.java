package com.vibhu.springaidemo.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class Test extends RecursiveTask<String> {

    @Override
    protected String compute() {
        return "";
    }

    record City(String name, int population, String category) {

            @Override
            public String toString() {
                return name + " (" + population + ")";
            }
        }

    public static void main(String[] args) {


        List<City> cities = List.of(
            new City("Smallville", 50_000, category(50_000)),
            new City("Mediapolis", 500_000, category(500_000)),
            new City("Metropolis", 5_000_000, category(5_000_000)),
            new City("Tincy", 12_000, category(12000)),
            new City("Bigburg", 1_200_000, category(1_200_000))
        );

        Map<String, List<City>> bySize = cities.stream()
            .collect(Collectors.groupingBy(City::category));

        bySize.forEach((size, list) -> {
            System.out.println(size + ":");
            list.forEach(city -> System.out.println("  - " + city));
        });

    }

    private static String category(int population) {
        if (population < 100_000) return "Small";
        if (population < 1_000_000) return "Medium";
        return "Large";
    }
}


