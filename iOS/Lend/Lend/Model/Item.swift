//
//  Item.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//  Converted from Java version.

import Foundation
public struct Item : Codable {


    /*
     id : The item id number
     category: The category of item being lended
     */
    let id : String!
    
    var type, deliveryMethod, location, lenderId, itemName, itemDescription, price, photoURL, category : String?
    
    var booked : Bool!
    
    var latitude, longitude : Double!
    
    var lender : LendUser!
    
    var formattedPrice : String {
        get {
            return String(format: "$%.02f /day", Double(price!)!)
        }
    }
    
    var priceAsDouble : Double {
        get {
            return Double(price!)!
        }
    }
    
    enum CodingKeys: String, CodingKey {
        case id = "ID"
        case booked = "Booked"
        case location = "Location"
        case lenderId = "Lender ID"
        case itemName = "Item Name"
        case itemDescription = "Item Description"
        case price = "Item Price"
        case photoURL = "Photo URL"
        case category = "Item Category"
        case lender = "Lender"
        case latitude = "Latitude"
        case longitude = "Longitude"
        case type = "Type"
        case deliveryMethod = "Delivery Method"
    }
    
    static var serviceCategories : [String] {
        get {
            var temp = ["Haircut", "Makeup", "Tutoring"].sorted()
            temp.append("Other")
            return temp
        }
    }
    
    static var lendableCategories : [String] {
        get {
            var temp = ["Mens Apparel", "Outdoor", "Home", "Electronic Appliances", "Jewlery", "Womens Apparel", "Hobbies"].sorted()
            temp.append("Other")
            return temp
        }
    }
}
