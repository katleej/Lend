//
//  BookingTableViewCell.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/14/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit

class PostingTableViewCell: UITableViewCell {

    @IBOutlet weak var itemImage : UIImageView!
    @IBOutlet weak var lenderPhoto : UIImageView!
    @IBOutlet weak var itemCategory : UILabel!
    @IBOutlet weak var itemName : UILabel!
    @IBOutlet weak var lenderName : UILabel!
    @IBOutlet weak var itemPrice : UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
