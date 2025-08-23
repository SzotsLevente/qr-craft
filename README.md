# QR Craft - Android QR Code Scanner App

A modern, fast, and smooth QR code scanner application built with Jetpack Compose and CameraX.

## Features

### 🚀 Core Functionality

- **Fast Camera-First Experience**: Instant access to QR code scanning
- **Real-Time Detection**: Live QR code scanning using Google ML Kit
- **Smart Content Type Detection**: Automatically recognizes different QR code content types
- **Responsive Design**: Optimized for both mobile and tablet devices

### 📱 Supported Screen Sizes

- **Mobile Devices**: Up to 600dp width
- **Tablets & Large Screens**: 600dp width and above
- **Content Block Width**: Fixed at 480dp for consistent UI across devices

### 🎨 User Interface

- **Splash Screen**: Dark gray background with bright yellow "QR Craft" logo
- **Camera Permission Handling**: Modal dialog with clear permission request
- **Loading States**: Smooth loading animation during QR code processing
- **Permission Confirmation**: Green snackbar when camera access is granted

### 📋 Supported QR Code Content Types

#### 🔗 **LINK**

- Detects URLs starting with `http://` or `https://`
- Highlighted with lime yellow background for visual emphasis
- Center-aligned text

#### 📞 **PHONE**

- Recognizes phone number patterns
- Supports international format with `+` prefix
- Handles spaces, dashes, parentheses

#### 👤 **CONTACT**

- Parses vCard format (`BEGIN:VCARD`)
- Center-aligned display

#### 📍 **LOCATION**

- Identifies latitude, longitude coordinates
- Format: `latitude, longitude`

#### 📶 **WIFI**

- Recognizes WiFi configuration strings
- Format: `WIFI:S:<SSID>;T:<WPA|WEP|nopass>;P:<password>;;`

#### 📄 **TEXT**

- Default fallback for all other content
- **Special Text Handling**:
    - Left-aligned for better readability
    - Auto-truncation to 6 lines
    - "Show more" button (dark blue #505F6A)
    - "Show less" button (lighter tone #8C99A2)

### 📊 Scan Result Screen Features

- **Top Navigation Bar**: Centered title with back arrow
- **QR Code Display**: Visual representation of scanned code
- **Content Type Label**: Clear indication of detected content type
- **Action Buttons**:
    - **Share**: Opens system sharing sheet (WhatsApp, Gmail, etc.)
    - **Copy**: Copies content to clipboard with confirmation toast

## 🛠 Technical Implementation

### Dependencies

- **CameraX 1.4.0**: Camera functionality and lifecycle management
- **ML Kit Barcode Scanning**: Real-time QR code detection
- **Jetpack Compose**: Modern UI framework
- **Navigation Compose**: Screen navigation
- **Accompanist Permissions**: Runtime permission handling
- **Kotlinx Coroutines**: Asynchronous operations

### Architecture

- **MVVM Pattern**: Clean separation of concerns
- **Compose Navigation**: Type-safe navigation between screens
- **State Management**: Reactive UI using Compose state
- **Content Detection**: Centralized logic for QR code type recognition

### Permission Handling

- **Runtime Permissions**: Proper Android 6.0+ permission model
- **User-Friendly Dialogs**: Clear explanation of camera necessity
- **Graceful Fallbacks**: Appropriate handling when permissions are denied

## 🚦 App Flow

1. **Splash Screen** → Shows QR Craft logo for 2 seconds
2. **Permission Check** → Requests camera permission if needed
3. **Scanner Screen** → Live camera preview with QR detection
4. **Loading State** → Processing animation when QR code is detected
5. **Result Screen** → Displays formatted content with actions

## 🎯 Key Features Implementation

### Camera Permission Dialog

```
Title: "Camera Required"
Message: "This app cannot function without camera access. To scan QR codes, please grant permission."
Actions: [Close App] [Grant Access]
```

### Loading Animation

- Centered spinner over camera preview
- "Loading..." text
- Simulated 1-second processing time for smooth UX

### Content Type Detection Logic

Intelligent pattern matching for different QR code formats:

- URL detection with protocol validation
- Phone number regex pattern matching
- vCard contact information parsing
- Geographic coordinate recognition
- WiFi configuration string detection

## 🔧 Build & Run

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK 26 (Android 8.0) or higher
- Kotlin 2.0.21

### Setup

1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run on device or emulator with camera support

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew test
```

## 📱 Device Requirements

- **Minimum SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 15 (API 36)
- **Camera**: Required for QR code scanning
- **Permissions**: CAMERA

## 🎨 Design Specifications

### Colors

- **Splash Background**: #424242 (Dark Gray)
- **Logo Color**: #FFEB3B (Bright Yellow)
- **Link Highlight**: #EBFF69 (Lime Yellow)
- **Permission Snackbar**: #4CAF50 (Green)
- **Show More Button**: #505F6A (Dark Blue)
- **Show Less Button**: #8C99A2 (Light Gray)

### Layout

- **Content Block**: 480dp fixed width, centered
- **Mobile Layout**: Responsive up to 600dp
- **Tablet Layout**: Optimized for 600dp+ screens

## 🚀 Performance Optimizations

- **Efficient Camera Usage**: Proper lifecycle management
- **Memory Management**: Automatic image proxy cleanup
- **Smooth Animations**: Hardware-accelerated transitions
- **Background Processing**: Coroutine-based async operations

## 📄 License

This project is built for demonstration purposes showcasing modern Android development practices
with Jetpack Compose and CameraX.