//
//  LendUser.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/6/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation

class LendUser {
    var id, username, photoURL, city, email : String
    var rating, latitude, longitude: Double
    var numberOfBookings, numReviews, yearJoined: Int
    var description = "No Description"

    init() {
        id = "000"
        username = "Default User"
        photoURL = "default"
        city = "Berkeley"
        email = "default@user.com"
        rating = 0.0
        latitude = 0.0
        longitude = 0.0
        numberOfBookings = 0
        numReviews = 0
        yearJoined = 0
    }

    func getId() -> String {
        return id;
    }

    func setId(id : String)  {
        self.id = id;
    }

    func getUsername() -> String {
        return username;
    }

    func setUsername(username : String) {
        self.username = username;
    }

    func getPhotoURL() -> String {
        return photoURL;
    }

    func setPhotoURL(photoURL : String)  {
        self.photoURL = photoURL;
    }

    func getCity() -> String {
        return city;
    }

    func setCity(city : String)  {
        self.city = city;
    }

    func getEmail() -> String {
        return email;
    }

    func setEmail(email : String)  {
        self.email = email;
    }

    func getRating() -> Double {
        return rating;
    }

    func setRating(rating : Double)  {
        self.rating = rating;
    }

    func getLatitude() -> Double {
        return latitude;
    }

    func setLatitude(latitude : Double)  {
        self.latitude = latitude;
    }

    func getLongitude() -> Double{
        return longitude;
    }

    func setLongitude(longitude : Double)  {
        self.longitude = longitude;
    }

    func getNumberOfBookings() -> Int {
        return numberOfBookings;
    }

    func setNumberOfBookings(numberOfBookings : Int)  {
        self.numberOfBookings = numberOfBookings;
    }

    func getNumReviews() -> Int {
        return numReviews;
    }

    func setNumReviews(numReviews : Int)  {
        self.numReviews = numReviews;
    }

    func getYearJoined() -> Int {
        return yearJoined;
    }

    func setYearJoined(yearJoined : Int)  {
        self.yearJoined = yearJoined;
    }

    func getDescription() -> String{
        return description;
    }

    func setDescription(description : String)  {
        self.description = description;
    }
}
