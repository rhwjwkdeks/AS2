package com.example.as2;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginUnitTest {

    public static final String user_empty = "Username is Required.";
    public static final String pass_empty = "Password is Required.";
    public static final String pass_length_fail = "Password must be more than five characters";
    public static final String user_fail = "No such username exists in the database.";
    public static final String pass_fail = "Wrong Password.";
    public static final String login_success = "Logged in Successfully.";
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);
    MainActivity m;

    @Before
    public void setUp() throws Exception {
        activityRule.launchActivity(null);
        m = activityRule.getActivity();
    }

    @Test
    public void testEmptyUsername() {
        String username = "";
        String password = "abcdeg";;
        String result = m.validateLogin(username, password);
        assertEquals(user_empty, result);
    }
    @Test
    public void testEmptyPassword() {
        String username = "abc";
        String password = "";
        String result = m.validateLogin(username, password);
        assertEquals(pass_empty, result);
    }
    @Test
    public void testPassLength() {
        String username = "abc";
        String password = "abc";
        String result = m.validateLogin(username, password);
        assertEquals(pass_length_fail, result);
    }
    @Test
    public void testUserExist() {
        String username = "abcdef";
        String password = "abcdef";
        String result = m.validateLogin(username, password);
        assertEquals(user_fail, result);
    }
    @Test
    public void testUserExist2() {
        String username = "abcdefadf";
        String password = "abcdefsfafdsa";
        String result = m.validateLogin(username, password);
        assertEquals(user_fail, result);
    }
    @Test
    public void testWrongPass() {
        String username = "abc";
        String password = "abcasfdsaf";
        String result = m.validateLogin(username, password);
        assertEquals(pass_fail, result);
    }
    @Test
    public void testLogin() {
        String username = "abc";
        String password = "abcdef";
        String result = m.validateLogin(username, password);
        assertEquals(login_success, result);
    }

}