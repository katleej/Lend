//
//  MyPostingsViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/14/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class MyPostingsViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    
    @IBOutlet weak var tableView: UITableView!
    
    var postings : [Item]!
    
    var selectedRow : Int?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupTableView()

        // Do any additional setup after loading the view.
    }
    
    func setupTableView() {
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: "PostingTableViewCell", bundle: nil), forCellReuseIdentifier: "PostingCell")
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return postings.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return setupPostingCell(indexPath: indexPath)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 150
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedRow = indexPath.row
        goToBooking()
    }
    
    private func setupPostingCell(indexPath : IndexPath) -> PostingTableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostingCell", for: indexPath) as! PostingTableViewCell
        let row = indexPath.row
        cell.itemName.text = postings[row].itemName
        cell.itemCategory.text = postings[row].category
        cell.itemPrice.text = "$\(postings[row].price!)"
        cell.itemImage.loadImage(url: postings[row].photoURL!)
        FirebaseQueries.getPropertyFromName(lenderName: postings[row].lenderName!, property: "photoURL") { url in
            cell.lenderPhoto.loadSmallImage(url: url)
            cell.lenderPhoto.makeRounded(borderWidth : 0.0, borderColor : UIColor.white.cgColor)
        }
        cell.lenderPhoto.makeRounded(borderWidth : 0.0, borderColor : UIColor.white.cgColor)
        cell.lenderName.text = postings[row].lenderName!
        cell.selectionStyle = .none
        return cell
    }
    
    private func goToBooking() {
        self.performSegue(withIdentifier: "toBooking", sender: self)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if (segue.identifier == "toBooking") {
            let destinationVC = segue.destination as! BookingViewController
            destinationVC.item = postings[selectedRow!]
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
