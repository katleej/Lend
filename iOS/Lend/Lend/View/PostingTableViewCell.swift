//
//  BookingTableViewCell.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/14/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class PostingTableViewCell: UITableViewCell {

    var booking : Booking!
    
    @IBOutlet weak var itemImage : UIImageView!
    @IBOutlet weak var lenderPhoto : UIImageView!
    @IBOutlet weak var itemCategory : UILabel!
    @IBOutlet weak var itemName : UILabel!
    @IBOutlet weak var lenderName : UILabel!
    @IBOutlet weak var itemPrice : UILabel!
    @IBOutlet weak var returnButton : UIButton!
    @IBOutlet weak var messageButton: UIButton!
    
    @IBOutlet weak var postedByLabel : UILabel!
    
    @IBAction func returnButtonClicked(_ sender: Any) {
        returnClickedAction?(self)
    }
    
    @IBAction func messageButtonClicked(_ sender: Any) {
        messageClickedAction?(self)
    }
    
    
    var returnClickedAction : ((PostingTableViewCell) -> Void)?
    
    var messageClickedAction : ((PostingTableViewCell) -> Void)?
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
