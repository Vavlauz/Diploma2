package ru.netology.domain.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.domain.data.Card;
import ru.netology.domain.data.DbUtils;
import ru.netology.domain.page.PaymentPage;
import ru.netology.domain.page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.domain.data.DataGenerator.*;


public class PaymentTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        DbUtils.clearTables();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    //HappyPath
    //passed
    @Test
    void shouldBuyInPaymentGate() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    //passed
    @Test
    void shouldBuyInPaymentGateWithNameInLatinLetters() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidNameInLatinLetters(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithDeclinedCardNumber() throws SQLException {
        Card card = new Card(getDeclinedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkDeclineNotification();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }


    //CardNumberField
    //failed
    @Test
    void shouldNotBuyInPaymentGateWithInvalidCardNumber() throws SQLException {
        Card card = new Card(getInvalidCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkDeclineNotification();

    }

    //passed
    @Test
    void shouldNotBuyInPaymentGateWithShortCardNumber() {
        Card card = new Card(getShortCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidFormat();
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithEmptyCardNumber() {
        Card card = new Card(null, getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем Номер карты на "Поле обязательно для заполнения"
    }


    //MonthField
    //failed
    @Test
    void shouldNotBuyInPaymentGateWithInvalidMonth() {
        Card card = new Card(getApprovedNumber(), "00", getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDate(); //TODO Изменить надпись под полем Месяц на "Неверно указан срок действия карты"
    }

    //passed
    @Test
    void shouldNotBuyInPaymentGateWithNonExistingMonth() {
        Card card = new Card(getApprovedNumber(), "13", getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDate();

    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithExpiredMonth() {
        Card card = new Card(getApprovedNumber(), getLastMonth(), getCurrentYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkExpiredDate(); //TODO Изменить надпись под полем Месяц на "Истёк срок действия карты"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithEmptyMonth() {
        Card card = new Card(getApprovedNumber(), null, getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем Месяц на "Поле обязательно для заполнения"
    }


    //YearField
    //passed
    @Test
    void shouldNotBuyInPaymentGateWithExpiredYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getLastYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkExpiredDate();
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithEmptyYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), null, getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем Год на "Поле обязательно для заполнения"
    }


    //NameField
    //failed
    @Test
    void shouldNotBuyInPaymentGateWithOnlyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithOnlyNameInLatinLetters() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyNameInLatin(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithOnlySurname() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyLastname(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithOnlySurnameInLatinLetters() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyLastnameInLatin(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithNameAndSurnameWithDash() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), "Иван-Иванов", getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidFormat();
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithTooLongName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getTooLongName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkLongName(); //TODO Изменить надпись под полем Владелец "Значение поля не может содержать более 100 символов"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithDigitsInName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithNumbers(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDataName(); //TODO Изменить надпись под полем Владелец "Значение поля может содержать только буквы и дефис"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithTooShortName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithOneLetter(), getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkShortName(); //TODO Изменить надпись под полем Владелец "Значение поля должно содержать больше одной буквы"
    }

    //passed
    @Test
    void shouldNotBuyInPaymentGateWithEmptyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), null, getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField();
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithSpaceInsteadOfName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), " ", getValidCvc());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidDataName(); //TODO Изменить надпись под полем Владелец "Значение поля может содержать только буквы и дефис"
    }


    //CVC/CVVField
    //failed
    @Test
    void shouldNotBuyInPaymentGateWithOneDigitInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithOneDigit());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidCvc(); //TODO Изменить надпись под полем CVC "Значение поля должно содержать 3 цифры"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithTwoDigitsInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithTwoDigits());
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkInvalidCvc(); //TODO Изменить надпись под полем CVC "Значение поля должно содержать 3 цифры"
    }

    //failed
    @Test
    void shouldNotBuyInPaymentGateWithEmptyCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), null);
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkRequiredField(); //TODO Изменить надпись под полем CVC на "Поле обязательно для заполнения"
    }


    //AllEmptyFields
    //failed
    @Test
    void shouldNotBuyInPaymentGateWithAllEmptyFields() {
        Card card = new Card(null, null, null, null, null);
        var startPage = new StartPage();
        startPage.buy();
        var paymentPage = new PaymentPage();
        paymentPage.fulfillData(card);
        paymentPage.checkAllFieldsAreRequired(); //TODO Изменить надписи под полями на "Поле обязательно для заполнения"
    }
}
