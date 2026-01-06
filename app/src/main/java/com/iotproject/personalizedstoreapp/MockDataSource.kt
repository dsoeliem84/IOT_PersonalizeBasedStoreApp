package com.iotproject.personalizedstoreapp

object MockDataSource {

    // Beacon to Aisle mapping
    private val beaconToAisle = mapOf(
        "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6-1-1" to AisleInfo(1, 1, "Kitchen stuffs"),
        "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6-1-2" to AisleInfo(1, 2, "Office stuffs"),
        "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6-1-3" to AisleInfo(1, 3, "Bathroom stuffs")
    )

    // All products
    private val allProducts = listOf(
        // Kitchen stuffs (aisle 1)
        Product(
            product_id = 1,
            product_name = "Frying Pan",
            product_description = "Non-stick frying pan perfect for everyday cooking.",
            product_image = "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=200",
            product_location = 1001,
            discount = "2 for 1",
            aisle_name = "Kitchen stuffs",
            floor = 1,
            aisle_id = 1
        ),
        Product(
            product_id = 2,
            product_name = "Knife Set",
            product_description = "Professional kitchen knife set with ergonomic handles.",
            product_image = "https://images.unsplash.com/photo-1593618998160-e34014e67546?w=200",
            product_location = 1002,
            discount = "10%",
            aisle_name = "Kitchen stuffs",
            floor = 1,
            aisle_id = 1
        ),
        Product(
            product_id = 3,
            product_name = "Mixing Bowl",
            product_description = "Stainless steel mixing bowl set for baking and cooking.",
            product_image = "https://images.unsplash.com/photo-1556911220-bff31c812dba?w=200",
            product_location = 1003,
            discount = null,
            aisle_name = "Kitchen stuffs",
            floor = 1,
            aisle_id = 1
        ),
        Product(
            product_id = 4,
            product_name = "Cutting Board",
            product_description = "Durable bamboo cutting board, easy to clean.",
            product_image = "https://images.unsplash.com/photo-1594755047795-29a464d7e1fc?w=200",
            product_location = 1004,
            discount = null,
            aisle_name = "Kitchen stuffs",
            floor = 1,
            aisle_id = 1
        ),

        // Office stuffs (aisle 2)
        Product(
            product_id = 5,
            product_name = "Notebook",
            product_description = "Premium lined notebook for notes and journaling.",
            product_image = "https://images.unsplash.com/photo-1531346878377-a5be20888e57?w=200",
            product_location = 2001,
            discount = "10 kr off",
            aisle_name = "Office stuffs",
            floor = 1,
            aisle_id = 2
        ),
        Product(
            product_id = 6,
            product_name = "Pen Set",
            product_description = "Smooth writing ballpoint pens in assorted colors.",
            product_image = "https://images.unsplash.com/photo-1586075010923-2dd4570fb338?w=200",
            product_location = 2002,
            discount = null,
            aisle_name = "Office stuffs",
            floor = 1,
            aisle_id = 2
        ),
        Product(
            product_id = 7,
            product_name = "Desk Organizer",
            product_description = "Multi-compartment desk organizer for office supplies.",
            product_image = "https://images.unsplash.com/photo-1588516903720-8caa3a7970ae?w=200",
            product_location = 2003,
            discount = null,
            aisle_name = "Office stuffs",
            floor = 1,
            aisle_id = 2
        ),
        Product(
            product_id = 8,
            product_name = "Sticky Notes",
            product_description = "Colorful sticky notes for reminders and organization.",
            product_image = "https://images.unsplash.com/photo-1628013640099-3dc8f65fddb5?w=200",
            product_location = 2004,
            discount = null,
            aisle_name = "Office stuffs",
            floor = 1,
            aisle_id = 2
        ),

        // Bathroom stuffs (aisle 3)
        Product(
            product_id = 9,
            product_name = "Shampoo",
            product_description = "Moisturizing shampoo for healthy, shiny hair.",
            product_image = "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=200",
            product_location = 3001,
            discount = "3 for 2",
            aisle_name = "Bathroom stuffs",
            floor = 1,
            aisle_id = 3
        ),
        Product(
            product_id = 10,
            product_name = "Soap",
            product_description = "Natural antibacterial hand soap with moisturizers.",
            product_image = "https://images.unsplash.com/photo-1585128792020-803d29415281?w=200",
            product_location = 3002,
            discount = null,
            aisle_name = "Bathroom stuffs",
            floor = 1,
            aisle_id = 3
        ),
        Product(
            product_id = 11,
            product_name = "Towel Set",
            product_description = "Soft, absorbent cotton towel set in various sizes.",
            product_image = "https://images.unsplash.com/photo-1622290291468-a28f7a7dc6a8?w=200",
            product_location = 3003,
            discount = null,
            aisle_name = "Bathroom stuffs",
            floor = 1,
            aisle_id = 3
        ),
        Product(
            product_id = 12,
            product_name = "Toothbrush",
            product_description = "Soft bristle toothbrush for gentle, effective cleaning.",
            product_image = "https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=200",
            product_location = 3004,
            discount = null,
            aisle_name = "Bathroom stuffs",
            floor = 1,
            aisle_id = 3
        )
    )

    fun getAisleInfo(uuid: String, major: String, minor: String): AisleInfo? {
        val beaconKey = "$uuid-$major-$minor"
        return beaconToAisle[beaconKey]
    }

    fun getProducts(uuid: String, major: String, minor: String, mode: String): List<Product> {
        val aisleInfo = getAisleInfo(uuid, major, minor) ?: return emptyList()

        val productsInAisle = allProducts.filter {
            it.floor == aisleInfo.floor && it.aisle_id == aisleInfo.aisleId
        }

        return if (mode == "promo") {
            productsInAisle.filter { it.discount != null }
        } else {
            productsInAisle
        }
    }
}

data class AisleInfo(
    val floor: Int,
    val aisleId: Int,
    val aisleName: String
)

data class Product(
    val product_id: Int,
    val product_name: String,
    val product_description: String?,
    val product_image: String?,
    val product_location: Int?,
    val discount: String?,
    val aisle_name: String?,
    val floor: Int,
    val aisle_id: Int
)