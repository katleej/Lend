//
//  EditProfileViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/17/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Eureka

class EditProfileViewController: FormViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        setupForm()
        // Do any additional setup after loading the view.
    }
    
    func setupForm() {
        form +++ Section("Edit Information")
        <<< TextAreaRow("descriptionTag"){ row in
            row.title = "About Me"
            row.value = CurrentUserData.currentUser.data!.description!
            row.placeholder = "Enter item name here"
            row.add(rule: RuleRequired())
        }
            <<< ButtonRow("buttonTag"){row in
                row.title = "Save Changes"
            }.onCellSelection{(cell, row) in
                let errors = self.form.validate()
                if (errors.count == 0) {
                    CurrentUserData.currentUser.data?.description = self.form.values()["descriptionTag"]! as! String
                    CurrentUserData.pushNewUserData() {
                        currentActiveProfile = CurrentUserData.currentUser.data!
                        self.performSegue(withIdentifier: "unwindToProfile", sender: self)
                    }
                } else {
                    return
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
