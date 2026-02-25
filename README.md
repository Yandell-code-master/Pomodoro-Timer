# Pomodoro Timer - Frontend

This repository contains the user interface (Frontend) for the **Pomodoro Timer** application, a tool designed to optimize productivity and focus during study or work sessions using the Pomodoro technique.

## Project Overview

The application implements a timer programmed for cycles of **25 minutes of concentration** followed by **5 minutes of rest**. The main goal is to help the user maintain a steady workflow and reduce mental fatigue.

> **Important Note:** This repository corresponds exclusively to the project's Frontend. For full functionality (session management, data persistence, etc.), it requires a connection to a Backend service currently deployed in **Railway** containers.

## Live Demo

The frontend is deployed and available for free through GitHub Pages:
🔗 [View Live Application](https://yandell-code-master.github.io/Pomodoro-Timer/)

## Tech Stack

The development was carried out under a clean architecture using standard web technologies to ensure compatibility and performance:

* **HTML5:** Semantic site structure.
* **CSS3 & Bootstrap:** Responsive design and component styling.
* **JavaScript (Vanilla):** Timer logic, DOM manipulation, and asynchronous requests.

## Functionality & Limitations

As this is the presentation layer, the project can be explored independently:

1.  **Testing Mode:** The interface is functional and allows running time cycles locally.
2.  **Backend Dependency:** Advanced features (such as login or saving settings) depend on the availability of the API service on Railway. Without this service active, the experience is limited strictly to the visual interface and basic timer.

## Repository Structure

Based on the code organization:
* `/css`: Stylesheets and Bootstrap customizations.
* `/js`: Logic scripts for the timer and API communication.
* `/resources`: Multimedia assets, including alert sounds for cycle changes.
* `index.html`: Main entry point of the application.
* `log-in.html` / `sing-in.html`: User authentication modules.

---
Developed by [Yandell-code-master](https://github.com/Yandell-code-master)