# Virtual Pet Application

## Description
Virtual pets are digital companions that simulate the experience of caring for a real pet. Players are tasked with managing their pet’s needs, such as hunger, happiness, and health, through interacting with their pet. This game provides a low-stakes environment to practice responsibility and routine management, similar to popular games like *Tamagotchi*, *Neopets*, and *Nintendogs*. This project, implemented in Java, offers an interactive and educational experience for users to engage with their virtual pet and develop life skills.

## Required Libraries and Third-Party Tools
The following libraries and tools are required to run or build the Virtual Pet application:
- **javax.swing** (UI components)
- **java.io** (file handling)
- **java.util** (data structures)
- **java.time** (time-based features)
- **java.awt** (graphics and UI)
- **JUnit 5** for testing:
  - `org.junit.jupiter.api.BeforeEach`
  - `org.junit.jupiter.api.Test`
  - `org.junit.jupiter.api.AfterEach`
  - `static org.junit.jupiter.api.Assertions.*`

Ensure all libraries are compatible with Java SE 8 or higher.


![demo1](https://github.com/user-attachments/assets/fe8c8b27-1bec-4528-864c-105fcf604b5c)
![demo2](https://github.com/user-attachments/assets/71f7c075-2356-4206-bbb4-c2e71b08c8ea)
![demo3](https://github.com/user-attachments/assets/f0d36c9d-97be-4958-b685-446bcdfdf191)





## Building the Software
To build the Virtual Pet application from source, follow these steps:

1. **Obtain the source code:**
   - Download the source code files from the repository or receive them from your instructor.
   - Ensure all `.java` files are stored in a single project directory.

2. **Set up your Java development environment:**
   - Install [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) version 8 or higher.
   - Install a Java IDE (e.g., IntelliJ IDEA, Eclipse, or NetBeans).

3. **Configure your project:**
   - Open the project directory in your chosen IDE.
   - Ensure the required libraries are included in the classpath:
     - `javax.swing`
     - `java.io`
     - `java.util`
     - `java.time`
     - `java.awt`
   - Add the JUnit 5 library to your testing configuration.

4. **Compile the source code:**
   - Use the build tools in your IDE or run the following command in your terminal:
     ```bash
     javac -cp .:junit-jupiter-api-5.11.3.jar *.java
     ```

5. **Run the unit tests (optional):**
   - Execute the JUnit test cases in your IDE, or use the following command:
     ```bash
     java -cp .:junit-jupiter-api-5.11.3.jar org.junit.runner.JUnitCore <TestClassName>
     ```

## Running the Software
To run the compiled Virtual Pet application, follow these steps:

1. **Execute the Main program:**
   - In your IDE, run the `main` method in the `Main` class.
   - Alternatively, in the terminal, execute the following command:
     ```bash
     java Main
     ```

2. **Interact with the application:**
   - Use the provided graphical user interface to manage your virtual pet's needs.

## User Guide
### Getting Started
- Launch the application to create your virtual pet.
- The interface will display your pet’s stats (hunger, happiness, health).
- Use the buttons to perform actions (e.g., feed, take to vet, give gift) to improve your pet's stats.

### Gameplay Tips
- Regularly interact with your pet to maintain its well-being.
- Neglecting your pet will cause its stats to decrease, affecting its happiness and health.

### Parental Controls
- Password: `2212`
- Access parental controls from the main menu in the application.

## TA Notes
- All necessary features have been implemented, including a responsive GUI and persistent state management.
- The application includes comprehensive JUnit tests to ensure code quality.
- No additional installations are required for parental controls—they are integrated into the main application.
