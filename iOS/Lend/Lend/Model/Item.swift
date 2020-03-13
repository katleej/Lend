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
    let id : Int!
    var booked, location, lenderId, lenderName, itemName, itemDescription, price, photoURL, category : String?
    var lat, lng : Double?
    
    enum CodingKeys: String, CodingKey {
        case id = "ID"
        case booked = "Booked"
        case location = "Location"
        case lenderId = "Lender ID"
        case lenderName = "Lender Name"
        case itemName = "Item Name"
        case itemDescription = "Item Description"
        case price = "Item Price"
        case lat = "Latitude"
        case lng = "Longitude"
        case photoURL = "Photo URL"
        case category = "Item Category"
    }
}
