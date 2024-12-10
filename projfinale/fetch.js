// Firebase configuration (reuse the same configuration as in mail.js)
const firebaseConfig = {
  apiKey: "AIzaSyA0BqOAG79RqwjMr0b05oQOe32L4sVPhSE",
  authDomain: "testsuject1-311c2.firebaseapp.com",
  databaseURL: "https://testsuject1-311c2-default-rtdb.firebaseio.com",
  projectId: "testsuject1-311c2",
  storageBucket: "testsuject1-311c2.firebasestorage.app",
  messagingSenderId: "118016217003",
  appId: "1:118016217003:web:50c426470c841cde4f0cee",
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Reference the database
const contactFormDB = firebase.database().ref("contactForm");

// Function to fetch and display data
function fetchMessages() {
  contactFormDB.on("value", (snapshot) => {
    const messages = snapshot.val();
    const tbody = document.querySelector("#messagesTable tbody");
    tbody.innerHTML = ""; // Clear existing data

    for (let id in messages) {
      const message = messages[id];
      const row = document.createElement("tr");

      const nameCell = document.createElement("td");
      nameCell.textContent = message.name;

      const emailCell = document.createElement("td");
      emailCell.textContent = message.emailid;

      const msgCell = document.createElement("td");
      msgCell.textContent = message.msgContent;

      row.appendChild(nameCell);
      row.appendChild(emailCell);
      row.appendChild(msgCell);

      tbody.appendChild(row);
    }
  });
}

// Call the function to fetch data
fetchMessages();
