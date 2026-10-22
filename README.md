# Smart Pantry Manager
It is an android application that assists a  user  with the prevention or reduction of food wastage. This application tracks the ingredients the user has at home, it provides them with recipes with the contents in their pantry. 

## Features
-Add, edit, delete and view pantry items (using the name, unit, quantity and expiry date)

-Input checks on name and quantity.

-The pantry list uses RecycleView with the customer's adapter.

-20 South African cuisines or recipes are loaded on the first launch.

-Strict recipe matching

-The recipe detail screen

-All recipe browser

-Settings screen

-Bottom navigation and toolbar navigation 

-Explicit intents between activities

## Database Choice
SQLite with SQLiteOpenHelper was selected because:

-It matches with the module's persistent data helper.

-It works fully offline and no accounts or internet is needed.

-It is good for a single user local pantry.

## Requirements
-Android Studio

-JDK 8+

-minSdk 24

-targetSdk 33

## How to open and run
1.Unzip or clone the project

2.Android Studio -> File -> Open -> choose the SmartPantryManager folder.

3.Gradle has to sync successfully.

4.Due to memory, to connect use a real device, the USB connection cable.

5.Run the program using the green play button to run everything.

## Package
com.richfield.smartpantrymanager