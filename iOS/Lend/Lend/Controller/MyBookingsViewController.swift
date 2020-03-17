//
//  MyBookingsViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/15/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Firebase
import CodableFirebase

class MyBookingsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    @IBOutlet weak var tableView : UITableView!
    
    let dispatchGroup = DispatchGroup()
    
    var bookingsBorrower : [Booking] = [Booking]()
    
    var bookingsLender : [Booking] = [Booking]()
    
    var selectedRow : Int?
    
    var numSections = 0
    var activeTable = ""
    
    

    override func viewDidLoad() {
        super.viewDidLoad()
        setupTableView()
        print(Utils.screenSize)
        // Do any additional setup after loading the view.
    }
    
    func setupTableView() {
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.register(UINib(nibName: "PostingTableViewCell", bundle: nil), forCellReuseIdentifier: "PostingCell")
        LoadingIndicator.show(self.view)
        updateBorrowerBookings()
        updateLenderBookings()

    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if (bookingsLender.count > 0 && bookingsBorrower.count > 0) {
            tableView.backgroundView = nil
            numSections = 2
            return 2
        } else if (bookingsLender.count > 0 || bookingsBorrower.count > 0) {
            tableView.backgroundView = nil
            numSections = 1
            if (bookingsLender.count > 0) {
                activeTable = "Lender"
            } else {
                activeTable = "Borrower"
            }
            return 1
        } else {
            tableView.EmptyMessage(message: "You haven't made any bookings yet. When you've booked something you like, it will appear here.")
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (numSections == 2) {
            if (section == 0) {
                return bookingsBorrower.count
            } else {
                return bookingsLender.count
            }
        } else {
            if (activeTable == "Borrower") {
                return bookingsBorrower.count
            } else {
                return bookingsLender.count
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if (numSections == 2) {
            if (indexPath.section == 0) {
                return setupBookingBorrowerCell(indexPath: indexPath)
            } else {
                return setupBookingLenderCell(indexPath: indexPath)
            }
        } else {
            if (activeTable == "Borrower") {
                return setupBookingBorrowerCell(indexPath: indexPath)
            } else {
                return setupBookingLenderCell(indexPath: indexPath)
            }
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return Utils.MAIN_CELL_HEIGHT / 2 + 10
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedRow = indexPath.row
        if (numSections == 2) {
            if (indexPath.section == 0) {
                goToBooking()
            } else {
                goToProfile()
            }
        } else {
            if (activeTable == "Borrower") {
                goToBooking()
            } else {
                goToProfile()
            }
        }
        
        
    }
    
    
    private func setupBookingBorrowerCell(indexPath : IndexPath) -> PostingTableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostingCell", for: indexPath) as! PostingTableViewCell
        let row = indexPath.row
        cell.tag = row
        cell.booking = bookingsBorrower[row]
        cell.itemName.text = bookingsBorrower[row].item.itemName!
        cell.itemCategory.text = bookingsBorrower[row].item.category!
        cell.itemPrice.text = bookingsBorrower[row].item.formattedPrice
        cell.itemImage.layer.cornerRadius = 10
        cell.itemImage.loadImage(url: bookingsBorrower[row].item.photoURL!)
        cell.returnButton.isHidden = false
        cell.returnButton.layer.cornerRadius = 10
        cell.messageButton.isHidden = false
        cell.messageButton.layer.cornerRadius = 10
        cell.lenderPhoto.loadSmallImage(url: bookingsBorrower[row].lender.photoURL!)
        cell.lenderPhoto.makeRounded(borderWidth : 0.0, borderColor : UIColor.white.cgColor)
        cell.lenderName.text = bookingsBorrower[row].lender.username!
        cell.selectionStyle = .none
        cell.returnClickedAction = {(actionCell : PostingTableViewCell) in
            let ok = {(alert : UIAlertAction!) in
                var newBooking = actionCell.booking
                newBooking!.active = false
                newBooking!.userReturned = true
                newBooking!.item.booked = false
                FirebaseQueries.pushItemData(item: newBooking!.item)
                FirebaseQueries.pushBookingData(booking: newBooking!)
                FirebaseQueries.updateBookingsArraysBorrower(user: newBooking!.borrower)
                FirebaseQueries.updateBookingsArraysLender(user: newBooking!.lender)
            }
            
            let cancel = {(alert : UIAlertAction!) in
                return
            }
            
            
            self.displayConfirmationAlert(title: "Please Confirm", message: "Are you sure you want to return \(actionCell.itemName.text!)?", okHandler: ok, cancelHandler: cancel)
        }
        cell.messageClickedAction = { (actionCell : PostingTableViewCell) in
            let email = actionCell.booking.lender.email!
            if let url = URL(string: "mailto:\(email)") {
              if #available(iOS 10.0, *) {
                UIApplication.shared.open(url)
              } else {
                UIApplication.shared.openURL(url)
              }
            }
            
        }
        return cell
    }
    
    
    private func setupBookingLenderCell(indexPath : IndexPath) -> PostingTableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostingCell", for: indexPath) as! PostingTableViewCell
        let row = indexPath.row
        cell.tag = row
        cell.booking = bookingsLender[row]
        cell.itemName.text = bookingsLender[row].item.itemName!
        cell.itemCategory.text = bookingsLender[row].item.category!
        cell.itemPrice.text = bookingsLender[row].item.formattedPrice
        cell.itemImage.layer.cornerRadius = 10
        cell.itemImage.loadImage(url: bookingsLender[row].item.photoURL!)
        cell.returnButton.isHidden = true
        cell.messageButton.isHidden = false
        cell.postedByLabel.text = "borrowed by"
        cell.messageButton.layer.cornerRadius = 10
        cell.lenderPhoto.loadSmallImage(url: bookingsLender[row].borrower!.photoURL!)
        cell.lenderPhoto.makeRounded(borderWidth : 0.0, borderColor : UIColor.white.cgColor)
        cell.lenderName.text = bookingsLender[row].borrower!.username!
        cell.selectionStyle = .none
        cell.messageClickedAction = { (actionCell : PostingTableViewCell) in
            let email = actionCell.booking.borrower!.email!
            if let url = URL(string: "mailto:\(email)") {
              if #available(iOS 10.0, *) {
                UIApplication.shared.open(url)
              } else {
                UIApplication.shared.openURL(url)
              }
            }
            
        }
        return cell
    }
 
    
    private func goToBooking() {
        self.performSegue(withIdentifier: "toBooking", sender: self)
    }
    
    private func goToProfile() {
        self.performSegue(withIdentifier: "toProfile", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "toBooking") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = bookingsBorrower[selectedRow!].item
        } else if (segue.identifier == "toProfile") {
            let destinationVC = segue.destination as! ProfileViewController
            currentActiveProfile = bookingsLender[selectedRow!].borrower!
        }
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        cell.alpha = 0
        UIView.animate(
            withDuration: 0.5,
            delay: 0.05 * Double(indexPath.row),
            animations: {
                cell.alpha = 1
        })
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        if (numSections == 2) {
            if (section == 0) {
                return "My Borrowed Items"
            } else {
                return "My Lended Items"
            }
        } else {
            if (activeTable == "Borrower") {
                return "My Borrowed Items"
            } else {
                return "My Lended Items"
            }
        }
        
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
 Data handler.
 */
extension MyBookingsViewController {
    
    func updateBorrowerBookings() {
        let db = Firestore.firestore()
        db.collection("borrowerToBookings").document(Auth.auth().currentUser!.uid).addSnapshotListener() { (document, error) in
            var allBookings : [Booking] = [Booking]()
            if let document = document, document.exists {
                let bookings = document.get("Bookings") as! [[String : Any]]
                for b in bookings {
                    let model = try! FirestoreDecoder().decode(Booking.self, from: b)
                    if (model.active){
                        allBookings.append(model)
                    }
                }
                self.bookingsBorrower = self.filterForActive(bookings: allBookings)
                LoadingIndicator.hide()
                self.tableView.reloadData()
            } else {
                print("Document does not exist")
            }
        }
    }
    
    func updateLenderBookings() {
        let db = Firestore.firestore()
        db.collection("lenderToBookings").document(Auth.auth().currentUser!.uid).addSnapshotListener() { (document, error) in
            var allBookings : [Booking] = [Booking]()
            if let document = document, document.exists {
                let bookings = document.get("Bookings") as! [[String : Any]]
                for b in bookings {
                    let model = try! FirestoreDecoder().decode(Booking.self, from: b)
                    if (model.active){
                        allBookings.append(model)
                    }
                }
                self.bookingsLender = self.filterForActive(bookings: allBookings)
                LoadingIndicator.hide()
                self.tableView.reloadData()
            } else {
                print("Document does not exist")
            }
        }
    }
    
    private func filterForActive(bookings : [Booking]) -> [Booking] {
        let filtered = bookings.filter({ booking in
            return booking.active == true
        })
        return filtered
    }
}
