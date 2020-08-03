package com.example.as2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FuelQuoteFormPresenterUnitTest {

    @Test
    public void testValidGallons() {
        FuelQuoteForm view = Mockito.mock(FuelQuoteForm.class);
        FuelQuoteFormPresenter p = new FuelQuoteFormPresenter(view, "H");
        assertEquals(0, p.validateGallons(""), 0);
        assertEquals(21, p.validateGallons("21"), 0);
        assertEquals(21.342, p.validateGallons("21.342"), 0);
        assertEquals(0, p.validateGallons("0"), 0);
        assertEquals(-1, p.validateGallons("."), 0);
    }

    @Test
    public void testValidDate() {
        FuelQuoteForm view = Mockito.mock(FuelQuoteForm.class);
        FuelQuoteFormPresenter p = new FuelQuoteFormPresenter(view, "h");
        assertEquals("valid date", p.validateDate(2020, 10, 30));
        assertEquals("invalid date", p.validateDate(2020, 04, 25));
        assertEquals("invalid date", p.validateDate(0, 0, 0));
    }

    @Test
    public void testSubmit() {
        FuelQuoteForm view = Mockito.mock(FuelQuoteForm.class);
        FuelQuoteFormPresenter p = new FuelQuoteFormPresenter(view, "h");
        assertEquals("success", p.onSubmit("43", "1234 Oppo Rd.", "10-10-2020", "$1.42", "$444.22"));
        assertEquals("invalid gallons", p.onSubmit("", "1234 Oppo Rd.", "10-10-2020", "$1.42", "$444.22"));
        assertEquals("invalid gallons", p.onSubmit("0", "1234 Oppo Rd.", "10-10-2020", "$1.42", "$444.22"));
        assertEquals("invalid address", p.onSubmit("43", "Select the address to deliver to...", "10-10-2020", "$1.42", "$444.22"));
        assertEquals("invalid date", p.onSubmit("43", "1234 Oppo Rd.", "", "$1.42", "$444.22"));
        assertEquals("invalid date", p.onSubmit("43", "1234 Oppo Rd.", "Select a date...", "$1.42", "$444.22"));
    }





}