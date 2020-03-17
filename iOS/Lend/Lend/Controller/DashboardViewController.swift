//
//  DashboardViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/7/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import GoogleMaps
import GooglePlaces
import Firebase
import FirebaseFirestoreSwift
import CodableFirebase


class DashboardViewController: UIViewController {

    @IBOutlet weak var mainTV: UITableView!
    
    var dataSource : DashboardTableData?
    
    let dispatchGroup = DispatchGroup()
    
    /*
     Tracks the user selected map marker
     */
    var markerSelected : String?
    
    var featuredItemSelected : Item?
    
    @IBOutlet weak var headerImage: UIImageView!
    
    @IBOutlet weak var headerView: UIView!
    
    @IBOutlet weak var makePostButton: UIButton!
    
    @IBAction func makePostButtonClicked(_ sender: Any) {
        self.performSegue(withIdentifier: "toPosting", sender: self)
    }
    
    
    /*
     Used to return to dashboard after selecting an item
     */
    @IBAction func unwindToDashboard(_ unwindSegue: UIStoryboardSegue) {

    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.dataSource = DashboardTableData()
        dataSource!.containerView = self
        currentActiveProfile = CurrentUserData.currentUser.data!
        if (Utils.DEBUG) {
            debug()
        } else {
            updateAllData()
            setupHeader()
            setupTableView()
        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        navigationController?.setNavigationBarHidden(true, animated: animated)
        //If in debug mode, we wants to update attributes of all items, so we do not filter.
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    func setupHeader() {
        headerView.frame = CGRect(x: 0, y: 0, width: view.frame.size.width, height: 0.25 * view.frame.size.height)
        headerView.bounds = CGRect(x: 0, y: 0, width: view.frame.size.width, height: 0.25 * view.frame.size.height)
        headerView.translatesAutoresizingMaskIntoConstraints = true
        makePostButton.layer.cornerRadius = makePostButton.frame.size.width / 2
        dataSource!.initializeHeaderImageHeight(header: headerView)
    }
    
    func filterFeaturedItems() {
        
        dataSource?.featuredItems = Utils.filterFeaturedItems(items: dataSource!.featuredItems)
    }

    
    func setupTableView() {
        mainTV.dataSource = dataSource!
        mainTV.delegate = dataSource!
        mainTV.separatorStyle = UITableViewCell.SeparatorStyle.none
        mainTV.contentInset = UIEdgeInsets(top: headerView.frame.size.height, left: 0, bottom: 0, right: 0)
        mainTV.frame = view.frame
        setupSearchCellFunction()
    }
    
    func setupSearchCellFunction() {
        let searchBarFunc = {(cell : SearchBarCell) in
            cell.searchBar.resignFirstResponder()
            self.performSegue(withIdentifier: "toSearchPage", sender: self)
        }
        let mapViewFunc = {(cell : MapViewCell) in
            self.performSegue(withIdentifier: "toSearchPage", sender: self)
        }
        let featuredItemsFunc = {(cell : FeaturedItemsCell) in
            self.performSegue(withIdentifier: "toSearchPage", sender: self)
        }
        dataSource!.searchBarFunc = searchBarFunc
        dataSource!.mapViewFunc = mapViewFunc
        dataSource!.featuredItemsFunc = featuredItemsFunc
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "toBookingFromMap") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = dataSource!.nearbyItems[Int(self.markerSelected!)!]
        } else if (segue.identifier == "toBookingFromFeatured") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = featuredItemSelected!
        }
    }
    
    func debug() {
        
        /*
        FirebaseQueries.getItemsDebug() { items in
            for item in items {
                Utils.addNewAttributeToAllItems(field: "type", item : item)
            }
        }
         */
    
         
        
        /*
        FirebaseQueries.getBookings() { bookings in
            for booking in bookings {
                Utils.addNewAttributeToAllBookings(field: "item", booking : booking)
            }
        }
        */
        
        //FirebaseQueries.updateBookingIds()
        
         
        
        /*
        FirebaseQueries.getAllLenders() { users in
            for user in users {
                FirebaseQueries.updateBookingsArrays(user: user)
            }
        }
         */
        
         
        
        //FirebaseQueries.updateItemIds()
        //FirebaseQueries.deleteDuplicateItems()
         
    }
    
    
    
    
    
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

/*
 Realtime database updating
 */
extension DashboardViewController {
    func updateAllData() {
        LoadingIndicator.show(self.view)
        updateFeaturedItems()
        updateFeaturedLenders()
    }
    
    private func updateFeaturedLenders() {
        let db = Firestore.firestore()
        db.collection("users").whereField("rating", isGreaterThan: 4).addSnapshotListener() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing queries: \(err)")
                } else {
                    var foundLenders = [LendUser]()
                    for document in querySnapshot!.documents {
                        let found = try! FirestoreDecoder().decode(LendUser.self, from: document.data())
                        foundLenders.append(found)
                    }
                    self.dataSource!.featuredLenders = foundLenders
                    self.mainTV.reloadData()
                    LoadingIndicator.hide()
                }
        }
    }
    
    private func updateFeaturedItems() {
        let db = Firestore.firestore()
        db.collection("items").whereField("Booked", isEqualTo: false).addSnapshotListener() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing queries: \(err)")
                } else {
                    var foundItems = [Item]()
                    for document in querySnapshot!.documents {
                        print(document.data())
                        let found = try! FirestoreDecoder().decode(Item.self, from: document.data())
                        foundItems.append(found)
                    }
                    let filtered = self.filterByLatitudeAndLongitude(items: foundItems)
                    self.dataSource!.featuredItems = filtered
                    self.dataSource!.nearbyItems = filtered
                    self.mainTV.reloadData()
                    LoadingIndicator.hide()
                }
        }
    }
    
    private func initializeFeaturedLenders() {
        dispatchGroup.enter()
        let db = Firestore.firestore()
        db.collection("users").whereField("rating", isGreaterThan: 4).getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing queries: \(err)")
                } else {
                    var foundLenders = [LendUser]()
                    for document in querySnapshot!.documents {
                        let found = try! FirestoreDecoder().decode(LendUser.self, from: document.data())
                        foundLenders.append(found)
                    }
                    self.dataSource!.featuredLenders = foundLenders
                    self.mainTV.reloadData()
                    self.dispatchGroup.leave()
                }
        }
    }
    
    private func initializeFeaturedItems() {
        dispatchGroup.enter()
        let db = Firestore.firestore()
        db.collection("items").whereField("Booked", isEqualTo: false).getDocuments() { (querySnapshot, err) in
                if let err = err {
                    print("Error performing queries: \(err)")
                } else {
                    var foundItems = [Item]()
                    for document in querySnapshot!.documents {
                        let found = try! FirestoreDecoder().decode(Item.self, from: document.data())
                        foundItems.append(found)
                    }
                    let filtered = self.filterByLatitudeAndLongitude(items: foundItems)
                    self.dataSource!.featuredItems = filtered
                    self.dataSource!.nearbyItems = filtered
                    self.mainTV.reloadData()
                    self.dispatchGroup.leave()
                }
        }
    }
    
    private func filterByLatitudeAndLongitude(items : [Item]) -> [Item] {
        var finalItems = [Item]()
        let currentUser = CurrentUserData.currentUser.data!
        for item in items {
            if (item.lender!.longitude! < currentUser.longitude! + 1
                && item.lender!.longitude! > currentUser.longitude! - 1
                && item.lender!.latitude! < currentUser.latitude! + 1
                && item.lender!.latitude! > currentUser.latitude! - 1) {
                finalItems.append(item)
            }
        }
        return finalItems
    }
    
    
}

