package com.iotproject.personalizedstoreapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductListActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvBeaconId: TextView
    private lateinit var tvAisleName: TextView
    private lateinit var rvProducts: RecyclerView
    private lateinit var tvError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        tvTitle = findViewById(R.id.tvTitle)
        tvBeaconId = findViewById(R.id.tvBeaconId)
        tvAisleName = findViewById(R.id.tvAisleName)
        rvProducts = findViewById(R.id.rvProducts)
        tvError = findViewById(R.id.tvError)

        val uuid = intent.getStringExtra("UUID") ?: ""
        val major = intent.getStringExtra("MAJOR") ?: ""
        val minor = intent.getStringExtra("MINOR") ?: ""
        val mode = intent.getStringExtra("MODE") ?: "all"

        tvBeaconId.text = "Beacon: $uuid-$major-$minor"
        tvTitle.text = if (mode == "promo") "Promotions" else "Products"

        rvProducts.layoutManager = LinearLayoutManager(this)

        loadProducts(uuid, major, minor, mode)
    }

    private fun loadProducts(uuid: String, major: String, minor: String, mode: String) {
        // Get aisle info
        val aisleInfo = MockDataSource.getAisleInfo(uuid, major, minor)

        if (aisleInfo == null) {
            tvError.text = "Beacon not found in system"
            tvError.visibility = View.VISIBLE
            rvProducts.visibility = View.GONE
            return
        }

        // Get products
        val products = MockDataSource.getProducts(uuid, major, minor, mode)

        if (products.isEmpty()) {
            tvError.text = if (mode == "promo") {
                "No promotions available in this aisle"
            } else {
                "No products found in this aisle"
            }
            tvError.visibility = View.VISIBLE
            rvProducts.visibility = View.GONE
        } else {
            tvAisleName.text = "Aisle: ${aisleInfo.aisleName} (Floor ${aisleInfo.floor})"
            tvAisleName.visibility = View.VISIBLE

            rvProducts.visibility = View.VISIBLE
            rvProducts.adapter = ProductAdapter(products)
            tvError.visibility = View.GONE
        }
    }
}

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProductImage: ImageView = view.findViewById(R.id.ivProductImage)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvProductDescription: TextView = view.findViewById(R.id.tvProductDescription)
        val tvProductLocation: TextView = view.findViewById(R.id.tvProductLocation)
        val tvDiscount: TextView = view.findViewById(R.id.tvDiscount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.tvProductName.text = product.product_name
        holder.tvProductDescription.text = product.product_description ?: "No description"

        // Show location if available
        if (product.product_location != null) {
            holder.tvProductLocation.text = "Location: ${product.product_location}"
            holder.tvProductLocation.visibility = View.VISIBLE
        } else {
            holder.tvProductLocation.visibility = View.GONE
        }

        // Show discount badge if available
        if (!product.discount.isNullOrEmpty()) {
            holder.tvDiscount.text = product.discount
            holder.tvDiscount.visibility = View.VISIBLE
        } else {
            holder.tvDiscount.visibility = View.GONE
        }

        // Load image with Glide
        if (!product.product_image.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(product.product_image)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(holder.ivProductImage)
        } else {
            holder.ivProductImage.setImageResource(R.drawable.ic_placeholder)
        }
    }

    override fun getItemCount() = products.size
}