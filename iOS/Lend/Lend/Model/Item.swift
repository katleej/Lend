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
    
    var location, lenderId, itemName, itemDescription, price, photoURL, category : String?
    
    var booked : Bool!
    
    var latitude, longitude : Double!
    
    var lender : LendUser!
    
    var formattedPrice : String {
        get {
            return "$\(price!)"
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
    }
}
