package com.github.mautini.pickaxe;

import com.google.common.collect.ImmutableList;
import com.google.schemaorg.SchemaOrgType;
import com.google.schemaorg.core.Event;
import com.google.schemaorg.core.Offer;
import com.google.schemaorg.core.Place;
import com.google.schemaorg.core.PostalAddress;
import com.google.schemaorg.core.Thing;
import com.google.schemaorg.core.datatype.DataType;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class ScraperTest {
    private static final Logger logger = Logger.getLogger(ScraperTest.class.getName());

    @Test
    public void scrape() throws IOException {
        Scraper scraper = new Scraper();
        File file = new File(getClass().getClassLoader().getResource("html.html").getFile());

        List<String> thingList = scraper.json(file);
        logger.info(thingList.toString());
    }

    @Test
    public void scraperJsonLdTest() throws IOException {
        Scraper scraper = new Scraper();
        List<Thing> thingList = scraper.thing(
                new File(getClass().getClassLoader().getResource("jsonld.html").getFile())
        );
        assertEvent(thingList);
    }

    @Test
    public void scraperMicrodataTest() throws IOException {
        Scraper scraper = new Scraper();
        List<Thing> thingList = scraper.thing(
                new File(getClass().getClassLoader().getResource("microdata.html").getFile())
        );
        assertEvent(thingList);
    }

    private void assertEvent(List<Thing> thingList) {
        Assert.assertEquals(1, thingList.size());
        Assert.assertTrue(thingList.get(0) instanceof Event);
        Event event = (Event) thingList.get(0);
        assertUniqueValue(event.getStartDateList(), "2013-09-14T21:30");
        assertUniqueValue(event.getNameList(), "Typhoon with Radiation City");

        Assert.assertEquals(1, event.getLocationList().size());
        Assert.assertTrue(event.getLocationList().get(0) instanceof Place);
        Place place = (Place) event.getLocationList().get(0);
        assertUniqueValue(place.getNameList(), "The Hi-Dive");

        Assert.assertEquals(1, place.getAddressList().size());
        Assert.assertTrue(place.getAddressList().get(0) instanceof PostalAddress);
        PostalAddress postalAddress = (PostalAddress) place.getAddressList().get(0);
        assertUniqueValue(postalAddress.getStreetAddressList(), "7 S. Broadway");
        assertUniqueValue(postalAddress.getAddressLocalityList(), "Denver");
        assertUniqueValue(postalAddress.getAddressRegionList(), "CO");
        assertUniqueValue(postalAddress.getPostalCodeList(), "80209");

        Assert.assertEquals(1, event.getOffersList().size());
        Assert.assertTrue(event.getOffersList().get(0) instanceof Offer);
        Offer offer = (Offer) event.getOffersList().get(0);
        assertUniqueValue(offer.getPriceList(), "13.00");
        assertUniqueValue(offer.getPriceCurrencyList(), "USD");
        assertUniqueValue(offer.getUrlList(), "http://www.ticketfly.com/purchase/309433");
    }

    private void assertUniqueValue(ImmutableList<SchemaOrgType> values, String expected) {
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(expected, ((DataType) values.get(0)).getValue());
    }
}
