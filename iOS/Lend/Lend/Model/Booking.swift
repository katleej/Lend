//
//  Booking.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/14/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation

public struct Booking : Codable {
    let id : Int!
    var active, userReturned : Bool!
    let lenderId, itemId: String!
    var bookingDays : Int?
    var borrowerId : String?
    
    enum CodingKeys : String, CodingKey {
        case id = "ID"
        case active = "Active"
        case userReturned = "User Returned"
        case lenderId = "Lender ID"
        case itemId = "Item ID"
        case bookingDays = "Booking Days"
        case borrowerId = "Borrower ID"
    }
    
}
