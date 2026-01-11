from flask import Flask, jsonify, request
from flask_caching import Cache

import mysql.connector

app = Flask(__name__)

DB_CONFIGURE = {
    "host" : "127.0.0.1",
    "user" : "root",
    "password" : "localpassword",
    "database" : "iot_store",
    "port" : 3306
}

app.config["CACHE_TYPE"] = "SimpleCache"
app.config["CACHE_DEFAULT_TIMEOUT"] = 300  # set caching for 5 minutes

cache = Cache(app)

@app.get("/api/products")
@cache.cached(
    timeout=300,
    query_string=True   # IMPORTANT: includes floor, aisle_id, mode
)
def products():
    floor = request.args.get("floor", type=int)
    aisle_id = request.args.get("aisle_id", type=int)
    mode = (request.args.get("mode") or "all").lower()

    if floor is None or aisle_id is None:
        return jsonify({
            "error" : "floor and aisle id is required"
        }), 400
    
    if mode not in ("promo", "all"):
        return jsonify({
            "error" : "mode must be either 'all' or 'promo'"   
        }), 400
    
    if mode=="promo":
        sql = """
            SELECT
                p.product_id,
                p.product_name,
                p.product_description,
                p.product_image,
                p.product_location,
                pr.discount,
                a.aisle_name
            FROM Product p, Promotion pr, Aisle a
            WHERE p.product_id = pr.product_id 
                AND a.floor = p.floor 
                AND a.aisle_id = p.aisle_id
                AND p.floor = %s
                AND p.aisle_id = %s
            ORDER BY p.product_location;
        """
    else:
        sql = """
            SELECT
                p.product_id,
                p.product_name,
                p.product_description,
                p.product_image,
                p.product_location,
                (
                    SELECT pr.discount
                    FROM Promotion pr
                    WHERE pr.product_id = p.product_id
                ) AS discount,
                a.aisle_name
            FROM Product p, Aisle a
            WHERE a.floor = p.floor 
                AND a.aisle_id = p.aisle_id 
                AND p.floor = %s 
                AND p.aisle_id = %s
            ORDER BY p.product_location;
        """

    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    cursor.execute(sql, (floor, aisle_id))
    rows = cursor.fetchall()

    cursor.close()
    conn.close()

    return jsonify(rows), 200

def get_connection():
    return mysql.connector.connect(**DB_CONFIGURE)

app.run(host="0.0.0.0", port=5001, debug=True)