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
    
    var item : Item!
    
    @IBAction func swipeRight(_ sender: Any) {
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        setupImages()
        setupLabels()
        print(itemImageView.bounds)
        print(itemImageView.frame)
        // Do any additional setup after loading the view.
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
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
