package com.gildedrose;

class GildedRose {
    Item[] items;
    int count = 0;
    
    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
    	for (Item item : items) {
    		if (item.quality > 50 && !item.name.equals("Sulfuras, Hand of Ragnaros") && count < items.length) {
    			item.quality = 50;
    		} else if (item.quality < 0 && !item.name.equals("Sulfuras, Hand of Ragnaros") && count < items.length) {
    			item.name = "Invalid Input";
    			item.quality = -1000;
    			item.sellIn = -1000;
    		}
    		count += 1;
    	}
    	
        for (int i = 0; i < items.length; i++) {
            if (!items[i].name.equals("Aged Brie")
                    && !items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                if (items[i].quality > 0) {
                    if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                        items[i].quality = items[i].quality - 1;
                        if (items[i].name.contains("Conjured")) {
                        	items[i].quality = items[i].quality - 1;
                        }
                    } else if (items[i].name.equals("Sulfuras, Hand of Ragnaros")){
                    	if (items[i].quality != 80) {
                    		items[i].name = "Invalid Input";
                    		items[i].quality = -1000;
                    		items[i].sellIn = -1000;
                    	}
                    }
                } else if (items[i].quality < 0) {
                	items[i].name = "Invalid Input";
                	items[i].quality = -1000;
                	items[i].quality = -1000;
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    if (items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].name.equals("Aged Brie")) {
                    if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].quality > 0) {
                            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
        }
    }
}