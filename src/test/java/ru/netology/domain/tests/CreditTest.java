package ru.netology.domain.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.domain.data.Card;
import ru.netology.domain.data.DbUtils;
import ru.netology.domain.page.CreditPage;
import ru.netology.domain.page.StartPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.domain.data.DataGenerator.*;

public class CreditTest {
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
    void shouldBuyInCreditGate() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getCreditStatus());
    }

    //passed
    @Test
    void shouldBuyInCreditGateWithNameInLatinLetters() throws SQLException {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidNameInLatinLetters(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkSuccessNotification();
        assertEquals("APPROVED", DbUtils.getCreditStatus());
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithDeclinedCardNumber() throws SQLException {
        Card card = new Card(getDeclinedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkDeclineNotification();

    }

    //CardNumberField
    //failed
    @Test
    void shouldNotBuyInCreditGateWithInvalidCardNumber() throws SQLException {
        Card card = new Card(getInvalidCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkDeclineNotification();

    }

    //passed
    @Test
    void shouldNotBuyInCreditGateWithShortCardNumber() {
        Card card = new Card(getShortCardNumber(), getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidFormat();
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithEmptyCardNumber() {
        Card card = new Card(null, getCurrentMonth(), getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkRequiredField(); //TODO Изменить надпись под полем Номер карты на "Поле обязательно для заполнения"
    }

    //MonthField
    //failed
    @Test
    void shouldNotBuyInCreditGateWithInvalidMonth() {
        Card card = new Card(getApprovedNumber(), "00", getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidDate(); //TODO Изменить надпись под полем Месяц на "Неверно указан срок действия карты"
    }

    //passed
    @Test
    void shouldNotBuyInCreditGateWithNonExistingMonth() {
        Card card = new Card(getApprovedNumber(), "13", getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidDate();

    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithExpiredMonth() {
        Card card = new Card(getApprovedNumber(), getLastMonth(), getCurrentYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkExpiredDate(); //TODO Изменить надпись под полем Месяц на "Истёк срок действия карты"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithEmptyMonth() {
        Card card = new Card(getApprovedNumber(), null, getNextYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkRequiredField(); //TODO Изменить надпись под полем Месяц на "Поле обязательно для заполнения"
    }

    //YearField
    //passed
    @Test
    void shouldNotBuyInCreditGateWithExpiredYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getLastYear(), getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkExpiredDate();
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithEmptyYear() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), null, getValidName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkRequiredField(); //TODO Изменить надпись под полем Год на "Поле обязательно для заполнения"
    }

    //NameField
    //failed
    @Test
    void shouldNotBuyInCreditGateWithOnlyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithOnlyNameInLatinLetters() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyNameInLatin(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithOnlySurname() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyLastname(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithOnlySurnameInLatinLetters() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getOnlyLastnameInLatin(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidName(); //TODO Изменить надпись под полем Владелец "Введите полное имя и фамилию"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithNameAndSurnameWithDash() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), "Иван-Иванов", getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidFormat();
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithTooLongName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getTooLongName(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkLongName(); //TODO Изменить надпись под полем Владелец "Значение поля не может содержать более 100 символов"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithDigitsInName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithNumbers(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidDataName(); //TODO Изменить надпись под полем Владелец "Значение поля может содержать только буквы и дефис"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithTooShortName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getNameWithOneLetter(), getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkShortName(); //TODO Изменить надпись под полем Владелец "Значение поля должно содержать больше одной буквы"
    }

    //passed
    @Test
    void shouldNotBuyInCreditGateWithEmptyName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), null, getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkRequiredField();
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithSpaceInsteadOfName() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), " ", getValidCvc());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidDataName(); //TODO Изменить надпись под полем Владелец "Значение поля может содержать только буквы и дефис"
    }

    //CVC/CVVField
    //failed
    @Test
    void shouldNotBuyInCreditGateWithOneDigitInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithOneDigit());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidCvc(); //TODO Изменить надпись под полем CVC "Значение поля должно содержать 3 цифры"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithTwoDigitsInCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), getCvcWithTwoDigits());
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkInvalidCvc(); //TODO Изменить надпись под полем CVC "Значение поля должно содержать 3 цифры"
    }

    //failed
    @Test
    void shouldNotBuyInCreditGateWithEmptyCvc() {
        Card card = new Card(getApprovedNumber(), getCurrentMonth(), getNextYear(), getValidName(), null);
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkRequiredField(); //TODO Изменить надпись под полем CVC на "Поле обязательно для заполнения"
    }

    //AllEmptyFields
    //failed
    @Test
    void shouldNotBuyInCreditGateWithAllEmptyFields() {
        Card card = new Card(null, null, null, null, null);
        var startPage = new StartPage();
        startPage.buyInCredit();
        var creditPage = new CreditPage();
        creditPage.fulfillData(card);
        creditPage.checkAllFieldsAreRequired(); //TODO Изменить надписи под полями на "Поле обязательно для заполнения"

    }
}
