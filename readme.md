# Apkanalyzer 

Apkanalyzer is an application designed for the dynamic analysis of APK files using the Frida dynamic instrumentation toolkit. The application automates the process of installing Android applications in isolated environments, such as emulators, and performs dynamic analysis to identify potential security threats.

Additionally, a proof-of-concept Android application is presented to demonstrate the analyzer's ability to flag malicious APKs during download or installation on a user's phone.

## Prerequisites
1. Android SDK
2. AVD images


## Installation Steps for Front-End application: 

1. Build the apkanalyzer-android folder using Android Studio.
2. Install resulting APK on a device (ADB or from storage)


## Installation Steps for Front-End application: 
1. Configure the analyzer
2. Run the webserver with the below commands
```
$ pip install -r requirements.txt
$ python main.py

```

## Configuration

Can be done by modifying the files found in `apk_analyzer/apk_analyzer/config`

| Key                 | Value Information                                                                                                  | Required |
|---------------------|--------------------------------------------------------------------------------------------------------------------| ------------- |
| `adb_path`          | Path to the ADB executable on the target machine                                                                   | **Yes** |
| `uri`               | Connection string to mongodb                                                                                       | **Yes** |
| `emulator_path`     | Path to emulator executable installed on target machine                                                            | **Yes** |
| `images`            | List of paths where the images used for analysis are located                                                       | **Yes** |






