//
//  PostingViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/16/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Eureka

class PostingViewController: FormViewController {

    
    
    @IBOutlet weak var postButton: UIBarButtonItem!
    
    @IBAction func postButtonClicked(_ sender: Any) {
        let errors = form.validate()
        guard errors == [] else {
            Utils.displayAlert(title: "Error", message: "Please make sure all fields are filled, then try again.", controller: self)
            return
        }
        let vals = form.values(includeHidden: false)
        if (vals["typeTag"] as! String == "Item") {
            postItem()
        } else {
            postService()
        }
    }
    
    func postItem() {
        let vals = form.values(includeHidden: false)
        let newItemId = FirebaseQueries.getNewItemId()
        let currentUser = CurrentUserData.currentUser.data!
        LoadingIndicator.show(self.view)
        FirebaseQueries.postItemImage(itemId: newItemId, image: vals["imageTag"] as! UIImage) { url in
            let item = Item(id: newItemId, type : "Item", deliveryMethod: vals["deliveryMethodTag"] as! String, location: currentUser.city!, lenderId: currentUser.id, itemName: vals["nameTag"] as! String, itemDescription: vals["descriptionTag"] as! String, price: String(vals["priceTag"] as! Double), photoURL: url, category: vals["itemCategoryTag"] as! String, booked: false, latitude: currentUser.latitude!, longitude: currentUser.longitude!, lender: currentUser)
            FirebaseQueries.pushItemData(item: item)
            LoadingIndicator.hide()
            self.displayAlertAndPop(title: "Success", message: "Successfully posted your listing.")
        }

        
        
    }
    
    func postService() {
        let vals = form.values(includeHidden: false)
        let newItemId = FirebaseQueries.getNewItemId()
        let currentUser = CurrentUserData.currentUser.data!
        LoadingIndicator.show(self.view)
        FirebaseQueries.postItemImage(itemId: newItemId, image: vals["imageTag"] as! UIImage) { url in
            let item = Item(id: newItemId, type : "Service", deliveryMethod: nil, location: currentUser.city!, lenderId: currentUser.id, itemName: vals["nameTag"] as! String, itemDescription: vals["descriptionTag"] as! String, price: String(vals["priceTag"] as! Double), photoURL: url, category: vals["serviceCategoryTag"] as! String, booked: false, latitude: currentUser.latitude!, longitude: currentUser.longitude!, lender: currentUser)
            FirebaseQueries.pushItemData(item: item)
            LoadingIndicator.hide()
            self.displayAlertAndPop(title: "Success", message: "Successfully posted your listing.")
        }
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupForm()
        setupPostButton()
        
        
            

        // Do any additional setup after loading the view.
    }
    
    func setupPostButton() {
        postButton.setTitleTextAttributes([ NSAttributedString.Key.font: UIFont(name: "Noto Sans Kannada", size: 16)!], for: .normal)
    }
    
    
    func setupForm() {
        form +++ Section("Basic Information")
            <<< TextRow("nameTag"){ row in
                row.title = "Item Name"
                row.placeholder = "Enter item name here"
                row.add(rule: RuleRequired())
            }
            <<< DecimalRow("priceTag"){ row in
                row.title = "Daily Price"
                row.placeholder = "Your daily price for this item"
                row.add(rule: RuleRequired())
            }
            <<< SegmentedRow<String>("typeTag") {
                $0.title = "Lendable Type"
                //$0.selectorTitle = "Pick a Category"
                $0.options = ["Item", "Service"]
                $0.value = "Item"    // initially selected
                $0.add(rule: RuleRequired())
            }
            
            +++ Section("Listing Information")
            <<< PushRow<String>("itemCategoryTag") {
                $0.title = "Item Category"
                //$0.selectorTitle = "Pick a Category"
                $0.hidden = Condition.function(["typeTag"], { form in
                    let val = (form.rowBy(tag: "typeTag") as? SegmentedRow<String>)?.value
                    if (val == "Item") {
                        return false
                    }
                    return true
                })
                $0.options = Item.lendableCategories
                $0.value = "Electronic Appliances"    // initially selected
                $0.add(rule: RuleRequired())
            }
            <<< ImageRow("imageTag"){ row in
                    row.title = "Listing Image"
                    row.add(rule: RuleRequired())
            }
            
            <<< SegmentedRow<String>("deliveryMethodTag") {
                $0.title = "Delivery/Pickup"
                //$0.selectorTitle = "Pick a Category"
                $0.options = ["Delivery", "Pickup", "Both"]
                $0.value = "Delivery"    // initially selected
                $0.hidden = Condition.function(["typeTag"], { form in
                        let val = (form.rowBy(tag: "typeTag") as? SegmentedRow<String>)?.value
                        if (val == "Item") {
                            return false
                        }
                        return true
                })
                $0.add(rule: RuleRequired())
            }
            
            
            <<< PushRow<String>("serviceCategoryTag") {
                $0.title = "Service Category"
                //$0.selectorTitle = "Pick a Category"
                $0.hidden = Condition.function(["typeTag"], { form in
                    let val = (form.rowBy(tag: "typeTag") as? SegmentedRow<String>)?.value
                    if (val == "Service") {
                        return false
                    }
                    return true
                })
                $0.options = Item.serviceCategories
                $0.value = "Haircut"    // initially selected
                $0.add(rule: RuleRequired())
            }
        
        +++ Section("Listing Description")
            <<< TextAreaRow("descriptionTag") { row in
                row.title = "Description"
                row.placeholder = "Enter a description. Some common things to include are rules, pickup locations, availability, etc."
                row.add(rule: RuleRequired())
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
