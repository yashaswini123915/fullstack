// Variable to prevent multiple clicks (Double Submission Guard)
let isProcessing = false;

function submitFeedback(event) {
    // Stops the browser from reloading the page
    if (event) event.preventDefault(); 

    // If a request is already in progress, stop further execution
    if (isProcessing) return; 

    const nameInput = document.getElementById('userName');
    const msgInput = document.getElementById('userMessage');
    const btn = document.getElementById('submitBtn');

    const name = nameInput.value.trim();
    const msg = msgInput.value.trim();

    // Check if fields are empty
    if (!name || !msg) {
        alert("⚠️ Please fill in all fields!");
        return;
    }

    // Activate Lock: Disable button and change text
    isProcessing = true;
    btn.disabled = true;
    btn.innerText = "Processing...";

    // Send data to Java Server using Fetch API
    fetch("http://localhost:8082/submit", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: name, message: msg }) 
    })
    .then(res => {
        if (res.ok) {
            // Success Alert
            alert("Entry Successful ✅"); 
            // Clear input fields
            nameInput.value = "";
            msgInput.value = "";
        } else {
            // Server side error alert
            alert("❌ Error: Server could not save data.");
        }
    })
    .catch(err => {
        // Connection error alert (Server offline)
        alert("🔌 Error: Server is not running! Please start the Java Server in Eclipse.");
    })
    .finally(() => {
        // Wait for 1 second before enabling the button again
        setTimeout(() => {
            isProcessing = false;
            btn.disabled = false;
            btn.innerText = "Send Message";
        }, 1000);
    });
}