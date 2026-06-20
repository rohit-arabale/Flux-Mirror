<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Jetpack_Compose-2024.09-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
  <img src="https://img.shields.io/badge/Android-API_24+-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Ktor-2.3.12-087CFA?style=for-the-badge&logo=ktor&logoColor=white" alt="Ktor"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge" alt="License"/>
  <img src="https://img.shields.io/badge/Developer-Rohit-blue?style=for-the-badge" alt="Developer Rohit"/>
</p>

<h1 align="center">📱 Flux Mirror</h1>

<p align="center">
  <b>Advanced Android Screen Mirroring &amp; Casting Solution</b>
</p>

<p align="center">
  A feature-rich Android application enabling seamless screen mirroring and casting across multiple protocols.<br/>
  Developed by <b>Rohit</b> with <b>Jetpack Compose</b> and <b>Kotlin</b>, featuring three powerful casting modes.
</p>

<p align="center">
  <a href="#-download">📥 Download APK</a> •
  <a href="#-features">✨ Features</a> •
  <a href="#-architecture">🏗️ Architecture</a> •
  <a href="#-getting-started">🚀 Getting Started</a>
</p>

---

## ✨ Features

### 📺 Local Cast — Miracast &amp; WiFi Direct

Cast your screen directly to smart TVs, monitors, and projectors without internet.

- **Miracast Discovery** — Auto-detect Miracast-enabled displays
- **Network Devices** — Find Chromecast, Apple TV, AirPlay devices via mDNS
- **WiFi Display** — Full Android WiFi Display framework integration
- **Multi-Protocol** — Support for WiFi Direct (P2P) and network casting
- **Real-time Scanning** — Continuous discovery with multicast optimization
- **Smart Connection** — Automated flow with live status monitoring

### 🌐 Web Cast — Browser Streaming

Stream your screen to any device with a web browser on the same network.

- **Embedded HTTP Server** — Ktor server on port 8080
- **WebSocket Streaming** — Low-latency real-time screen capture
- **Cross-Platform** — Works on desktop, laptop, tablet, phone browsers
- **Auto IP Detection** — Displays connection URL automatically
- **Interactive Viewer** — Fullscreen mode, screenshot capture, FPS counter
- **MediaProjection** — High-quality screen capture API

### 🔧 Floating Tools — Overlay Productivity

System-level floating windows for drawing and camera overlays.

- **Overlay Windows** — Using `SYSTEM_ALERT_WINDOW` permission
- **Drawing Canvas** — Annotate over any app
- **Camera Preview** — Floating facecam window
- **Draggable UI** — Repositionable anywhere on screen
- **Compose Integration** — Modern Jetpack Compose in overlay context

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                              UI LAYER                                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐     │
│  │ MainActivity│  │LocalCastScr │  │ WebCastScr  │  │FloatingScreen│    │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘     │
└─────────┼────────────────┼────────────────┼────────────────┼────────────┘
          │                │                │                │
          ▼                ▼                ▼                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                           VIEWMODEL LAYER                                │
│                      ┌─────────────────────┐                             │
│                      │   DevicesViewModel  │                             │
│                      └──────────┬──────────┘                             │
└─────────────────────────────────┼───────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                           SERVICE LAYER                                  │
│  ┌──────────────────┐  ┌───────────────┐  ┌──────────────────────┐      │
│  │ScreenMirroringSvc│  │ MiracastSvc   │  │ FloatingToolsService │      │
│  └────────┬─────────┘  └───────┬───────┘  └──────────┬───────────┘      │
│           │                    │                     │                   │
│           ▼                    ▼                     ▼                   │
│  ┌─────────────┐      ┌─────────────┐      ┌─────────────────────┐      │
│  │ Ktor Server │      │ VirtualDisp │      │ DrawingOverlaySvc   │      │
│  │ + WebSocket │      │ + MediaProj │      │ CameraPreviewSvc    │      │
│  └─────────────┘      └─────────────┘      └─────────────────────┘      │
└─────────────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          NETWORK LAYER                                   │
│         ┌─────────────────────────────────────────┐                      │
│         │        DeviceDiscoveryManager           │                      │
│         └─────────────────┬───────────────────────┘                      │
│                           │                                              │
│    ┌──────────┬───────────┼───────────┬──────────┐                      │
│    ▼          ▼           ▼           ▼          ▼                      │
│ ┌──────┐  ┌────────┐  ┌────────┐  ┌────────┐  ┌──────┐                  │
│ │ NSD  │  │MediaRtr│  │WiFi P2P│  │DispMgr │  │jmDNS │                  │
│ └──────┘  └────────┘  └────────┘  └────────┘  └──────┘                  │
└─────────────────────────────────────────────────────────────────────────┘
```

### Screen Capture Pipeline

```
MediaProjection → VirtualDisplay → ImageReader → Bitmap → JPEG → WebSocket → Browser
                                        │
                              ImageReader Thread
                                        │
                              ServiceScope (IO)
```

---

## 🛠️ Tech Stack

**Developer:** Rohit

| Category | Technology | Version |
|----------|------------|---------|
| **Language** | Kotlin | 2.0.21 |
| **Build System** | Gradle (Kotlin DSL) | 8.13.1 |
| **UI Framework** | Jetpack Compose | BOM 2024.09 |
| **Design System** | Material 3 | 1.4.0 |
| **HTTP Server** | Ktor (Netty) | 2.3.12 |
| **Device Discovery** | jmDNS | 3.4.1 |
| **Async** | Kotlin Coroutines | 1.7.3 |
| **Camera** | CameraX | 1.3.1 |
| **Media Routing** | AndroidX MediaRouter | 1.6.0 |
| **Min SDK** | Android 7.0 | API 24 |
| **Target SDK** | Android 14 | API 36 |

---

## 📁 Project Structure

```
Flux_Mirror/
│
├── app/src/main/java/com/flux_mirror/
│   │
│   ├── MainActivity.kt                 # Main entry with tab navigation
│   │
│   ├── network/
│   │   ├── DeviceDiscoveryManager.kt   # Multi-protocol device discovery
│   │   └── MiracastConnectionManager.kt
│   │
│   ├── service/
│   │   ├── ScreenMirroringService.kt   # Web casting + Ktor server
│   │   ├── MiracastService.kt          # Miracast projection
│   │   ├── FloatingToolsService.kt     # Overlay window service
│   │   ├── DrawingOverlayService.kt    # Drawing annotations
│   │   └── CameraPreviewService.kt     # Camera overlay
│   │
│   ├── screen/
│   │   ├── WebcastingScreen.kt         # Web cast UI
│   │   ├── SimpleTestScreen.kt         # Floating tools control
│   │   └── FloatingScreen.kt           # Floating window content
│   │
│   ├── viewmodel/
│   │   └── DevicesViewModel.kt         # Device state management
│   │
│   ├── permission/
│   │   └── PermissionsHelper.kt        # Permission handling
│   │
│   └── ui/theme/
│       └── ScreenMirroringTheme.kt     # Custom dark theme
│
├── build.gradle.kts                    # Project config
├── app/build.gradle.kts                # Module config
├── gradle/libs.versions.toml           # Version catalog
└── README.md
```

---

## 📥 Download

> **📦 Latest Release:** [Download APK](https://github.com/infiniteflux/Flux_Mirror/releases)

### Build from Source

```bash
# Clone Rohit's project repository
git clone <your-rohit-repository-url>
cd Flux_Mirror

# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk

# Or install directly
./gradlew installDebug
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio **Hedgehog 2023.1.1+**
- JDK **11+**
- Android SDK **API 36**
- Android device **API 24+** (Android 7.0+)

### Installation

1. **Clone the repo**
   ```bash
   git clone <your-rohit-repository-url>
   ```

2. **Open in Android Studio**
   - File → Open → Select `Flux_Mirror` folder

3. **Sync Gradle**
   - Wait for auto-sync or click "Sync Project with Gradle Files"

4. **Run the app**
   - Connect your device or start emulator
   - Click ▶️ Run

---

## 📖 Usage Guide

### Tab 1: Local Cast

| Step | Action |
|------|--------|
| 1 | Grant all permissions when prompted |
| 2 | App automatically scans for Miracast/Chromecast devices |
| 3 | Tap **"Cast to Device"** |
| 4 | Select device from the list |
| 5 | For Miracast: Connect manually in Cast Screen settings |
| 6 | Tap **"Stop Casting"** when done |

### Tab 2: Web Cast

| Step | Action |
|------|--------|
| 1 | Tap **"Start Web Cast"** |
| 2 | Grant screen capture permission |
| 3 | Note the URL displayed (e.g., `http://192.168.1.100:8080`) |
| 4 | Open URL in any browser on same WiFi |
| 5 | Use fullscreen/screenshot in web viewer |
| 6 | Tap **"Stop Web Cast"** when done |

### Tab 3: Floating Tools

| Step | Action |
|------|--------|
| 1 | Tap **"Start Floating"** |
| 2 | Allow "Display over other apps" permission |
| 3 | Drag the floating button to reposition |
| 4 | Tap to expand tools (Draw, Camera, Screenshot) |
| 5 | Toggle switch to collapse or stop from app |

---

## 🔐 Permissions

| Permission | Purpose |
|------------|---------|
| `INTERNET` | Network communication |
| `ACCESS_WIFI_STATE` | WiFi connection info |
| `CHANGE_WIFI_STATE` | WiFi Direct operations |
| `NEARBY_WIFI_DEVICES` | Device discovery (API 31+) |
| `ACCESS_FINE_LOCATION` | Device discovery (older APIs) |
| `RECORD_AUDIO` | Audio during screen capture |
| `CAMERA` | Camera preview overlay |
| `SYSTEM_ALERT_WINDOW` | Floating windows |
| `FOREGROUND_SERVICE_MEDIA_PROJECTION` | Screen capture service |
| `POST_NOTIFICATIONS` | Casting notifications |

---

## ⚠️ Known Limitations

| Issue | Details |
|-------|---------|
| **Miracast** | Requires manual connection via Cast Screen settings (Android restriction) |
| **Web Cast** | Both devices must be on same WiFi network |
| **Performance** | FPS depends on device CPU, network speed, connected clients |
| **Battery** | Screen capture consumes significant battery |

---

## 🔧 Troubleshooting

<details>
<summary><b>No devices found in Local Cast</b></summary>

- Ensure WiFi is enabled
- Check all permissions: Settings → Apps → Flux Mirror → Permissions
- Ensure target device is on and in pairing mode
- Restart WiFi on your phone
- Debug: `adb logcat | grep DeviceDiscovery`
</details>

<details>
<summary><b>Web Cast URL not accessible</b></summary>

- Verify both devices on same WiFi
- Check firewall on viewing device
- Try direct IP: `http://<phone-ip>:8080`
- Debug: `adb logcat | grep ScreenMirrorService`
</details>

<details>
<summary><b>Floating window not appearing</b></summary>

- Settings → Apps → Special access → Display over other apps → Enable
- Restart the app
- Debug: `adb logcat | grep FloatingToolsService`
</details>

---

## 🧪 Testing

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

---

## 🤝 Contributing

1. **Fork** the repository
2. **Create** feature branch: `git checkout -b feature/NewFeature`
3. **Commit** changes: `git commit -m 'Add NewFeature'`
4. **Push** to branch: `git push origin feature/NewFeature`
5. **Open** Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see [LICENSE](LICENSE) for details.

---

## 👨‍💻 Author

**Rohit**  
Developer and project owner of Flux Mirror.  
Package: `com.flux_mirror`

---

## 🙏 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose) — Modern Android UI
- [Ktor](https://ktor.io/) — Lightweight HTTP framework
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) — Async programming
- [Material Design 3](https://m3.material.io/) — Design system

---

<p align="center">
  <b>Made by Rohit using Kotlin &amp; Jetpack Compose</b>
</p>

<p align="center">
  <sub>Last Updated: January 2026 • Version 1.0 • Android 7.0+ (API 24)</sub>
</p>
