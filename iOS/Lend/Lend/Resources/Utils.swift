//
//  Utils.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/6/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import Foundation
import UIKit

class Utils {
    static func makeButtonRounded(button : UIButton, cornerRadius : CGFloat, borderWidth : CGFloat, borderColor : UIColor, backgroundColor : UIColor, textColor: UIColor) {
        button.backgroundColor = backgroundColor
        button.layer.cornerRadius = cornerRadius
        button.layer.borderWidth = borderWidth
        button.layer.borderColor = borderColor.cgColor
        button.setTitleColor(textColor, for: .normal)
        button.contentHorizontalAlignment = .center
    }
    
    static func displayAlert(title: String, message: String, controller : UIViewController) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        let defaultAction = UIAlertAction(title: "OK", style: .default, handler: nil)
        alert.addAction(defaultAction)
        controller.present(alert, animated: true, completion: nil)
    }
    
}
