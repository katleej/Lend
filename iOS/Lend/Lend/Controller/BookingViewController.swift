//
//  BookingViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/13/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class BookingViewController: UIViewController {

    
    @IBOutlet weak var itemImageView: UIImageView!
    
    @IBOutlet weak var lenderProfileImageView: UIImageView!
    
    @IBOutlet weak var lenderNameLabel: UILabel!
    
    @IBOutlet weak var lenderLocationLabel: UILabel!
    
    @IBOutlet weak var itemNameLabel: UILabel!
    
    @IBOutlet weak var navbar: UINavigationBar!
    
    @IBAction func goToProfileClicked(_ sender: Any) {
        LoadingIndicator.show(self.view)
        FirebaseQueries.getLenderFromName(lenderName: lenderNameLabel.text!) { user in
            guard user != nil else {
                print("Error: User does not exist")
                return
            }
            currentActiveProfile = user!
            LoadingIndicator.hide()
            self.performSegue(withIdentifier: "toProfile", sender: self)
        }
    }
    
    @IBOutlet weak var goToProfileButton: UIButton!
    
    
    
    var item : Item!

    override func viewDidLoad() {
        super.viewDidLoad()
        setupImages()
        setupLabels()
        setupProfileButton()
        print(itemImageView.bounds)
        print(itemImageView.frame)
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.tabBarController?.tabBar.isHidden = false
    }
    
    func setupImages() {
        self.itemImageView.loadSmallImage(url: item.photoURL!)
        self.lenderProfileImageView.image = UIImage(named: "elephant_green")
        self.lenderProfileImageView.makeRounded()
        FirebaseQueries.getPropertyFromName(lenderName: item.lenderName!, property: "photoURL") { property in
            self.lenderProfileImageView.loadImage(url: property)
        }
    }
    
    func setupLabels() {
        self.lenderNameLabel.text = item.lenderName!
        self.lenderLocationLabel.text = item.location!
        self.itemNameLabel.text = item.itemName!
    }
    
    func setupProfileButton() {
        goToProfileButton.alpha = 0.02
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
