package com.example.as2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FuelQuoteFormDatabaseUnitTest {

    @Test
    public void testGettersAndSetters() {
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        FuelQuoteFormDatabase database = new FuelQuoteFormDatabase(mockFirestore, mockAuth);

        database.setAddress("1234 Jane St.");
        assertEquals("1234 Jane St.", database.getAddress());

        database.setDate("08-10-10");
        assertEquals("08-10-10", database.getDate());

        database.setUserID("alice");
        assertEquals("alice", database.getUserID());

        database.setGallons(34);
        assertEquals(34, database.getGallons(), 0.01);

        database.setSuggestedPrice(3421);
        assertEquals(3421, database.getSuggestedPrice(), 0.01);

        database.setTotalPrice(123);
        assertEquals(123, database.getTotalPrice(), 0.01);
    }

    @Test
    public void testAccessAddresses() {
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        FuelQuoteFormDatabase database = new FuelQuoteFormDatabase(mockFirestore, mockAuth);
        database.setUserID("AY6ZTeSk6xeAlkTufZKA9P4m9vy1");
        try {
            DocumentReference documentReference = mockFirestore.collection("users").document("AY6ZTeSk6xeAlkTufZKA9P4m9vy1");
            Map<String, Object> user = new HashMap<>();
            user.put("FullName", "Jane Dow");
            user.put("Address1", "1234 Jane St.");
            user.put("Address2", "4567 Jane Dr.");
            user.put("City", "Houston");
            user.put("Zip", "27384");
            user.put("State", "Texas");
            documentReference.set(user);
            FirestoreCallback mockCallback = new FirestoreCallback() {
                @Override
                public void onCallback(String[] addresses) {
                    assertArrayEquals(new String[]{"1234 Jane St.", "4567 Jane Dr."}, addresses);
                }

                @Override
                public void onCallback(double s, double t) {

                }
            };
        }
        catch (Exception e) {
            
        }
    }

    @Test
    public void testPricingModule() {
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        FuelQuoteFormDatabase database = new FuelQuoteFormDatabase(mockFirestore, mockAuth);
        database.setUserID("AY6ZTeSk6xeAlkTufZKA9P4m9vy1");
        database.setGallons(21);
        FirestoreCallback mockCallback = new FirestoreCallback() {
            @Override
            public void onCallback(String[] addresses) {
            }
            @Override
            public void onCallback(double s, double t) {
                assertEquals(1.73, s, 0.01);
                assertEquals(36.33, t, 0.01);
            }
        };
        database.pricingModule(mockCallback);
    }

    /*@Test
    public void testStoreFields() throws Exception {
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        FuelQuoteFormDatabase database = new FuelQuoteFormDatabase(mockFirestore, mockAuth);

        database.setAddress("1234 Jane St.");

        database.setDate("08-10-10");

        database.setUserID("alice");

        database.setGallons(34);

        database.setSuggestedPrice(342421);

        database.setTotalPrice(123);

        assertArrayEquals(new String[]{"34.0", "08-10-10", "1234 Jane St.", "342421.0", "123.0", "alice"}, database.storeFieldsInDatabase());
}*/

}