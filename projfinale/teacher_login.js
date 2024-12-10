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
const loginForm = document.getElementById("loginForm");
const teacherEmail = document.getElementById("teacherEmail");
const teacherPassword = document.getElementById("teacherPassword");
const alertDiv = document.querySelector(".alert");

// Add event listener for form submission
loginForm.addEventListener("submit", (event) => {
    event.preventDefault(); // Prevent form from reloading the page

    const email = teacherEmail.value;
    const password = teacherPassword.value;

    // Sign in with email and password
    firebase.auth().signInWithEmailAndPassword(email, password)
        .then((userCredential) => {
            // Successfully logged in
            console.log("Teacher logged in:", userCredential.user);

            // Redirect to dashboard page
            window.location.href = "index.html";
        })
        .catch((error) => {
            // Handle errors
            console.error("Error during login:", error.message);
            alertDiv.style.display = "block";  // Show alert on error
        });
});
