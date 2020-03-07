//
//  FirebaseLendUser.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import Firebase

public struct FirebaseLendUser : Codable {

    let id, username, photoURL, city, email : String?
    let rating, latitude, longitude: Double?
    let numberOfBookings, numReviews, yearJoined: Int?
    let description = "No Description"

    enum CodingKeys: String, CodingKey {
        case id
        case username
        case photoURL
        case city
        case email
        case rating
        case latitude
        case longitude
        case numberOfBookings
        case numReviews
        case yearJoined
        case description
    }

}
