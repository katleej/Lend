//
//  Item.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//  Converted from Java version.

import Foundation
class Item {


    /*
     id : The item id number
     category: The category of item being lended
     */
    var id, lenderId, lenderName, itemName, itemDescription, price, lat, lng, photoURL, category : String
    

    init() {
        id = "000"
        lenderId = "000"
        lenderName = "Default"
        itemName = "Default Item"
        itemDescription = "Default Description"
        price = "0"
        lat = "0"
        lng = "0"
        photoURL = "default"
        category = "Default Category"
    }
    
    init(id : String, lenderId : String, lenderName : String, itemName : String, itemDescription : String, price : String, lat : String, lng : String, photoURL : String, category : String) {
        self.id = id
        self.lenderId = lenderId
        self.lenderName = lenderName
        self.itemName = itemName
        self.itemDescription = itemDescription
        self.price = price
        self.lat = lat
        self.lng = lng
        self.photoURL = photoURL
        self.category = category
    }

    func getLenderName() -> String {
        return lenderName;
    }

    func setLenderName(lenderName : String) {
        self.lenderName = lenderName;
    }

    func getid() -> String{
        return id;
    }

    func setid(ID : String) {
        self.id = ID;
    }

    func getLender() -> String {
        return lenderId;
    }

    func setLender(lenderId : String) {
        self.lenderId = lenderId;
    }

    func getItemName() -> String {
        return itemName;
    }

    func setItemName(itemName : String) {
        self.itemName = itemName;
    }

    func getItemDescription() -> String {
        return itemDescription;
    }

    func setItemDescription(itemDescription : String) {
        self.itemDescription = itemDescription;
    }

    func getPrice() -> String {
        return price;
    }

    func setPrice(price : String) {
        self.price = price;
    }

    func getLat() -> String {
        return lat;
    }

    func setLat(lat : String) {
        self.lat = lat;
    }

    func getLng() -> String {
        return lng;
    }

    func setLng(lng : String) {
        self.lng = lng;
    }

    func getPhotoURL() -> String {
        return photoURL;
    }

    func setPhotoURL(photoURL : String) {
        self.photoURL = photoURL;
    }

    func getCategory() -> String{
        return category;
    }

    func setCategory(category : String) {
        self.category = category;
    }
}
