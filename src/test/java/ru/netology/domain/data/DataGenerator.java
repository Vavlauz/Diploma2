package ru.netology.domain.data;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    public static Faker faker = new Faker(new Locale("en"));

    public static String getApprovedNumber() {
        return "1111 2222 3333 4444";
    }

    public static String getDeclinedNumber() {
        return "5555 6666 7777 8888";
    }

    public static String getInvalidCardNumber() {
        return "1111 2222 3333 4445";
    }

    public static String getShortCardNumber() {
        return "4444 4444 4444 444";
    }

    public static String getCurrentMonth() {
        LocalDate localDate = LocalDate.now();
        return String.format("%02d", localDate.getMonthValue());
    }

    public static String getLastMonth() {
        LocalDate localDate = LocalDate.now();
        LocalDate lastMonth = localDate.minusMonths(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM");
        String monthValue = lastMonth.format(dateTimeFormatter);
        return monthValue;
    }

    public static String getCurrentYear() {
        return String.format("%ty", Year.now());
    }

    public static String getLastYear() {
        LocalDate localDate = LocalDate.now();
        return String.format("%ty", localDate.minusYears(1));
    }

    public static String getNextYear() {
        LocalDate localDate = LocalDate.now();
        return String.format("%ty", localDate.plusYears(1));
    }

    public static String getValidName() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getValidNameInLatinLetters() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getOnlyName() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().firstName();
    }

    public static String getOnlyNameInLatin() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().firstName();
    }

    public static String getOnlyLastname() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().lastName();
    }

    public static String getOnlyLastnameInLatin() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().lastName();
    }

    public static String getTooLongName() {
        return faker.lorem().fixedString(250);
    }

    public static String getNameWithNumbers() {
        return faker.number().digits(9);
    }

    public static String getNameWithOneLetter() {
        return faker.lorem().characters(1);
    }

    public static String getValidCvc() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en"), new RandomService());
        return fakeValuesService.numerify("###");
    }

    public static String getCvcWithOneDigit() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en"), new RandomService());
        return fakeValuesService.numerify("#");
    }

    public static String getCvcWithTwoDigits() {
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en"), new RandomService());
        return fakeValuesService.numerify("##");
    }

}
