//
//  EmptyTableViewHelper.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/16/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit

extension UITableView {
    func EmptyMessage(message:String) {
        let rect = CGRect(origin: CGPoint(x: 20,y :0), size: CGSize(width: self.frame.width * 0.9, height: self.frame.height))
        let messageLabel = UILabel(frame: rect)
        messageLabel.text = message
        messageLabel.textColor = UIColor.black
        messageLabel.numberOfLines = 0;
        messageLabel.textAlignment = .center;
        messageLabel.font = UIFont(name: "Noto Sans Kannada", size: 16)
        self.backgroundView = messageLabel;
        self.backgroundView!.widthAnchor.constraint(equalToConstant: self.frame.width * 0.9).isActive = true
        self.separatorStyle = .none;
    }
}
