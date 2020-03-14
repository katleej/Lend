//
//  ProfileViewController.swift
//  Lend
//
//  Created by Matthew Lacayo on 3/11/20.
//  Copyright © 2020 mdb. All rights reserved.
//

import UIKit
import Firebase
import Cosmos

class ProfileViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    @IBOutlet weak var usernameLabel: UILabel!
    
    @IBOutlet weak var profilePicImageView: UIImageView!
    

    @IBOutlet weak var descriptionLabel: UILabel!
    
    
    @IBOutlet weak var editImageButton: UIButton!
    
    @IBOutlet weak var memberSinceLabel: UILabel!
    
    @IBOutlet weak var locationLabel: UILabel!
    
    @IBOutlet weak var numReviewsLabel: UILabel!
    
    @IBOutlet weak var starsView: CosmosView!
    
    @IBOutlet weak var editProfileButton: UIButton!
    
    @IBOutlet weak var signOutButton: UIButton!
    
    
    static let TAB_VIEW_INDEX = 2
    
    let imagePicker = UIImagePickerController()
    
    
    
    @IBAction func editImageButtonClicked(_ sender: Any) {
        print("PUSHED")
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func editProfileButtonClicked(_ sender: Any) {
        Utils.displayAlert(title: "Sorry!", message: "This feature is still in development. Please check back soon.", controller: self)
    }
    
    
    
    
    var currentUser : LendUser!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        getCurrentUser()
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
        
        setupProfile()
        setupLabels()
        setupEditProfileButton()
        setupStars()
        
    }
    
    private func setupLabels() {
        usernameLabel.text = currentUser.username
        descriptionLabel.text = currentUser.description!
        locationLabel.text = currentUser.city!
        numReviewsLabel.text = String(currentUser.numReviews!)
        memberSinceLabel.text = "member since  \(currentUser.yearJoined!)"
    }
    
    func getCurrentUser() {
        self.currentUser = currentActiveProfile
    }
    
    func setupEditProfileButton() {
        if (currentUser.id == CurrentUserData.currentUser.data!.id!) {
            editImageButton.alpha = 0.02
            editImageButton.layer.cornerRadius = 0.5 * editImageButton.bounds.size.width
            editProfileButton.layer.cornerRadius = 5
        } else {
            editImageButton.alpha = 0.0
            editProfileButton.setTitle("View Postings", for: .normal)
            editProfileButton.layer.cornerRadius = 5
            signOutButton.alpha = 0.0
            //self.tabBarController?.tabBar.isHidden = true
        }
    }
    
    func setupProfile() {
        profilePicImageView.loadImage(url: currentUser.photoURL!)
        imagePicker.delegate = self
        profilePicImageView.makeRounded()
    }
    
    func setupStars() {
        starsView.rating = currentUser.rating!
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
