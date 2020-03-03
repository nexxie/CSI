import org.junit.Before;
import org.junit.Test;


import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PriceTaskTest {

    private int year ;
    private String productCode;
    private final String valueError = "Value wrong.";
    private final String logicError = "Logic error.";
    private PriceTask mixer;

    @Before
    public void initValues() {
        year =  new Date(System.currentTimeMillis()).getYear();
        productCode = "2448";
        mixer = new PriceTask();
    }

    private void checkCollections(Collection example, Collection result) {
        assertEquals(valueError, example.size(), result.size());
        Iterator iterator = example.iterator();
        while (iterator.hasNext()) {
            assertTrue(logicError, result.contains(iterator.next()));
        }
    }

    private Date getDate(int month, int date) {
        return new Date(year, month - 1, date);
    }

    @Test
    public void taskExample() {
        List<Price> oldPrices = new ArrayList<>();
        List<Price> newPrices = new ArrayList<>();
        List<Price> result = new ArrayList<>();

        oldPrices.add(new Price("122856", 1, 1, getDate(1, 1), getDate(1, 31),11000));
        oldPrices.add(new Price("122856", 2, 1, getDate(1, 10), getDate(1, 20),99000));
        oldPrices.add(new Price("6654", 1, 2, getDate(1, 1), getDate(1, 31),5000));

        newPrices.add(new Price("122856", 1, 1, getDate(1, 20), getDate(2, 20),11000));
        newPrices.add(new Price("122856", 2, 1, getDate(1, 15), getDate(1, 25),92000));
        newPrices.add(new Price("6654", 1, 2, getDate(1, 12), getDate(1, 13),4000));

        result.add(new Price("122856", 1, 1, getDate(1, 1), getDate(2, 20),11000));
        result.add(new Price("122856", 2, 1, getDate(1, 10), getDate(1, 15),99000));
        result.add(new Price("122856", 2, 1, getDate(1, 15), getDate(1, 25),92000));
        result.add(new Price("6654", 1, 2, getDate(1, 1), getDate(1, 12),5000));
        result.add(new Price("6654", 1, 2, getDate(1, 12), getDate(1, 13),4000));
        result.add(new Price("6654", 1, 2, getDate(1, 13), getDate(1, 31),5000));

        checkCollections(result, mixer.insert(oldPrices, newPrices));
    }

    @Test
    public void dataSplit() {
        List<Price> oldPrices = new ArrayList<>();
        List<Price> newPrices = new ArrayList<>();
        List<Price> result = new ArrayList<>();

        oldPrices.add(new Price(productCode, 1, 1, getDate(1, 2), getDate(1, 31),11000));

        newPrices.add(new Price(productCode, 1, 1, getDate(1, 5), getDate(1, 7),22000));
        newPrices.add(new Price(productCode, 1, 1, getDate(1, 16), getDate(1, 20),33000));

        result.add(new Price(productCode, 1, 1, getDate(1, 2), getDate(1, 5),11000));
        result.add(new Price(productCode, 1, 1, getDate(1, 5), getDate(1, 7),22000));
        result.add(new Price(productCode, 1, 1, getDate(1, 7), getDate(1, 16),11000));
        result.add(new Price(productCode, 1, 1, getDate(1, 16), getDate(1, 20),33000));
        result.add(new Price(productCode, 1, 1, getDate(1, 20), getDate(1, 31),11000));

        checkCollections(result, mixer.insert(oldPrices, newPrices));
    }

    @Test
    public void rewriteParts() {
        List<Price> oldPrices = new ArrayList<>();
        List<Price> newPrices = new ArrayList<>();
        List<Price> result = new ArrayList<>();

        oldPrices.add(new Price(productCode, 1, 1, getDate(1, 5), getDate(1, 15),11000));
        oldPrices.add(new Price(productCode, 1, 1, getDate(1, 15), getDate(1, 25),22000));

        newPrices.add(new Price(productCode, 1, 1, getDate(1, 1), getDate(1, 7),22448));
        newPrices.add(new Price(productCode, 1, 1, getDate(1, 10), getDate(1, 20),884488));
        newPrices.add(new Price(productCode, 1, 1, getDate(1, 23), getDate(2, 10),844844));
        newPrices.add(new Price(productCode, 1, 1, getDate(2, 10), getDate(2, 23),2424484));

        result.add(new Price(productCode, 1, 1, getDate(1, 1), getDate(1, 7),22448));
        result.add(new Price(productCode, 1, 1, getDate(1, 7), getDate(1, 10),11000));
        result.add(new Price(productCode, 1, 1, getDate(1, 10), getDate(1, 20),884488));
        result.add(new Price(productCode, 1, 1, getDate(1, 20), getDate(1, 23),22000));
        result.add(new Price(productCode, 1, 1, getDate(1, 23), getDate(2, 10),844844));
        result.add(new Price(productCode, 1, 1, getDate(2, 10), getDate(2, 23),2424484));

        checkCollections(result, mixer.insert(oldPrices, newPrices));
    }

    @Test
    public void createNewProducts() {
        List<Price> oldPrices = new ArrayList<>();
        List<Price> newPrices = new ArrayList<>();
        List<Price> result = new ArrayList<>();

        oldPrices.add(new Price(productCode, 1, 1, getDate(1, 20), getDate(1, 30),900));

        newPrices.add(new Price(productCode, 1, 1, getDate(1, 15), getDate(1, 25),850));
        newPrices.add(new Price(productCode + "2", 1, 1, getDate(1, 1), getDate(1, 30),900));
        newPrices.add(new Price(productCode + "4", 2, 1, getDate(1, 15), getDate(1, 20),900));
        newPrices.add(new Price(productCode + "8", 1, 1, getDate(1, 1), getDate(12, 30),800));

        result.add(new Price(productCode, 1, 1, getDate(1, 15), getDate(1, 25),850));
        result.add(new Price(productCode, 1, 1, getDate(1, 25), getDate(1, 30),900));
        result.add(new Price(productCode + "2", 1, 1, getDate(1, 1), getDate(1, 30),900));
        result.add(new Price(productCode + "4", 2, 1, getDate(1, 15), getDate(1, 20),900));
        result.add(new Price(productCode + "8", 1, 1, getDate(1, 1), getDate(12, 30),800));

        checkCollections(result, mixer.insert(oldPrices, newPrices));
    }

    @Test
    public void autoAdd() {
        List<Price> oldPrices = new ArrayList<>();
        List<Price> newPrices = new ArrayList<>();
        oldPrices.add(new Price(productCode, 8, 4, getDate(2, 4), getDate(2, 8),80));
        oldPrices.add(new Price(productCode, 8, 4, getDate(2, 8), getDate(2, 16),48));
        oldPrices.add(new Price(productCode, 8, 4, getDate(2, 16), getDate(2, 24),88));
        int days = 28;
        int months = 12;
        int products = 1000;
        for (int month = 0; month < months; month++) {
            for (int day = 0; day < days; day++) {
                for (int i = 0; i < products; i++) {
                    newPrices.add(new Price(productCode + i, 1, 1, getDate(month, day), getDate(month, day + 1),
                            month + day + i));
                }
            }
        }
        assertEquals(valueError, mixer.insert(oldPrices, newPrices).size(), days*months*products + 3);
    }

    @Test
    public void emptySets() {
        List<Price> priceList1 = new ArrayList<>();
        List<Price> priceList2 = new ArrayList<>();

        priceList1.add(new Price(productCode, 8, 4, getDate(4, 4), getDate(4, 8),24));
        checkCollections(priceList1, mixer.insert(priceList1, priceList2));

        priceList1 = new ArrayList<>();

        priceList2.add(new Price(productCode, 8, 4, getDate(2, 20), getDate(2, 24),88));
        checkCollections(priceList2, mixer.insert(priceList1, priceList2));

    }

    @Test(expected = NullPointerException.class)
    public void testNulls() {
        mixer.insert(null, null);
    }

}