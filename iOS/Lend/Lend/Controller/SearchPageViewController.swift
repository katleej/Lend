//
//  SearchPageViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/16/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Firebase
import CodableFirebase

class SearchPageViewController: UIViewController, UISearchBarDelegate {
    
    @IBOutlet weak var tableView: UITableView!
    
    var items = [Item]()
    
    
    var filteredItems = [Item]()
    
    var selectedRow : Int?
    
    let searchController = UISearchController(searchResultsController: nil)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        LoadingIndicator.show(self.view)
        updateItems()
        setupTableView()
        setupSearchController()
        
        
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        filterItemsByName(text: searchController.searchBar.text ?? "")
    }
    
    func setupTableView() {
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.register(UINib(nibName: "PostingTableViewCell", bundle: nil), forCellReuseIdentifier: "PostingCell")
        self.tableView.contentInset = UIEdgeInsets(top: 30, left: 0, bottom: 0, right: 0)
    }
    
    
    
    func setupSearchController() {
        // 1
        searchController.searchResultsUpdater = self
        // 2
        searchController.obscuresBackgroundDuringPresentation = false
        // 3
        searchController.searchBar.placeholder = "Search for Lendables"
        // 4
        navigationItem.searchController = searchController
        // 5
        definesPresentationContext = true
        if let textfield = searchController.searchBar.value(forKey: "searchField") as? UITextField {
            textfield.backgroundColor = UIColor.white
        }
    }
    
    var searchTerms = ""
    var searchWasCancelled = false

    func searchBarTextDidBeginEditing(searchBar: UISearchBar) {
        searchWasCancelled = false
    }

    func searchBarCancelButtonClicked(searchBar: UISearchBar) {
        searchWasCancelled = true
    }

    func searchBarTextDidEndEditing(searchBar: UISearchBar) {
        if searchWasCancelled {
            searchBar.text = self.searchTerms
        } else {
            searchTerms = searchBar.text!
        }
    }
    
    
    
       
       private func goToBooking() {
           self.performSegue(withIdentifier: "toBooking", sender: self)
       }
       
       override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
           if (segue.identifier == "toBooking") {
               let destinationVC = segue.destination as! BookingViewController
                let item: Item
                if searchController.isActive && searchController.searchBar.text != "" {
                  item = filteredItems[selectedRow!]
                } else {
                  item = items[selectedRow!]
                }
               destinationVC.item = item
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

extension SearchPageViewController : UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if searchController.isActive && searchController.searchBar.text != "" {
          return filteredItems.count
        }
        return items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return setupBookingCell(indexPath: indexPath)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 160
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedRow = indexPath.row
        goToBooking()
    }
    
    
    private func setupBookingCell(indexPath : IndexPath) -> PostingTableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PostingCell", for: indexPath) as! PostingTableViewCell
        let row = indexPath.row
        let item: Item
        if searchController.isActive && searchController.searchBar.text != "" {
          item = filteredItems[indexPath.row]
        } else {
          item = items[indexPath.row]
        }
        cell.tag = row
        cell.itemName.text = item.itemName!
        cell.itemCategory.text = item.category!
        cell.itemPrice.text = item.formattedPrice
        cell.itemImage.layer.cornerRadius = 10
        cell.itemImage.loadImage(url: item.photoURL!)
        cell.returnButton.isHidden = true
        cell.lenderPhoto.loadSmallImage(url: item.lender.photoURL!)
        cell.lenderPhoto.makeRounded(borderWidth : 0.0, borderColor : UIColor.white.cgColor)
        cell.lenderName.text = item.lender.username!
        cell.selectionStyle = .none
        return cell
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
}

extension SearchPageViewController : UISearchResultsUpdating {
    func updateSearchResults(for searchController: UISearchController) {
        filterItemsByName(text: searchController.searchBar.text ?? "")
    }
    
    private func filterItemsByName(text : String) {
        filteredItems = items.filter({ item in
            return item.itemName!.lowercased().contains(text.lowercased())
        })
        tableView.reloadData()
    }
}

extension SearchPageViewController {
    func updateItems() {
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
                    self.items = foundItems
                    self.tableView.reloadData()
                    LoadingIndicator.hide()
                }
        }
    }
}
