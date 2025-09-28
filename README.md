# 🖩 RMI Java Calculator

![Java](https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white) ![RMI](https://img.shields.io/badge/RMI-remote-blue) ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

A **remote Java calculator** using **RMI (Remote Method Invocation)** to perform operations on a server while displaying results on a client GUI.  

---

## 💡 Project Overview

This project demonstrates a **client-server Java application** where:  

- ✅ The **server** hosts calculator methods and registers them for remote invocation.  
- ✅ The **client** (GUI) connects to the server to call these methods seamlessly.  
- ✅ Each project (server & client) can run in **different JVMs** on the **same computer** or on **different computers** in a **LAN**.  

---

## 🗂️ Project Structure

RMI-Calculator/
├─ server/       # Server project (hosts calculator logic)
├─ client/       # Client project (GUI for user interaction)

---

## 🚀 How to Run

1. **Start the Server**  
   - Open the `server` project in IntelliJ IDEA.  
   - Run the server and register it in the RMI registry.  

2. **Configure the Client**  
   - Open the `client` project in IntelliJ IDEA.  
   - Open `CalculatorGUI.java`.  
   - Change the IP address to the server’s IP address on line 293.  

3. **Run the Client GUI**  
   - Run `CalculatorClient.java`.  
   - The GUI window will appear.  
   - Click **Connect** to link to the server.  

4. **Perform Calculations**  
   - Enter operations in the GUI.  
   - Results appear in the client GUI.  
   - Logs are printed on the server terminal.  

---

## 🖥️ Features

- Real-time **remote calculations** using Java RMI.  
- **GUI interface** for easy user interaction.  
- **Server logs** each operation for monitoring.  
- Can run across **multiple JVMs** or **networked computers**.  

---

## 📌 Requirements

- Java JDK 8+  
- IntelliJ IDEA (or any Java IDE)  
- LAN connection (if running server & client on different computers)  

---

## ⚡ Notes

- Ensure the server is running and registered **before** starting the client.  
- Update the client IP to the server’s actual IP address.  
- Click **Connect** on the client GUI before performing calculations.  

---

## 🔗 References

- [Java RMI Documentation](https://docs.oracle.com/en/java/javase/20/docs/api/java.rmi/package-summary.html)  
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)  
