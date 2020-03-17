//
//  BookingViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/13/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class BookingViewController: UIViewController, UIScrollViewDelegate, UITextFieldDelegate {

    
    @IBOutlet weak var itemImageView: UIImageView!
    
    @IBOutlet weak var lenderProfileImageView: ProfileImage!
    
    @IBOutlet weak var lenderNameLabel: UILabel!
    
    @IBOutlet weak var lenderLocationLabel: UILabel!
    
    @IBOutlet weak var itemNameLabel: UILabel!
    
    @IBOutlet weak var itemDescriptionLabel: UILabel!
    
    
    @IBOutlet weak var scrollView: UIScrollView!
    
    
    @IBOutlet weak var makeBookingView: UIView!
    
    @IBOutlet weak var bookingLengthTextField: UnderLineTextField!
    
    @IBOutlet weak var bookingLengthStepper: UIStepper!
    
    @IBOutlet weak var calculationLabel: UILabel!
    
    @IBOutlet weak var yourPriceLabel: UILabel!
    
    @IBOutlet weak var bookButton: UIButton!
    
    
    @IBAction func bookButtonClicked(_ sender: Any) {
        let confirmation = { (alert : UIAlertAction!) in
            var newItem = self.item
            newItem!.booked = true
            FirebaseQueries.getBookings(for: CurrentUserData.currentUser.data!) { borrowerBookings in
                FirebaseQueries.getBookingsForLender(lender: newItem!.lender) { lenderBookings in
                    FirebaseQueries.makeBookingForItem(item: newItem!, bookingDays: self.numDays!, pastBookingsBorrower: borrowerBookings, pastBookingsLender: lenderBookings)
                    FirebaseQueries.pushItemData(item: newItem!)
                    self.displayAlertAndPop(title: "Success", message: "Successfully booked \(self.item.itemName!)")
                }
                
            }
            
        }
        let cancellation = { (alert : UIAlertAction!) in
            return
        }
        displayConfirmationAlert(title: "Please Confirm", message: "Would you like to book \(item.itemName!) for \(numDays!) days at a total price of \(String(format: "$%.02f", (Double(numDays!) * item.priceAsDouble).truncate(places: 2)))", okHandler: confirmation, cancelHandler: cancellation)
    }
    
    
    
    @IBAction func bookingLengthStepperValueChanged(_ sender: Any) {
        bookingLengthTextField.text = String(Int(bookingLengthStepper.value))
        numDays = Int(bookingLengthStepper.value)
    }
    
    
    
    @IBAction func goToProfileClicked(_ sender: Any) {
        LoadingIndicator.show(self.view)
        FirebaseQueries.getLenderFromName(lenderName: lenderNameLabel.text!) { user in
            guard user != nil else {
                print("Error: User does not exist")
                return
            }
            currentActiveProfile = user!
            LoadingIndicator.hide()
            Utils.segueToProfile(sender: self)
        }
    }
    
    @IBOutlet weak var goToProfileButton: UIButton!
    
    
    
    var item : Item!
    
    var numDays : Int? {
        didSet {
            let product = (item.priceAsDouble * Double(self.numDays!)).truncate(places: 2)
            if (self.numDays! == 1) {
                bookingLengthTextField.text = "\(self.numDays!) day"
            } else {
                bookingLengthTextField.text = "\(self.numDays!) days"
            }
            calculationLabel.text = "\(item.formattedPrice) * \(self.numDays!) = \(product)"
            yourPriceLabel.text = "Your Total Price: \(String(format: "$%.02f", product))"
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        //Tracker to lower keyboard when tapping around
        self.hideKeyboardWhenTappedAround()
        setupImages()
        setupLabels()
        setupProfileButton()
        setupScroll()
        setupMakeBookingView()
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func setupImages() {
        self.itemImageView.loadSmallImage(url: item.photoURL!)
        self.lenderProfileImageView.loadImage(url: item.lender.photoURL!)
    }
    
    func setupLabels() {
        self.lenderNameLabel.text = item.lender.username!
        self.lenderLocationLabel.text = item.location!
        self.itemNameLabel.text = item.itemName!
        self.itemDescriptionLabel.text = item.itemDescription!
    }
    
    
    func setupProfileButton() {
        goToProfileButton.alpha = 0.02
    }
    
    func setupScroll() {
        scrollView.delegate = self
        scrollView.contentOffset.x = 0
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        scrollView.contentOffset.x = 0
    }
    
    func setupMakeBookingView() {
        if (item.lenderId! == CurrentUserData.currentUser.data!.id) {
            makeBookingView.isHidden = true
        } else {
            setupTextField()
            self.bookButton.layer.cornerRadius = 10
            //Setups your price and calculation labels via a computed property.
            numDays = 1
        }
    }
    
    func setupTextField() {
        self.bookingLengthTextField.delegate = self
        Utils.setupTextField(textField: bookingLengthTextField, placeholderText: "", backgroundColor: UIColor.white)
    }
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        guard numDays != nil else {
            return
        }
        textField.text = String(self.numDays!)
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        guard textField.text != nil && Int(textField.text!) != nil else {
            Utils.displayAlert(title: "Slow down", message: "Please enter how long you want to reserve the item for.", controller: self)
            textField.becomeFirstResponder()
            return
        }
        let fieldValue = Double(Int(textField.text!)!)
        if (fieldValue > bookingLengthStepper.maximumValue) {
            bookingLengthStepper.value = bookingLengthStepper.maximumValue
            numDays = Int(bookingLengthStepper.maximumValue)
        } else if (fieldValue < bookingLengthStepper.minimumValue) {
            bookingLengthStepper.value = bookingLengthStepper.minimumValue
            numDays = Int(bookingLengthStepper.minimumValue)
        } else {
            bookingLengthStepper.value = fieldValue
            numDays = Int(fieldValue)
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


