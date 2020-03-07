//
//  LendTests.swift
//  LendTests
//
//  Created by Matthew Lacayo on 2/22/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import XCTest
@testable import Lend

class LendTests: XCTestCase {
    
    var defaultUser : LendUser!
    var defaultItem : Item!

    override func setUp() {
        // Put setup code here. This method is called before the invocation of each test method in the class.
        defaultUser = LendUser()
        defaultItem = Item()
    }

    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        defaultUser = nil
    }

    func testDefaultUser() {
        // This is an example of a functional test case.
        // Use XCTAssert and related functions to verify your tests produce the correct results.
        XCTAssertEqual("Default User", defaultUser.getUsername(), "Incorrect Username Retrieved.")
    }
    
    func testItem() {
        XCTAssertEqual("Default Category", defaultItem.getCategory(), "Incorrect Username Retrieved.")
    }


}
