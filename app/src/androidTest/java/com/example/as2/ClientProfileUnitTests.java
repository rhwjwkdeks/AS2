package com.example.as2;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ClientProfileUnitTests {

    public static final String bad_state = "Invalid State";
    public static final String bad_name = "Full name length must be same or less than 50 characters";
    public static final String bad_ad1 = "Address1 length must be same or less than 100 characters";
    public static final String bad_ad2 = "Address2 length must be same or less than 100 characters";
    public static final String bad_city = "City length must be same or less than 100 characters";
    public static final String bad_zip = "Zip code must be five or nine length";
    public static final String good_profile = "Profile Completed!";
    public ActivityTestRule<Clientprofile> activityRule = new ActivityTestRule<>(Clientprofile.class);
    Clientprofile c;

    @Before
    public void setUp() throws Exception {
        activityRule.launchActivity(null);
        c = activityRule.getActivity();
    }

    @Test
    public void testBadState() {
        String state = "";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_state, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadState2() {
        String state = "Select your state";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_state, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadName() {
        String state = "TX";
        String name = "";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_name, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadName2() {
        String state = "TX";
        String name = "sdahfjhasifuhuewhfcnjdslafkhsakjlfhjkheuifbcsdlkafhjlsakfhuewqohfrdjslhjdbcskahfjsalhfuieoqyrheufhlakbfcdklhfjdsahlfkayheaifuewhflasdbncjksdalhfjahslfjh";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_name, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadName3() {
        String state = "TX";
        String name = "    @  2 $  44432444!!!  09";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_name, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadName4() {
        String state = "TX";
        String name = "John";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_name, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadAd1() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_ad1, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadAd12() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "safhslafhjksdafhcuohiewuqobclaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaackajsdhfuiewqorhfdjalkcnklasdjfhieowqhfuesldkfhdksjcbnikuashyuoiewqhfudhajcnlksajhfuwqsfdasdfdsaf";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_ad1, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadAd13() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "I love Applebees!!! dsfa gpiis 802";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_ad1, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadAd21() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_ad2, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadAd22() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_ad2, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadAd23() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "I love Applebees!!! dsfa gpiis 802";
        String city = "Houston";
        String zip = "77004";
        assertEquals(bad_ad2, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadCity() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "";
        String zip = "77004";
        assertEquals(bad_city, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadCity2() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "aufioewqihcfsjanslksahfueiwqoefbckasjfhueiowaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaqgfbhdskacbhaskufeoweyqoueryoorfhdsklabchk";
        String zip = "77004";
        assertEquals(bad_city, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadZip() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "";
        assertEquals(bad_zip, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadZip2() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "2";
        assertEquals(bad_zip, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testBadZip3() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "5734890275432";
        assertEquals(bad_zip, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testCorrectInfo() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "77004";
        assertEquals(good_profile, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testCorrectInfo2() {
        String state = "TX";
        String name = "John Smith";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "770044321";
        assertEquals(good_profile, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testCorrectInfo3() {
        String state = "TX";
        String name = "John Smith III";
        String ad1 = "1111 Apple Avenue";
        String ad2 = "222 Smith Street";
        String city = "Houston";
        String zip = "532952133";
        assertEquals(good_profile, c.validateProfile(state, name, ad1, ad2, city, zip));
    }
    @Test
    public void testCorrectInfo4() {
        String state = "TX";
        String name = "Katy Perry";
        String ad1 = "1111 Apple Dr.";
        String ad2 = "222 Smith St.";
        String city = "Houston";
        String zip = "532952133";
        assertEquals(good_profile, c.validateProfile(state, name, ad1, ad2, city, zip));
    }







}