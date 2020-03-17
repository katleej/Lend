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
    
    var bookings : [Booking] = [Booking]()
    
    var selectedRow : Int?
    
    

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
        initializeBookings()
        updateBookings()
        self.dispatchGroup.notify(queue: .main) {
            self.tableView.reloadData()
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if (bookings.count > 0) {
            tableView.backgroundView = nil
            return 1
        } else {
            tableView.EmptyMessage(message: "You haven't made any bookings yet. When you've booked something you like, it will appear here.")
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return bookings.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return setupBookingCell(indexPath: indexPath)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return Utils.MAIN_CELL_HEIGHT / 2 + 10
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedRow = indexPath.row
        goToBooking()
    }
    
    
    private func setupBookingCell(indexPath : IndexPath) -> PostingTableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostingCell", for: indexPath) as! PostingTableViewCell
        let row = indexPath.row
        cell.tag = row
        cell.booking = bookings[row]
        cell.itemName.text = bookings[row].item.itemName!
        cell.itemCategory.text = bookings[row].item.category!
        cell.itemPrice.text = bookings[row].item.formattedPrice
        cell.itemImage.layer.cornerRadius = 10
        cell.itemImage.loadImage(url: bookings[row].item.photoURL!)
        cell.returnButton.isHidden = false
        cell.returnButton.layer.cornerRadius = 10
        cell.lenderPhoto.loadSmallImage(url: bookings[row].lender.photoURL!)
        cell.lenderPhoto.makeRounded(borderWidth : 0.0, borderColor : UIColor.white.cgColor)
        cell.lenderName.text = bookings[row].lender.username!
        cell.selectionStyle = .none
        cell.returnClickedAction = {(actionCell : PostingTableViewCell) in
            let ok = {(alert : UIAlertAction!) in
                var newBooking = actionCell.booking
                newBooking!.active = false
                newBooking!.userReturned = true
                newBooking!.item.booked = false
                FirebaseQueries.pushItemData(item: newBooking!.item)
                FirebaseQueries.pushBookingData(booking: newBooking!)
                FirebaseQueries.updateBookingsArrays(user: newBooking!.borrower)
            }
            
            let cancel = {(alert : UIAlertAction!) in
                return
            }
            
            
            self.displayConfirmationAlert(title: "Please Confirm", message: "Are you sure you want to return \(actionCell.itemName.text!)?", okHandler: ok, cancelHandler: cancel)
        }
        return cell
    }
 
    
    private func goToBooking() {
        self.performSegue(withIdentifier: "toBooking", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "toBooking") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = bookings[selectedRow!].item
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
    func initializeBookings() {
        self.dispatchGroup.enter()
        let db = Firestore.firestore()
        db.collection("borrowerToBookings").document(Auth.auth().currentUser!.uid).getDocument() { (document, error) in
            var allBookings : [Booking] = [Booking]()
            if let document = document, document.exists {
                let bookings = document.get("Bookings") as! [[String : Any]]
                for b in bookings {
                    let model = try! FirestoreDecoder().decode(Booking.self, from: b)
                    if (model.active){
                        allBookings.append(model)
                    }
                }
                self.bookings = self.filterForActive(bookings: allBookings)
                self.tableView.reloadData()
                self.dispatchGroup.leave()
            } else {
                print("Document does not exist")
            }
        }
    }
    
    func updateBookings() {
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
                self.bookings = self.filterForActive(bookings: allBookings)
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
