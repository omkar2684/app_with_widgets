// Firebase Config (reuse your existing configuration)
const firebaseConfig = {
    apiKey: "AIzaSyA0BqOAG79RqwjMr0b05oQOe32L4sVPhSE",
    authDomain: "testsuject1-311c2.firebaseapp.com",
    databaseURL: "https://testsuject1-311c2-default-rtdb.firebaseio.com",
    projectId: "testsuject1-311c2",
    storageBucket: "testsuject1-311c2.firebasestorage.app",
    messagingSenderId: "118016217003",
    appId: "1:118016217003:web:50c426470c841cde4f0cee"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Select form elements
const registerForm = document.getElementById('registerForm');
const teacherEmail = document.getElementById('teacherEmail');
const teacherPassword = document.getElementById('teacherPassword');
const alertDiv = document.querySelector('.alert');

// Add event listener for form submission
registerForm.addEventListener('submit', (event) => {
    event.preventDefault();  // Prevent form from reloading the page

    const email = teacherEmail.value;
    const password = teacherPassword.value;

    // Create new user with email and password
    firebase.auth().createUserWithEmailAndPassword(email, password)
        .then((userCredential) => {
            const userId = userCredential.user.uid;

            // Store user information (e.g., email, role) in Firebase Realtime Database
            firebase.database().ref("users/" + userId).set({
                email: email,
                role: "teacher"  // Store the role as 'teacher'
            });

            alertDiv.style.display = 'none';  // Hide alert on successful registration
            // Optionally, redirect the user or show a success message
            console.log('Teacher registered successfully:', userCredential.user);
        })
        .catch((error) => {
            // Handle errors
            console.error("Error during registration:", error.message);
            alertDiv.style.display = 'block';  // Show alert on error
        });
});
