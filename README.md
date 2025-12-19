# Smart Parking Management System

A comprehensive Android application for managing smart parking slots, developed using Java and Android Studio.

## Features

- **User Authentication**: Secure login mechanism to access the system (`LoginActivity`).
- **Real-time Dashboard**: View available and occupied parking slots (`MainActivity`).
- **Slot Details**: Comprehensive information for individual parking spaces (`ParkingDetailActivity`).
- **Web Integration**: In-app browser for external parking resources (`WebViewActivity`).
- **Local Storage**: SQLite implementation for efficient data persistence (`ParkingDatabaseHelper`).
- **Network Accessibility**: Dedicated utilities for handling network operations (`NetworkUtils`).

## Tech Stack

- **Language**: Java
- **Platform**: Android
- **IDE**: Android Studio
- **Database**: SQLite
- **Minimum SDK**: API 24 (Android 7.0)
- **Compile SDK**: API 36

## Getting Started

1. **Clone**: Clone the repository to your local machine.
2. **Open**: Open the project folder in Android Studio.
3. **Sync**: Allow Gradle to sync dependencies.
4. **Run**: Launch the application on an Android Emulator or physical device.

## Project Structure

The core logic is located in `com.example.assignment4`:

- **Activities**: `LoginActivity`, `MainActivity`, `ParkingDetailActivity`, `WebViewActivity`
- **Data & Utils**: `ParkingDatabaseHelper`, `NetworkUtils`, `ParkingAdapter`
- **Models**: `ParkingSlot`
