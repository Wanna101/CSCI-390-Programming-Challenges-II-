package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    // @Test
    void foo() {
        Item[] items = new Item[] { new Item("foo", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("fixme", app.items[0].name);
    }
    
    /**
     * testQualityAgedBrie:
     * - happy path test
     * - tests a random set of values for "Aged Brie" and expects
     * a certain value
     */
    @Test
    void testQualityAgedBrie() {
    	Item[] items = new Item[] { new Item("Aged Brie", 5, 5) };
    	GildedRose app = new GildedRose(items);
    	app.updateQuality();
    	assertEquals(6, app.items[0].quality);
    }
    
    /**
     * testQualitySulfuras:
     * - happy path test
     * - negative testing
     * - tests a random set of values for "Sulfuras" and expects
     * a certain value
     * - sets item as invalid if not equal to 80
     */
    @Test
    void testQualitySulfuras() {
    	Item[] items = new Item[] { 
    			new Item("Sulfuras, Hand of Ragnaros", 0, 80), 
    			new Item("Sulfuras, Hand of Ragnaros", -1, 81), 
    			new Item("Sulfuras, Hand of Ragnaros", 0, 79) };
    	GildedRose app = new GildedRose(items);
    	app.updateQuality();
    	assertEquals(80, app.items[0].quality);
    	// added new code in GildedRose which sets the name to 
    	// Invalid Input and sets sellIn and quality values to -1000
    	// if user puts anything other than 80
    	assertEquals(-1000, app.items[1].quality);
    	assertEquals(-1000, app.items[2].quality);
    }
    
    /**
     * testQualityBackstagePasses:
     * - happy path test
     * - tests a random set of values for "Backstage passes..." and expects
     * a certain value
     */
    @Test
    void testQualityBackstagePasses() {
    	Item[] items = new Item[] { new Item("Backstage passes to a TAFKAL80ETC concert", 12, 10) };
    	GildedRose app = new GildedRose(items);
    	int days = 12;
    	for (int i = 0; i < days; i++) {
    		if (i == 1) {
    			assertEquals(11, app.items[0].quality);
    		} else if (i == 6) {
    			assertEquals(20, app.items[0].quality);
    		} else if (i == 9) {
    			assertEquals(28, app.items[0].quality);
    		} else if (i == 12) {
    			assertEquals(0, app.items[0].quality);
    		}
    		app.updateQuality();
    	}
    }
    
    /**
     * testQualityConjured:
     * - happy path test
     * - tests a random set of values for "Conjured..." and expects
     * a certain value
     * - added code into GildedRose so Conjured subtracts double the amount
     * in Quality
     */
    @Test
    void testQualityConjured() {
    	Item[] items = new Item[] { 
    			new Item("Conjured Mana Cake", 3, 6),
    			new Item("Conjured Water Fountain", 5, 7) };
    	GildedRose app = new GildedRose(items);
    	app.updateQuality();
    	assertEquals(4, app.items[0].quality);
    	assertEquals(5, app.items[1].quality);
    }
    
    /**
     * testQualityAfterSellDate:
     * - boundary condition
     * - tests multiple items to check Quality after sell date 
     */
    @Test
    void testQualityAfterSellDate() {
    	Item[] items = new Item[] {
    			new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Elixir of the Mongoose", 5, 7),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Sulfuras, Hand of Ragnaros", -1, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                new Item("Conjured Mana Cake", 3, 6) };
    	GildedRose app = new GildedRose(items);
    	int days = 16;
    	for (int i = 0; i < days; i++) {
    		if (i == 1) {
    			assertEquals(80, items[3].quality);
    			assertEquals(80, items[4].quality);
    		} else if (i == 3) {
    			assertEquals(4, items[1].quality);
    		} else if (i == 4) {
    			assertEquals(0, items[8].quality);
    		} else if (i == 6) {
    			assertEquals(0, items[2].quality);
    			assertEquals(0, items[7].quality);
    		} else if (i == 11) {
    			assertEquals(0, items[6].quality);
    		} else if (i == 16) {
    			assertEquals(0, items[0].quality);
    			assertEquals(0, items[5].quality);
    		}
    		app.updateQuality();
    	}
    }
    
    /**
     * testQualityNotNegative:
     * - boundary condition
     * - tests multiple items to check that they are not negative
     * including Sulfuras
     */
    @Test
    void testQualityNotNegative() {
    	Item[] items = new Item[] {
    			new Item("Elixir of the Mongoose", 5, 7),
    			new Item("Aged Brie", 2, 0),
    			new Item("Conjured Mana Cake", 3, 6),
    			new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
    	GildedRose app = new GildedRose(items);		
    	int days = 16;
    	for (int i = 0; i < days; i++) {
    		if (i == 1) {
    			assertEquals(80, items[3].quality);
    		} else if (i == 3) {
    			assertEquals(4, items[1].quality);
    		} else if (i == 4) {
    			assertEquals(0, items[2].quality);
    		} else if (i == 6) {
    			assertEquals(0, items[0].quality);
    		}
    		app.updateQuality();
    	}
    	
    	
    	Item[] itemsTwo = new Item[] {
    			new Item("Elixir of the Mongoose", 5, -7),
    			new Item("Aged Brie", 2, 0),
    			new Item("Conjured Mana Cake", 3, -6),
    			new Item("Sulfuras, Hand of Ragnaros", 0, -80) };
    	app = new GildedRose(itemsTwo);
    	days = 16;
    	for (int i = 0; i < days; i++) {
    		if (i == 1) {
    			assertEquals(-1000, itemsTwo[3].quality);
    		} else if (i == 3) {
    			assertEquals(4, itemsTwo[1].quality);
    		} else if (i == 4) {
    			assertEquals(-1000, itemsTwo[2].quality);
    		} else if (i == 6) {
    			assertEquals(-1000, itemsTwo[0].quality);
    		}
    		app.updateQuality();
    	}
    }
    
    /**
     * testQualityLessThanFifty:
     * - boundary condition
     * - tests input of multiple items to check that they are under 50
     * - Sets quality to 50 if higher than 50;
     */
    @Test
    void testQualityLessThanFifty() {
    	Item[] items = new Item[] {
    			new Item("Elixir of the Mongoose", 5, 67),
    			new Item("Aged Brie", 2, 62),
    			new Item("Conjured Mana Cake", 3, 60),
    			new Item("Sulfuras, Hand of Ragnaros", 0, 80),
    			new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49) };
    	GildedRose app = new GildedRose(items);		
    	int days = 16;
    	for (int i = 0; i < days; i++) {
    		if (i <= 0) {
    			assertEquals(80, items[3].quality);
    		} else if (i == 2) {
    			assertEquals(50, items[1].quality);
    		} else if (i == 3) {
    			assertEquals(44, items[2].quality);
    		} else if (i == 5) {
    			assertEquals(45, items[0].quality);
    		} else if (i == 10) {
    			assertEquals(50, items[4].quality);
    		}
    		app.updateQuality();
    	}
    }
    
    /**
     * testQualityOutputAfterOneMonth:
     * - happy path
     * - tests multiple items to check that they output
     * expected values for one month
     */
    @Test
    void testQualityOutputAfterOneMonth() {
    	Item[] items = new Item[] {
    			new Item("+5 Dexterity Vest", 10, 20),
                new Item("Aged Brie", 2, 0),
                new Item("Elixir of the Mongoose", 5, 7),
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),
                new Item("Sulfuras, Hand of Ragnaros", -1, 80),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 10, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                new Item("Conjured Mana Cake", 3, 6) };
    	GildedRose app = new GildedRose(items);
    	int days = 28;
    	for (int i = 0; i < days; i++) {
    		if (i == 1) {
    			assertEquals(80, items[3].quality);
    			assertEquals(80, items[4].quality);
    		} else if (i == 3) {
    			assertEquals(4, items[1].quality);
    		} else if (i == 4) {
    			assertEquals(0, items[8].quality);
    		} else if (i == 6) {
    			assertEquals(0, items[2].quality);
    			assertEquals(0, items[7].quality);
    		} else if (i == 11) {
    			assertEquals(0, items[6].quality);
    		} else if (i == 16) {
    			assertEquals(0, items[0].quality);
    			assertEquals(0, items[5].quality);
    		} else if (i == 27) {
    			assertEquals(0, items[0].quality);
    			assertEquals(50, items[1].quality);
    			assertEquals(0, items[2].quality);
    			assertEquals(80, items[3].quality);
    			assertEquals(80, items[4].quality);
    			assertEquals(0, items[5].quality);
    			assertEquals(0, items[6].quality);
    			assertEquals(0, items[7].quality);
    			assertEquals(0, items[8].quality);
    		}
    		app.updateQuality();
    	}
    }
    
    /**
     * testSellInValues:
     * - happy path
     * - tests multiple items to check that they output expected values
     */
    @Test
    void testSellInValues() {
    	Item[] items = new Item[] {
    			new Item("Elixir of the Mongoose", 5, 7),
    			new Item("Aged Brie", 2, 0),
    			new Item("Conjured Mana Cake", 3, 6),
    			new Item("Sulfuras, Hand of Ragnaros", 0, 80) };
    	GildedRose app = new GildedRose(items);		
    	int days = 16;
    	for (int i = 0; i < days; i++) {
    		if (i == 0) {
    			assertEquals(0, items[3].sellIn);
    		} else if (i == 2) {
    			assertEquals(0, items[1].sellIn);
    		} else if (i == 3) {
    			assertEquals(0, items[2].sellIn);
    		} else if (i == 5) {
    			assertEquals(0, items[0].sellIn);
    		}
    		app.updateQuality();
    	}
    }
    
    /**
     * testNameInput:
     * - happy path
     * - tests multiple items to check that they output expected values
     */
    @Test
    void testNameInput() {
    	Item[] items = new Item[] {
    			new Item("Elixir of the Mongoose", 5, 0),
    			new Item("Aged Brie", 2, -1),
    			new Item("Conjurod Mana Cake", 3, 1),
    			new Item("Sulfuras, Hand of Ragnaros", 9, 80) };
    	GildedRose app = new GildedRose(items);
    	int days = 16;
    	for (int i = 0; i < days; i++) {
    		if (i == 2) {
    			assertEquals("Invalid Input", items[1].name);
    		} else if (i == 3) {
    			assertEquals("Conjurod Mana Cake", items[2].name);
    		} else if (i == 5) {
    			assertEquals("Elixir of the Mongoose", items[0].name);
    		} else if (i == 9) {
    			assertEquals("Sulfuras, Hand of Ragnaros", items[3].name);
    		}
    		app.updateQuality();
    	}
    }
    
}
