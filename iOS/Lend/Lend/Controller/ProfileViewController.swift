//
//  ProfileViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/11/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Firebase
import GrowingTextView

class ProfileViewController: UIViewController, GrowingTextViewDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var usernameLabel: UILabel!
    
    @IBOutlet weak var profilePicImageView: UIImageView!
    

    @IBOutlet weak var descriptionLabel: UILabel!
    
    
    @IBOutlet weak var editImageButton: UIButton!

    let imagePicker = UIImagePickerController()
    
    
    
    @IBAction func editImageButtonClicked(_ sender: Any) {
        print("PUSHED")
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }
    
    
    var currentUser : LendUser!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getCurrentUser()
        profilePicImageView.loadImage(url: currentUser.photoURL!)
        imagePicker.delegate = self
        setupViews()
        currentActiveProfile = CurrentUserData.currentUser.data!
        // Do any additional setup after loading the view.
    }
    

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "unwindToLogin" {
            do{
                try AuthInstance.instance.auth!.signOut()
            }
            catch let signOutError as NSError {
                print ("Error signing out: %@", signOutError)
            }
        }
    }
    
    /*
     Handles the return to a past view. Reassigns currentActiveUser to the current user.
     */
    override func didMove(toParent parent: UIViewController?) {
        if !(parent?.isEqual(self.parent) ?? false) {
            print("Entered")
            currentActiveProfile = CurrentUserData.currentUser.data!
            self.tabBarController?.tabBar.isHidden = false
        }
        super.didMove(toParent: parent)
    }
    
    
    func setupViews() {
        profilePicImageView.makeRounded()
        
        setupLabels()
        if (currentUser.id == CurrentUserData.currentUser.data!.id!) {
            editImageButton.alpha = 0.02
            editImageButton.layer.cornerRadius = 0.5 * editImageButton.bounds.size.width
        } else {
            editImageButton.alpha = 0.0
            //self.tabBarController?.tabBar.isHidden = true
        }
        
    }
    
    private func setupLabels() {
        usernameLabel.text = currentUser.username
        descriptionLabel.text = currentUser.description!
    }
    
    func getCurrentUser() {
        self.currentUser = currentActiveProfile
    }

    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        if let pickedImage = info[UIImagePickerController.InfoKey.originalImage] as? UIImage {
            let reducedImage = pickedImage.resized(toWidth: CGFloat(Utils.REDUCED_IMAGE_SIZE))
            profilePicImageView.image = reducedImage!
            FirebaseQueries.postImage(image: reducedImage!)
        }
        
        dismiss(animated: true, completion: nil)
    }
    
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
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
