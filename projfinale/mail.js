const firebaseConfig = {
  apiKey: "AIzaSyA0BqOAG79RqwjMr0b05oQOe32L4sVPhSE",
  authDomain: "testsuject1-311c2.firebaseapp.com",
  databaseURL: "https://testsuject1-311c2-default-rtdb.firebaseio.com",
  projectId: "testsuject1-311c2",
  storageBucket: "testsuject1-311c2.firebasestorage.app",
  messagingSenderId: "118016217003",
  appId: "1:118016217003:web:50c426470c841cde4f0cee"
};

// initialize firebase
firebase.initializeApp(firebaseConfig);

// reference your database
var contactFormDB = firebase.database().ref("contactForm");

document.getElementById("contactForm").addEventListener("submit", submitForm);

function submitForm(e) {
  e.preventDefault();   // doesn't refresh the form

  var name = getElementVal("name");
  var emailid = getElementVal("emailid");
  var msgContent = getElementVal("msgContent");
  var course = getElementVal("course");  // Get the selected course

  saveMessages(name, emailid, msgContent, course);

  // enable alert
  document.querySelector(".alert").style.display = "block";

  // remove the alert
  setTimeout(() => {
    document.querySelector(".alert").style.display = "none";
  }, 3000);

  // reset the form
  document.getElementById("contactForm").reset();
}

const saveMessages = (name, emailid, msgContent, course) => {
  var newContactForm = contactFormDB.push();

  newContactForm.set({
    name: name,
    emailid: emailid,
    msgContent: msgContent,
    course: course  // Add the course field to the message data
  });
};

const getElementVal = (id) => {
  return document.getElementById(id).value;
};
